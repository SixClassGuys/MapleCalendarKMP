package com.sixclassguys.maplecalendar

import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import kotlinx.coroutines.flow.Flow

interface MusicPlayer {

    // --- 기본적인 재생 컨트롤 ---
    fun play(bgmList: List<MapleBgm>, initialIndex: Int)

    fun replaceQueue(newBgmList: List<MapleBgm>)

    fun pause()

    fun resume()

    fun stop()

    // --- 재생 위치 제어 ---
    fun seekTo(positionMs: Long)

    val currentPosition: Flow<Long> // 현재 재생 시간 (1:00)

    val duration: Flow<Long>        // 곡의 총 길이 (1:41)

    // --- 플레이리스트 내 이동 (와이어프레임 아이콘 대응) ---
    fun skipToNext()     // 다음 곡 버튼

    fun skipToPrevious() // 이전 곡 버튼

    // --- 재생 모드 (와이어프레임 좌우측 아이콘 대응) ---
    fun toggleShuffle()                // 셔플 모드 토글

    val isShuffleModeEnabled: Flow<Boolean>

    fun setRepeatMode(mode: RepeatMode) // 반복 모드 (안함/한곡/전체)

    val repeatMode: Flow<RepeatMode>

    // --- 상태 정보 ---
    val isPlaying: Flow<Boolean>      // 재생/일시정지 버튼 아이콘 변경용

    val currentTrackId: Flow<Long?>   // 현재 어떤 곡이 재생 중인지 확인용
}

enum class RepeatMode {

    NONE,
    ONE,
    ALL
}