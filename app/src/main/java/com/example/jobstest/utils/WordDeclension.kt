package com.example.jobstest.utils

class WordDeclension() {

    fun getHumanCountString(count: Int): String {
        val remainder10 = count % 10
        val remainder100 = count % 100

        return when {
            remainder100 in 11..14 -> "$count человек" // Для чисел 11-14
            remainder10 == 1 -> "$count человек" // Для чисел, оканчивающихся на 1 (кроме 11)
            remainder10 in 2..4 -> "$count человека" // Для чисел, оканчивающихся на 2, 3, 4 (кроме 12, 13, 14)
            else -> "$count человек" // Для всех остальных чисел
        }
    }

    fun getVacancyCountString(count: Int): String {
        val remainder10 = count % 10
        val remainder100 = count % 100

        return when {
            remainder100 in 11..14 -> "$count вакансий" // Для чисел 11-14
            remainder10 == 1 -> "$count вакансия" // Для чисел, оканчивающихся на 1 (кроме 11)
            remainder10 in 2..4 -> "$count вакансии" // Для чисел, оканчивающихся на 2, 3, 4 (кроме 12, 13, 14)
            else -> "$count вакансий" // Для всех остальных чисел
        }
    }

}