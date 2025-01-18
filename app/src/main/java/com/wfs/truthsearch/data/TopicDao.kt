
package com.wfs.truthsearch.data

import androidx.room.*
import com.wfs.truthsearch.models.TopicEntity

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity)

    @Update
    suspend fun updateTopic(topic: TopicEntity)

    @Delete
    suspend fun deleteTopic(topic: TopicEntity)

    @Query("SELECT * FROM topics WHERE id = :topicId")
    suspend fun getTopicById(topicId: Int): TopicEntity?

    @Query("SELECT * FROM topics")
    suspend fun getAllTopics(): List<TopicEntity>
}
