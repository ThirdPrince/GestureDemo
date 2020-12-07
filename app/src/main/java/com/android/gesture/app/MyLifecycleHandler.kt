package com.android.gesture.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import kotlin.coroutines.coroutineContext

/**
 * 监听 前后台启动
 */
class MyLifecycleHandler constructor(context:Context): Application.ActivityLifecycleCallbacks {

    private val TAG = "MyLifecycleHandler"
    private var mFinalCount: Int = 0

    private val mContext:Context = context

    private var rangTime = System.currentTimeMillis();

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {
        mFinalCount ++
        rangTime  = System.currentTimeMillis() - rangTime
        Log.e(TAG, "rangTime = $rangTime " )
        //如果mFinalCount ==1，说明是从后台到前台
        Log.e(TAG, "onActivityStarted:"+mFinalCount.toString())
        if (mFinalCount == 1) {
            //说明从后台回到了前台
            Log.e(TAG, mFinalCount.toString() + "说明从后台回到了前台")
            val intent = Intent("GESTURE_BROADCAST")
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {
        mFinalCount--
        //如果mFinalCount ==0，说明是前台到后台
        Log.e(TAG, "onActivityStopped:"+mFinalCount.toString())
        if (mFinalCount == 0) {
            //说明从前台回到了后台
            Log.e(TAG, mFinalCount.toString() + "说明从前台回到了后台")
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }
}