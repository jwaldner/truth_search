package com.wfs.truthsearch.ui.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


@Composable
fun JustifiedContent(
    viewModel: SearchViewModel,
    onVerseClick: (String) -> Unit
) {
    val tag = "SearchFragmentUI"
    var isDialogVisible by rememberSaveable { mutableStateOf(true) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val context = LocalContext.current

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                isDialogVisible = false
                viewModel.updateQuery("")  // Clear the search query
                viewModel.clearResults()  // Clear the search results
            },
            title = { Text("Search Dialog") },
            text = {
                Column {
                    val focusManager = LocalFocusManager.current
                    val keyboardController = LocalSoftwareKeyboardController.current

                    // Input for search query
                    OutlinedTextField(

                        value = searchQuery,
                        onValueChange = {
                            viewModel.updateQuery(it)
                            if (it.isBlank()) {
                                viewModel.clearResults()
                            }
                        },
                        label = { Text("Enter search query") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchQuery.isNotBlank()) {

                                    viewModel.performSearch(context, searchQuery)
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Display search results
                    LazyColumn {
                        items(searchResults) { result ->
                            val (verseId, friendlyVerse, text) = result

                            Row(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                // Friendly verse (blue and clickable)
                                Text(
                                    text = "$friendlyVerse: ",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.clickable {
                                        Log.d(tag, "Clicked verse ID: $verseId") // Log the verseId
                                        onVerseClick(verseId)
                                    }
                                )
                                // Verse text (natural wrapping, not clickable)
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                TextButton(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.performSearch(context, searchQuery)
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
                ) {
                    Text("Search")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDialogVisible = false
                    viewModel.setSearchViewOpen(false)
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
@Composable
fun JustifiedContent(
    result: Triple<String, String, String>, // Single result as a triple
    onVerseClick: (String) -> Unit
) {
    val (verseId, friendlyVerse, text) = result

    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        // Friendly verse (blue and clickable)
        Text(
            text = "$friendlyVerse: ",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                Log.d("VerseSearchJustified", "Clicked verse ID: $verseId")
                onVerseClick(verseId)
            }
        )
        // Verse text (natural wrapping, not clickable)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
