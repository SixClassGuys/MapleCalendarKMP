package com.sixclassguys.maplecalendar.presentation.character

import io.github.aakira.napier.Napier

class MapleCharacterReducer {

    fun reduce(
        currentState: MapleCharacterUiState,
        intent: MapleCharacterIntent
    ): MapleCharacterUiState = when (intent) {
        is MapleCharacterIntent.PullToRefresh -> {
            currentState.copy(
                isLoading = true,
                isRefreshing = true
            )
        }

        is MapleCharacterIntent.FetchCharacters -> {
            currentState.copy(
                isLoading = true
            )
        }

        is MapleCharacterIntent.FetchCharactersSuccess -> {
            Napier.d("Character Summeries: ${intent.characterSummeries}")
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                characterSummeries = intent.characterSummeries
            )
        }

        is MapleCharacterIntent.FetchCharactersFailed -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                errorMessage = intent.message
            )
        }

        is MapleCharacterIntent.SelectWorldGroup -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                selectedWorldGroup = intent.worldGroup,
                selectedWorld = intent.world
            )
        }

        is MapleCharacterIntent.SelectWorld -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                selectedWorld = intent.world
            )
        }

        is MapleCharacterIntent.InitApiKey -> {
            currentState.copy(
                nexonOpenApiKey = ""
            )
        }

        is MapleCharacterIntent.UpdateApiKey -> {
            currentState.copy(
                nexonOpenApiKey = intent.apiKey
            )
        }

        is MapleCharacterIntent.SubmitApiKey -> {
            currentState.copy(
                isLoading = true
            )
        }

        is MapleCharacterIntent.SubmitApiKeySuccess -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                newCharacterSummeries = intent.newCharacterSummeries,
                isFetchStarted = true
            )
        }

        is MapleCharacterIntent.SubmitApiKeyFailed -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                errorMessage = intent.message
            )
        }

        is MapleCharacterIntent.LockIsFetchStarted -> {
            currentState.copy(
                isRefreshing = false,
                isFetchStarted = false
            )
        }

        is MapleCharacterIntent.InitNewCharacters -> {
            currentState.copy(
                isRefreshing = false,
                newCharacterSummeries = emptyMap(),
                selectedCharacterOcids = emptyList(),
                characterImages = emptyMap(),
                showFetchWorldSheet = false,
                selectedFetchWorldGroup = "일반 월드",
                selectedFetchWorld = "스카니아",
                isSubmitSuccess = false
            )
        }

        is MapleCharacterIntent.ShowFetchWorldSheet -> {
            currentState.copy(
                isRefreshing = false,
                showFetchWorldSheet = intent.isShow
            )
        }

        is MapleCharacterIntent.SelectFetchWorldGroup -> {
            currentState.copy(
                isRefreshing = false,
                selectedFetchWorldGroup = intent.worldGroupName
            )
        }

        is MapleCharacterIntent.SelectFetchWorld -> {
            currentState.copy(
                isRefreshing = false,
                selectedFetchWorld = intent.worldName
            )
        }

        is MapleCharacterIntent.SelectNewCharacter -> {
            val newCharacters = currentState.selectedCharacterOcids.toMutableList()
            newCharacters.add(intent.ocid)

            currentState.copy(
                isRefreshing = false,
                selectedCharacterOcids = newCharacters.toList()
            )
        }

        is MapleCharacterIntent.SelectNewCharacterCancel -> {
            val newCharacters = currentState.selectedCharacterOcids.toMutableList()
            newCharacters.remove(intent.ocid)

            currentState.copy(
                isRefreshing = false,
                selectedCharacterOcids = newCharacters.toList()
            )
        }

        is MapleCharacterIntent.SubmitNewCharacters -> {
            currentState.copy(
                isLoading = true
            )
        }

        is MapleCharacterIntent.SubmitNewCharactersSuccess -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                characterSummeries = intent.newCharacterSummeries,
                isSubmitSuccess = true,
                successMessage = intent.successMessage
            )
        }

        is MapleCharacterIntent.SubmitNewCharactersFailed -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                errorMessage = intent.message
            )
        }

        is MapleCharacterIntent.InitMessage -> {
            currentState.copy(
                isLoading = false,
                isRefreshing = false,
                successMessage = null,
                errorMessage = null
            )
        }
    }
}