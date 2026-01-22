package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.CharacterRanking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingResponse(
    @SerialName("date")
    val date: String,

    @SerialName("ranking")
    val rank: Int,

    @SerialName("character_name")
    val characterName: String,

    @SerialName("world_name")
    val worldName: String,

    @SerialName("class_name")
    val className: String,

    @SerialName("sub_class_name")
    val subClassName: String,

    @SerialName("character_level")
    val characterLevel: Int,

    @SerialName("character_exp")
    val characterExp: Long,

    @SerialName("character_popularity")
    val characterPopularity: Int,

    @SerialName("character_guildname")
    val characterGuildName: String?
) {
    
    fun toDomain(): CharacterRanking {
        return CharacterRanking(
            rank = this.rank,
            characterName = this.characterName,
            worldName = this.worldName,
            className = this.className,
            subClassName = this.subClassName,
            characterLevel = this.characterLevel,
            characterExp = this.characterExp,
            characterPopularity = this.characterPopularity,
            characterGuildName = this.characterGuildName ?: "없음"
        )
    }
}