package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleBgmPlaylist
import com.sixclassguys.maplecalendar.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class DeleteMapleBgmPlaylistUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(playlistId: Long): Flow<ApiState<List<MapleBgmPlaylist>>> {
        return repository.deletePlaylist(playlistId)
    }
}