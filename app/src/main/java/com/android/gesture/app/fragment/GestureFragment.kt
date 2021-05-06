package com.android.gesture.app.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView

import com.android.gesture.R
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.util.FragmentUtil
import com.android.gesture.app.util.Md5Utils
import com.android.gesture.app.view.LockIndicator
import com.android.gesture.app.view.LocusPassWordView
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


private const val OPEN_HAND_LOCK = "open_hand_lock"

private const val MODIFY_HAND_PW = "modifyHandPw"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GestureFragment : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters
    private var openHandLock: Boolean? = false

    private var modifyPw: Boolean = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            openHandLock = it.getBoolean(OPEN_HAND_LOCK)
            modifyPw = it.getBoolean(MODIFY_HAND_PW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_gesture, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(view)
        initView(view)


    }

    private fun initData(view: View) {
        pwd = SPUtils.getInstance().getString("password", "")
        passWordText = view.findViewById(R.id.multi_tv_token_time_hint)

        if (modifyPw) {
            // 从修改手势密码进入
            passWordText?.setText(R.string.more_watch_old)
            pwdVerify = true

        } else if (openHandLock!!) {
            // 从打开手势密码进入
            pwd = ""
            pwdVerify = false
            person_img?.setVisibility(View.GONE)
            mPwdView_small?.setVisibility(View.VISIBLE)
        } else {
            // 验证手势密码
            passWordText?.setText(R.string.more_watch)

        }
    }

    private fun initView(view: View) {
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
                        context,
                        R.anim.gesture_shake
                    )// 加载动画资源文件
                    if (openHandLock!!
                    ) run {
                        passWordText?.setText(R.string.more_watch_draw_error)
                        passWordText?.setTextColor(Color.parseColor("#ffff695e"))
                        passWordText?.startAnimation(shake)
                        if (!pwdVerify) {
                        }
                    }
                }

            }

            if (passed) {

                when {
                    modifyPw -> run {
                        passedTime += 1
                        pwd = ""
                        mPwdView?.clearPassword()
                        mPwdView?.invalidate()
                        passWordText?.setText(R.string.more_watch_draw)
                        person_img?.setVisibility(View.GONE)
                        mPwdView_small?.setVisibility(View.VISIBLE)
                        passWordText?.setTextColor(Color.parseColor("#8e8e8e"))
                        passWordText?.invalidate()
                        if (passedTime == 2) {
                            SPUtils.getInstance().put("password", md5.toMd5(it, ""))
                            //  finish()
                        }

                    }
                    openHandLock!! -> run {
                        SPUtils.getInstance().put("password", md5.toMd5(it, ""))
                        SPUtils.getInstance().put("isOpenHandLock", true)
                        val data = Intent()
                        activity!!.supportFragmentManager.beginTransaction().remove(this).commit()

                    }
                    else -> FragmentUtil.removeFragment(activity!!,this)
                }

            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters


        @JvmStatic
        fun newInstance(openHandLock: Boolean, modifyPw: Boolean) =
            GestureFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(OPEN_HAND_LOCK, openHandLock)
                    putBoolean(MODIFY_HAND_PW, modifyPw)
                }
            }
    }
}
