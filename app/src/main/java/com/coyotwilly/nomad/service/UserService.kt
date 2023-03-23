package com.coyotwilly.nomad.service

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface UserService {
    suspend fun getUser(): User
    suspend fun createUser(user: User): User?
    companion object {
        fun create(): UserService {
            return UserServiceImpl(
                client = HttpClient(Android) {
                    install(ContentNegotiation){
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        })
                    }
                }
            )
        }
    }
}