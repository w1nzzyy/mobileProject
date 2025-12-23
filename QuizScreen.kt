package com.example.quizapp

import android.content.Context
import android.os.Vibrator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

// ... остальной код файла без изменений ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModel.provideFactory(vibrator)
    )

    val currentIndex by viewModel.currentIndex.collectAsState()
    val selectedOptions by viewModel.selectedOptions.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isTimeOut by viewModel.isTimeOut.collectAsState()
    val score by viewModel.score.collectAsState()

    val questions = quizQuestions
    val currentQuestion = questions.getOrNull(currentIndex)

    // Проверяем завершение викторины
    LaunchedEffect(currentIndex, isTimeOut) {
        if (currentIndex >= questions.size) {
            delay(1000L)
            navController.navigate("result/${score}/${questions.size}") {
                popUpTo("quiz") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Вопрос ${currentIndex + 1}/${questions.size}") },
                actions = {
                    Text("Время: $timeLeft")
                }
            )
        }
    ) { paddingValues ->
        if (currentQuestion == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Прогресс бар
            LinearProgressIndicator(
                progress = (currentIndex + 1).toFloat() / questions.size,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Вопрос
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.text,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Варианты ответов
                    currentQuestion.options.forEachIndexed { index, option ->
                        val isSelected = index in selectedOptions

                        Card(
                            onClick = { viewModel.selectOption(index) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (currentQuestion.isMultiple) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = { viewModel.selectOption(index) }
                                    )
                                } else {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.selectOption(index) }
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            if (isTimeOut) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Время вышло!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка подтверждения для множественного выбора
            if (currentQuestion.isMultiple) {
                Button(
                    onClick = { viewModel.confirmAnswer() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    enabled = selectedOptions.isNotEmpty()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Подтвердить ответ")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}