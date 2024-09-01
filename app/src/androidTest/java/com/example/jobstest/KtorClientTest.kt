package com.example.jobstest

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class KtorClientTest {
    private val mockEngine = MockEngine { request ->
        respond(
            content = """{"message": "Hello, World!"}""",
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
        )
    }

    private val client = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    @Test
    fun testKtorClient() = runBlocking {
        val response: HttpResponse = client.get("https://example.com/api")
        val body = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"message": "Hello, World!"}""", body)
    }
}