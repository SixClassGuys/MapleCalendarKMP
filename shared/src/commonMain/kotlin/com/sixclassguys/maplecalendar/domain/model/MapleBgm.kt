package com.sixclassguys.maplecalendar.domain.model

import com.sixclassguys.maplecalendar.util.MapleBgmLikeStatus

data class MapleBgm(
    val id: Long,
    val title: String,
    val audioUrl: String,
    val mapName: String,
    val region: String,
    val description: String,
    val thumbnailUrl: String,
    val likeCount: Long,
    val dislikeCount: Long,
    val likeStatus: MapleBgmLikeStatus,
    val playCount: Long
)