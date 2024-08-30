package com.example.jobstest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Offer
import com.example.domain.model.Vacancy
import com.example.domain.repository.ApiRepository
import com.example.domain.repository.DbRepository
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
      //  fetchOffers()
      //  fetchVacancies()
        viewModelScope.launch {
            fetchData()
            fetchFavoriteVacancies()
        }
    }

    private fun fetchFavoriteVacancies() {
        viewModelScope.launch {
            dbRepository.getItemList().collect { favorites ->
                _favoriteVacancies.value = favorites
            }
        }
    }

    private fun fetchOffers() {
        viewModelScope.launch {
            val offersList = apiRepository.getOffers()
            _offers.value = offersList
        }
    }

    private fun fetchVacancies() {
        viewModelScope.launch {
            val vacanciesList = apiRepository.getVacancies()
            _vacancies.value = vacanciesList
        }
    }


    suspend fun fetchData() {
        // Асинхронная загрузка данных
        val offersList = apiRepository.getOffers()
        _offers.value = offersList

        val vacanciesList = apiRepository.getVacancies()
        _vacancies.value = vacanciesList

    }

    fun updateVacancies(newVacancies: List<Vacancy>) {
        _vacancies.value = newVacancies
    }

    fun toggleFavorite(vacancy: Vacancy) {
        val updatedVacancies = _vacancies.value.map {
            if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it
        }
        _vacancies.value = updatedVacancies
        // Здесь добавлю сохранение и удаление в БД
        viewModelScope.launch {
            if (vacancy.isFavorite) {
                dbRepository.delete(vacancy)
            } else {
                val updatedVacancy = vacancy.copy(isFavorite = true)
                dbRepository.upsertItem(updatedVacancy)
            }
        }
    }
}