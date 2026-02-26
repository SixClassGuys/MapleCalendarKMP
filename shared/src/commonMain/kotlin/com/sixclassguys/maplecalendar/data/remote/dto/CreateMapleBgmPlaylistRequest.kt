package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateMapleBgmPlaylistRequest(
    val name: String,
    val description: String,
    val isPublic: Boolean
)