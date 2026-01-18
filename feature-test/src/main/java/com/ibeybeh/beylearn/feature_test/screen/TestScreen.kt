package com.ibeybeh.beylearn.feature_test.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibeybeh.beylearn.feature_test.contract.ProfileEffect
import com.ibeybeh.beylearn.feature_test.contract.ProfileEvent
import com.ibeybeh.beylearn.feature_test.viewmodel.ProfileTestViewModel

@Composable
fun TestScreen(viewModel: ProfileTestViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is ProfileEffect.NavigateToLogin -> {
                    Toast.makeText(context, "Navigation To Login!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = state.profile?.name ?: "No Data")
            Button(
                onClick = { viewModel.onEvent(ProfileEvent.RefreshProfile(true)) },
                content = { Text("Refresh") }
            )
            Button(
                onClick = { viewModel.onEvent(ProfileEvent.NavigateToLogin) },
                content = { Text("Go To Login") }
            )
        }
    }
}