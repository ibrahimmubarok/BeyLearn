package com.ibeybeh.beylearn.core_navigation.action

import android.net.Uri
import androidx.navigation.NavOptionsBuilder

sealed interface NavigationCommand {
    data class NavigateTo(
        val route: Any,
        val builder: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationCommand

    object NavigateBack : NavigationCommand

    data class NavigateAndClearBackStack(
        val route: Any,
        val popUpToRoute: Any? = null
    ) : NavigationCommand

    data class NavigateByUri(
        val uri: Uri,
        val builder: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationCommand
}