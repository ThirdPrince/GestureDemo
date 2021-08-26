package com.android.gesture.app.util

import android.text.TextUtils
import com.android.gesture.app.activity.GESTURE_SETTING_TYPE
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.activity.IsOpenHandLock
import com.android.gesture.app.activity.PASSWORD
import com.blankj.utilcode.util.SPUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 获取手势密码是否开启的状态
 */
object GestureManager {

    suspend fun getGestureState():Boolean = withContext(Dispatchers.IO){

        return@withContext  SPUtils.getInstance().getBoolean(IsOpenHandLock)
    }

    /**
     * 进入应用时的逻辑判断
     */
    suspend fun getAppGestureState():Boolean = withContext(Dispatchers.IO){

        val appType:Boolean = (SPUtils.getInstance().getInt(GESTURE_SETTING_TYPE) == GestureActivity.GestureSettingType.AppType.ordinal)
        return@withContext  SPUtils.getInstance().getBoolean(IsOpenHandLock) && appType
    }

    suspend fun getFragmentGestureState():Boolean = withContext(Dispatchers.IO){

        val appType =  (SPUtils.getInstance().getInt(GESTURE_SETTING_TYPE) === GestureActivity.GestureSettingType.FragmentType.ordinal)
        return@withContext  SPUtils.getInstance().getBoolean(IsOpenHandLock) && appType
    }

    suspend fun setGestureState(boolean: Boolean  = true, password:String = "", gestureType: GestureActivity.GestureSettingType = GestureActivity.GestureSettingType.AppType) = withContext(Dispatchers.IO){
        if(boolean) {
            SPUtils.getInstance().put(IsOpenHandLock, true)
            SPUtils.getInstance().put(GESTURE_SETTING_TYPE, gestureType.ordinal)
            if(!TextUtils.isEmpty(password)) {
                SPUtils.getInstance().put(PASSWORD, password)
            }
        }else{
            SPUtils.getInstance().remove(IsOpenHandLock)
            SPUtils.getInstance().remove(GESTURE_SETTING_TYPE)
        }
    }
    suspend fun setAppGestureState() = withContext(Dispatchers.IO){
        SPUtils.getInstance().put(GESTURE_SETTING_TYPE, GestureActivity.GestureSettingType.AppType.ordinal)
    }
    suspend fun setSFragmentState() = withContext(Dispatchers.IO){
        SPUtils.getInstance().put(GESTURE_SETTING_TYPE, GestureActivity.GestureSettingType.FragmentType.ordinal)
    }

}