package com.sixclassguys.maplecalendar.util

import kotlinx.datetime.Month


// Month Enum 확장을 위한 헬퍼 함수
fun Month.plus(months: Long): Month {
    val res = (this.ordinal + months) % 12
    return Month.entries[if (res < 0) (res + 12).toInt() else res.toInt()]
}

fun Month.minus(months: Long): Month = plus(-months)