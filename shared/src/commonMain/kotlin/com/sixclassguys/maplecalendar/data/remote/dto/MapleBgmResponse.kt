package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import com.sixclassguys.maplecalendar.util.MapleBgmLikeStatus
import kotlinx.serialization.Serializable

@Serializable
data class MapleBgmResponse(
    val id: Long = 0L,
    val title: String? = null,
    val audioUrl: String? = null,
    val mapName: String? = null,
    val region: String? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val likeCount: Long = 0L,
    val dislikeCount: Long = 0L,
    val likeStatus: MapleBgmLikeStatus? = null,
    val playCount: Long = 0L
) {

    fun toDomain(): MapleBgm {
        return MapleBgm(
            id = this.id,
            title = this.title ?: "",
            audioUrl = this.audioUrl ?: "",
            mapName = this.mapName ?: "",
            region = this.region ?: "",
            description = this.description ?: "",
            thumbnailUrl = this.thumbnailUrl ?: "",
            likeCount = this.likeCount,
            dislikeCount = this.dislikeCount,
            likeStatus = this.likeStatus ?: MapleBgmLikeStatus.NONE,
            playCount = this.playCount
        )
    }
}