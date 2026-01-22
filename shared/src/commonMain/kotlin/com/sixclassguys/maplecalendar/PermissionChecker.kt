package com.sixclassguys.maplecalendar

expect class PermissionChecker {

    fun isNotificationGranted(): Boolean
}