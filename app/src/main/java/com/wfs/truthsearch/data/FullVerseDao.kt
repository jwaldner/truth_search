package com.wfs.truthsearch.data

import androidx.room.*
@Dao
interface FullVerseDao {
    @Query("SELECT COUNT(*) FROM full_verse")
    suspend fun getFullVerseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fullVerses: List<FullVerse>)

    @Query("SELECT text FROM full_verse WHERE verseId = :verseId")
    suspend fun getVerse(verseId: String): String

    // New Query: Fetch a list of text for a given list of verseIds
    @Query("SELECT text FROM full_verse WHERE verseId IN (:verseIds)")
    suspend fun getVerses(verseIds: List<String>): List<String>
}

