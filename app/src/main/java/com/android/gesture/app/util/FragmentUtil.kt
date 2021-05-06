package com.android.gesture.app.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.gesture.R



object FragmentUtil {

   lateinit var view:View

    fun hadFragment(activity: AppCompatActivity): Boolean {
        return activity.supportFragmentManager.backStackEntryCount != 0
    }

    fun replaceFragment(activity: androidx.fragment.app.FragmentActivity, contentId: Int, fragment: androidx.fragment.app.Fragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
        view = activity.findViewById(contentId)
        view.visibility = View.VISIBLE
        transaction.replace(contentId, fragment, fragment.javaClass.simpleName)

        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun addFragment(activity: androidx.fragment.app.FragmentActivity, contentId: Int, fragment: androidx.fragment.app.Fragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)

        transaction.add(contentId, fragment, fragment.javaClass.simpleName)

        transaction.commit()
    }

    fun removeFragment(activity: androidx.fragment.app.FragmentActivity, fragment: androidx.fragment.app.Fragment) {
        val decor = activity.getWindow().decorView as ViewGroup
        view.visibility = View.GONE
        decor.removeView(view)
        activity.supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
    }


    fun showFragment(activity: AppCompatActivity, fragment: androidx.fragment.app.Fragment) {
        activity.supportFragmentManager.beginTransaction()
                .show(fragment)
                .commit()
    }

    fun hideFragment(activity: AppCompatActivity, fragment: androidx.fragment.app.Fragment) {
        activity.supportFragmentManager.beginTransaction()
                .hide(fragment)
                .commit()
    }

    fun attachFragment(activity: AppCompatActivity, fragment: androidx.fragment.app.Fragment) {
        activity.supportFragmentManager.beginTransaction()
                .attach(fragment)
                .commit()
    }

    fun detachFragment(activity: AppCompatActivity, fragment: androidx.fragment.app.Fragment) {
        activity.supportFragmentManager.beginTransaction()
                .detach(fragment)
                .commit()
    }

    fun getFragmentByTag(appCompatActivity: AppCompatActivity, tag: String): androidx.fragment.app.Fragment? {
        return appCompatActivity.supportFragmentManager.findFragmentByTag(tag)
    }

}