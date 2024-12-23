package com.wfs.truthsearch.ui.settings

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

    // Function to update the selected style
    fun updateVerseResultStyle(style: String) {
        _verseResultStyle.value = style
        prefManager.setVerseResultStyle(style) // Save to the preference manager
    }
}
