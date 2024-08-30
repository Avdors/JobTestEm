package com.example.jobstest.di
import com.example.data.apirepositoryimpl.ApiRepositoryImpl
import com.example.data.database.MyDataBase
import com.example.domain.repository.DbRepository
import com.example.data.dbrepositoryimpl.DbRepositoryImpl
import com.example.domain.repository.ApiRepository
import com.example.jobstest.viewmodel.JobsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single {
        MyDataBase.getDataBase(androidContext())
    }
    // Регистрация HttpClient
    single {
        HttpClient(Android){
            install(ContentNegotiation){
                json()
            }
            install(Logging){
                level = LogLevel.ALL
            }
        }
    }

    single<ApiRepository> { ApiRepositoryImpl(get()) }
    single<DbRepository> { DbRepositoryImpl(get()) }

    viewModel { JobsViewModel(get(), get()) }


}