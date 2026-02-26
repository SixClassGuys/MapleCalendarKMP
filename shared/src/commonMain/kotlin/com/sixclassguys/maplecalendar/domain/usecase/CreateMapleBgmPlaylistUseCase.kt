package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleBgmPlaylist
import com.sixclassguys.maplecalendar.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class CreateMapleBgmPlaylistUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(
        name: String,
        description: String,
        isPublic: Boolean
    ): Flow<ApiState<List<MapleBgmPlaylist>>> {
        return repository.createPlaylist(name, description, isPublic)
    }
}