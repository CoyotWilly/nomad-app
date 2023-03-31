package com.coyotwilly.nomad.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PastTrips(
    @SerialName("id")
    val id: Long = 0,
    val startDate: String = "",
    val endDate: String = "",
    val destination: String = "",
    val memories: List<Image> = arrayListOf(Image())
)
