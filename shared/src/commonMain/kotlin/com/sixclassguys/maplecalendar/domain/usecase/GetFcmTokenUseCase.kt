package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.repository.NotificationRepository

class GetFcmTokenUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getFcmToken()
    }
}