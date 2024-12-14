package com.wfs.truthsearch.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferenceManager {
    private const val PREF_NAME = "AppPrefs" // SharedPreferences file name
    private lateinit var preferences: SharedPreferences

    // Keys for preferences
    const val KEY_PLACE_HOLDER = "place_holder"
    const val KEY_DATA_VERSION = "data_version"

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
}
