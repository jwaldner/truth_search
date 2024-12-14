package com.wfs.truthsearch.utils

import android.util.Log
import org.json.JSONObject

    fun parsePayload(payloadString: String) {
        try {
            // Parse the JSON string
            val jsonObject = JSONObject(payloadString)
            val payload = jsonObject.optString("payload", "")

            if (payload.contains("_") && payload.contains(":")) {
                // Split the payload into bookId, chapter, and verse
                val (bookId, chapterAndVerse) = payload.split("_", limit = 2)
                val (chapter, verse) = chapterAndVerse.split(":", limit = 2)

                // Print the parsed components
                println("Book ID: $bookId")
                println("Chapter: $chapter")
                println("Verse: $verse")

            } else {
                Log.e("ServerStatus","Invalid payload format")
            }
        } catch (e: Exception) {
            Log.e("ServerStatus","Error parsing payload: ${e.message}")
        }
    }

