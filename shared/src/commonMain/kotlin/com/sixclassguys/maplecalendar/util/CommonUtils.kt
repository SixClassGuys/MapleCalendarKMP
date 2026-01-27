package com.sixclassguys.maplecalendar.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


fun getTodayDate(): LocalDate {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
}