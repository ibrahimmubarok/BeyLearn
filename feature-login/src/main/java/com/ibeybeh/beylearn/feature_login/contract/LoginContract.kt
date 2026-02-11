package com.ibeybeh.beylearn.feature_login.contract

import com.ibeybeh.beylearn.core.util.UiEffect
import com.ibeybeh.beylearn.core.util.UiEvent

sealed class LoginEvent : UiEvent {
    object NavigateBack : LoginEvent()
}

sealed class LoginEffect : UiEffect {
    object NavigateBack : LoginEffect()
}