package com.android.gesture.app.life

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.android.gesture.app.GestureLifecycleHandler
import com.android.gesture.app.OnStartGestureLock
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.fragment.GestureLockFragment
import com.android.gesture.app.fragment.SettingFragment
import com.android.gesture.app.util.GestureManager
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Title: $
 * @Package $
 * @Description: $(用一句话描述)
 * @author $
 * @date $
 * @version V1.0
 */

private const val TAG = "GestureLife"

 open class GestureLife(val fragment: GestureLockFragment) :LifecycleObserver{


     private val uiScope  =  CoroutineScope(Dispatchers.Main)

     private  var onStartGestureLock:OnStartGestureLock?= null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onVisible() {
       EasyLog.e(TAG,"==ON_RESUME==")
        if(fragment.isHidden){
            EasyLog.e(TAG,"等待台跳出手势密码")

           fragment.waitingGesture = true

        }
        if(!fragment.isHidden||fragment.isVisible){
            EasyLog.e(TAG,"==isVisible==")
            uiScope.launch {
                withContext(Dispatchers.IO){
                    val  isOpenHandLock =  GestureManager.getGestureState()
                    if(isOpenHandLock ){
                          GestureActivity.actionStart(ActivityUtils.getTopActivity(),GestureActivity.GestureType.Verify)
                    }
                }

            }
        }


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        EasyLog.e(TAG,"==ON_PAUSE==")
        onStartGestureLock = null
       // fragment.waitingGesture = true
    }
}