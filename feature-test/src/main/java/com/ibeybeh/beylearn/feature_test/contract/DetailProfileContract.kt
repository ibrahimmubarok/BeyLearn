package com.ibeybeh.beylearn.feature_test.contract

import com.ibeybeh.beylearn.core.utils.UiEffect
import com.ibeybeh.beylearn.core.utils.UiEvent

data class DetailProfileState(
    val name: String = ""
)

sealed class DetailProfileEvent : UiEvent {
    object NavigateToLogin : DetailProfileEvent()
    object NavigateBack : DetailProfileEvent()
}

sealed class DetailProfileEffect : UiEffect {
    object NavigateToLogin : DetailProfileEffect()
    object NavigateBack : DetailProfileEffect()
}