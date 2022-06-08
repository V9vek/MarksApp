package com.vivek.marksapp.domain.model

data class QuestionListing(
    val id: String,
    val type: String,
    val source: String,
    val exams: List<String>,
    val subjects: List<String>,
    val chapters: List<String>,
    val previousYearPapers: List<String>,
    val questionText: String,
    val options: List<Option>,
    val solutionText: String,
    val isAttempted: Boolean
)
