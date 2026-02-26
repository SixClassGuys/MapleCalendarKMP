package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthAppleRequest(
    val provider: String,
    val idToken: String,
    val fcmToken: String,
    val platform: String
)