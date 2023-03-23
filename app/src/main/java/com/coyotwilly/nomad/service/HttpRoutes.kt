package com.coyotwilly.nomad.service

object HttpRoutes {
    private const val BASE_URL: String = "http://10.0.2.2:8080/api"
    const val GET_USER: String = "$BASE_URL/user/1"
    const val NEW_USER: String = "$BASE_URL/user/sign-up"
}