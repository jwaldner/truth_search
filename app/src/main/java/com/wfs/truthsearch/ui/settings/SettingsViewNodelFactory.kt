package com.wfs.truthsearch.ui.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wfs.truthsearch.utils.PreferenceManager

class SettingsViewModelFactory(
    private val prefManager: PreferenceManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(prefManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
