package com.sixclassguys.maplecalendar.domain.repository

import com.sixclassguys.maplecalendar.domain.model.ApiState
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun getFcmToken(): String?

    fun registerToken(token: String): Flow<ApiState<Unit>>
}