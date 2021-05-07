package com.android.gesture.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.activity.SplashActivity

/**
 * 监听 前后台启动
 * 自定义 可以很容易过滤一些不需要跳出手势密码的特殊的场景，比如 登录页
 */
class GestureLifecycleHandler constructor(context:Context): Application.ActivityLifecycleCallbacks {


    companion object{
        private const val TAG = "GestureLifecycleHandler"
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
        if (mActivityCount == 1) {
            GestureActivity.actionStart(activity!!,GestureActivity.GestureType.Verify)
        }
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

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    private fun activityFilter(activity: Activity?):Boolean{
        return activity is SplashActivity
    }
}