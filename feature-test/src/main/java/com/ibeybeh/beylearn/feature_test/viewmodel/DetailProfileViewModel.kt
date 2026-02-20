package com.ibeybeh.beylearn.feature_test.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ibeybeh.beylearn.core.base.BaseViewModel
import com.ibeybeh.beylearn.core_navigation.routes.savedStateExt.getDetailProfileRoute
import com.ibeybeh.beylearn.feature_test.contract.DetailProfileEffect
import com.ibeybeh.beylearn.feature_test.contract.DetailProfileEvent
import com.ibeybeh.beylearn.feature_test.contract.DetailProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailProfileState, DetailProfileEvent, DetailProfileEffect>(DetailProfileState()) {

    init {
        val args = savedStateHandle.getDetailProfileRoute()
        setState { copy(name = args.profile.name) }
    }

    override fun onEvent(event: DetailProfileEvent) {
        when (event) {
            is DetailProfileEvent.NavigateToLogin -> setEffect { DetailProfileEffect.NavigateToLogin }
            is DetailProfileEvent.NavigateBack -> setEffect { DetailProfileEffect.NavigateBack }
        }
    }
}