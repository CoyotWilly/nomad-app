package com.coyotwilly.nomad.model
@kotlinx.serialization.Serializable
data class LoginCredentials(
    val emailAddress: String = "",
    val password: String = ""
)
