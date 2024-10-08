package com.example.jobstest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Offer
import com.example.domain.model.Vacancy
import com.example.domain.repository.ApiRepository
import com.example.domain.repository.DbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DbRepository
) : ViewModel() {



    private val _offers = MutableStateFlow<List<Offer>>(emptyList())
    val offers: StateFlow<List<Offer>> = _offers

    private val _vacancies = MutableStateFlow<List<Vacancy>>(emptyList())
    val vacancies: StateFlow<List<Vacancy>> = _vacancies

    private val _favoriteVacancies = MutableStateFlow<List<Vacancy>>(emptyList())
    val favoriteVacancies: StateFlow<List<Vacancy>> = _favoriteVacancies

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchFavoriteVacancies()
        }
        viewModelScope.launch(Dispatchers.IO) {
            fetchData()
        }
    }


    private fun fetchFavoriteVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.getItemList().collect { favorites ->
                _favoriteVacancies.value = favorites
            }
        }
    }


    suspend fun fetchData() {
        // Асинхронная загрузка данных
        val offersList = apiRepository.getOffers()
        _offers.value = offersList

        val vacanciesList = apiRepository.getVacancies()
        _vacancies.value = vacanciesList

    }

    fun toggleFavorite(vacancy: Vacancy) {
        val updatedVacancies = _vacancies.value.map {
            if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it
        }
        _vacancies.value = updatedVacancies
        // Здесь добавлю сохранение и удаление в БД
        viewModelScope.launch(Dispatchers.IO) {
            if (vacancy.isFavorite) {
                dbRepository.delete(vacancy)
            } else {
                val updatedVacancy = vacancy.copy(isFavorite = true)
                dbRepository.upsertItem(updatedVacancy)
            }
        }
    }
}