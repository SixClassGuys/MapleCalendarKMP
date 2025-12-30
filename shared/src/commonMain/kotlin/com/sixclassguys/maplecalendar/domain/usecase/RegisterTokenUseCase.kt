package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class RegisterTokenUseCase(
    private val repository: NotificationRepository
) {

    operator fun invoke(token: String): Flow<ApiState<Unit>> =
        repository.registerToken(token)
}