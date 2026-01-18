package com.ibeybeh.beylearn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
//import com.ibeybeh.beylearn.BuildConfig.DEBUG

@HiltAndroidApp
class BeylearnApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    private fun initTimber() {
//        if (DEBUG) Timber.plant(DebugTree()) else Timber.uprootAll()
    }

}