package com.coyotwilly.nomad.service

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FutureTrips(
    @SerialName("id")
    val id: Long = 0,
    val startDate: String = "",
    val endDate: String = "",
    val destination: String = ""
)
