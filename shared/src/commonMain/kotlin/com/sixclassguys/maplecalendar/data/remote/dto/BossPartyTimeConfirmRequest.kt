package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BossPartyTimeConfirmRequest(
    val selectedIndex: Int,
    val message: String
)