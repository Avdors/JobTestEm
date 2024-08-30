package com.example.data.dbrepositoryimpl

import com.example.domain.repository.DbRepository
import com.example.data.database.MyDataBase
import com.example.domain.model.Vacancy
import kotlinx.coroutines.flow.Flow

class DbRepositoryImpl(private val db: MyDataBase) : DbRepository {
    override suspend fun upsertItem(item: Vacancy) {
        db.Dao().upsertItem(item)
    }

    override fun getItemList(): Flow<List<Vacancy>> {
        return db.Dao().getItemList()
    }

    override suspend fun delete(item: Vacancy) {
        db.Dao().delete(item)
    }
}