package com.sixclassguys.maplecalendar.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sixclassguys.maplecalendar.domain.repository.NotificationRepository
import io.github.aakira.napier.Napier
import org.koin.android.ext.android.inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // NotificationRepository 주입, 다만 Service는 필드 주입을 해야 함
    private val notificationRepository: NotificationRepository by inject()

    // 새로운 토큰이 발급될 때마다 호출, 이 토큰은 추후 DB에 저장
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Napier.d("Refreshed token: $token")
        // TODO: 백엔드 서버로 토큰 전송하는 로직 추가 예정
    }

    // 포그라운드 상태에서 알림을 받았을 때 호출
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Napier.d("From: ${message.from}")

        message.notification?.let {
            Napier.d("Message Notification Body: ${it.body}")
        }
    }
}