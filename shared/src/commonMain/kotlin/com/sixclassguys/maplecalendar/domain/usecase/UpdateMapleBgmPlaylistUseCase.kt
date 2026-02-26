package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleBgmPlaylist
import com.sixclassguys.maplecalendar.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class UpdateMapleBgmPlaylistUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(
        playlistId: Long,
        name: String?,
        isPublic: Boolean?,
        bgmIdOrder: List<Long>
    ): Flow<ApiState<MapleBgmPlaylist>> {
        return repository.updatePlaylist(playlistId, name, isPublic, bgmIdOrder)
    }
}