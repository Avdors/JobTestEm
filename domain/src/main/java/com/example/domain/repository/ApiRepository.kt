package com.example.domain.repository

import com.example.domain.model.Offer
import com.example.domain.model.Vacancy


interface ApiRepository {

    suspend fun getOffers(): List<Offer>

    suspend fun getVacancies(): List<Vacancy>


}