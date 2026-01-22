package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.CharacterUnion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnionResponse(
    @SerialName("date")
    val date: String?,

    @SerialName("union_level")
    val unionLevel: Int,

    @SerialName("union_grade")
    val unionGrade: String,

    @SerialName("union_artifact_level")
    val unionArtifactLevel: Int,

    @SerialName("union_artifact_exp")
    val unionArtifactExp: Long,

    @SerialName("union_artifact_point")
    val unionArtifactPoint: Int
) {
    
    fun toDomain(): CharacterUnion {
        return CharacterUnion(
            unionLevel = this.unionLevel,
            unionGrade = this.unionGrade,
            unionArtifactLevel = this.unionArtifactLevel,
            unionArtifactExp = this.unionArtifactExp,
            unionArtifactPoint = this.unionArtifactPoint
        )
    }
}