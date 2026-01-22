package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.CharacterDojangRanking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DojangRankingResponse(
    @SerialName("date")
    val date: String?,

    @SerialName("character_class")
    val characterClass: String?,

    @SerialName("world_name")
    val worldName: String?,

    @SerialName("dojang_best_floor")
    val dojangBestFloor: String?,

    @SerialName("date_dojang_record")
    val dateDojangRecord: String?,

    @SerialName("dojang_best_time")
    val dojangBestTime: Long?,
) {
    
    fun toDomain(): CharacterDojangRanking {
        return CharacterDojangRanking(
            characterClass = this.characterClass ?: "초보자",
            worldName = this.worldName ?: "스카니아",
            dojangBestFloor = this.dojangBestFloor ?: "0",
            dateDojangRecord = this.dateDojangRecord ?: "2003-04-29",
            dojangBestTime = this.dojangBestTime ?: 0L
        )
    }
}