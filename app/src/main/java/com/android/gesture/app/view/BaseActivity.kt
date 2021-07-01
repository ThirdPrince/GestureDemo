package com.android.gesture.app.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.gesture.R
import com.android.gesture.app.life.GestureLife
import kotlinx.android.synthetic.main.base.view.*


open class BaseActivity : AppCompatActivity() {

     private val TAG = "BaseActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.base)
        lifecycle.addObserver(GestureLife())


    }

    override fun onResume() {
        super.onResume()
        //GestureActivity.actionStart(this!!,GestureActivity.GestureType.Verify)
    }

}