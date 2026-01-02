package com.sixclassguys.maplecalendar.presentation.calendar

import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

data class CalendarUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val year: Int = 0,
    val month: Month = Month.JANUARY,
    val days: List<LocalDate?> = emptyList(),
    val eventsMap: Map<String, List<MapleEvent>> = emptyMap(),
    val selectedDate: LocalDate? = null,
    val showBottomSheet: Boolean = false,
    val errorMessage: String? = null
)