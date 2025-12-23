package com.example.quizapp

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndices: List<Int>,  // Для multiple — несколько индексов
    val isMultiple: Boolean
)

val quizQuestions = listOf(
    Question(
        "Какой язык используется в Android-разработке?",
        listOf("Java", "Kotlin", "Swift", "Python"),
        listOf(1),
        false
    ),
    Question(
        "Что такое Jetpack Compose?",
        listOf("UI toolkit", "Database", "Network library", "Testing framework"),
        listOf(0),
        false
    ),
    Question(
        "Какие компоненты Android lifecycle?",
        listOf("onCreate", "onStart", "onResume", "onPause"),
        listOf(0,1,2,3),
        true
    ),
    Question(
        "Что такое ViewModel?",
        listOf("UI controller", "Data holder", "Activity subclass", "Fragment manager"),
        listOf(1),
        false
    ),
    Question(
        "Какие библиотеки для навигации?",
        listOf("Navigation Compose", "Fragment Transaction", "Intent", "All of above"),
        listOf(3),
        false
    ),
    Question(
        "Что такое Coroutines?",
        listOf("Asynchronous programming", "Synchronous only", "UI thread blocker", "Memory leak cause"),
        listOf(0),
        false
    ),
    Question(
        "Какие способы хранения данных?",
        listOf("SharedPreferences", "Room", "Files", "SQLite"),
        listOf(0,1,2,3),
        true
    ),
    Question(
        "Что такое Material Design?",
        listOf("Google's design system", "Apple's design", "Custom UI", "No design"),
        listOf(0),
        false
    ),
    Question(
        "Какие permissions нужны для location?",
        listOf("ACCESS_FINE_LOCATION", "ACCESS_COARSE_LOCATION", "INTERNET", "Both 1 and 2"),
        listOf(3),
        false
    ),
    Question(
        "Что такое LiveData?",
        listOf("Observable data holder", "Static data", "UI view", "Service"),
        listOf(0),
        false
    )
)