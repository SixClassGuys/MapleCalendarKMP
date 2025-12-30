package com.sixclassguys.maplecalendar.data.repository

import com.sixclassguys.maplecalendar.data.remote.datasource.NotificationDataSource
import com.sixclassguys.maplecalendar.data.remote.dto.TokenRequest
import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.repository.NotificationRepository
import com.sixclassguys.maplecalendar.getPlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
import io.github.aakira.napier.Napier
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FirebaseNotificationRepository(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {

    override suspend fun getFcmToken(): String? {
        return try {
            Firebase.messaging.getToken()
        } catch (e: Exception) {
            Napier.e("Token Error", e)
            null
        }
    }

    override fun registerToken(token: String): Flow<ApiState<Unit>> = flow {
        emit(ApiState.Loading) // 로딩 시작 알림

        try {
            val response = notificationDataSource.registerToken(
                TokenRequest(
                    token = token,
                    platform = getPlatform().name
                )
            )

            if (response.status.isSuccess()) {
                emit(ApiState.Success(Unit)) // 성공 알림
            } else {
                emit(ApiState.Error("서버 에러: ${response.status}")) // 서버 측 에러
            }
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: "알 수 없는 에러")) // 네트워크 에러 등
        }
    }.flowOn(Dispatchers.IO)
}