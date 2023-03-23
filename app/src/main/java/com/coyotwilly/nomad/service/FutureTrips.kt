package com.coyotwilly.nomad.service

@kotlinx.serialization.Serializable
data class FutureTrips(
    val id: Long = 0,
    val startDate: String = "",
    val endDate: String = "",
    val destination: String = ""
)
