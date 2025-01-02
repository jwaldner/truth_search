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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wfs.truthsearch.R
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
fun VersePreview(
    context: Context,
    style: String,
    verse: Triple<String, String, String>
) {
    when (style) {
        "Sharpie" -> {
            SharpieContent(
                result = verse,
                onVerseClick = { verseId ->
                    Log.d("VerseClicked", verseId)
                    Toast.makeText(
                        context,
                        "For the word of God is living and active, sharper than " +
                                "any two-edged sword, piercing to the division of soul and of spirit, of " +
                                "joints and of " + "marrow, and discerning the thoughts and intentions " +
                                "of the heart.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        "Warm" -> {
            TextContent(
                result = verse,
                onVerseClick = { verseId ->
                    Log.d("VerseClicked", verseId)
                    Toast.makeText(
                        context,
                        "and one of you says to them, " +
                                "“Go in peace, be warmed and filled,” " +
                                "without giving them the things needed for the body, " +
                                "what good is that?",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        "Justified" -> {
            JustifiedContent(
                result = verse,
                onVerseClick = { verseId ->
                    Log.d("VerseClicked", verseId)
                    Toast.makeText(
                        context,
                        "For we hold that one is justified by faith apart from works of the law.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Title
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Verse Preference Section
            val ssl by viewModel.sslPref.observeAsState(PreferenceManager.getBool(PreferenceManager.KEY_PREFS_SSL))
            val sslOptions = listOf(true, false)

            Column {
                Text(
                    text = "SSL Preference",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                sslOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = ssl == option,
                            onClick = { viewModel.updateSslPref(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = option.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the current mode
            Text(
                text = if (isNightMode()) "Mode: Night" else "Mode: Day",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Preference Section
            val currentMode by viewModel.lightDarkModePref.observeAsState( PreferenceManager.getLightDarkModePref())
            val themeOptions = listOf(
                "Light" to AppCompatDelegate.MODE_NIGHT_NO,
                "Dark" to AppCompatDelegate.MODE_NIGHT_YES,
                "System Default" to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )

            Column {
                Text(
                    text = "Theme Preference",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                themeOptions.forEach { (label, mode) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = currentMode == mode,
                            onClick = {
                                viewModel.updateLightDarkModePref(mode)
                                AppCompatDelegate.setDefaultNightMode(mode)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verse Preference Section
            val verseResultStyle by viewModel.verseResultStyle.observeAsState("Warm")
            val verseOptions = listOf("Sharpie", "Warm", "Justified")

            Column {
                Text(
                    text = "Verse Preference",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                verseOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = verseResultStyle == option,
                            onClick = { viewModel.updateVerseResultStyle(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verse Preview
            val friendlyVerse = "John 3:16"
            val verseText =
                "For God so loved the world that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."
            val verseData = Triple("43_03:016", friendlyVerse, verseText)

            VersePreview(
                context = context,
                style = verseResultStyle,
                verse = verseData
            )
        }
    }
}
