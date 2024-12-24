package com.wfs.truthsearch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wfs.truthsearch.data.BibleDatabase
import com.wfs.truthsearch.databinding.ActivityMainBinding
import com.wfs.truthsearch.models.BookVerseData
import com.wfs.truthsearch.models.getBookFromAssetsMyVerse
import com.wfs.truthsearch.models.resolveBookById
import com.wfs.truthsearch.ui.search.SearchFragment
import com.wfs.truthsearch.utils.AppCloser
import com.wfs.truthsearch.utils.PreferenceManager
import com.wfs.truthsearch.utils.populateDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)


        binding.appBarMain.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()

            val dialog = SearchFragment()
            dialog.show(supportFragmentManager, "ComposeDialog")

        }

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!
        val navController = navHostFragment.navController

        binding.navView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings
                ),
                binding.drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }

        binding.appBarMain.contentMain.bottomNavView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            it.setupWithNavController(navController)
        }

        PreferenceManager.init(this)

        CoroutineScope(Dispatchers.Main).launch {
            populateDatabase(this@MainActivity)
        }

        val database = BibleDatabase.getInstance(this@MainActivity)
        val fullVerseDao = database.fullVerseDao()
        val searchIndexDao = database.searchIndexDao()

        lifecycleScope.launch {
            val count = fullVerseDao.getFullVerseCount()
            if (count == 0) Log.e(tag, "FullVerse row count: $count")
            else  Log.d(tag, "FullVerse row count: $count")

            val count2 = searchIndexDao.getSearchIndexCount()
            if (count2 == 0) Log.e(tag, "SearchIndex row count: $count2")
            else  Log.d(tag, "SearchIndex row count: $count2")
        }

        AppCloser.setCloseAppCallback {
            Log.w(tag, "app closing called: ${this.applicationInfo.name}")
        // this closes the WebServer but not the app
        // finishAffinity() // Properly closes the app
        // finish()
        }

        // Copy assets to cache for server use
        copyAssetsToCache(this, "bibles", "bibles")

        val serviceIntent = Intent(this, ServerService::class.java)
        startService(serviceIntent)

        Log.d("tag", "File Cache: ${this.cacheDir}")

        // Observe the browser launch event

        // Observe the browser launch event
        lifecycleScope.launch {
            sharedViewModel.launchBrowserEvent.collect { (version, verseId, id) ->
                Log.d(tag, "view model: ${verseId} ${id} ")
                launchBrowser(version, verseId)
            }
        }
    }

    fun launchBrowser(version: String, verseId: String ) {

        // val savedVerseId = PreferenceManager.getString(PreferenceManager.KEY_PLACE_HOLDER, "01_01_01:001")
        val savedVerseId = verseId
        val resolvedBook = resolveBookById(this, version,  savedVerseId)
        if (resolvedBook != null) {
            Log.d(tag, "Resolved ${resolvedBook.id} Book: ${resolvedBook.displayName}, File: ${resolvedBook.filename}")
            // Use the resolved book (e.g., launch an intent, update the UI, etc.)
            val bookData = getBookFromAssetsMyVerse(this, version,  savedVerseId)
            Log.d(tag, "verse: ${savedVerseId}, section: ${bookData?.testament}, file: ${bookData?.filename}")
            bookData?.let { buildBrowserIntent(this, it, savedVerseId) }
        } else {
            Log.w(tag,"Book not found for ID: $savedVerseId")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val serviceIntent = Intent(this, ServerService::class.java)
        stopService(serviceIntent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        val navView: NavigationView? = findViewById(R.id.nav_view)
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            menuInflater.inflate(R.menu.overflow, menu)
        }
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_settings)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun copyAssetsToCache(context: Context, assetPath: String, cachePath: String) {
        val assets = context.assets.list(assetPath) ?: return

        val targetDir = File(context.cacheDir, cachePath)
        if (!targetDir.exists()) targetDir.mkdirs()

        assets.forEach { asset ->
            val assetFilePath = "$assetPath/$asset"
            val targetFile = File(targetDir, asset)

            if (context.assets.list(assetFilePath)?.isNotEmpty() == true) {
                copyAssetsToCache(context, assetFilePath, "$cachePath/$asset")
            } else {
                context.assets.open(assetFilePath).use { inputStream ->
                    targetFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
    }

    fun encodeUrl(url: String): String {
        val parts = url.split("#", limit = 2) // Separate the fragment
        val baseUrl = parts[0].replace(" ", "%20") // Encode spaces in the path
        val fragment = parts.getOrNull(1)?.replace(" ", "%20") // Encode spaces in the fragment
        return if (fragment != null) "$baseUrl#$fragment" else baseUrl
    }


    private fun handleIntent() {
        if (intent.resolveActivity(this.packageManager) == null) {
            Toast.makeText(this, "No browser or app found to open HTML files.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildBrowserIntent(context: Context, bookVerseData: BookVerseData, verseId: String) {

        // Genesis 28:04
        //val url = "http://127.0.0.1:8080/bibles/esv/$selectedItem/$filename#01_28:004"
        // Genesis 28:04
        //val url = "http://127.0.0.1:8080/bibles/esv/$selectedItem/$filename#01_28:004"
        //val url = "http://127.0.0.1:8080/bibles/esv/$selectedItem/$filename"

        val versionEntry = PreferenceManager.versionMap.entries.first { bookVerseData.version.contains(it.key) }

       // val verse = PreferenceManager.getString(versionEntry.value,"01_01:001")

        val url =
            encodeUrl("http://127.0.0.1:8080/${bookVerseData.testament}/${bookVerseData.filename}#${verseId}")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent) // Launch the intent
        } else {
            Toast.makeText(context, "No application can handle this request.", Toast.LENGTH_SHORT).show()
        }
    }


}