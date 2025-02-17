package com.wfs.truthsearch.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wfs.truthsearch.utils.PreferenceManager

class SettingsViewModel(private val prefManager: PreferenceManager) : ViewModel() {


    init {
        // Apply the saved preference on startup
        PreferenceManager.getLightDarkModePref()?.let { AppCompatDelegate.setDefaultNightMode(it) }
    }

    // Backing property to observe the current verse result style
    private val _verseResultStyle = MutableLiveData<String>().apply {
        value = prefManager.getVerseResultStyle() ?: "Warm" // Default to "Warm"
    }
    val verseResultStyle: LiveData<String> = _verseResultStyle

    // Function to update the selected verse result style
    fun updateVerseResultStyle(style: String) {
        _verseResultStyle.value = style
        prefManager.setVerseResultStyle(style) // Save to the preference manager
    }

    // Backing property to observe the current light/dark mode preference
    private val _lightDarkModePref = MutableLiveData<Int>().apply {
        value = prefManager.getLightDarkModePref() ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // Default to system
    }
    val lightDarkModePref: LiveData<Int> get() = _lightDarkModePref

    // Function to update the selected light/dark mode preference
    fun updateLightDarkModePref(pref: Int) {
        _lightDarkModePref.value = pref
        prefManager.setLightDarkModePref(pref) // Save to the preference manager
    }

    // Backing property to observe the current ssl preference
    private val _sslPref = MutableLiveData<Boolean>().apply {
        value = prefManager.getBool(PreferenceManager.KEY_PREFS_SSL) ?: false // Default to system
    }

    val sslPref: LiveData<Boolean> get() = _sslPref

    // Function to update the selected light/dark mode preference
    fun updateSslPref(pref: Boolean) {
        _sslPref.value = pref
        prefManager.saveBool(PreferenceManager.KEY_PREFS_SSL, pref) // Save to the preference manager
    }

    // Backing property to observe the current search index preference
    private val _searchIndexPref = MutableLiveData<Boolean>().apply {
        value = prefManager.getBool(PreferenceManager.KEY_PREFS_SEARCH_ESV) ?: false // Default to system
    }

    val searchIndexPref: LiveData<Boolean> get() = _searchIndexPref

    // Function to update the selected light/dark mode preference
    fun updateSearchIndexPref(pref: Boolean) {
        _searchIndexPref.value = pref
        prefManager.saveBool(PreferenceManager.KEY_PREFS_SEARCH_ESV, pref) // Save to the preference manager
    }





}
