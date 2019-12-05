package com.android.gesture.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

class MyApplication :Application() {


    private val TAG = "MyApplication"
    
    private var mFinalCount: Int = 0

    private var myLifecycleHandler: MyLifecycleHandler? = null

    override fun onCreate() {
        super.onCreate()
        myLifecycleHandler = MyLifecycleHandler(this)
        registerActivityListener()

    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(myLifecycleHandler)
    }

    private fun registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(myLifecycleHandler)
        }
    }

}