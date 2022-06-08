package com.vivek.marksapp.data.remote.mapper

import com.vivek.marksapp.data.remote.model.OptionDto
import com.vivek.marksapp.data.remote.model.QuestionListingDto
import com.vivek.marksapp.domain.model.Option
import com.vivek.marksapp.domain.model.QuestionListing

fun OptionDto.toOption(): Option {
    return Option(
        id = id,
        isCorrect = isCorrect,
        text = text
    )
}

fun QuestionListingDto.toQuestionListing(): QuestionListing {
    return QuestionListing(
        id = id,
        type = type,
        source = source,
        exams = exams,
        subjects = subjects,
        chapters = chapters,
        previousYearPapers = previousYearPapers,
        questionText = question.text,
        options = options.map { it.toOption() },
        solutionText = solution.text,
        isAttempted = false                         // initially all questions isAttempted = false
    )
}

















