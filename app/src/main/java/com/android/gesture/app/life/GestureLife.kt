package com.android.gesture.app.life

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.fragment.GestureLockFragment
import com.android.gesture.app.util.GestureManager
import com.blankj.utilcode.util.ActivityUtils
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
                    val  isOpenHandLock =  GestureManager.getFragmentGestureState()
                    if(isOpenHandLock ){
                          GestureActivity.actionStart(ActivityUtils.getTopActivity(),GestureActivity.GestureState.Verify)
                    }
                }

            }
        }


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        EasyLog.e(TAG,"==ON_PAUSE==")
    }
}