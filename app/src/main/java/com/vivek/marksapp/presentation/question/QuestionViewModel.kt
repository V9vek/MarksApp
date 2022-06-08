package com.vivek.marksapp.presentation.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.marksapp.domain.model.QuestionListing
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
class QuestionViewModel @Inject constructor(
    private val getQuestionListings: GetQuestionListings,
    private val updateQuestion: UpdateQuestion,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val questionListings = mutableListOf<QuestionListing>()
    var currentQuestionId: String = ""

    private val _uiState = MutableStateFlow(QuestionState())
    val uiState = _uiState.asStateFlow()

    private val _selectedOptionState = MutableStateFlow(-1)
    val selectedOptionState = _selectedOptionState.asStateFlow()

    private val _correctStreakState = MutableStateFlow(0)
    val correctStreakStake = _correctStreakState.asStateFlow()

    private val _events = MutableSharedFlow<QuestionEvent>()
    val events = _events.asSharedFlow()

    init {
        getQuestionListings()

        savedStateHandle.get<String>("questionId")?.let { questionId ->
            currentQuestionId = questionId
        }
        savedStateHandle.get<Int>("questionNo")?.let { questionNo ->
            _uiState.value = uiState.value.copy(questionNo = questionNo)
        }
    }

    fun onTriggerEvent(event: QuestionEvent) = viewModelScope.launch {
        when (event) {
            is QuestionEvent.ShowToast -> {
                _events.emit(QuestionEvent.ShowToast(message = event.message))
            }
            is QuestionEvent.NavigateToQuestionListingsScreen -> {
                _events.emit(QuestionEvent.NavigateToQuestionListingsScreen)
            }
            is QuestionEvent.ShowOptionStatus -> {
                _events.emit(QuestionEvent.ShowOptionStatus(event.isCorrect))
            }
            is QuestionEvent.ShowStreakStatus -> {
                _events.emit(QuestionEvent.ShowStreakStatus)
            }
        }
    }

    private fun getQuestionListings() {
        viewModelScope.launch {
            getQuestionListings.execute()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { questions ->
                                questionListings.addAll(questions)
                                getQuestion(id = currentQuestionId)
                            }
                        }
                        is Resource.Error -> {
                            onTriggerEvent(
                                event = QuestionEvent.ShowToast(
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

    fun updateIsAttemptedOfQuestion() {
        viewModelScope.launch {
            uiState.value.question?.let { question ->
                updateQuestion.execute(
                    id = question.id,
                    isAttempted = true
                )
            }
        }
    }

    fun updateSelectedOption(selectedOption: Int) {
        _selectedOptionState.value = selectedOption
    }

    fun updateCorrectStreak(isCorrect: Boolean) {
        if (isCorrect) {
            _correctStreakState.value = _correctStreakState.value + 1
            if (correctStreakStake.value == 4) {
                _correctStreakState.value = 1
            }
        } else {
            _correctStreakState.value = 0
        }
    }

    private fun getQuestion(id: String) {
        val question = questionListings.find { it.id == id }
        _uiState.value = uiState.value.copy(question = question)
    }

    fun getPreviousQuestion() {
        updateSelectedOption(selectedOption = -1)
        val currentQuestionNo = uiState.value.questionNo
        _uiState.value = uiState.value.copy(questionNo = currentQuestionNo - 1)
        val updatedQuestionNo = uiState.value.questionNo
        _uiState.value = uiState.value.copy(question = questionListings[updatedQuestionNo - 1])
    }

    fun getNextQuestion() {
        updateSelectedOption(selectedOption = -1)
        val currentQuestionNo = uiState.value.questionNo
        _uiState.value = uiState.value.copy(questionNo = currentQuestionNo + 1)
        val updatedQuestionNo = uiState.value.questionNo
        _uiState.value = uiState.value.copy(question = questionListings[updatedQuestionNo - 1])
    }
}































