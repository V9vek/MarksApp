package com.vivek.marksapp.presentation.question_listings

import com.vivek.marksapp.domain.model.QuestionListingFilter

sealed class QuestionListingsEvent {

    data class ShowToast(val message: String) : QuestionListingsEvent()

    data class NavigateToQuestionScreen(
        val questionId: String,
        val questionNo: Int
    ) : QuestionListingsEvent()

    data class UpdateQuestionsFilter(val filter: QuestionListingFilter) : QuestionListingsEvent()

    object RefreshList : QuestionListingsEvent()
}
