package com.android.gesture.app.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.gesture.R
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.fragment.GestureFragment
import com.android.gesture.app.util.FragmentUtil
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import java.util.*

open class BaseActivity :AppCompatActivity() {

    private val TAG = "BaseActivity"
     private var gestureReceiver: GestureReceiver? = null
    private var filter: IntentFilter? = null

     var gestureFragment:GestureFragment ?= null
    var ft: FragmentTransaction? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base)
        gestureFragment = GestureFragment.newInstance(false,false)
        receiverGesture();


    }




    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gestureReceiver!!)//注册
    }

     private fun receiverGesture() {
         Log.e(TAG, "receiverGesture")
         filter = IntentFilter()
         filter?.addAction("GESTURE_BROADCAST")
         gestureReceiver = GestureReceiver()//创建广播接受者对象
         LocalBroadcastManager.getInstance(this).registerReceiver(gestureReceiver!!, filter!!)//注册
     }

     private fun gestureSetting() {

         val nowTime = Calendar.getInstance().timeInMillis
         val openGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false)

         Handler().postDelayed({
             if(openGesture){
                GestureActivity.actionStart(this,false)
             }
         }, 200)

     }
     internal inner class GestureReceiver : BroadcastReceiver() {
         override fun onReceive(context: Context, intent: Intent) {
             Log.e(TAG, "this activity = ${BaseActivity::class.java.simpleName}  onReceive")
             gestureSetting()
         }
     }
}