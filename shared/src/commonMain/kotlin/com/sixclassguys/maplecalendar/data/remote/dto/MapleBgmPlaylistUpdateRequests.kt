package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapleBgmPlaylistUpdateRequests(
    val name: String?,
    val isPublic: Boolean?,
    val bgmIdOrder: List<Long>
)