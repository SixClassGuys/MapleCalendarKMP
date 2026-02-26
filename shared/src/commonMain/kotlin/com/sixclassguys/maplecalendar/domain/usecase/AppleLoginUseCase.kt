package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.LoginResult
import com.sixclassguys.maplecalendar.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AppleLoginUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        provider: String,
        idToken: String,
        fcmToken: String
    ): Flow<ApiState<LoginResult>> {
        return repository.loginWithApple(provider, idToken, fcmToken)
    }
}