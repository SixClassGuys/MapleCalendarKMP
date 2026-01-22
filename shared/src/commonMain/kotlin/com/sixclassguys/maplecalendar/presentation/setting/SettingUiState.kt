package com.sixclassguys.maplecalendar.presentation.setting

data class SettingUiState(
    val isLoading: Boolean = false,
    val nexonApiKey: String? = null,
    val isLoginSuccess: Boolean = false,
    val fcmToken: String? = null,
    val isGlobalAlarmEnabled: Boolean = false,
    val errorMessage: String? = null
)