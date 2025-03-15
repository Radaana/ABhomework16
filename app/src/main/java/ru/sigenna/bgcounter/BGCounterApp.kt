package ru.sigenna.bgcounter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BGCounterApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}