package com.vivek.marksapp.presentation.question_listings

import com.vivek.marksapp.domain.model.QuestionListing
import com.vivek.marksapp.domain.model.QuestionListingFilter

data class QuestionListingsState(
    val questions: List<QuestionListing> = emptyList(),
    val filteredQuestions: List<QuestionListing> = emptyList(),
    val isLoading: Boolean = true,
    val filter: QuestionListingFilter = QuestionListingFilter.NotAttempted
)
