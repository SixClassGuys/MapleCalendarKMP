package com.sixclassguys.maplecalendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.usecase.GetFcmTokenUseCase
import com.sixclassguys.maplecalendar.domain.usecase.RegisterTokenUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val getFcmTokenUseCase: GetFcmTokenUseCase,
    private val registerTokenUseCase: RegisterTokenUseCase
) : ViewModel() {

    private val _registrationState = MutableStateFlow<ApiState<Unit>>(ApiState.Idle)
    val registrationState = _registrationState.asStateFlow()

    private fun registerFCMToken(token: String) {
        viewModelScope.launch {
            val response = registerTokenUseCase(token)

            response.collect { state -> _registrationState.value = state }
        }
    }

    fun initNotification() {
        viewModelScope.launch {
            try {
                val token = getFcmTokenUseCase()
                if (token != null) {
                    Napier.d("성공적으로 토큰을 가져왔습니다: $token")
                    registerFCMToken(token)
                } else {
                    Napier.w("토큰이 null입니다.")
                    _registrationState.value = ApiState.Error("토큰을 가져올 수 없습니다.")
                }
            } catch (e: Exception) {
                Napier.e("FCM 초기화 중 에러 발생", e)
                _registrationState.value = ApiState.Error("토큰을 가져올 수 없습니다.")
            }
        }
    }
}