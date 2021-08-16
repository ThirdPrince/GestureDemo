package com.android.gesture.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.gesture.R
import com.android.gesture.app.view.BaseActivity

class SecActivity : BaseActivity() {

    companion object{

        fun actionStart(activity: Activity){
            var  intent = Intent(activity,SecActivity::class.java)
            activity?.startActivity(intent)

        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec)
    }
}