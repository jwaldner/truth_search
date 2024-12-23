package com.wfs.truthsearch

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wfs.truthsearch.data.BibleDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class SearchIndexTest {

    @Test
    fun testSearchIndexReturnsResults() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = BibleDatabase.getInstance(context)
        val searchIndexDao = db.searchIndexDao()

        val search = "and he said unto them i beheld satan as lightning fall from heaven"
        //val search = "I beheld Satan as lightning fall from heaven"
        // Perform a search

        val results =  searchIndexDao.search(search)

        val count = searchIndexDao.getSearchIndexCount()
        if (count == 0) Log.e("test", "SearchIndex row count: $count")
        else  Log.d("test", "SearchIndex row count: $count")

        // Verify the search results
        assertEquals(1, results.size)
        assertEquals("42_10:018", results[0])
        assertEquals(31102, count)

    }

    @Test
    fun testSearchIndexPartialReturnsResults() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = BibleDatabase.getInstance(context)
        val searchIndexDao = db.searchIndexDao()

        val search = "I beheld Satan as lightning fall from heaven"
        // Perform a search

        val results =  searchIndexDao.search(search)

        val count = searchIndexDao.getSearchIndexCount()
        if (count == 0) Log.e("test", "SearchIndex row count: $count")
        else  Log.d("test", "SearchIndex row count: $count")


        // Verify the search results
        assertEquals(1, results.size)
        assertEquals("42_10:018", results[0])
        assertEquals(31102, count)

    }

    @Test
    fun testVerseIndexReturnsResults() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = BibleDatabase.getInstance(context)
        val fullVerseDao = db.fullVerseDao()

        val count = fullVerseDao.getFullVerseCount()
        if (count == 0) Log.e("test", "FullVerse row count: $count")
        else  Log.d("test", "FullVerse row count: $count")
        assertEquals( 31104, count)
    }

}
