package com.ibeybeh.beylearn.feature_login.contract

import com.ibeybeh.beylearn.core.utils.UiEffect
import com.ibeybeh.beylearn.core.utils.UiEvent

sealed class LoginEvent : UiEvent {
    object NavigateBack : LoginEvent()
}

sealed class LoginEffect : UiEffect {
    object NavigateBack : LoginEffect()
}