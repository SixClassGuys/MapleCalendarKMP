package com.sixclassguys.maplecalendar.service

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sixclassguys.maplecalendar.MainActivity
import com.sixclassguys.maplecalendar.domain.repository.NotificationRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val notificationRepository: NotificationRepository by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 토큰이 갱신되면 즉시 백엔드에 등록합니다.
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.registerToken(token).first()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // 🔥 중요: Napier 대신 우선 기본 안드로이드 Log를 써보세요 (초기화 문제 방지)
        android.util.Log.d("FCM_TEST", "포그라운드 메시지 수신 성공!")
        android.util.Log.d("FCM_TEST", "Title: ${message.notification?.title}")
        android.util.Log.d("FCM_TEST", "Data: ${message.data}")

        val title = message.notification?.title ?: message.data["title"] ?: "알림"
        val body = message.notification?.body ?: message.data["body"] ?: "내용이 없습니다."

        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        // 🔥 채널 ID를 기존과 다르게 설정 (예: 끝에 _V3 추가)
        val channelId = "MAPLE_CALENDAR_HIGH_V3"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "이벤트 알림",
                NotificationManager.IMPORTANCE_HIGH // 팝업(헤드업)을 위한 설정
            ).apply {
                description = "이벤트 종료 및 중요 알림"
                setShowBadge(true)
                enableLights(true)
                enableVibration(true)
                // 잠금화면에서도 보이게 설정
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 구버전 대응
            .setDefaults(NotificationCompat.DEFAULT_ALL)   // 소리, 진동 필수
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)      // 팝업을 더 강하게 유도

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}