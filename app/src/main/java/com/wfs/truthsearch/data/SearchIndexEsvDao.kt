package com.wfs.truthsearch.data

import androidx.room.*

@Dao
interface SearchIndexEsvDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(searchIndexes: List<SearchIndexEsv>)

    @Query("SELECT COUNT(*) FROM search_index_esv")
    suspend fun getSearchIndexCount(): Int

    @Query("SELECT verseId FROM search_index_esv WHERE text LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<String>
}
