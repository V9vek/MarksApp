package com.vivek.marksapp.data.json

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vivek.marksapp.R
import com.vivek.marksapp.data.remote.mapper.toQuestionListing
import com.vivek.marksapp.data.remote.model.QuestionListingDto
import com.vivek.marksapp.domain.model.QuestionListing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionListingsParser @Inject constructor(
    private val gson: Gson,
    private val context: Application
) : JsonParser<QuestionListing> {

    override suspend fun parse(): List<QuestionListing> = withContext(Dispatchers.IO) {
        val jsonResponseString = context.resources
            .openRawResource(R.raw.gravitation_response)
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<QuestionListingDto>>() {}.type

        val questionListingsDto = gson.fromJson<List<QuestionListingDto>>(jsonResponseString, type)

        questionListingsDto.map { it.toQuestionListing() }
    }
}

















