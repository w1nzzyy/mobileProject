// ResultScreen.kt (обновленный)
package com.example.quizapp

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.content.SharedPreferences

@Composable
fun ResultScreen(score: Int, total: Int, navController: NavController, prefs: SharedPreferences) {
    val context = LocalContext.current
    val username = prefs.getString("username", "Пользователь") ?: "Пользователь"
    val percentage = if (total > 0) (score * 100 / total) else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Результат, $username!",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "$score / $total",
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    "($percentage%)",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = when {
                score == total -> "🎉 Отлично! Идеальный результат!"
                score >= total * 2 / 3 -> "👍 Хорошо! Почти идеально!"
                score >= total / 2 -> "😊 Неплохо! Можно лучше."
                else -> "🤔 Попробуйте ещё раз!"
            },
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Кнопка поделиться
        OutlinedButton(
            onClick = {
                val shareText = "$username набрал $score из $total ($percentage%) в викторине по Android!"
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(intent, null))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Поделиться результатом")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка начать заново
        Button(
            onClick = {
                navController.navigate("start") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Начать заново")
        }
    }
}