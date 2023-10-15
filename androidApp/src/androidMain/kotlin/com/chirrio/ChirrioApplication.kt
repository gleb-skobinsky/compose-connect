package com.chirrio

import android.app.Application
import di.startKoinApp

class ChirrioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoinApp()
    }
}