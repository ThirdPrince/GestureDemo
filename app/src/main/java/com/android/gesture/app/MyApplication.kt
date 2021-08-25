package com.android.gesture.app

import android.app.Application
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.android.gesture.app.life.GestureLife
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.ProcessUtils


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

        ProcessLifecycleOwner.get().lifecycle.addObserver(object:LifecycleObserver{

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForeground(){
                EasyLog.e(TAG,"== onForeground==")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackground(){
                EasyLog.e(TAG,"== onBackground==")
            }


        });
        CrashUtils.init()
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