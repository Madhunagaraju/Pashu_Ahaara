package com.pashuaahar

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PashuAaharApp : Application() {
    override fun onCreate() {
        super.onCreate()
        com.google.firebase.FirebaseApp.initializeApp(this)
    }
}
