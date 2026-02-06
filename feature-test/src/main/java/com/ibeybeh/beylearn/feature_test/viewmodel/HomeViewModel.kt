package com.ibeybeh.beylearn.feature_test.viewmodel

import com.ibeybeh.beylearn.api_test.domain.get.GetProfileTestUseCase
import com.ibeybeh.beylearn.core.base.BaseViewModel
import com.ibeybeh.beylearn.feature_test.contract.ProfileEffect
import com.ibeybeh.beylearn.feature_test.contract.ProfileEvent
import com.ibeybeh.beylearn.feature_test.contract.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileTestUseCase
) : BaseViewModel<ProfileState, ProfileEvent, ProfileEffect>(ProfileState()) {

    init {
        onEvent(ProfileEvent.LoadProfile)
    }

    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadProfile -> getProfile()
            is ProfileEvent.RefreshProfile -> getProfile()
            is ProfileEvent.NavigateToDetailProfile -> setEffect { ProfileEffect.NavigateToDetailProfile }
        }
    }

    private fun getProfile() {
        getProfileUseCase().collectApiResult(
            onLoading = { setState { copy(isLoading = true, errorMessage = null) } },
            onSuccess = { data ->
                setState { copy(isLoading = false, profile = data) }
                setEffect { ProfileEffect.ShowToast(data.name) }
            },
            onError = { error ->
                setState { copy(isLoading = false, errorMessage = error?.message) }
                setEffect { ProfileEffect.ShowToast(error?.message ?: "Unknown Error") }
            }
        )
    }
}