package com.vivek.marksapp.domain.repository

import com.vivek.marksapp.domain.model.QuestionListing

interface QuestionCache {

    suspend fun insertQuestionListings(questionListing: List<QuestionListing>)

    suspend fun getQuestionListingById(id: String): QuestionListing?

    suspend fun getAllQuestionListings(): List<QuestionListing>

    suspend fun updateIsAttemptedQuestionListing(
        id: String,
        isAttempted: Boolean
    )

    suspend fun updateAllIsAttemptedQuestionListing(isAttempted: Boolean)
}
























