package com.vivek.marksapp.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionListingEntity(
    @PrimaryKey val id: String,
    val type: String,
    val source: String,
    val exams: List<String>,
    val subjects: List<String>,
    val chapters: List<String>,
    val previousYearPapers: List<String>,
    val questionText: String,
    val options: List<OptionEntity>,
    val solutionText: String,
    val isAttempted: Boolean
)
