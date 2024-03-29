package com.android.gesture.app.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.gesture.R
import com.android.gesture.app.util.GestureManager
import com.android.gesture.app.util.Md5Utils
import com.android.gesture.app.view.LockIndicator
import com.android.gesture.app.view.LocusPassWordView
import com.blankj.utilcode.util.SPUtils
import kotlinx.coroutines.launch
import java.io.Serializable



private const val OPEN_HAND_LOCK = "open_hand_lock"

private const val MODIFY_HAND_PW = "modifyHandPw"

private const val GestureTypePara = "GestureType"

 const val PASSWORD = "password"

const val IsOpenHandLock = "isOpenHandLock"

const val GESTURE_SETTING_TYPE = "gesture_setting_type"

const val GESTURE_FOR_RESULT = 1024





/**
 * 手势密码
 * @author dhl
 */
class GestureActivity : AppCompatActivity() {


    /**
     *  几种状态
     *  Cancel(取消，取消之前要验证)
     *  Setting(设置，设置一般要两次)
     *  Verify (验证，一般输入一次，成功后关闭）
     *  Modify(verify - setting)
     */
    enum class GestureState(i: Int) :Serializable{

        Cancel(-2),
        Setting(-1),
        Verify(1),
        Modify(2);

    }

    /**
     * 手势密码支持两种，一个是进入应用时（AppType）,一个是进入理财时（FragmentType）
     */
    enum class GestureSettingType(i: Int) :Serializable{

        AppType(0),
        FragmentType(1)
    }
    /**
     * 绘制手势密码时 ，上面展示小的手势密码 流程图
     */
    private  val mPwdViewSmall: LockIndicator by lazy {
        findViewById<LockIndicator>(R.id.mPassWordView_small)
    }

    private val mPwdView: LocusPassWordView by lazy {
        findViewById<LocusPassWordView>(R.id.mPwdView)
    }

    private val passWordText: TextView by lazy {
        findViewById<TextView>(R.id.multi_tv_token_time_hint)
    }
    private var passWordWarnText: TextView? = null
    private var pwd: String? = null
    private var pwdTime = 0
    // 设置手势密码时，重设的标识
    private var pwdVerify = false
    // 修改手势密码时，手势密码验证通过标识
    private var modifyVerified = false

    private val personImg: ImageView by lazy {
        findViewById<ImageView>(R.id.person_img)
    }

    private var gestureType :GestureState ?= null

    private var gestureSettingType:GestureSettingType ?= null;


    /**
     * 对外提供启动入口
     */
    companion object{

        fun actionStart(activity: Activity,gestureType: GestureState){
            var  intent = Intent(activity,GestureActivity::class.java).apply {
                putExtra(GestureTypePara,gestureType)
            }
            activity?.startActivity(intent)

        }

        fun actionStartForResult(activity: Activity,gestureType: GestureState){
            var  intent = Intent(activity,GestureActivity::class.java).apply {
                putExtra(GestureTypePara,gestureType)
            }
            activity?.startActivityForResult(intent,GESTURE_FOR_RESULT)

        }
        fun actionStartForResult(activity: Activity, gestureType: GestureState, gestureSettingType: GestureSettingType){
            var  intent = Intent(activity,GestureActivity::class.java).apply {
                putExtra(GestureTypePara,gestureType)
                putExtra(GESTURE_SETTING_TYPE,gestureSettingType)
            }
            activity?.startActivityForResult(intent,GESTURE_FOR_RESULT)

        }

        fun actionStartForResult(fragment: Fragment, gestureType: GestureState, gestureSettingType: GestureSettingType){
            var  intent = Intent(fragment.activity,GestureActivity::class.java).apply {
                putExtra(GestureTypePara,gestureType)
                putExtra(GESTURE_SETTING_TYPE,gestureSettingType)
            }
            fragment?.startActivityForResult(intent,RESULT_FIRST_USER)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        initData()
        initView()
    }

    private fun initData() {
        pwd = SPUtils.getInstance().getString(PASSWORD, "")
        gestureType = intent.getSerializableExtra(GestureTypePara) as GestureState?
        gestureSettingType = intent.getSerializableExtra(GESTURE_SETTING_TYPE) as GestureSettingType?
        when(gestureType){
            GestureState.Setting -> {
                pwd = ""
                pwdVerify = false
                personImg?.visibility = View.GONE
                mPwdViewSmall?.visibility = View.VISIBLE
            }
            GestureState.Verify ->    passWordText?.setText(R.string.more_watch)
            else ->  ""
        }

        }


    private fun initView() {
        mPwdView!!.setOnCompleteListener {
            val md5 = Md5Utils()
            var passed = false
            if (pwd?.length == 0) run {
                pwd = md5.toMd5(it, "")
                mPwdView?.clearPassword()
                mPwdView?.invalidate()
                passWordText?.setText(R.string.more_watch_draw_next)
                passWordText?.setTextColor(Color.parseColor("#8e8e8e"))
                mPwdViewSmall?.setPath(it)
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
                    )
                    passWordText?.setText(R.string.more_watch_draw_error)
                    passWordText?.setTextColor(Color.parseColor("#ffff695e"))
                    passWordText?.startAnimation(shake)
                }

            }
            if (passed) {
                when(gestureType) {
                    GestureState.Verify ->{
                      finish()

                    }
                    GestureState.Setting ->{
                        lifecycleScope.launch{
                            when(gestureSettingType){
                                GestureSettingType.AppType ->{
                                    GestureManager.setGestureState(true,md5.toMd5(it,""))
                                }
                                GestureSettingType.FragmentType ->{
                                    GestureManager.setGestureState(true,md5.toMd5(it,""), GestureSettingType.FragmentType)
                                }
                            }

                            val data = Intent()
                            setResult(RESULT_OK, data)
                            finish()
                        }
                    }

                    GestureState.Modify ->{
                        GestureActivity.actionStartForResult(this, GestureActivity.GestureState.Setting)
                        finish()
                    }
                    GestureState.Cancel -> {
                        lifecycleScope.launch{
                            GestureManager.setGestureState(false)
                            val data = Intent()
                            setResult(RESULT_OK, data)
                            finish()
                        }
                    }

                    else -> finish()
                }

            }
        }
    }

}





