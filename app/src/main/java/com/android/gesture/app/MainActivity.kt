package com.android.gesture.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.android.gesture.R
import com.android.gesture.app.activity.SecActivity
import com.android.gesture.app.fragment.*
import com.android.gesture.app.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * 模拟主页面
 *
 */

private const val MAIN_INDEX = 0x001


private const val MONEY_INDEX = 0x002

private const val TEST_INDEX = 0x003

private const val SETTING_INDEX = 0x004

private const val MINE_INDEX = 0x005


class MainActivity : BaseActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }



    private   var  settingFragment: SettingFragment ?= null

    private var manageMoneyFragment:ManageMoneyFragment ? = null


    private  var  testFragment:TestFragment ?= null


    private  var mainFragment:MainFragment ?=null

    private var mineFragment:MineFragment ? = null



    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                   selectTab(MAIN_INDEX)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_manage_money ->{
                    selectTab(MONEY_INDEX)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    selectTab(TEST_INDEX)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    selectTab(SETTING_INDEX)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_mine ->{
                    selectTab(MINE_INDEX)
                    return@OnNavigationItemSelectedListener true
                }

            }

            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        selectTab(MAIN_INDEX)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }


    /**
     * 选择 Fragment
     */
    private fun selectTab(index:Int){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragment(fragmentTransaction)
        when(index){
            MAIN_INDEX ->{
                if (mainFragment == null) {
                    mainFragment = MainFragment.newInstance("","")
                    fragmentTransaction.add(R.id.content, mainFragment!!,MainFragment::class.java.simpleName)
                } else {
                    fragmentTransaction.show(mainFragment!!)
                }
            }

            MONEY_INDEX  ->{
                if (manageMoneyFragment == null) {
                    manageMoneyFragment = ManageMoneyFragment.newInstance("","")
                    fragmentTransaction.add(R.id.content, manageMoneyFragment!!,ManageMoneyFragment::class.java.simpleName)
                } else {
                    fragmentTransaction.show(manageMoneyFragment!!)
                }
            }

            TEST_INDEX -> {
                if (testFragment == null) {
                    testFragment = TestFragment.newInstance("","")
                    fragmentTransaction.add(R.id.content, testFragment!!,TestFragment::class.java.simpleName)
                } else {
                    fragmentTransaction.show(testFragment!!)
                }
            }
            SETTING_INDEX -> {
                if (settingFragment == null) {
                    settingFragment = SettingFragment.newInstance("","")
                    fragmentTransaction.add(R.id.content, settingFragment!!,SettingFragment::class.java.simpleName)
                } else {
                    fragmentTransaction.show(settingFragment!!)
                }
            }
            MINE_INDEX ->{
                if (mineFragment == null) {
                    mineFragment = MineFragment.newInstance("","")
                    fragmentTransaction.add(R.id.content, mineFragment!!,MineFragment::class.java.simpleName)
                } else {
                    fragmentTransaction.show(mineFragment!!)
                }
            }
        }
        fragmentTransaction.commit()



    }

    private fun hideFragment(ft: FragmentTransaction) {
         mainFragment?.let {
             ft.hide(mainFragment!!)
         }
        manageMoneyFragment?.let {
            ft.hide(manageMoneyFragment!!)
        }
        testFragment?.let {
            ft.hide(testFragment!!)
        }
        settingFragment?.let {
            ft.hide(settingFragment!!)
        }
        mineFragment?.let {
            ft.hide(mineFragment!!)
        }

    }

}
