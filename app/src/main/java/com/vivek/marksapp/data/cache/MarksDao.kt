package com.vivek.marksapp.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vivek.marksapp.data.cache.model.QuestionListingEntity

@Dao
interface MarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionListings(questionListingEntities: List<QuestionListingEntity>)

    @Query("SELECT * FROM questionlistingentity where id = :id")
    suspend fun getQuestionListingById(id: String): QuestionListingEntity?

    @Query("SELECT * FROM questionlistingentity")
    suspend fun getAllQuestionListings(): List<QuestionListingEntity>

    @Query("UPDATE questionlistingentity SET isAttempted = :isAttempted WHERE id = :id")
    suspend fun updateIsAttemptedQuestionListing(
        id: String,
        isAttempted: Boolean
    )

    @Query("UPDATE questionlistingentity SET isAttempted = :isAttempted")
    suspend fun updateAllIsAttemptedQuestionListing(
        isAttempted: Boolean
    )
}
























