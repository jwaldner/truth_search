package com.wfs.truthsearch.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "search_index")
data class SearchIndex(
    @PrimaryKey val verseId: String,
    val text: String
)

@Entity(tableName = "full_verse")
data class FullVerse(
    @PrimaryKey val verseId: String,
    val text: String
)

@Entity(tableName = "search_index_esv")
data class SearchIndexEsv(
    @PrimaryKey val verseId: String,
    val text: String
)

@Database(entities = [SearchIndex::class, FullVerse::class, SearchIndexEsv::class], version = 1)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun searchIndexDao(): SearchIndexDao
    abstract fun fullVerseDao(): FullVerseDao
    abstract fun searchIndexEsvDao(): SearchIndexEsvDao

    companion object {
        @Volatile private var instance: BibleDatabase? = null

        fun getInstance(context: Context): BibleDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BibleDatabase::class.java,
                    "bible_database"
                ).build().also { instance = it }
            }
        }
    }
}
