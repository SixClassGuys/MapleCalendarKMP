package com.sixclassguys.maplecalendar.domain.model

data class MapleBgmPlaylist(
    val id: Long,
    val name: String,
    val description: String,
    val isPublic: Boolean,
    val bgms: List<MapleBgm>
)