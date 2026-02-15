package com.sixclassguys.maplecalendar.domain.repository

import kotlinx.coroutines.flow.SharedFlow

interface NotificationEventBus {

    val events: SharedFlow<Long>

    val bossPartyId: SharedFlow<Long>

    val invitedPartyId: SharedFlow<Long?>

    val acceptedPartyId: SharedFlow<Long?>

    val kickedPartyId: SharedFlow<Long?> // 추방 전용 Flow

    suspend fun emitEvent(eventId: Long)

    suspend fun emitBossPartyId(bossPartyId: Long)

    suspend fun emitInvitedPartyId(partyId: Long?)

    suspend fun emitAcceptedPartyId(bossPartyId: Long?)

    suspend fun emitKickedPartyId(partyId: Long?) // 추방 신호 발생
}