package com.coyotwilly.nomad.service

import com.coyotwilly.nomad.model.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface UserService {
    suspend fun getUser(id: Long): User
    suspend fun getFutureTrips(id: Long): List<FutureTrips>
    suspend fun getActiveTrips(id: Long): List<ActiveTrips>
    suspend fun getPastTrips(id: Long): List<PastTrips>
    suspend fun getCommunityPhotos(): List<Image>
    suspend fun createUser(user: User): User?
    suspend fun postFutureTrip(id: Long, imgId: Long, futureTrip: FutureTrips)
    suspend fun postImg(id: Long, content: ByteArray): Long
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