package com.wfs.truthsearch.ui.settings

import SharpieContent
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wfs.truthsearch.ui.search.JustifiedContent
import com.wfs.truthsearch.ui.search.TextContent
import com.wfs.truthsearch.ui.theme.AppTheme
import com.wfs.truthsearch.utils.PreferenceManager

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefManager = com.wfs.truthsearch.utils.PreferenceManager
        val factory = SettingsViewModelFactory(prefManager)
        settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    SettingsScreen(viewModel = settingsViewModel)
                }
            }
        }
    }
}


@Composable
fun isNightMode(): Boolean {
    val context = LocalContext.current
    return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

@Composable
fun ThemePreferenceSetting(viewModel: SettingsViewModel) {
    val currentMode by viewModel.lightDarkModePref.observeAsState(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    val options = listOf(
        "Light" to AppCompatDelegate.MODE_NIGHT_NO,
        "Dark" to AppCompatDelegate.MODE_NIGHT_YES,
        "System Default" to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )

    Column {
        Text(text = "Theme Preference", style = MaterialTheme.typography.h6)

        options.forEach { (label, mode) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = currentMode == mode,
                    onClick = { viewModel.updateLightDarkModePref(mode) } // Notify ViewModel of the change
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ListSettingsScreen(viewModel: SettingsViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(), // Fullscreen container
        color = MaterialTheme.colors.background // Respect theme colors
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Ensure constraints for scrolling
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()) // Enable scrolling
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "User Interface",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display the current mode
                Text(
                    text = if (isNightMode()) "Mode: Night" else "Mode: Day",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Options Section
                val options = listOf("Sharpie", "Warm", "Justified")
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (viewModel.verseResultStyle.value == option),
                            onClick = { viewModel.updateVerseResultStyle(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colors.primary,
                                unselectedColor = MaterialTheme.colors.onSurface
                            )
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dummy content to test scrolling
                repeat(20) {
                    Text(
                        text = "Item $it",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val verseResultStyle by viewModel.verseResultStyle.observeAsState("Warm") // Observe LiveData

    val context = LocalContext.current
    val isNightMode = isNightMode() // Check current mode

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background // Use background from MaterialTheme

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = "User Interface",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onBackground
            )

            // Display the current mode
            Text(
                text = if (isNightMode) "Mode: Night" else "Mode: Day",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Options Section
            val options = listOf("Sharpie", "Warm", "Justified")
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (verseResultStyle == option),
                        onClick = { viewModel.updateVerseResultStyle(option) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary,
                            unselectedColor = MaterialTheme.colors.onSurface
                        )
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preview Section
            Text(
                text = "Preview",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic Preview Content Based on Selection
            val friendlyVerse = "John 3:16"
            val verseText = "For God so loved the world that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."
            val data = listOf(Triple("43_03:016", friendlyVerse, verseText))

            when (verseResultStyle) {
                "Justified" -> {
                    JustifiedContent(
                        searchResults = data,
                        onVerseClick = { verseId ->
                            Log.d("SettingsScreen", "Clicked verse ID: $verseId")
                            Toast.makeText(context, "We are justified by faith!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                "Sharpie" -> {
                    SharpieContent(
                        searchResults = data,
                        onVerseClick = { verseId ->
                            Log.d("SettingsScreen", "Clicked verse ID: $verseId")
                            Toast.makeText(
                                context,
                                "For the word of God is living and active, sharper than any two-edged sword.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
                "Warm" -> {
                    TextContent(
                        searchResults = data,
                        onVerseClick = { verseId ->
                            Log.d("SettingsScreen", "Clicked verse ID: $verseId")
                            Toast.makeText(
                                context,
                                "Go in peace, be warmed and filled!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))


}
