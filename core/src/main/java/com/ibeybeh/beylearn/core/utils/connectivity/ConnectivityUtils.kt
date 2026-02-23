package com.ibeybeh.beylearn.core.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

object ConnectivityUtils {

    fun networkConnectivityObserver(context: Context): Flow<NetworkConnectionState> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = NetworkCallback { connectionStatus -> launch { send(connectionStatus) } }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged().flowOn(Dispatchers.IO)

    class NetworkCallback(
        var onNetworkStateChanged: (NetworkConnectionState) -> Unit
    ) : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkStateChanged(NetworkConnectionState.Connected)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onNetworkStateChanged(NetworkConnectionState.Disconnected)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            onNetworkStateChanged(NetworkConnectionState.Disconnected)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            onNetworkStateChanged(NetworkConnectionState.Disconnected)
        }
    }

    sealed class NetworkConnectionState {
        object Initial : NetworkConnectionState()
        object Connected : NetworkConnectionState()
        object Disconnected : NetworkConnectionState()
    }
}