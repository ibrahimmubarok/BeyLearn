package com.ibeybeh.beylearn.feature_test.contract

import com.ibeybeh.beylearn.core.util.UiEffect
import com.ibeybeh.beylearn.core.util.UiEvent
import com.ibeybeh.beylearn.core.util.UiState

data class DetailProfileState(
    val name: String = ""
) : UiState

sealed class DetailProfileEvent : UiEvent {
    object NavigateToLogin : DetailProfileEvent()
    object NavigateBack : DetailProfileEvent()
}

sealed class DetailProfileEffect : UiEffect {
    object NavigateToLogin : DetailProfileEffect()
    object NavigateBack : DetailProfileEffect()
}