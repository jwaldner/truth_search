package com.wfs.truthsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique ID for each topic
    var title: String,
    var description: String,
    var notes: String = "",
    val verses: MutableList<String> = mutableListOf() // Room doesn't support lists directly
)
