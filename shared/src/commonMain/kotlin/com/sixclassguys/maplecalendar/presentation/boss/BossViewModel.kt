package com.sixclassguys.maplecalendar.presentation.boss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.usecase.CreateBossPartyUseCase
import com.sixclassguys.maplecalendar.domain.usecase.GetBossPartiesUseCase
import com.sixclassguys.maplecalendar.domain.usecase.GetBossPartyDetailUseCase
import com.sixclassguys.maplecalendar.domain.usecase.GetCharactersUseCase
import com.sixclassguys.maplecalendar.util.Boss
import com.sixclassguys.maplecalendar.util.BossDifficulty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BossViewModel(
    private val reducer: BossReducer,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getBossPartiesUseCase: GetBossPartiesUseCase,
    private val createBossPartyUseCase: CreateBossPartyUseCase,
    private val getBossPartyDetailUseCase: GetBossPartyDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BossUiState>(BossUiState())
    val uiState = _uiState.asStateFlow()

    private fun getBossParties() {
        viewModelScope.launch {
            getBossPartiesUseCase().collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        onIntent(BossIntent.FetchBossPartiesSuccess(state.data))
                    }

                    is ApiState.Error -> {
                        onIntent(BossIntent.FetchBossPartiesFailed(state.message))
                    }

                    else -> {}
                }
            }
        }
    }
    
    private fun createBossParty(
        boss: Boss,
        bossDifficulty: BossDifficulty,
        title: String,
        description: String,
        characterId: Long
    ) {
        viewModelScope.launch {
            createBossPartyUseCase(boss, bossDifficulty, title, description, characterId).collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        onIntent(BossIntent.CreateBossPartySuccess(state.data))
                    }

                    is ApiState.Error -> {
                        onIntent(BossIntent.CreateBossPartyFailed(state.message))
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getBossPartyDetail(bossPartyId: Long) {
        viewModelScope.launch {
            getBossPartyDetailUseCase(bossPartyId).collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        onIntent(BossIntent.FetchBossPartyDetailSuccess(state.data))
                    }

                    is ApiState.Error -> {
                        onIntent(BossIntent.FetchBossPartyDetailFailed(state.message))
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getSavedCharacters(allWorldNames: List<String>) {
        viewModelScope.launch {
            getCharactersUseCase(allWorldNames).collect { state ->
                when (state) {
                    is ApiState.Success -> {
                        onIntent(BossIntent.FetchCharactersSuccess(state.data))
                    }

                    is ApiState.Error -> {
                        onIntent(BossIntent.FetchCharactersFailed(state.message))
                    }

                    else -> {}
                }
            }
        }
    }

    fun onIntent(intent: BossIntent) {
        _uiState.update { currentState ->
            reducer.reduce(currentState, intent)
        }

        when (intent) {
            is BossIntent.FetchBossParties -> {
                getBossParties()
            }
            
            is BossIntent.CreateBossParty -> {
                createBossParty(
                    boss = _uiState.value.selectedBoss,
                    bossDifficulty = _uiState.value.selectedBossDifficulty ?: BossDifficulty.NORMAL,
                    title = _uiState.value.bossPartyCreateTitle,
                    description = _uiState.value.bossPartyCreateDescription,
                    characterId = _uiState.value.bossPartyCreateCharacter?.id ?: 0L
                )
            }

            is BossIntent.CreateBossPartySuccess -> {
                getBossPartyDetail(intent.bossPartyId)
            }

            is BossIntent.FetchBossPartyDetail -> {
                getBossPartyDetail(intent.bossPartyId)
            }

            is BossIntent.FetchCharacters -> {
                getSavedCharacters(intent.allWorldNames)
            }

            else -> {}
        }
    }
}