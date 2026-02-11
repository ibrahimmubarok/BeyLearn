package com.ibeybeh.beylearn.feature_login.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibeybeh.beylearn.core_navigation.annotation.BeyDestination
import com.ibeybeh.beylearn.core_navigation.navigator.NavigationManager
import com.ibeybeh.beylearn.core_navigation.routes.DetailProfile
import com.ibeybeh.beylearn.core_navigation.routes.HomeRoute
import com.ibeybeh.beylearn.core_navigation.routes.LoginNavGraph
import com.ibeybeh.beylearn.core_navigation.routes.LoginRoute
import com.ibeybeh.beylearn.feature_login.contract.LoginEffect
import com.ibeybeh.beylearn.feature_login.contract.LoginEvent
import com.ibeybeh.beylearn.feature_login.viewmodel.LoginViewModel

@BeyDestination(
    route = LoginRoute::class,
    parentGraph = LoginNavGraph::class,
    isStartDestination = true
)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigationManager: NavigationManager
) {

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateBack -> {
                    navigationManager.navigateAndClearBackStack(HomeRoute)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ini login screen")
        Button(
            content = { Text("Back Button") },
            onClick = { viewModel.onEvent(LoginEvent.NavigateBack) }
        )
    }
}