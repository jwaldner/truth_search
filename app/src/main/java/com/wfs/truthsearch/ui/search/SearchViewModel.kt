package com.wfs.truthsearch.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.wfs.truthsearch.data.BibleDatabase
import com.wfs.truthsearch.utils.PreferenceManager
import com.wfs.truthsearch.utils.VerseUtils
class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _searchResults = MutableStateFlow<List<Triple<String, String, String>>>(emptyList())
    val searchResults: StateFlow<List<Triple<String, String, String>>> get() = _searchResults

    private val _isSearchViewOpen = MutableStateFlow(false)
    val isSearchViewOpen: StateFlow<Boolean> get() = _isSearchViewOpen

    fun setSearchViewOpen(isOpen: Boolean) {
        _isSearchViewOpen.value = isOpen
    }

    fun updateQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearResults() {
        _searchResults.value = emptyList()
    }

    fun performSearch(context: Context, query: String) {
        if (query.isBlank()) {
            clearResults()
            return
        }

        viewModelScope.launch {
            val database = BibleDatabase.getInstance(context)
            val fullVerseDao = database.fullVerseDao()

            val searchIndexDao = database.searchIndexDao()
            val searchIndexEsvDao = database.searchIndexEsvDao()

            val results = if (PreferenceManager.getBool(PreferenceManager.KEY_PREFS_SEARCH_ESV))
                searchIndexEsvDao.search(query)
            else
                searchIndexDao.search(query)

            val friendlyVerses = VerseUtils.formatFriendlyVerseIds(results)
            val verseText = fullVerseDao.getVerses(results)

            _searchResults.value = results.mapIndexed { index, verseId ->
                Triple(
                    verseId,
                    friendlyVerses.getOrNull(index) ?: "Unknown Reference",
                    verseText.getOrNull(index) ?: "Text not found"
                )
            }
        }
    }
}
