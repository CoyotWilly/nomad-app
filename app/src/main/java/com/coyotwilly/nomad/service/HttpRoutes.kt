package com.coyotwilly.nomad.service

object HttpRoutes {
    private const val BASE_URL: String = "http://10.0.2.2:8080/api"
    const val NEW_USER: String = "$BASE_URL/user/sign-up"
    const val LOGIN_VERIFIER: String = "$BASE_URL/user/login"
    const val ID_MATCHER: String = "$BASE_URL/matchingUserId"
    const val PASSWORD_RESET: String = "$BASE_URL/user/passwordReset"

    fun getUser(id: Long): String{
        return "$BASE_URL/user/$id"
    }

}