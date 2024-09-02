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
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.Test

@ExperimentalCoroutinesApi
class JobsViewModelTest {

    @Mock
    private lateinit var mockApiRepository: ApiRepository

    @Mock
    private lateinit var mockDbRepository: DbRepository

    private lateinit var viewModel: JobsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = JobsViewModel(mockApiRepository, mockDbRepository)
    }

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

        viewModel.toggleFavorite(vacancy)

        val updatedVacancy = viewModel.vacancies.value.find { it.id == vacancy.id }
        assertEquals(true, updatedVacancy?.isFavorite)
    }

    @Test
    fun toggleFavoriteRemovesVacancyFromFavoritesWhenFavorite()  = runTest(testDispatcher) {
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

        `when`(mockDbRepository.delete(vacancy)).thenReturn(Unit)

        // Act
        viewModel.toggleFavorite(vacancy)

        // Assert
        assertEquals(false, viewModel.vacancies.value.find { it.id == vacancy.id }?.isFavorite)
        verify(mockDbRepository).delete(vacancy)
    }
}