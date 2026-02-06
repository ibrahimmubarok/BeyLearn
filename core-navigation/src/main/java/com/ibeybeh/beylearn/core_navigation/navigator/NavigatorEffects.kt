package com.ibeybeh.beylearn.core_navigation.navigator

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.ibeybeh.beylearn.core_navigation.action.NavigationCommand
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NavigationEffects(
    navigationManager: NavigationManager,
    navController: NavHostController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = navigationManager) {
        navigationManager.commands.collectLatest { command ->
            when (command) {
                is NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        command.builder(this)
                    }
                }

                is NavigationCommand.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        // Jika stack habis, tutup aplikasi (opsional)
                        (context as? Activity)?.finish()
                    }
                }

                is NavigationCommand.NavigateAndClearBackStack -> {
                    navController.navigate(command.route) {
                        // Pop up to start atau route tertentu
                        popUpTo(command.popUpToRoute ?: 0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                is NavigationCommand.NavigateByUri -> {
                    // DeepLink request
                    val request = androidx.navigation.NavDeepLinkRequest.Builder
                        .fromUri(command.uri)
                        .build()

                    navController.navigate(request, navOptions(command.builder))
                }
            }
        }
    }
}