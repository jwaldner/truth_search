package com.wfs.truthsearch.ui.kjv

import TestamentDropdown
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.wfs.truthsearch.SharedViewModel
import com.wfs.truthsearch.ui.theme.AppTheme
import com.wfs.truthsearch.utils.PreferenceManager
import kotlinx.coroutines.launch
import kotlin.random.Random


class KjvFragment : Fragment() {

    private val tag ="kjvFrag"
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        val bibleVersion = "kjv"
                        TestamentDropdown(
                            modifier = Modifier.fillMaxWidth(),
                            version = bibleVersion,
                            onSelectionChange = { selectedTestament ->
                                Log.d("TestamentSelection", "Selected Testament: $selectedTestament")
                            },
                            onBookClick = { selectedBook ->
                                Log.d("kjvFrag", "Selected Book: $selectedBook")
                                Toast.makeText(
                                    context,
                                    "Selected Book: $selectedBook",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val versionEntry = PreferenceManager.versionMap.entries.first { "kjv".contains(it.key) }
        val verse = PreferenceManager.getString(versionEntry.value,"01_01:001")
        Log.w(tag, "fetch ${versionEntry} ${verse}")
        if (verse != null) {
            sharedViewModel.setVerseId(verse)
        }

        // Set the Bible version and verse ID
        sharedViewModel.setBibleVersion("kjv")
        // Assign a new ID
        sharedViewModel.setId(Random.nextInt())

        // Emit the launch browser event
        lifecycleScope.launch {
            sharedViewModel.emitLaunchBrowserEvent()
        }
    }
}