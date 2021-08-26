package com.android.gesture.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.activity.SplashActivity
import com.android.gesture.app.util.GestureManager
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 监听 前后台启动
 * 自定义 可以很容易过滤一些不需要跳出手势密码的特殊的场景，比如 登录页
 */
class GestureLifecycleHandler constructor(context:Context): Application.ActivityLifecycleCallbacks {


    companion object{
        private const val TAG = "GestureLifecycleHandler"
    }

    private val uiScope  =  CoroutineScope(Dispatchers.Main)

    private var isOpenHandLock = false
    init {


    }


    /**
     * 记录 activity 前后台情况
     */
    private var mActivityCount: Int = 0

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {



    }

    override fun onActivityStarted(activity: Activity?) {
        if(activityFilter(activity)){
            return
        }

        mActivityCount ++
        EasyLog.e(TAG,"onForeground = $mActivityCount")
        uiScope.launch {
            withContext(Dispatchers.IO){
                isOpenHandLock =  GestureManager.getAppGestureState()
                if(isOpenHandLock && mActivityCount == 1){
                   GestureActivity.actionStart(activity!!,GestureActivity.GestureState.Verify)
                }
            }

        }
        val manActivity = ActivityUtils.getTopActivity()

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {
        if(activityFilter(activity)){
            return
        }
        mActivityCount--
        EasyLog.e(TAG,"onBackground = $mActivityCount")

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    private fun activityFilter(activity: Activity?):Boolean{
        return activity is SplashActivity
    }
}