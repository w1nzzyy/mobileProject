// QuizViewModel.kt (обновленный)
package com.example.quizapp

import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(private val vibrator: Vibrator) : ViewModel() {

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _selectedOptions = MutableStateFlow(setOf<Int>())
    val selectedOptions: StateFlow<Set<Int>> = _selectedOptions.asStateFlow()

    private val _timeLeft = MutableStateFlow(30)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _isTimeOut = MutableStateFlow(false)
    val isTimeOut: StateFlow<Boolean> = _isTimeOut.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun selectOption(index: Int) {
        val current = quizQuestions.getOrNull(_currentIndex.value) ?: return

        if (current.isMultiple) {
            // Для множественного выбора
            val newSet = _selectedOptions.value.toMutableSet()
            if (index in newSet) {
                newSet.remove(index)
            } else {
                newSet.add(index)
            }
            _selectedOptions.value = newSet
        } else {
            // Для одиночного выбора
            _selectedOptions.value = setOf(index)
            // Автоматически переходим дальше
            checkAnswerAndNext()
        }
    }

    fun confirmAnswer() {
        if (_selectedOptions.value.isNotEmpty()) {
            checkAnswerAndNext()
        }
    }

    private fun checkAnswerAndNext() {
        val current = quizQuestions.getOrNull(_currentIndex.value) ?: return
        val selected = _selectedOptions.value.sorted()
        val correct = current.correctIndices.sorted()

        if (selected == correct) {
            _score.value++
        }

        moveToNextQuestion()
    }

    private fun moveToNextQuestion() {
        timerJob?.cancel()

        if (_currentIndex.value < quizQuestions.size - 1) {
            _currentIndex.value++
            _selectedOptions.value = emptySet()
            _timeLeft.value = 30
            _isTimeOut.value = false
            startTimer()
        } else {
            // Это был последний вопрос
            _currentIndex.value = quizQuestions.size
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value--
            }
            // Время вышло
            _isTimeOut.value = true
            delay(1000L) // Даем время увидеть сообщение
            moveToNextQuestion()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        fun provideFactory(vibrator: Vibrator): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return QuizViewModel(vibrator) as T
                }
            }
    }
}