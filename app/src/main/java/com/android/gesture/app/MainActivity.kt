package com.android.gesture.app

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.gesture.R
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.fragment.SettingFragment
import com.blankj.utilcode.util.SPUtils
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    var lastGestureTime: Long = 0

    private var filter: IntentFilter? = null
    private var gestureReceiver: GestureReceiver? = null

    private   var settingFragment:SettingFragment? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                hideFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                hideFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
                if(settingFragment!!.isAdded)
                {
                    supportFragmentManager.beginTransaction().show(settingFragment!!).commit()
                }else
                {
                    supportFragmentManager.beginTransaction().add(R.id.content,settingFragment!!).commit()
                }

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if(supportFragmentManager.findFragmentByTag("SettingFragment")== null) {
            settingFragment = SettingFragment.newInstance("", "")
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        receiverGesture()
        //toast("测试")
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG,"onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gestureReceiver!!)//注册
    }
    private fun hideFragment()
    {
        settingFragment?.let{
            supportFragmentManager.beginTransaction().hide(it).commit()
        }

    }

    private fun receiverGesture() {
        filter = IntentFilter()
        filter?.addAction("GESTURE_BROADCAST")
        gestureReceiver = GestureReceiver()//创建广播接受者对象
        LocalBroadcastManager.getInstance(this).registerReceiver(gestureReceiver!!, filter!!)//注册
    }
    /**
     * 手势密码
     */
    internal inner class GestureReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            gestureSetting()
        }
    }


    private fun gestureSetting()
    {

            val nowTime = Calendar.getInstance().timeInMillis
            val openGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false)
            if (nowTime - lastGestureTime > 1000 && openGesture) {
                lastGestureTime = nowTime
                Handler().postDelayed({
                    val intent = Intent(this@MainActivity, GestureActivity::class.java)
                    startActivity(intent)
                    // overridePendingTransition(0, 0);
                }, 200)


        }
    }
}
