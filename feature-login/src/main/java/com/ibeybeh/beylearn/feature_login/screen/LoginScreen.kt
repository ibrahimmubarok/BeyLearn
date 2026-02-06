package com.ibeybeh.beylearn.feature_login.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ibeybeh.beylearn.core_navigation.annotation.BeyDestination
import com.ibeybeh.beylearn.core_navigation.navigator.NavigationManager
import com.ibeybeh.beylearn.core_navigation.routes.LoginNavGraph
import com.ibeybeh.beylearn.core_navigation.routes.LoginRoute

@BeyDestination(
    route = LoginRoute::class,
    parentGraph = LoginNavGraph::class,
    isStartDestination = true
)
@Composable
fun LoginScreen(navigationManager: NavigationManager) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ini login screen")
        Button(
            content = {
                Text("Back Button")
            }, onClick = {

            }
        )
    }
}