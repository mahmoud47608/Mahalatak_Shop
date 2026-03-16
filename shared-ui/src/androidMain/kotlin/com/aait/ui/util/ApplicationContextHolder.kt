package com.aait.ui.util

import android.app.Application

object ApplicationContextHolder {
    lateinit var application: Application
        private set

    fun init(app: Application) {
        application = app
    }
}
