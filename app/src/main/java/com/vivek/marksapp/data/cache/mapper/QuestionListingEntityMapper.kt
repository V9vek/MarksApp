package com.vivek.marksapp.data.cache.mapper

import com.vivek.marksapp.data.cache.model.OptionEntity
import com.vivek.marksapp.data.cache.model.QuestionListingEntity
import com.vivek.marksapp.domain.model.Option
import com.vivek.marksapp.domain.model.QuestionListing

fun OptionEntity.toOption(): Option {
    return Option(
        id = id,
        isCorrect = isCorrect,
        text = text
    )
}

fun Option.toOptionEntity(): OptionEntity {
    return OptionEntity(
        id = id,
        isCorrect = isCorrect,
        text = text
    )
}

fun QuestionListingEntity.toQuestionListing(): QuestionListing {
    return QuestionListing(
        id = id,
        type = type,
        source = source,
        exams = exams,
        subjects = subjects,
        chapters = chapters,
        previousYearPapers = previousYearPapers,
        questionText = questionText,
        options = options.map { it.toOption() },
        solutionText = solutionText,
        isAttempted = isAttempted
    )
}

fun QuestionListing.toQuestionListingEntity(): QuestionListingEntity {
    return QuestionListingEntity(
        id = id,
        type = type,
        source = source,
        exams = exams,
        subjects = subjects,
        chapters = chapters,
        previousYearPapers = previousYearPapers,
        questionText = questionText ?: "",
        options = options.map { it.toOptionEntity() },
        solutionText = solutionText ?: "",
        isAttempted = isAttempted
    )
}

















