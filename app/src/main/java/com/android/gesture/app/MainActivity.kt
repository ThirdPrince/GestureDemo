package com.android.gesture.app

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.android.gesture.R
import com.android.gesture.app.activity.SecActivity
import com.android.gesture.app.fragment.SettingFragment
import com.android.gesture.app.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"
    var lastGestureTime: Long = 0
    private var settingFragment: SettingFragment? = null



    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
                    ft = supportFragmentManager.beginTransaction()
                    ft!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    if (settingFragment!!.isAdded) {
                        ft!!.show(settingFragment!!).commit()
                    } else {
                        ft!!.add(R.id.content, settingFragment!!).commit()
                    }

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if (supportFragmentManager.findFragmentByTag("SettingFragment") == null) {
            settingFragment = SettingFragment.newInstance("", "")
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var intent = Intent(this,SecActivity::class.java)
        startActivity(intent)
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }



    private fun hideFragment() {
        settingFragment?.let {
            supportFragmentManager.beginTransaction().hide(it).commit()
        }

    }



    /**
     * 手势密码
     */




}
