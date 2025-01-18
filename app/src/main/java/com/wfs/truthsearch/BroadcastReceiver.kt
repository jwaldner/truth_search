package com.wfs.truthsearch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wfs.truthsearch.models.Topic

class BroadcastReceiver(private val onTopicUpdate: (Topic) -> Unit) : BroadcastReceiver() {
    val tag = "TopicBroadcast"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Topic.ACTION_TOPIC_UPDATE) {

            val title = intent.getStringExtra("topic_title") ?: return
            val description = intent.getStringExtra("topic_description") ?: ""
            val notes = intent.getStringExtra("topic_notes") ?: ""
            val verses = intent.getStringArrayListExtra("topic_verses") ?: arrayListOf()

            // Create a Topic object and pass it to the callback
            val topic = Topic(title, description,  notes, verses)
            Log.d(tag, "topic ${title} received")
            onTopicUpdate(topic)
        }
    }
}
