package com.vivek.marksapp.presentation.question

import com.vivek.marksapp.domain.model.QuestionListing

data class QuestionState(
    val questionNo: Int = -1,
    val question: QuestionListing? = null,
    val selectedOption: Int = -1,
    val isLoading: Boolean = true
)
