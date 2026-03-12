package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.data.local.AppPreferences
import kotlinx.coroutines.flow.Flow

class ToggleDarkModeStatusUseCase(
    private val dataStore: AppPreferences
) {

    val isDarkMode: Flow<Boolean> = dataStore.isDarkMode

    // 💡 다크모드 상태 업데이트
    suspend operator fun invoke(enabled: Boolean) {
        dataStore.setDarkMode(enabled)
    }
}