package com.sixclassguys.maplecalendar.domain.model

sealed interface BossWebSocketEvent {

    data class Chat(val data: BossPartyChat) : BossWebSocketEvent

    data class ScheduleUpdate(val candidates: List<BossPartyCommonSchedule>) : BossWebSocketEvent

    data class ScheduleCancel(val scheduleCancellation: BossPartyScheduleCancellation) :
        BossWebSocketEvent
}