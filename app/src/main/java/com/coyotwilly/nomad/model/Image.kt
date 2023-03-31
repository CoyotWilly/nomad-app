package com.coyotwilly.nomad.model

import android.graphics.Bitmap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
data class Image(
    @SerialName("id")
    val id: Long = 0,
    val name: String = "",
    val userId: Long = 0,
    val content: String = "",
    @Transient
    var bitmap: Bitmap? = null
)
