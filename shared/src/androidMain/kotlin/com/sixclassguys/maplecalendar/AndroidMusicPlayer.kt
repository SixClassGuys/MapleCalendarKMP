package com.sixclassguys.maplecalendar

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import androidx.core.net.toUri

class AndroidMusicPlayer(
    private val context: Context,
    private val servicePackageName: String
) : MusicPlayer {
    private var mediaController: MediaController? = null

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: Flow<Boolean> = _isPlaying

    private val _isShuffleModeEnabled = MutableStateFlow(false)
    override val isShuffleModeEnabled: Flow<Boolean> = _isShuffleModeEnabled

    private val _repeatMode = MutableStateFlow(RepeatMode.NONE)
    override val repeatMode: Flow<RepeatMode> = _repeatMode

    private val _currentTrackId = MutableStateFlow<Long?>(null)
    override val currentTrackId: Flow<Long?> = _currentTrackId

    init {
        val sessionToken = SessionToken(
            context,
            ComponentName(context.packageName, servicePackageName)
        )

        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener({
            try {
                mediaController = controllerFuture.get()
                setupPlayerListener()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    private fun setupPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentTrackId.value = mediaItem?.mediaId?.toLongOrNull()
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                _isShuffleModeEnabled.value = shuffleModeEnabled
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                _repeatMode.value = when (repeatMode) {
                    Player.REPEAT_MODE_ONE -> RepeatMode.ONE
                    Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                    else -> RepeatMode.NONE
                }
            }
        })
    }

    // 1초마다 현재 재생 위치를 방출 (mediaController 사용)
    override val currentPosition: Flow<Long> = flow {
        while (true) {
            emit(mediaController?.currentPosition ?: 0L)
            delay(1000)
        }
    }

    override val duration: Flow<Long> = flow {
        while (true) {
            val d = mediaController?.duration ?: 0L
            emit(if (d > 0) d else 0L)
            delay(2000)
        }
    }

    override fun play(bgmList: List<MapleBgm>, initialIndex: Int) {
        // 1. 모든 MapleBgm을 MediaItem으로 변환
        val mediaItems = bgmList.map { bgm ->
            // 아이콘 리소스를 가져오는 로직 (예시: region 필드 활용 등)
            val artworkUri = bgm.thumbnailUrl.toUri()

            val metadata = MediaMetadata.Builder()
                .setTitle(bgm.title)
                .setArtist(bgm.mapName)
                .setArtworkUri(artworkUri)
                // [추가] 시스템 UI(상태바/알림창)에 이 데이터를 우선적으로 쓰라고 명시
                .setDisplayTitle(bgm.title)
                .setSubtitle(bgm.mapName)
                .build()

            MediaItem.Builder()
                .setMediaId(bgm.id.toString())
                .setUri(bgm.audioUrl)
                .setMediaMetadata(metadata)
                .build()
        }

        mediaController?.let { controller ->
            // 2. 단일 곡이 아닌 리스트 전체를 세팅
            controller.clearMediaItems()
            controller.setMediaItems(mediaItems)

            // 3. 클릭한 곡의 위치로 이동 (0ms 지점)
            controller.seekTo(initialIndex, 0L)

            controller.prepare()
            controller.play()
        }
    }

    override fun replaceQueue(newBgmList: List<MapleBgm>) {
        val controller = mediaController ?: return

        // 1. 현재 재생 중인 정보 백업
        val currentMediaId = controller.currentMediaItem?.mediaId
        val currentPos = controller.currentPosition
        val wasPlaying = controller.isPlaying

        // 2. 새로운 리스트를 MediaItem으로 변환
        val newMediaItems = newBgmList.map { bgm ->
            MediaItem.Builder()
                .setMediaId(bgm.id.toString())
                .setUri(bgm.audioUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(bgm.title)
                        .setArtist(bgm.mapName)
                        .setArtworkUri(Uri.parse(bgm.thumbnailUrl))
                        .build()
                )
                .build()
        }

        // 3. 새 리스트에서 현재 곡의 인덱스 찾기
        val indexInNewList = newBgmList.indexOfFirst { it.id.toString() == currentMediaId }

        if (indexInNewList != -1) {
            // [핵심] 현재 곡이 새 리스트에 있다면, 해당 위치와 시간을 유지하며 교체
            controller.setMediaItems(newMediaItems, indexInNewList, currentPos)
        } else {
            // 현재 곡이 새 리스트에 없다면, 현재 곡을 0번에 강제 삽입하여 재생 유지
            // (이 부분은 기획에 따라 리스트만 갈아끼울지 결정하시면 됩니다)
            controller.setMediaItems(newMediaItems)
        }

        // 4. 상태 유지 (재생 중이었다면 계속 재생)
        if (wasPlaying) {
            controller.play()
        }
    }

    // 모든 제어 메서드를 mediaController로 변경
    override fun pause() { mediaController?.pause() }

    override fun resume() { mediaController?.play() }

    override fun stop() { mediaController?.stop() }

    override fun seekTo(positionMs: Long) { mediaController?.seekTo(positionMs) }

    override fun skipToNext() { mediaController?.seekToNext() }

    override fun skipToPrevious() { mediaController?.seekToPrevious() }

    override fun toggleShuffle() {
        mediaController?.let { it.shuffleModeEnabled = !it.shuffleModeEnabled }
    }

    override fun setRepeatMode(mode: RepeatMode) {
        mediaController?.repeatMode = when (mode) {
            RepeatMode.NONE -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }

    // release 시에는 controller만 해제 (본체인 ExoPlayer는 서비스가 onDestroy에서 해제함)
    fun release() {
        mediaController?.let {
            MediaController.releaseFuture(Futures.immediateFuture(it))
            mediaController = null
        }
    }
}