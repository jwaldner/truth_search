package com.wfs.truthsearch.models

data class Topic(
    var title: String,
    var description: String,
    var notes: String = "",
    val verses: MutableList<String> = mutableListOf()
) {
    companion object {
        const val ACTION_TOPIC_UPDATE = "com.wfs.truthsearch.TOPIC_UPDATE"
    }
}
