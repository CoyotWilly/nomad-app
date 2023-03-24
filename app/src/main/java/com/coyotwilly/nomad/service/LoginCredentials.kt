package com.coyotwilly.nomad.service
@kotlinx.serialization.Serializable
data class LoginCredentials(
    val emailAddress: String = "",
    val password: String = ""
)
