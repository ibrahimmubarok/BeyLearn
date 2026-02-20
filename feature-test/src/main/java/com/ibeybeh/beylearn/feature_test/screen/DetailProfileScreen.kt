package com.ibeybeh.beylearn.feature_test.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibeybeh.beylearn.core_navigation.annotation.BeyDestination
import com.ibeybeh.beylearn.core_navigation.navigator.NavigationManager
import com.ibeybeh.beylearn.core_navigation.routes.DetailProfileRoute
import com.ibeybeh.beylearn.core_navigation.routes.HomeNavGraph
import com.ibeybeh.beylearn.core_navigation.routes.LoginRoute
import com.ibeybeh.beylearn.feature_test.contract.DetailProfileEffect
import com.ibeybeh.beylearn.feature_test.contract.DetailProfileEvent
import com.ibeybeh.beylearn.feature_test.viewmodel.DetailProfileViewModel

@BeyDestination(
    route = DetailProfileRoute::class,
    parentGraph = HomeNavGraph::class
)
@Composable
fun DetailProfileScreen(
    viewModel: DetailProfileViewModel = hiltViewModel(),
    navigationManager: NavigationManager
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailProfileEffect.NavigateToLogin -> {
                    navigationManager.navigateTo(LoginRoute)
                }

                DetailProfileEffect.NavigateBack -> {
                    navigationManager.navigateBack()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome ${state.name}")
        Spacer(Modifier.size(6.dp))
        Button(
            content = { Text("Go to login") },
            onClick = { viewModel.onEvent(DetailProfileEvent.NavigateToLogin) }
        )
        Button(
            content = { Text("Navigate back") },
            onClick = { viewModel.onEvent(DetailProfileEvent.NavigateBack) }
        )
    }
}