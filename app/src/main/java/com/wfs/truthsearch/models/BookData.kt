package com.wfs.truthsearch.models
import android.content.Context
import android.util.Log

data class BookData(val id: String, val displayName: String, val filename: String)

data class BookVerseData(val id: String, val displayName: String, val filename: String, val testament: String)

fun getBooksFromAssets(context: Context, testament: String): Map<String, BookData> {
    val path = when (testament) {
        "Old Testament" -> "bibles/esv/Old Testament"
        "New Testament" -> "bibles/esv/New Testament"
        else -> return emptyMap()
    }

    return try {
        context.assets.list(path)
            ?.associate { filename ->
                // Extract ID, display name, and filename
                val id = "${filename.substringBefore("_")}_01:001" // Get the prefix before the underscore (e.g., "01")
                val displayName = filename
                    .substringAfter("_") // Remove the ID prefix
                    .replace("_", " ")
                    .removeSuffix(".html") // Remove the file extension

                displayName to BookData(id, displayName, filename) // Create the key-value pair
            } ?: emptyMap()
    } catch (e: Exception) {
        Log.e("AssetError", "Error accessing assets at $path: ${e.message}")
        emptyMap()
    }
}

fun getBookFromAssets(context: Context, fileName: String): BookData? {

    val bookId = fileName.substringBefore("_").toInt()
    val testament = if (bookId < 40) {
        "Old Testament"
    } else {
        "New Testament"
    }

    val displayName = fileName
        .substringAfter("_") // Remove the ID prefix
        .replace("_", " ")
        .removeSuffix(".html") // Remove the file extension

    return getBooksFromAssets(context, testament) [displayName]
}
//
//fun getBookFromVerse(context: Context, verse: String): String? {
//
//    val bookId = verse.substringBefore("_")
//    val testament = if (bookId.toInt() < 40) {
//        "Old Testament"
//    } else {
//        "New Testament"
//    }
//    val bookMap = getBooksFromAssets(context, testament)
//
//    val id = bookMap.values.find { it.id == verse }?.id
//    return id
//}

fun resolveBookById(context: Context, bookId: String): BookData? {

    val bookList =
        getBooksFromAssets(context, "Old Testament") + getBooksFromAssets(context, "New Testament") // Combined book list
    val book =   bookList.values.firstOrNull { it.id.substringBefore("_") == bookId.substringBefore("_") }
    return book
}

fun getBookFromAssetsMyVerse(context: Context, verseId: String): BookVerseData? {
    val path = when (val id = verseId.substringBefore("_").toInt()) {
        in 1..39 -> "bibles/esv/Old Testament"
        in 40..66 -> "bibles/esv/New Testament"
        else -> return null
    }

    return try {
        context.assets.list(path)?.firstOrNull { filename ->
            filename.substringBefore("_") == verseId.substringBefore("_") // Match based on the verse ID
        }?.let { filename ->
            val id = "${filename.substringBefore("_")}_01:001"
            val displayName = filename
                .substringAfter("_")
                .replace("_", " ")
                .removeSuffix(".html")
            BookVerseData(id, displayName, filename, path)
        }
    } catch (e: Exception) {
        Log.e("AssetError", "Error accessing assets at $path: ${e.message}")
        null
    }
}
