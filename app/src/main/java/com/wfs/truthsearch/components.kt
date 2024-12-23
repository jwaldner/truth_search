
// import com.wfs.truthsearch.ui.theme.TruthSearchBrowserTheme

// Jetpack Compose imports
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wfs.truthsearch.models.BookData
import com.wfs.truthsearch.models.getBooksFromAssets


@Composable
fun TestamentDropdown(
    modifier: Modifier = Modifier,
    version: String,
    onSelectionChange: (String) -> Unit,
    onBookClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Dropdown visibility state
    var selectedItem by remember { mutableStateOf("Select Testament") } // Selected testament
    var books by remember { mutableStateOf<List<String>>(emptyList()) } // List of books
    var booksMap by remember { mutableStateOf<Map<String, BookData>>(emptyMap()) }

    val context = LocalContext.current // Context for accessing assets

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Dropdown Button
        Button(
            onClick = { expanded = !expanded }, // Toggles dropdown visibility
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedItem)
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Close dropdown when dismissed
        ) {
            listOf("Old Testament", "New Testament").forEach { testament ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false // Close dropdown
                        books = emptyList() // Clear books immediately
                        selectedItem = testament // Update selected testament
                        onSelectionChange(testament) // Notify parent
                        // Delay reload of books to ensure UI clears first
                        booksMap = getBooksFromAssets(context, version, testament) // Load map of display names to filenames
                        books = booksMap.keys.toList() // Extract display names
                    },
                    text = { Text(text = testament) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable list of books
        if (books.isNotEmpty()) {
            Text(
                text = "Books in $selectedItem",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Scrollable height for the book list
            ) {
                items(books) { book ->
                    Text(
                        text = book,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onBookClick(book) // Trigger book click callback
                                books = emptyList() // Clear the book list after selection
                                val filename = booksMap[book]?.filename

                                // Genesis 28:04
                                //val url = "http://127.0.0.1:8080/bibles/esv/$selectedItem/$filename#01_28:004"
                                val url = "http://127.0.0.1:8080/bibles/${version}/$selectedItem/$filename"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }

                                if (intent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(intent) // Launch the intent
                                } else {
                                    Toast.makeText(context, "No application can handle this request.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchDialogExample() {
    // State to control the visibility of the dialog
    var isDialogOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Pair<String, String>>()) } // Verse references and texts

    // Action button
    Button(onClick = { isDialogOpen = true }) {
        Text("Open Search Dialog")
    }

    // Dialog
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text(text = "Search Bible") },
            text = {
                Column {
                    // Search input field
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Enter search query") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Search button
                    Button(
                        onClick = {
                            // Simulate a search action (replace with actual search logic)
                            searchResults = performSearch(searchQuery)
                        }
                    ) {
                        Text("Search")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // LazyColumn to display results
                    LazyColumn {
                        items(searchResults) { result ->
                            Text(text = "${result.first}: ${result.second}")
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { isDialogOpen = false }) {
                    Text("Close")
                }
            }
        )
    }
}


// Simulated search function
fun performSearch(query: String): List<Pair<String, String>> {
    // Replace with real search logic
    if (query.isBlank()) return emptyList()
    return listOf(
        "John 3:16" to "For God so loved the world...",
        "Psalm 23:1" to "The Lord is my shepherd; I shall not want."
    ).filter { it.second.contains(query, ignoreCase = true) }
}
