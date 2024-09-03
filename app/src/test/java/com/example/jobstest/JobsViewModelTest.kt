package com.example.jobstest

import com.example.domain.model.Address
import com.example.domain.model.Experience
import com.example.domain.model.Salary
import com.example.domain.model.Vacancy
import com.example.domain.repository.ApiRepository
import com.example.domain.repository.DbRepository
import com.example.jobstest.viewmodel.JobsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.quality.Strictness
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import kotlin.test.Test


@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JobsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var mockApiRepository: ApiRepository

    @Mock
    private lateinit var mockDbRepository: DbRepository

    @InjectMocks
    private lateinit var jobsViewModel: JobsViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

   // @Nested
   // inner class ToggleFavoriteTests {

        @Test
        fun toggleFavorite_changesFavoriteStatus() = runTest(testDispatcher) {
            // Arrange
            val vacancy = Vacancy(
                id = "1",
                lookingNumber = 10,
                title = "Android Developer",
                address = Address("City", "Street", "House"),
                company = "Company Name",
                experience = Experience("3 years", "еще что то"),
                publishedDate = "2023-01-01",
                isFavorite = false,
                salary = Salary("1000$", "1500$"),
                schedules = listOf("Full-time"),
                appliedNumber = 5,
                description = "Description",
                responsibilities = "Responsibilities",
                questions = listOf("Question 1", "Question 2")
            )

            // Act
            jobsViewModel.toggleFavorite(vacancy)

            // Assert
            val updatedVacancy = jobsViewModel.vacancies.value.find { it.id == vacancy.id }
            assertEquals(true, updatedVacancy?.isFavorite)
        }

        @Test
        fun toggleFavoriteRemovesVacancyFromFavoritesWhenFavorite() = runTest(testDispatcher) {
            // Arrange
            val vacancy = Vacancy(
                id = "1",
                lookingNumber = 10,
                title = "Android Developer",
                address = Address("City", "Street", "House"),
                company = "Company Name",
                experience = Experience("3 years", "Еще что то"),
                publishedDate = "2023-01-01",
                isFavorite = true,
                salary = Salary("1000$", "1500$"),
                schedules = listOf("Full-time"),
                appliedNumber = 5,
                description = "Description",
                responsibilities = "Responsibilities",
                questions = listOf("Question 1", "Question 2")
            )

            Mockito.`when`(mockDbRepository.delete(vacancy)).thenReturn(Unit)

            // Act
            jobsViewModel.toggleFavorite(vacancy)

            // Assert
            assertEquals(false, jobsViewModel.vacancies.value.find { it.id == vacancy.id }?.isFavorite)
            verify(mockDbRepository).delete(vacancy)
        }
  //  }
}