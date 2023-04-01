package com.coyotwilly.nomad.service

object HttpRoutes {
    private const val BASE_URL: String = "http://10.0.2.2:8085/api"
    const val NEW_USER: String = "$BASE_URL/user/sign-up"
    const val LOGIN_VERIFIER: String = "$BASE_URL/user/login"
    const val ID_MATCHER: String = "$BASE_URL/matchingUserId"
    const val PASSWORD_RESET: String = "$BASE_URL/user/passwordReset"

    fun getUser(id: Long): String{
        return "$BASE_URL/user/$id"
    }
    fun getFutureTrips(id: Long): String {
        return "$BASE_URL/user/$id/get-all-trips"
    }
    fun getActiveTrips(id: Long): String {
        return "$BASE_URL/user/$id/get-all-active-trips"
    }
    fun getPastTrips(id: Long): String {
        return "$BASE_URL/user/$id/get-all-past-trips"
    }
    fun postNewImg(id: Long): String {
        return "$BASE_URL/img/add/$id"
    }
    fun postNewTrip(id: Long, imgId: Long): String {
        return "$BASE_URL/user/$id/add-trip/$imgId"
    }
}