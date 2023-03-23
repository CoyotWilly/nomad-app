package com.coyotwilly.nomad.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class UserServiceImpl(
    private var client: HttpClient
) : UserService{
    override suspend fun getUser(): User {
        return client.get{
            url(HttpRoutes.USER)
        }.body()
    }

    override suspend fun createUser(user: User): User? {
//        TODO: POST USER
        return null
    }
}