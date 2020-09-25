package com.h4rz.remoteconfigdemo.application

import android.app.Application
import com.h4rz.remoteconfigdemo.BuildConfig
import timber.log.Timber

/**
 * Created by Harsh Rajgor on 25/09/20.
 */

class CoreApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}