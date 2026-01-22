package com.sixclassguys.maplecalendar.presentation.calendar

import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

data class CalendarUiState(
    val isLoading: Boolean = false,
    val nexonApiKey: String? = null,
    val isGlobalAlarmEnabled: Boolean = false,
    val isRefreshing: Boolean = false,
    val monthOffset: Int = 0,
    val year: Int = 0,
    val month: Month = Month.JANUARY,
    val days: List<LocalDate?> = emptyList(),
    val eventsMapByDay: Map<String, List<MapleEvent>> = emptyMap(),
    val eventsMapByMonth: Map<String, List<MapleEvent>> = emptyMap(),
    val selectedDate: LocalDate? = null,
    val selectedEvent: MapleEvent? = null,
    val isNotificationEnabled: Boolean = false,
    val showAlarmDialog: Boolean = false,
    val scheduledNotifications: List<LocalDateTime> = emptyList(),
    val showBottomSheet: Boolean = false,
    val errorMessage: String? = null
)