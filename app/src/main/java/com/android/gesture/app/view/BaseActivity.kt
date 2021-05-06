package com.android.gesture.app.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity : AppCompatActivity() {

    private val TAG = "BaseActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.base)
        EasyLog.d(TAG,"onCreate")
    }

}