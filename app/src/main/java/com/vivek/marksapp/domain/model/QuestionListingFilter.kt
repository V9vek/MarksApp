package com.vivek.marksapp.domain.model

sealed class QuestionListingFilter(val uiValue: String) {

    object Attempted : QuestionListingFilter(uiValue = "Attempted")

    object NotAttempted : QuestionListingFilter(uiValue = "Not Attempted")
}
