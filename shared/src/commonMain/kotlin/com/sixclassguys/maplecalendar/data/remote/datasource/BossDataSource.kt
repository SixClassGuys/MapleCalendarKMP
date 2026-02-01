package com.sixclassguys.maplecalendar.data.remote.datasource

import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyChatMessageRequest
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyCreateRequest
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyCreateResponse
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyDetailResponse
import com.sixclassguys.maplecalendar.data.remote.dto.BossPartyResponse
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.Flow

interface BossDataSource {

    suspend fun getBossParties(accessToken: String): List<BossPartyResponse>

    suspend fun createBossParty(accessToken: String, request: BossPartyCreateRequest): BossPartyCreateResponse

    suspend fun getBossPartyDetail(accessToken: String, bossPartyId: Long): BossPartyDetailResponse

    suspend fun connect(partyId: String, token: String)

    suspend fun sendMessage(request: BossPartyChatMessageRequest)

    suspend fun deleteMessage(messageId: Long, token: String)

    fun observeMessages(): Flow<Frame>

    suspend fun disconnect()
}