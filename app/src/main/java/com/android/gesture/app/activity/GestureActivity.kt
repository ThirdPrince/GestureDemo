package com.android.gesture.app.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.android.gesture.R
import com.android.gesture.app.util.Md5Utils
import com.android.gesture.app.view.LockIndicator
import com.android.gesture.app.view.LocusPassWordView
import com.blankj.utilcode.util.SPUtils

/**
 * 手势密码
 */
class GestureActivity : AppCompatActivity() {


    private val TAG = "GestureActivity"

    private var mPwdView_small: LockIndicator? = null
    private lateinit var mPwdView: LocusPassWordView

    private var passWordText: TextView? = null
    private var passWordWarnText: TextView? = null
    private var pwd: String? = null

    private var passedTime = 0
    private var pwdTime = 0

    // 设置手势密码时，重设的标识
    private var pwdVerify = false
    // 修改手势密码时，手势密码验证通过标识
    private var modifyVerified = false

    private var person_img: ImageView? = null

    companion object{
        fun actionStart(activity: AppCompatActivity,boolean: Boolean){
            var  intent = Intent(activity,GestureActivity::class.java).apply {
                putExtra("openHandLock",boolean)
            }
            activity?.startActivity(intent)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        initData()
        initView()
    }

    private fun initData() {
        pwd = SPUtils.getInstance().getString("password", "")
        passWordText = findViewById(R.id.multi_tv_token_time_hint)

        intent = this.intent
        if (intent.getBooleanExtra("modifyHandPw", false)) {
            // 从修改手势密码进入
            passWordText?.setText(R.string.more_watch_old)
            pwdVerify = true

        } else if (intent.getBooleanExtra("openHandLock", false)) {
            // 从打开手势密码进入
            pwd = ""
            pwdVerify = false
            person_img?.setVisibility(View.GONE)
            mPwdView_small?.setVisibility(View.VISIBLE)
        }  else {
            // 验证手势密码
            passWordText?.setText(R.string.more_watch)

        }
    }

    private fun initView() {
        mPwdView = findViewById(R.id.mPassWordView)
        mPwdView!!.setOnCompleteListener {

            val md5 = Md5Utils()
            var passed = false
            if (pwd?.length == 0) run {
                pwd = md5.toMd5(it, "")
                mPwdView?.clearPassword()
                mPwdView?.invalidate()
                passWordText?.setText(R.string.more_watch_draw_next)
                passWordText?.setTextColor(Color.parseColor("#8e8e8e"))
                mPwdView_small?.setPath(it)
                passWordText?.setTextColor(Color.parseColor("#8e8e8e"))
                passWordText?.invalidate()
            } else {
                val encodedPwd = md5.toMd5(it, "")
                if (encodedPwd == pwd) run {
                    passed = true
                    pwdTime = 0
                    if (pwdVerify) {
                        pwdVerify = false
                        modifyVerified = true
                    }
                    passWordWarnText?.setVisibility(View.INVISIBLE)
                } else {
                    mPwdView?.markError()

                    val shake = AnimationUtils.loadAnimation(
                        this@GestureActivity,
                        R.anim.gesture_shake
                    )// 加载动画资源文件
                    passWordText?.setText(R.string.more_watch_draw_error)
                    passWordText?.setTextColor(Color.parseColor("#ffff695e"))
                    passWordText?.startAnimation(shake)
                  /*  if (intent.getBooleanExtra("openHandLock", false)
                    ) run {

                        if (!pwdVerify) {
                        }
                    }*/
                }

            }

            if (passed) {

                when {
                    intent.getBooleanExtra("modifyHandPw", false) -> run {
                        passedTime += 1
                        pwd = ""
                        mPwdView?.clearPassword()
                        mPwdView?.invalidate()
                        passWordText?.setText(R.string.more_watch_draw)
                        setTitle(R.string.more_watch_set)
                        person_img?.setVisibility(View.GONE)
                        mPwdView_small?.setVisibility(View.VISIBLE)
                        passWordText?.setTextColor(Color.parseColor("#8e8e8e"))
                        passWordText?.invalidate()
                        if (passedTime == 2) {
                            SPUtils.getInstance().put("password", md5.toMd5(it, ""))
                            finish()
                        }

                    }
                    intent.getBooleanExtra("openHandLock", false) -> run {
                        SPUtils.getInstance().put("password", md5.toMd5(it, ""))
                        SPUtils.getInstance().put("isOpenHandLock", true)
                        val data = Intent()
                        setResult(RESULT_OK, data)
                        finish()
                    }
                    else -> finish()
                }

            }
        }
    }




}
