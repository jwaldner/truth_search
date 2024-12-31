package com.wfs.truthsearch.ui.search

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp


@Composable
fun TextContent(
    viewModel: SearchViewModel,
    onVerseClick: (String) -> Unit
) {
    val tag = "SearchWithClickableVerses"
    var isDialogVisible by rememberSaveable { mutableStateOf(true) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val context = LocalContext.current


    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDialogVisible = false },
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

                            // Build AnnotatedString for clickable verses
                            val annotatedLinkString = buildAnnotatedString {
                                pushStringAnnotation(
                                    tag = "verse",
                                    annotation = verseId
                                )
                                withStyle(
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.primary
                                    ).toSpanStyle()
                                ) {
                                    append("$friendlyVerse: ")
                                }
                                pop()
                                append(text)
                            }

                            ClickableText(
                                text = annotatedLinkString,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                onClick = { offset ->
                                    annotatedLinkString.getStringAnnotations(
                                        tag = "verse",
                                        start = offset,
                                        end = offset
                                    ).firstOrNull()?.let { annotation ->
                                        Log.d(tag, "Clicked verse ID: ${annotation.item}")
                                        onVerseClick(annotation.item)
                                    }
                                }
                            )
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
fun TextContent(
    result: Triple<String, String, String>,
    onVerseClick: (String) -> Unit
) {
    val (verseId, friendlyVerse, text) = result

    // Build AnnotatedString for clickable verses
    val annotatedLinkString = buildAnnotatedString {
        pushStringAnnotation(
            tag = "verse",
            annotation = verseId
        )
        withStyle(
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ).toSpanStyle()
        ) {
            append("$friendlyVerse: ")
        }
        pop()
        append(text)
    }

    ClickableText(
        text = annotatedLinkString,
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { offset ->
            annotatedLinkString.getStringAnnotations(
                tag = "verse",
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                Log.d("VerseSearchText", "Clicked verse ID: ${annotation.item}")
                onVerseClick(annotation.item)
            }
        }
    )
}
