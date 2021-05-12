package com.android.gesture.app.util

import com.android.gesture.app.activity.IsOpenHandLock
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

    suspend fun setGestureState(boolean: Boolean = true) = withContext(Dispatchers.IO){
        if(boolean) {
            SPUtils.getInstance().put(IsOpenHandLock, true)
        }else{
            SPUtils.getInstance().remove(IsOpenHandLock)
        }
    }

}