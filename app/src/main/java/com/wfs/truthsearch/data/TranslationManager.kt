package com.wfs.truthsearch.data

import android.content.Context
import android.util.Log

class TranslationManager(private val context: Context) {

    private val basePath = "bibles"

    /**
     * List all available translations (e.g., "esv", "kjv").
     * @return List of translation names.
     */
    fun listTranslations(): List<String> {
        return context.assets.list(basePath)?.toList() ?: emptyList()
    }

    /**
     * List all sections (e.g., "New Testament", "Old Testament") in a specific translation.
     * @param translation Name of the translation (e.g., "esv").
     * @return List of section names.
     */
    fun listSections(translation: String): List<String> {
        val translationPath = "$basePath/$translation"
        return try {
            val sections = context.assets.list(translationPath)
            if (sections == null) {
                Log.w("TranslationManager", "No sections found for path: $translationPath")
                emptyList()
            } else {
                Log.d("TranslationManager", "Found sections: ${sections.joinToString(", ")}")
                sections.toList()
            }
        } catch (e: Exception) {
            Log.e("TranslationManager", "Error accessing path: $translationPath", e)
            emptyList()
        }
    }


    /**
     * List all books in a specific section of a translation.
     * @param translation Name of the translation (e.g., "esv").
     * @param section Name of the section (e.g., "New Testament").
     * @return List of book names without file extensions.
     */
    fun listBooks(translation: String, section: String): List<String> {
        val sectionPath = "$basePath/$translation/$section"
        return context.assets.list(sectionPath)?.map { it.removeSuffix(".md") } ?: emptyList()
    }

    /**
     * Load the Markdown content of a specific book in a section of a translation.
     * @param translation Name of the translation (e.g., "esv").
     * @param section Name of the section (e.g., "New Testament").
     * @param book Name of the book (e.g., "Matthew").
     * @return The content of the book as a String.
     */
    fun loadMarkdown(translation: String, section: String, book: String): String {
        val filePath = "$basePath/$translation/$section/$book.md"
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }
}
