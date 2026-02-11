package com.ibeybeh.beylearn.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibeybeh.beylearn.core.remote.entity.ApiResult
import com.ibeybeh.beylearn.core.remote.entity.BaseErrorResponse
import com.ibeybeh.beylearn.core.util.UiEffect
import com.ibeybeh.beylearn.core.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : Any, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    abstract fun onEvent(event: Event)

    /**
     * Helper for change state safety (Thread-safe logic can be add here )
     */
    protected fun setState(reduce: State.() -> State) {
        _uiState.update { currentState -> currentState.reduce() }
    }

    /**
     * Helper for send Effect in UI
     */
    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch {
            _effect.send(builder())
        }
    }

    protected fun <T> Flow<ApiResult<T>>.collectApiResult(
        onSuccess: (T) -> Unit,
        onError: (BaseErrorResponse?) -> Unit = {},
        onLoading: () -> Unit = {},
        onEmpty: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            this@collectApiResult.collect { result ->
                when (result) {
                    is ApiResult.Loading -> onLoading()
                    is ApiResult.Success -> onSuccess(result.data)
                    is ApiResult.Error -> onError(result.data)
                    is ApiResult.Empty -> onEmpty?.invoke()
                }
            }
        }
    }
}