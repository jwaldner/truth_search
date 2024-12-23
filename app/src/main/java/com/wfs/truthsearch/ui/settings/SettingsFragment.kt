package com.wfs.truthsearch.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
// import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.ViewModelProvider
import com.wfs.truthsearch.R

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
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}

//
// h6 → headlineSmall
// h5 → it
// h4 → headlineLarge
// body1 → bodyLarge
// body2 → bodyMedium
// Smaller custom body styles → bodySmall

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val verseResultStyle by viewModel.verseResultStyle.observeAsState("Warm") // Observe LiveData from ViewModel

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "User Interface",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            val options = listOf("Sharpie", "Warm", "Justified")

            // Option buttons
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (verseResultStyle == option),
                        onClick = {
                            viewModel.updateVerseResultStyle(option) // Update ViewModel on selection
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preview Section
            Text(
                text = "Preview",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic Verse Preview
            val friendlyVerse = "John 3:16"
            val verseText = "For God so loved the world that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."

            when (verseResultStyle) {
                "Justified" -> {
                    Row(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // Friendly verse (blue and clickable)
                        Text(
                            text = "$friendlyVerse: ",
                            color = MaterialTheme.colorScheme.primary, // Blue color
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable {
                                Log.d("SettingsScreen", "Clicked verse ID: $friendlyVerse")
                            }
                        )
                        // Verse text (black and not clickable)
                        Text(
                            text = verseText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground // Black color
                            )
                        )
                    }
                }
                "Sharpie" -> {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // Friendly verse (blue and clickable)
                        Text(
                            text = "$friendlyVerse: ",
                            color = MaterialTheme.colorScheme.primary, // Blue color
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.permanent_marker)),
                                fontSize = 20.sp // Smaller font size for better fit
                            ),
                            modifier = Modifier.clickable {
                                Log.d("SettingsScreen", "Clicked verse ID: $friendlyVerse")
                            }
                        )
                        // Verse text (black and not clickable)
                        Text(
                            text = verseText,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.permanent_marker)),
                                fontSize = 16.sp, // Smaller font size for better fit
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
                "Warm" -> {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // Friendly verse (blue and clickable)
                        Text(
                            text = "$friendlyVerse: ",
                            color = MaterialTheme.colorScheme.primary, // Blue color
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable {
                                Log.d("SettingsScreen", "Clicked verse ID: $friendlyVerse")
                            }
                        )
                        // Verse text (black and not clickable)
                        Text(
                            text = verseText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.tertiary // Warm color
                            )
                        )
                    }
                }
            }
        }
    }
}
