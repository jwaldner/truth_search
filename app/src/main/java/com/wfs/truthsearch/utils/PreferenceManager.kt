package com.wfs.truthsearch.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferenceManager {
    private const val PREF_NAME = "AppPrefs" // SharedPreferences file name
    private lateinit var preferences: SharedPreferences

     val versionMap: Map<String, String>
        get() = mapOf(
            "esv" to KEY_PLACE_HOLDER_ESV,
            "kjv" to KEY_PLACE_HOLDER_KJV,
        )


    // Keys for preferences
    const val KEY_PLACE_HOLDER_ESV = "place_holder_esv"
    const val KEY_PLACE_HOLDER_KJV = "place_holder_kjv"
    const val KEY_DATA_VERSION = "data_version"
    const val KEY_PREFS_SEARCH_RESULTS_STYLE = "key_prefs_search_results_style"


    // Initialize preferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        Log.d(PREF_NAME, "Initialized SharedPreferences")
    }

    // Save a string preference
    fun saveString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
        Log.d(PREF_NAME, "Saved string: key=$key, value=$value")
    }

    // Retrieve a string preference
    fun getString(key: String, defaultValue: String? = null): String? {
        val result = preferences.getString(key, defaultValue)
        Log.d(PREF_NAME, "Retrieved string: key=$key, value=$result")
        return result
    }

    // Save an integer preference
    fun saveInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
        Log.d(PREF_NAME, "Saved int: key=$key, value=$value")
    }

    // Retrieve an integer preference
    fun getInt(key: String, defaultValue: Int = 0): Int {
        val result = preferences.getInt(key, defaultValue)
        Log.d(PREF_NAME, "Retrieved int: key=$key, value=$result")
        return result
    }

    // Clear a specific preference
    fun clearKey(key: String) {
        preferences.edit().remove(key).apply()
        Log.d(PREF_NAME, "Cleared key: $key")
    }

    // Clear all preferences
    fun clearAll() {
        preferences.edit().clear().apply()
        Log.d(PREF_NAME, "Cleared all preferences")
    }

    fun getPlaceholderESV(verseId: String): String? {
        fun containsAllDelimiters(input: String, delimiters: List<String>): Boolean {
            return delimiters.all { delimiter -> input.contains(delimiter) }
        }

        return if (containsAllDelimiters(verseId, listOf("_", "_", ":"))) getString(KEY_PLACE_HOLDER_ESV + "_${verseId.substringBefore("_")}", "${verseId.substringBefore("_")}_01:001")
        else "01_01:001"
    }

    fun setPlaceholderESV(verseId: String) {
        saveString(KEY_PLACE_HOLDER_ESV + "_${verseId.substringBefore("_")}", verseId)
    }


    fun getVerseResultStyle(): String? {
        return getString(KEY_PREFS_SEARCH_RESULTS_STYLE, "Warm")
    }

    fun setVerseResultStyle(style: String) {
        saveString(KEY_PREFS_SEARCH_RESULTS_STYLE, style)
    }
}
