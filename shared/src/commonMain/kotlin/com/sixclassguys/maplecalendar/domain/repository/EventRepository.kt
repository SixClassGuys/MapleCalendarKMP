package com.sixclassguys.maplecalendar.domain.repository

import com.sixclassguys.maplecalendar.domain.model.MapleEvent

interface EventRepository {

    suspend fun getEvents(year: Int, month: Int): List<MapleEvent>
}