package com.ibeybeh.beylearn.feature_login.viewmodel

import com.ibeybeh.beylearn.core.base.BaseViewModel
import com.ibeybeh.beylearn.feature_login.contract.LoginEffect
import com.ibeybeh.beylearn.feature_login.contract.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<Unit, LoginEvent, LoginEffect>(Unit) {
    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.NavigateBack -> setEffect { LoginEffect.NavigateBack }
        }
    }
}