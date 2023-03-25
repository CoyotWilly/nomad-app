package com.coyotwilly.nomad.service

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface UserService {
    suspend fun getUser(id: Long): User
    suspend fun createUser(user: User): User?
    suspend fun canLogin(user: LoginCredentials): Boolean
    suspend fun idChecker(user: LoginCredentials): Long
    suspend fun userPasswordReset(user: LoginCredentials): User
    suspend fun deleteUserAccount(id: Long): String

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