package com.sixclassguys.maplecalendar

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform