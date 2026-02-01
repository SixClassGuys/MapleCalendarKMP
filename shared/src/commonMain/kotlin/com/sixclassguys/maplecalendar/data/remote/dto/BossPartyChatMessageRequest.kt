package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BossPartyChatMessageRequest(
    val bossPartyId: Long,
    val characterId: Long,
    val content: String,
    val messageType: String
)