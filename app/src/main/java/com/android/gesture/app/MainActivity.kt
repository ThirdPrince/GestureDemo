package com.android.gesture.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import android.util.Log
import android.widget.Toast
import com.android.gesture.R
import com.android.gesture.app.activity.SecActivity
import com.android.gesture.app.fragment.MainFragment
import com.android.gesture.app.fragment.SettingFragment
import com.android.gesture.app.fragment.TestFragment
import com.android.gesture.app.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * 模拟主页面
 *
 */
class MainActivity : BaseActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    private   var  settingFragment: SettingFragment ?= null


    private  var  testFragment:TestFragment ?= null


    private  val mainFragment:MainFragment by lazy {
        MainFragment.newInstance("","")
    }

    private lateinit var ft:FragmentTransaction

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            hideFragment()
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (mainFragment.isAdded) {
                        ft.show(mainFragment)
                    }
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    if (testFragment!= null) {
                        ft.show(testFragment!!)
                    } else {
                        testFragment = TestFragment.newInstance("","")
                        ft.add(R.id.content, testFragment!!,TestFragment.TAG)
                    }
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    if (settingFragment == null) {
                        settingFragment = SettingFragment.newInstance("","")
                        ft.add(R.id.content, settingFragment!!,SettingFragment.TAG)
                    } else {
                        ft.show(settingFragment!!)
                    }
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }

            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().add(R.id.content,mainFragment,MainFragment.TAG).commit()
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    private fun hideFragment() {
        ft = supportFragmentManager.beginTransaction()
        ft.hide(mainFragment)
        if(testFragment != null) {
            ft.hide(testFragment!!)
        }
        if(settingFragment != null) {
            ft.hide(settingFragment!!)
        }

    }

}
