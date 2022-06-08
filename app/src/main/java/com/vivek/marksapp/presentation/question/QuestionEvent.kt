package com.vivek.marksapp.presentation.question

sealed class QuestionEvent {

    data class ShowToast(val message: String) : QuestionEvent()

    object NavigateToQuestionListingsScreen : QuestionEvent()

    data class ShowOptionStatus(val isCorrect: Boolean) : QuestionEvent()

    object ShowStreakStatus : QuestionEvent()
}
