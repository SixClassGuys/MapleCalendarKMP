package com.sixclassguys.maplecalendar.domain.model

data class MapleBgmHistory(
    val bgms: List<MapleBgm>,
    val isLastPage: Boolean
)