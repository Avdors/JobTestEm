package com.example.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "vacancies")
data class Vacancy(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "looking_number")
    val lookingNumber: Int? = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @Embedded(prefix = "address_")
    val address: Address,

    @ColumnInfo(name = "company")
    val company: String,

    @Embedded(prefix = "experience_")
    val experience: Experience,

    @ColumnInfo(name = "published_date")
    val publishedDate: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,

    @Embedded(prefix = "salary_")
    val salary: Salary,

    @ColumnInfo(name = "schedules")
    val schedules: List<String>,

    @ColumnInfo(name = "applied_number")
    val appliedNumber: Int? = 0,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "responsibilities")
    val responsibilities: String? = null,

    @ColumnInfo(name = "questions")
    val questions: List<String>? = null
)

@Serializable
data class Address(
    @ColumnInfo(name = "town")
    val town: String,

    @ColumnInfo(name = "street")
    val street: String,

    @ColumnInfo(name = "house")
    val house: String
)

@Serializable
data class Experience(
    @ColumnInfo(name = "preview_text")
    val previewText: String,

    @ColumnInfo(name = "text")
    val text: String
)

@Serializable
data class Salary(
    @ColumnInfo(name = "short")
    val short: String? = null,

    @ColumnInfo(name = "full")
    val full: String
)