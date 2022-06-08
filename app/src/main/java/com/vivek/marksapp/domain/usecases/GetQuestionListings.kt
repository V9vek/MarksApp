package com.vivek.marksapp.domain.usecases

import com.vivek.marksapp.data.json.JsonParser
import com.vivek.marksapp.domain.model.QuestionListing
import com.vivek.marksapp.domain.repository.QuestionCache
import com.vivek.marksapp.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQuestionListings @Inject constructor(
    private val cache: QuestionCache,
    private val questionListingsParser: JsonParser<QuestionListing>
) {
    fun execute(): Flow<Resource<List<QuestionListing>>> = flow {
        emit(Resource.Loading(isLoading = true))
        // delay(1000)     // custom delay for showing shimmer

        val cachedQuestionListings = cache.getAllQuestionListings()

        emit(Resource.Success(data = cachedQuestionListings))

        val isDbEmpty = cachedQuestionListings.isEmpty()
        val shouldLoadFromCache = !isDbEmpty

        if (shouldLoadFromCache) {
            emit(Resource.Loading(isLoading = false))
            return@flow
        }

        val remoteQuestionListings = try {
            questionListingsParser.parse()
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = "Couldn't load data"))
            null
        }

        remoteQuestionListings?.let { listings ->
            cache.insertQuestionListings(questionListing = listings)

            emit(Resource.Success(data = cache.getAllQuestionListings()))
        }

        emit(Resource.Loading(isLoading = false))
    }
}
























