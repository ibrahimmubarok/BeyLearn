package com.ibeybeh.beylearn.core_navigation.navigator

import android.net.Uri
import androidx.navigation.NavOptionsBuilder
import com.ibeybeh.beylearn.core_navigation.action.NavigationCommand
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    /**
     * Menggunakan MutableSharedFlow dengan konfigurasi khusus:
     * - replay = 0: Agar event tidak diputar ulang ke subscriber baru (menghindari navigasi ganda saat rotasi layar).
     * - extraBufferCapacity = 1: Memberikan buffer cadangan agar 'tryEmit' tidak gagal jika UI sedang sibuk/belum siap.
     * - onBufferOverflow = DROP_OLDEST: Jika terjadi spam navigasi, event lama dibuang, yang baru diproses.
     */
    private val _commands = MutableSharedFlow<NavigationCommand>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    // Expose sebagai Read-only SharedFlow
    val commands = _commands.asSharedFlow()

    // 1. Handler Navigate To
    fun navigateTo(route: Any, builder: NavOptionsBuilder.() -> Unit = {}) {
        _commands.tryEmit(NavigationCommand.NavigateTo(route, builder))
    }

    // 2. Handler Navigate Back
    fun navigateBack() {
        _commands.tryEmit(NavigationCommand.NavigateBack)
    }

    // 3. Handler Clear Backstack
    fun navigateAndClearBackStack(route: Any) {
        _commands.tryEmit(NavigationCommand.NavigateAndClearBackStack(route))
    }

    // Variasi: Clear sampai route tertentu
    fun navigateAndClearUpTo(route: Any, popUpTo: Any) {
        _commands.tryEmit(NavigationCommand.NavigateAndClearBackStack(route, popUpTo))
    }

    // 4. Handler Navigate by URI
    fun navigateByUri(uri: Uri, builder: NavOptionsBuilder.() -> Unit = {}) {
        _commands.tryEmit(NavigationCommand.NavigateByUri(uri, builder))
    }
}