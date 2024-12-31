package com.wfs.truthsearch.utils
import org.json.JSONObject

fun validatePayload(jsonString: String): Boolean {
    return try {
        val json = JSONObject(jsonString)
        json.has("payload") && json.has("version")
    } catch (e: Exception) {
        false
    }
}


fun extractPayload(jsonString: String): Pair<String, String> {
    val jsonObject = JSONObject(jsonString)
    val payload = jsonObject.getString("payload")
    val version = jsonObject.getString("version")
    return payload to version
}