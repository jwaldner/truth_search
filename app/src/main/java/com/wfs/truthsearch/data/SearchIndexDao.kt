package com.wfs.truthsearch.data
import androidx.room.*

@Dao
interface SearchIndexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(searchIndexes: List<SearchIndex>)

    @Query("SELECT COUNT(*) FROM search_index")
    suspend fun getSearchIndexCount(): Int

    @Query("SELECT verseId FROM search_index WHERE text LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<String>
}