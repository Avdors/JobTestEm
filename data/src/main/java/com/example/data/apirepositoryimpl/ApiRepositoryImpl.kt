package com.example.data.apirepositoryimpl

import com.example.domain.model.ApiResponse
import com.example.domain.model.Offer
import com.example.domain.model.Vacancy
import com.example.domain.repository.ApiRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json

class ApiRepositoryImpl(
    private val client: HttpClient
) : ApiRepository {

    private var response: ApiResponse? = null
    // нужна процедура так как ссылка возвращает файл, а не передача JSON в теле ответа
    private suspend fun downloadAndParseJson(url: String): ApiResponse {
        val response: HttpResponse = client.get(url)

        // Сохраняем файл на устройство
        val fileBytes = response.body<ByteArray>()
        val jsonString = fileBytes.toString(Charsets.UTF_8) // Преобразование байтов в строку

        // Десериализуем JSON из строки
        return Json.decodeFromString(jsonString)
    }

    // Функция для загрузки данных
    private suspend fun fetchData() {
        if (response == null) { // Загружаем данные, только если они еще не загружены
            response = downloadAndParseJson("https://drive.usercontent.google.com/u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download")
        }
    }

    override suspend fun getOffers(): List<Offer> {
        fetchData() // Загружаем данные, если они еще не были загружены
        return response?.offers ?: emptyList() // Возвращаем offers или пустой список, если данные не загружены
    }

    override suspend fun getVacancies(): List<Vacancy> {
        fetchData() // Загружаем данные, если они еще не были загружены
        return response?.vacancies ?: emptyList() // Возвращаем vacancies или пустой список, если данные не загружены
    }
}

