package com.projet.app_location_outils

import android.app.Application
import android.content.Context

class AppLocationOutils : Application() {

    companion object {
        private var instance: AppLocationOutils? = null

        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}