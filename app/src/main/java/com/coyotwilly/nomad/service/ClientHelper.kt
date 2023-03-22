package com.coyotwilly.nomad.service

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

class ClientHelper {
    companion object {
        private var instance: ClientHelper? = null
        fun createInstance(): ClientHelper {
            if (instance == null) {
                instance = ClientHelper()
                instance!!.initialize()
            }
            return instance!!
        }
    }
    private var client: HttpClient? = null
    fun initialize() {
        client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    suspend fun sendGetRequest(): String {
        return try {
            client!!.get {
                url("http://10.0.2.2:8080/api/user/1")
            }.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            e.toString()
        }
    }
}