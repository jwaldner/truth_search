package com.wfs.truthsearch

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wfs.truthsearch.models.Topic
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class SharedViewModel : ViewModel() {
    private val tag = "sharedViewModel"

    private var currentBibleVersion: String = "esv"
    private var currentVerseId: String = "01_01:001"
    private var currentId: Int = Random.nextInt()

    // SharedFlow for browser launch events
    private val _launchBrowserEvent = MutableSharedFlow<Triple<String, String, Int>>(replay = 0)
    val launchBrowserEvent = _launchBrowserEvent.asSharedFlow()

    // LiveData for topics
    private val _topicsLiveData = MutableLiveData<List<Topic>>(emptyList())
    val topicsLiveData: LiveData<List<Topic>> get() = _topicsLiveData

    // StateFlow for topics
    private val _topics = MutableStateFlow<List<Topic>>(emptyList())
    val topics: StateFlow<List<Topic>> = _topics.asStateFlow()

    // SharedFlow for topic update events
    private val _topicUpdateEvent = MutableSharedFlow<List<Topic>>(replay = 1)
    val topicUpdateEvent: SharedFlow<List<Topic>> = _topicUpdateEvent.asSharedFlow()

    // Temporary topic holder
    private val _temporaryTopic = MutableStateFlow<Topic?>(null)
    val temporaryTopic: StateFlow<Topic?> = _temporaryTopic.asStateFlow()

    fun setTemporaryTopic(topic: Topic) {
        _temporaryTopic.value = topic
        Log.d(tag, "Temporary topic set: $topic")
    }

    fun clearTemporaryTopic() {
        _temporaryTopic.value = null
        Log.d(tag, "Temporary topic cleared")
    }


    // Add a new topic and update LiveData, StateFlow, and SharedFlow
    fun addTopic(title: String, description: String, verses: List<String>) {
        val updatedTopics = _topics.value.toMutableList()
        val topicTitle = if (title.isBlank()) "Topic ${updatedTopics.size + 1}" else title
        updatedTopics.add(Topic(title = topicTitle,  description, verses = verses.toMutableList()))

        // Update StateFlow
        _topics.value = updatedTopics

        // Update LiveData
        _topicsLiveData.postValue(updatedTopics)

        // Emit to SharedFlow
        _topicUpdateEvent.tryEmit(updatedTopics)



    }

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

    fun sendTopicUpdateBroadcast(context: Context) {
        val intent = Intent("com.wfs.truthsearch.TOPIC_UPDATE")
        context.sendBroadcast(intent)
        Log.d(tag, "Broadcast sent for topic update.")
    }

}
