package com.vivek.marksapp.util

import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.vivek.marksapp.databinding.FragmentQuestionBinding
import com.vivek.marksapp.domain.model.Option

fun FragmentQuestionBinding.getCardOnBasisOfSelectedOption(selectedOption: Int): Pair<MaterialCardView, TextView> {
    return when (selectedOption) {
        0 -> {
            Pair(cvOptionA, tvOptionA)
        }
        1 -> {
            Pair(cvOptionB, tvOptionB)
        }
        2 -> {
            Pair(cvOptionC, tvOptionC)
        }
        3 -> {
            Pair(cvOptionD, tvOptionD)
        }
        else -> {
            Pair(cvOptionA, tvOptionA)
        }
    }
}

fun findCorrectOptionIndex(options: List<Option>): Int {
    options.forEachIndexed { index, option ->
        if (option.isCorrect) {
            return index
        }
    }
    return -1
}

fun View.poppingAnimation(poppingAnim: Animation, isVisible: Boolean) {
    startAnimation(poppingAnim)
    if (isVisible.not()) {
        postDelayed(
            { this.isVisible = false },
            2000
        )
    }
}












