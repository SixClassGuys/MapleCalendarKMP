package com.sixclassguys.maplecalendar.util

class ApiException(
    val code: Int? = null,
    override val message: String
) : Exception(message)