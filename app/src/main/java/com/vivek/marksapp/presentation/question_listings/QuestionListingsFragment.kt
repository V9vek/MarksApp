package com.vivek.marksapp.presentation.question_listings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vivek.marksapp.R
import com.vivek.marksapp.databinding.FragmentQuestionListingsBinding
import com.vivek.marksapp.domain.model.QuestionListingFilter.Attempted
import com.vivek.marksapp.domain.model.QuestionListingFilter.NotAttempted
import com.vivek.marksapp.presentation.question_listings.QuestionListingsEvent.*
import com.vivek.marksapp.presentation.question_listings.adapter.QuestionListingsAdapter
import com.vivek.marksapp.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionListingsFragment : Fragment(R.layout.fragment_question_listings) {

    private lateinit var binding: FragmentQuestionListingsBinding

    private val viewModel by viewModels<QuestionListingsViewModel>()

    private val questionListingsAdapter = QuestionListingsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionListingsBinding.bind(view)

        viewModel.getQuestionListings()
        setListeners()
        initRecyclerView()
        collectUIState()
        collectUIEvents()
    }

    private fun setListeners() {
        binding.apply {
            tvAttemptedUnattempted.setOnClickListener {
                if (viewModel.uiState.value.filter is NotAttempted) {
                    viewModel.onTriggerEvent(
                        event = UpdateQuestionsFilter(filter = Attempted)
                    )
                } else {
                    viewModel.onTriggerEvent(
                        event = UpdateQuestionsFilter(filter = NotAttempted)
                    )
                }
            }

            layoutEmptyPlaceholder.apply {
                ivPlaceholder.loadImage(R.drawable.ic_empty_placeholder)
                btnClearFilters.setOnClickListener {
                    viewModel.clearIsAttemptedFilter()
                }
            }
        }
    }

    private fun collectUIState() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect { state ->
                binding.apply {
                    rvQuestions.isVisible = false
                    layoutShimmer.isVisible = state.isLoading
                    if (!state.isLoading) {
                        layoutEmptyPlaceholder.root.isVisible = state.filteredQuestions.isEmpty()
                        questionListingsAdapter.submitList(state.filteredQuestions)
                        rvQuestions.isVisible = state.filteredQuestions.isNotEmpty()
                    }

                    tvAttemptedUnattempted.text = state.filter.uiValue
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
                    is NavigateToQuestionScreen -> {
                        navigateToQuestionFragment(
                            questionId = event.questionId,
                            questionNo = event.questionNo
                        )
                    }
                    is UpdateQuestionsFilter -> {
                        viewModel.updateQuestionsFilter(event.filter)
                    }
                    is RefreshList -> {
                        viewModel.getQuestionListings()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvQuestions.apply {
            adapter = questionListingsAdapter
            setHasFixedSize(true)
        }
        questionListingsAdapter.setOnItemClickListener { questionId, questionNo ->
            viewModel.onTriggerEvent(
                NavigateToQuestionScreen(
                    questionId = questionId,
                    questionNo = questionNo
                )
            )
        }
    }

    private fun navigateToQuestionFragment(questionId: String, questionNo: Int) {
        val action =
            QuestionListingsFragmentDirections.actionQuestionListingFragmentToQuestionFragment(
                questionId = questionId,
                questionNo = questionNo
            )
        findNavController().navigate(action)
    }
}



























