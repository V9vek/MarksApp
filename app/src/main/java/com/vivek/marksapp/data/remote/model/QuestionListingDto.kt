package com.vivek.marksapp.data.remote.model

data class QuestionListingDto(
    val id: String,
    val type: String,
    val level: String? = null,
    val category: String? = null,
    val approximateTimeRequired: String? = null,
    val imageBaseUrl: String? = null,
    val helperText: String? = null,
    val source: String,
    val classes: List<String>,
    val exams: List<String>,
    val subjects: List<String>,
    val chapters: List<String>,
    val topics: List<String>,
    val concepts: List<String>,
    val microConcepts: List<String>,
    val questionLabels: List<String>,
    val previousYearPapers: List<String>,
    val question: QuestionDto,
    val options: List<OptionDto>,
    val correctValue: String,
    val solution: SolutionDto,
    val videoSolution: String,
)












