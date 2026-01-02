package com.apptive.japkor

import android.app.Application
import com.apptive.japkor.data.local.TokenProvider

class JapkorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenProvider.init(this)
    }
}
