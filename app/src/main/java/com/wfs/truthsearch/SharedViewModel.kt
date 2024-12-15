package com.wfs.truthsearch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class SharedViewModel : ViewModel() {

    private var currentBibleVersion: String = "esv"
    private var currentVerseId: String = "01_01:001"
    private var currentId: Int = Random.nextInt()

    // SharedFlow for browser launch events
    private val _launchBrowserEvent = MutableSharedFlow<Triple<String, String, Int>>(replay = 0)
    val launchBrowserEvent = _launchBrowserEvent.asSharedFlow()

    // Set Bible version
    fun setBibleVersion(version: String) {
        currentBibleVersion = version
    }

    // Set verse ID
    fun setVerseId(id: String) {
        currentVerseId = id
    }

    // Set a new ID
    fun setId(id: Int) {
        currentId = id
    }

    // Emit the browser launch event with all three values
    suspend fun emitLaunchBrowserEvent() {
        _launchBrowserEvent.emit(Triple(currentBibleVersion, currentVerseId, currentId))
    }
}
