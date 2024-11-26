package com.wfs.truthsearch

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wfs.truthsearch.data.TranslationManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TranslationManagerTest {

    private lateinit var context: Context
    private lateinit var translationManager: TranslationManager

    @Before
    fun setUp() {
        // Initialize TranslationManager with the application context
        context = ApplicationProvider.getApplicationContext()
        translationManager = TranslationManager(context)
    }

    @Test
    fun testListTranslations() {
        // Ensure translations directory exists and contains specific translations
        val translations = translationManager.listTranslations()
        assertTrue(translations.contains("esv"))
        assertTrue(translations.contains("kjv"))
    }

    @Test
    fun testListSections() {
        // Verify sections for a specific translation
        val sections = translationManager.listSections("esv")
        assertTrue(sections.contains( "New Testament"))
        assertTrue(sections.contains("Old Testament"))
    }

    @Test
    fun testListBooks() {
        // Check that books in a section of a translation are listed without extensions
        val books = translationManager.listBooks("esv", "New Testament")
        assertTrue(books.contains("01_Matthew"))
        assertTrue(books.contains("02_Mark"))
    }

    @Test
    fun testLoadMarkdownEsv() {
        // Verify the content of a specific book is loaded correctly
        val content = translationManager.loadMarkdown("esv", "New Testament", "01_Matthew")
        assertTrue(content.isNotEmpty())
        assertTrue(content.contains("he was hungry"))
    }

    @Test
    fun testLoadMarkdownKjv() {
        // Verify the content of a specific book is loaded correctly
        val content = translationManager.loadMarkdown("kjv", "New Testament", "01_Matthew")
        assertTrue(content.isNotEmpty())
        assertTrue(content.contains("an hungred"))
    }

}


