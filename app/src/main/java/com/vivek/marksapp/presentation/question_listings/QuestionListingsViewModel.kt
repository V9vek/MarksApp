package com.vivek.marksapp.presentation.question_listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.marksapp.domain.model.QuestionListingFilter
import com.vivek.marksapp.domain.model.QuestionListingFilter.NotAttempted
import com.vivek.marksapp.domain.usecases.FilterQuestionListings
import com.vivek.marksapp.domain.usecases.GetQuestionListings
import com.vivek.marksapp.domain.usecases.UpdateQuestion
import com.vivek.marksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionListingsViewModel @Inject constructor(
    private val getQuestionListings: GetQuestionListings,
    private val updateQuestion: UpdateQuestion,
    private val filterQuestionListings: FilterQuestionListings
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionListingsState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<QuestionListingsEvent>()
    val events = _events.asSharedFlow()

    init {
        getQuestionListings()
    }

    fun onTriggerEvent(event: QuestionListingsEvent) = viewModelScope.launch {
        when (event) {
            is QuestionListingsEvent.ShowToast -> {
                _events.emit(QuestionListingsEvent.ShowToast(message = event.message))
            }
            is QuestionListingsEvent.NavigateToQuestionScreen -> {
                _events.emit(
                    QuestionListingsEvent.NavigateToQuestionScreen(
                        questionId = event.questionId,
                        questionNo = event.questionNo
                    )
                )
            }
            is QuestionListingsEvent.UpdateQuestionsFilter -> {
                _events.emit(QuestionListingsEvent.UpdateQuestionsFilter(filter = event.filter))
            }
            is QuestionListingsEvent.RefreshList -> {
                _events.emit(QuestionListingsEvent.RefreshList)
            }
        }
    }

    fun getQuestionListings() {
        viewModelScope.launch {
            getQuestionListings.execute()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { questions ->
                                _uiState.value = uiState.value.copy(questions = questions)
                                filterQuestions()
                            }
                        }
                        is Resource.Error -> {
                            onTriggerEvent(
                                event = QuestionListingsEvent.ShowToast(
                                    result.message ?: "Unknown Error"
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _uiState.value = uiState.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    fun clearIsAttemptedFilter() {
        viewModelScope.launch {
            updateQuestion.execute(
                id = "ALL",
                isAttempted = false
            )
            updateQuestionsFilter(filter = NotAttempted)
            getQuestionListings()
        }
    }

    fun updateQuestionsFilter(filter: QuestionListingFilter) {
        _uiState.value = uiState.value.copy(filter = filter)
        filterQuestions()
    }

    private fun filterQuestions() {
        val filteredQuestions = filterQuestionListings.execute(
            current = uiState.value.questions,
            filter = uiState.value.filter
        )

        _uiState.value = uiState.value.copy(filteredQuestions = filteredQuestions)
    }
}





























