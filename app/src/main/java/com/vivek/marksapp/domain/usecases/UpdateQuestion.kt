package com.vivek.marksapp.domain.usecases

import com.vivek.marksapp.domain.repository.QuestionCache
import javax.inject.Inject

class UpdateQuestion @Inject constructor(
    private val cache: QuestionCache
) {
    suspend fun execute(id: String, isAttempted: Boolean) {
        try {
            if (id == "ALL") {
                cache.updateAllIsAttemptedQuestionListing(isAttempted = isAttempted)
            } else {
                cache.updateIsAttemptedQuestionListing(id = id, isAttempted = isAttempted)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



























