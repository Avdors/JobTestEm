package com.example.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.domain.model.Vacancy
import kotlinx.coroutines.flow.Flow

@Dao
interface DbRepository {

    @Upsert
    suspend fun upsertItem(item: Vacancy)

    @Query("select * from vacancies")
    fun getItemList(): Flow<List<Vacancy>>

    @Delete
    suspend fun delete(item: Vacancy)
}