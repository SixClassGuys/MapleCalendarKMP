package com.sixclassguys.maplecalendar.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSourceBitmapLoader
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.session.CacheBitmapLoader
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

@OptIn(UnstableApi::class)
class MusicService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    // ExoPlayer 인스턴스를 서비스 내부에서 관리
    override fun onCreate() {
        super.onCreate()

        val extractorsFactory = DefaultExtractorsFactory()
            .setConstantBitrateSeekingEnabled(true)
        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this, extractorsFactory))
            .build()
        val bitmapLoader = CacheBitmapLoader(DataSourceBitmapLoader(this))

        mediaSession = MediaSession.Builder(this, player)
            .setBitmapLoader(bitmapLoader)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 1. 서비스가 실행될 때 세션이 활성화되어 있어야 함
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    // 시스템이 서비스를 호출할 때 연결할 세션 반환
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        // AndroidMusicPlayer(MediaController)가 연결을 시도할 때 이 세션을 넘겨줌
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player

        // 1. 재생 중이거나 플레이어가 존재하면 정지 및 리소스 해제
        if (player != null) {
            if (player.playWhenReady) {
                player.stop()
            }
            // 필요한 경우 release는 onDestroy에 맡기거나 여기서 미리 수행
        }

        // 2. 서비스 종료 (알림창 제거 및 포그라운드 해제)
        stopSelf()

        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}