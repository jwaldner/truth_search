package com.wfs.truthsearch.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wfs.truthsearch.data.BibleDatabase
import com.wfs.truthsearch.data.SearchIndex
import com.wfs.truthsearch.data.FullVerse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "DBUtils"

// Parse JSON into a list of SearchIndex objects for the search index
fun parseSearchIndexJson(json: String): List<SearchIndex> {
    val gson = Gson()

    // Parse the root JSON object
    val type = object : TypeToken<Map<String, Any>>() {}.type
    val root: Map<String, Any> = gson.fromJson(json, type)

    // Extract the "data" object and cast it to a Map
    val data = root["data"] as Map<String, String>

    // Convert the data map into a list of SearchIndex objects
    return data.map { SearchIndex(verseId = it.key, text = it.value.trim()) }
}

// Parse JSON into a list of FullVerse objects for the full verse data
fun parseFullVerseJson(json: String): List<FullVerse> {
    val gson = Gson()

    // Parse the root JSON object
    val type = object : TypeToken<Map<String, Any>>() {}.type
    val root: Map<String, Any> = gson.fromJson(json, type)

    // Extract the "data" object and cast it to a Map
    val data = root["data"] as Map<String, String>

    // Convert the data map into a list of FullVerse objects
    return data.map { FullVerse(verseId = it.key, text = it.value.trim()) }
}

// Load JSON file from the assets folder
fun loadJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

// Extract version from JSON
fun extractDataVersion(json: String): Int {
    val gson = Gson()
    val jsonObject = gson.fromJson(json, Map::class.java)
    return (jsonObject["version"] as Double).toInt() // Assuming version is numeric
}

// Populate the database with new data if versions differ
suspend fun populateDatabase(context: Context) {
    val database = BibleDatabase.getInstance(context)

    withContext(Dispatchers.IO) {
        // Load JSON files
        val searchIndexJson = loadJsonFromAssets(context, "bibles/kjv/search_index.json")
        val fullVerseJson = loadJsonFromAssets(context, "bibles/esv/full_verse_index.json")

        // Extract current and new versions
        val currentVersion = PreferenceManager.getInt(PreferenceManager.KEY_DATA_VERSION, 0)
        val newSearchIndexVersion = extractDataVersion(searchIndexJson)
        val newFullVerseVersion = extractDataVersion(fullVerseJson)

        // Determine if updates are required
        if (newSearchIndexVersion > currentVersion || newFullVerseVersion > currentVersion) {
            Log.wtf(TAG, "Updating database to version $newSearchIndexVersion or $newFullVerseVersion...")

            // Parse JSON data
            val searchIndexes = parseSearchIndexJson(searchIndexJson)
            val fullVerses = parseFullVerseJson(fullVerseJson)

            // Insert new data
            database.searchIndexDao().insertAll(searchIndexes)
            database.fullVerseDao().insertAll(fullVerses)

            // Save the new version in preferences
            val updatedVersion = maxOf(newSearchIndexVersion, newFullVerseVersion)
            PreferenceManager.saveInt(PreferenceManager.KEY_DATA_VERSION, updatedVersion)
            Log.wtf(TAG, "Database updated successfully to version $updatedVersion.")
        } else {
            Log.wtf(TAG, "Database is already up-to-date. Current version: $currentVersion")
        }
    }
}
