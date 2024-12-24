package com.wfs.truthsearch.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wfs.truthsearch.utils.PreferenceManager

class SettingsViewModel(private val prefManager: PreferenceManager) : ViewModel() {

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
}
