package com.vivek.marksapp.domain.usecases

import com.vivek.marksapp.domain.model.QuestionListing
import com.vivek.marksapp.domain.model.QuestionListingFilter
import javax.inject.Inject

class FilterQuestionListings @Inject constructor() {

    fun execute(
        current: List<QuestionListing>,
        filter: QuestionListingFilter
    ): List<QuestionListing> {
        var questionListings = current.toMutableList()

        questionListings = when (filter) {
            is QuestionListingFilter.Attempted -> {
                questionListings
                    .filter { it.isAttempted }
                    .toMutableList()
            }
            is QuestionListingFilter.NotAttempted -> {
                questionListings
                    .filter { it.isAttempted.not() }
                    .toMutableList()
            }
        }

        return questionListings
    }
}




























