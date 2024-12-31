package com.wfs.truthsearch.ui.search

import SharpieContent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wfs.truthsearch.SharedViewModel
import com.wfs.truthsearch.ui.theme.AppTheme
import com.wfs.truthsearch.utils.PreferenceManager
import kotlinx.coroutines.launch
import kotlin.random.Random

class SearchFragment : DialogFragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    fun launchBrowser(verse: String) {

        verse.let {
            sharedViewModel.setVerseId(it)
        }

        sharedViewModel.setBibleVersion("esv")
        sharedViewModel.setId(Random.nextInt())

        // Emit the launch browser event
        lifecycleScope.launch {
            sharedViewModel.emitLaunchBrowserEvent()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        return ComposeView(requireContext()).apply {

            setContent {
                AppTheme {
                val verseResultStyle = PreferenceManager.getVerseResultStyle()

                when (verseResultStyle) {
                    "Warm" -> {

                        TextContent(
                            viewModel = searchViewModel,
                            onVerseClick = { launchBrowser(it) })
                    }

                    "Justified" -> {
                        JustifiedContent(
                            viewModel = searchViewModel,
                            onVerseClick = { launchBrowser(it) })
                    }
                    "Sharpie" -> {
                        SharpieContent(
                            viewModel = searchViewModel,
                            onVerseClick = { launchBrowser(it) })
                    }
                    else -> {
                        TextContent(
                            viewModel = searchViewModel,
                            onVerseClick = { launchBrowser(it) })
                    }
                }
            }
        }}
    }
}

/**
 * Composable UI for the SearchFragment
 */

