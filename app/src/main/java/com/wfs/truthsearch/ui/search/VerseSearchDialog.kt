package com.wfs.truthsearch.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import com.wfs.truthsearch.utils.PreferenceManager

@Composable
fun VerseSearchDialog(
    viewModel: SearchViewModel,
    onSearchResults: @Composable (List<Triple<String, String, String>>) -> Unit
) {
    var isDialogVisible by rememberSaveable { mutableStateOf(true) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState() // List<Triple<String, String, String>>
    val context = LocalContext.current

    if (isDialogVisible) {
        val version = if (PreferenceManager.getBool(PreferenceManager.KEY_PREFS_SEARCH_ESV)) "[ ESV ]" else "[ KJV ]"
        AlertDialog(
            onDismissRequest = { isDialogVisible = false },

            title = { Text("Search Dialog $version") },

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

                    // Delegate rendering of search results
                    onSearchResults(searchResults)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.performSearch(context, searchQuery)
                        }
                    }
                ) {
                    Text("Search")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogVisible = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
