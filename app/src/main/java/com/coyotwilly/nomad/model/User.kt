package com.coyotwilly.nomad.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class User(
    @SerialName("id")
    val id: Long = 0,
    val pin: Int = 0,
    val login: String = "",
    val emailAddress: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val passportNo: String = "",
    val documentNo: String = "",
    val street: String = "",
    val homeNo: String = "",
    val apartmentNo: Int? = null,
    val city: String = "",
    val country: String = "",
    val pastTrips: Set<PastTrips> = setOf(),
    val futureTrips: Set<FutureTrips> = setOf()
)
