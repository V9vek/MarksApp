package com.vivek.marksapp.data.repository

import com.vivek.marksapp.data.cache.MarksDatabase
import com.vivek.marksapp.data.cache.mapper.toQuestionListing
import com.vivek.marksapp.data.cache.mapper.toQuestionListingEntity
import com.vivek.marksapp.domain.model.QuestionListing
import com.vivek.marksapp.domain.repository.QuestionCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionCacheImpl @Inject constructor(
    private val db: MarksDatabase
) : QuestionCache {

    private val dao = db.dao

    override suspend fun insertQuestionListings(questionListing: List<QuestionListing>) {
        dao.insertQuestionListings(questionListingEntities = questionListing.map { it.toQuestionListingEntity() })
    }

    override suspend fun getQuestionListingById(id: String): QuestionListing? {
        return dao.getQuestionListingById(id = id)?.toQuestionListing()
    }

    override suspend fun getAllQuestionListings(): List<QuestionListing> {
        return dao.getAllQuestionListings().map { it.toQuestionListing() }
    }

    override suspend fun updateIsAttemptedQuestionListing(id: String, isAttempted: Boolean) {
        dao.updateIsAttemptedQuestionListing(
            id = id,
            isAttempted = isAttempted
        )
    }

    override suspend fun updateAllIsAttemptedQuestionListing(isAttempted: Boolean) {
        dao.updateAllIsAttemptedQuestionListing(isAttempted)
    }
}













