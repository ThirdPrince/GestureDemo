package com.android.gesture.app.life

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.android.gesture.app.activity.GestureActivity

/**
 * @Title: $
 * @Package $
 * @Description: $(用一句话描述)
 * @author $
 * @date $
 * @version V1.0
 */

private const val TAG = "GestureLife"

class GestureLife :LifecycleObserver{

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
       EasyLog.e(TAG,"==ON_RESUME==")
       // GestureActivity.
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        EasyLog.e(TAG,"==ON_PAUSE==")

    }
}