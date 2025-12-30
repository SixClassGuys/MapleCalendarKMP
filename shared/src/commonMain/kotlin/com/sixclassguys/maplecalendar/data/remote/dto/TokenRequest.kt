package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val token: String,
    val platform: String
)
