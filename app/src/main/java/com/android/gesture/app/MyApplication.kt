package com.android.gesture.app

import android.app.Application
import android.os.Build


/**
 * 手势密码应该属于进程
 * @author dhl
 */
class MyApplication :Application() {


     companion object{
         private const val TAG = "MyApplication"
     }

    private var gestureLifecycleHandler: GestureLifecycleHandler? = null

    override fun onCreate() {
        super.onCreate()
        gestureLifecycleHandler = GestureLifecycleHandler(this)
        registerActivityListener()


    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(gestureLifecycleHandler)
    }

    /**
     * 注册生命周期方法
     */
    private fun registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(gestureLifecycleHandler)
        }
    }

}