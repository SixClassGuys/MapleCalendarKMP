package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.MapleBgmPlaylist
import kotlinx.serialization.Serializable

@Serializable
data class MapleBgmPlaylistResponse(
    val id: Long = 0L,
    val name: String? = null,
    val description: String? = null,
    val isPublic: Boolean = false,
    val bgms: List<MapleBgmResponse> = emptyList()
) {

    fun toDomain(): MapleBgmPlaylist {
        return MapleBgmPlaylist(
            id = this.id,
            name = this.name ?: "",
            description = this.description ?: "",
            isPublic = this.isPublic,
            bgms = this.bgms.map { it.toDomain() }
        )
    }
}