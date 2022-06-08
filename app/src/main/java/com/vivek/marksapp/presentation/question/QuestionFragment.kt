package com.vivek.marksapp.presentation.question

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.vivek.marksapp.R
import com.vivek.marksapp.databinding.FragmentQuestionBinding
import com.vivek.marksapp.presentation.question.QuestionEvent.*
import com.vivek.marksapp.util.findCorrectOptionIndex
import com.vivek.marksapp.util.getCardOnBasisOfSelectedOption
import com.vivek.marksapp.util.poppingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : Fragment(R.layout.fragment_question) {

    private lateinit var binding: FragmentQuestionBinding

    private val viewModel by viewModels<QuestionViewModel>()

    private lateinit var animation: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionBinding.bind(view)

        animation = AnimationUtils.loadAnimation(requireContext(), R.anim.popping)

        setOnClickListenerForOptions()
        setOnClickListener()
        collectUIState()
        collectUIEvents()
        collectSelectedOptionState()
        collectCorrectStreak()
    }

    private fun setOnClickListener() {
        binding.apply {
            btnBack.setOnClickListener {
                viewModel.onTriggerEvent(NavigateToQuestionListingsScreen)
            }
            btnNextArrow.setOnClickListener {
                viewModel.getNextQuestion()
            }
            btnPrevArrow.setOnClickListener {
                viewModel.getPreviousQuestion()
            }
            streakView.btnViewSolution.setOnClickListener {
                streakView.root.isVisible = false
                tvSolutionText.requestFocus()
            }
            streakView.btnNextQues.setOnClickListener {
                streakView.root.isVisible = false
                viewModel.getNextQuestion()
            }
            btnCheckAnswer.setOnClickListener {
                checkIsSelectedOptionCorrect()
                tvSolutionText.requestFocus()
                viewModel.updateIsAttemptedOfQuestion()
            }
            btnNextQuestion.setOnClickListener {
                viewModel.getNextQuestion()
            }
        }
    }

    private fun setOnClickListenerForOptions() {
        binding.apply {
            cvOptionA.setOnClickListener {
                viewModel.updateSelectedOption(0)
            }
            cvOptionB.setOnClickListener {
                viewModel.updateSelectedOption(1)
            }
            cvOptionC.setOnClickListener {
                viewModel.updateSelectedOption(2)
            }
            cvOptionD.setOnClickListener {
                viewModel.updateSelectedOption(3)
            }
        }
    }

    private fun collectUIState() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect { state ->
                binding.apply {
                    if (!state.isLoading) {
                        state.question?.let { question ->
                            tvPaper.text = "${question.exams[0]} ${question.previousYearPapers[0]}"
                            tvQuestionType.text = "Q${state.questionNo} (Single Correct)"
                            tvQuestionText.text = question.questionText
                            tvOptionAText.text = question.options[0].text
                            tvOptionBText.text = question.options[1].text
                            tvOptionCText.text = question.options[2].text
                            tvOptionDText.text = question.options[3].text
                            tvSolutionText.text = question.solutionText

                            showSolution(visible = false)
                            enableOptions(isEnabled = true)
                            showStreakStatus(isVisible = false)
                            btnCheckAnswer.isVisible = true

                            btnNextArrow.isVisible = (state.questionNo != 98)
                            btnPrevArrow.isVisible = (state.questionNo != 1)
                            btnCheckAnswer.isEnabled = false
                            tvCorrect.isVisible = false
                            tvWrong.isVisible = false
                            streakView.root.isVisible = false
                            btnNextQuestion.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun collectSelectedOptionState() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.selectedOptionState.collect { selectedOption ->
                updateSelectedOption(-1)
                updateSelectedOption(selectedOption)
            }
        }
    }

    private fun collectCorrectStreak() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.correctStreakStake.collect { count ->
                if (count == 3) {
                    viewModel.onTriggerEvent(event = ShowStreakStatus)
                }
            }
        }
    }

    private fun collectUIEvents() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is ShowToast -> {
                        Snackbar.make(
                            binding.root,
                            event.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is NavigateToQuestionListingsScreen -> {
                        navigateToQuestionListingsScreen()
                    }
                    is ShowOptionStatus -> {
                        showOptionStatus(event.isCorrect)
                    }
                    is ShowStreakStatus -> {
                        showStreakStatus(isVisible = true)
                    }
                }
            }
        }
    }

    private fun navigateToQuestionListingsScreen() {
        findNavController().popBackStack()
    }

    private fun updateSelectedOption(selectedOption: Int) {
        when (selectedOption) {
            -1 -> {
                unselectedCardUI(binding.cvOptionA, binding.tvOptionA)
                unselectedCardUI(binding.cvOptionB, binding.tvOptionB)
                unselectedCardUI(binding.cvOptionC, binding.tvOptionC)
                unselectedCardUI(binding.cvOptionD, binding.tvOptionD)
            }
            0 -> {
                selectedCardUI(binding.cvOptionA, binding.tvOptionA)
            }
            1 -> {
                selectedCardUI(binding.cvOptionB, binding.tvOptionB)
            }
            2 -> {
                selectedCardUI(binding.cvOptionC, binding.tvOptionC)
            }
            3 -> {
                selectedCardUI(binding.cvOptionD, binding.tvOptionD)
            }
        }
    }

    private fun unselectedCardUI(cardView: MaterialCardView, textView: TextView) {
        textView.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.background_circle_unselected_option
        )
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        cardView.background = null
        cardView.strokeColor = ContextCompat.getColor(
            requireContext(),
            R.color.grey_dark
        )
        cardView.isChecked = false
    }

    private fun selectedCardUI(cardView: MaterialCardView, textView: TextView) {
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        textView.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.background_circle_selected_option
        )
        cardView.strokeColor = ContextCompat.getColor(
            requireContext(),
            R.color.blue
        )
        cardView.isChecked = true

        binding.btnCheckAnswer.isEnabled = true
    }

    private fun checkIsSelectedOptionCorrect() {
        showSolution(visible = true)

        val selectedOptionIndex = viewModel.selectedOptionState.value
        val selectedOption =
            viewModel.uiState.value.question?.let { it.options[selectedOptionIndex] }

        val view = binding.getCardOnBasisOfSelectedOption(selectedOptionIndex)

        selectedOption?.let { option ->
            if (option.isCorrect) {
                viewModel.onTriggerEvent(event = ShowOptionStatus(isCorrect = true))
                correctOptionCardUI(view.first, view.second)
                viewModel.updateCorrectStreak(isCorrect = true)
            } else {
                viewModel.onTriggerEvent(event = ShowOptionStatus(isCorrect = false))
                wrongOptionCardUI(view.first, view.second)
                showCorrectOption(
                    correctionOption = findCorrectOptionIndex(
                        options = viewModel.uiState.value.question?.options ?: listOf()
                    )
                )
                viewModel.updateCorrectStreak(isCorrect = false)
            }
        }

        showNextButton()
        enableOptions(false)
    }

    private fun correctOptionCardUI(cardView: MaterialCardView, textView: TextView) {
        textView.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.background_circle_correct_option
        )
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        cardView.background = null
        cardView.strokeColor = ContextCompat.getColor(
            requireContext(),
            R.color.green
        )
        cardView.isChecked = false
    }

    private fun wrongOptionCardUI(cardView: MaterialCardView, textView: TextView) {
        textView.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.backgrond_circle_wrong_option
        )
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        cardView.background = null
        cardView.strokeColor = ContextCompat.getColor(
            requireContext(),
            R.color.red
        )
        cardView.isChecked = false
    }

    private fun showSolution(visible: Boolean) {
        binding.apply {
            tvSolutionText.isVisible = visible
            tvSolution.isVisible = visible
        }
    }

    private fun showNextButton() {
        binding.apply {
            btnCheckAnswer.apply {
                isVisible = false
                isEnabled = false
            }
            btnNextQuestion.apply {
                isVisible = true
                isEnabled = true
            }
        }
    }

    private fun showCorrectOption(correctionOption: Int) {
        if (correctionOption == -1) return

        val view = binding.getCardOnBasisOfSelectedOption(selectedOption = correctionOption)
        correctOptionCardUI(view.first, view.second)
    }

    private fun enableOptions(isEnabled: Boolean) {
        binding.apply {
            cvOptionA.isEnabled = isEnabled
            cvOptionB.isEnabled = isEnabled
            cvOptionC.isEnabled = isEnabled
            cvOptionD.isEnabled = isEnabled
        }
    }

    private fun showOptionStatus(isCorrect: Boolean) {
        binding.apply {
            tvCorrect.isVisible = isCorrect
            tvWrong.isVisible = !isCorrect
            if (tvCorrect.isVisible) {
                tvCorrect.poppingAnimation(animation, false)
            }
            if (tvWrong.isVisible) {
                tvWrong.poppingAnimation(animation, false)
            }
        }
    }

    private fun showStreakStatus(isVisible: Boolean) {
        binding.apply {
            streakView.root.isVisible = isVisible
            if (streakView.root.isVisible) {
                streakView.root.poppingAnimation(animation, true)
            }
            tvCorrect.isVisible = false
            tvWrong.isVisible = false
        }
    }
}



























