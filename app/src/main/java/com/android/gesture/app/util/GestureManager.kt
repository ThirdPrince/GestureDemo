package com.android.gesture.app.util

import android.text.TextUtils
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

    suspend fun setGestureState(boolean: Boolean  = true,password:String = "") = withContext(Dispatchers.IO){
        if(boolean) {
            SPUtils.getInstance().put(IsOpenHandLock, true)
            if(!TextUtils.isEmpty(password)) {
                SPUtils.getInstance().put(PASSWORD, password)
            }
        }else{
            SPUtils.getInstance().remove(IsOpenHandLock)
        }
    }

}