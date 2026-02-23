package com.ibeybeh.beylearn.feature_test.contract

import com.ibeybeh.beylearn.core.utils.UiEffect
import com.ibeybeh.beylearn.core.utils.UiEvent
import com.ibeybeh.beylearn.core_entity.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val errorMessage: String? = null
)

sealed class ProfileEvent : UiEvent {
    object LoadProfile : ProfileEvent()
    data class RefreshProfile(val forceUpdate: Boolean) : ProfileEvent()
    object NavigateToDetailProfile : ProfileEvent()
}

sealed class ProfileEffect : UiEffect {
    data class ShowToast(val message: String) : ProfileEffect()
    object NavigateToDetailProfile : ProfileEffect()
}