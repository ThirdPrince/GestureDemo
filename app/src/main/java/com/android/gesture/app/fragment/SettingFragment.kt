package com.android.gesture.app.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.android.gesture.R
import com.android.gesture.app.activity.GESTURE_SETTING_TYPE
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.util.GestureManager
import com.blankj.utilcode.util.SPUtils
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 设置手势密码页面
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  var isGestureCheck = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modify_gesture_pwd.setOnClickListener {
            GestureActivity.actionStartForResult(activity!!, GestureActivity.GestureState.Modify,GestureActivity.GestureSettingType.AppType)
        }
        app_gesture_lay.setOnClickListener {


            lifecycleScope.launch {
                val appGestureState = GestureManager.getFragmentGestureState()
                if (appGestureState) {
                    GestureManager.setAppGestureState()
                    gesture_switch.isChecked = true
                    gesture_switch_by_money.isChecked = false
                    return@launch
                }
                if (gesture_switch.isChecked) {
                    GestureActivity.actionStartForResult(
                        this@SettingFragment,
                        GestureActivity.GestureState.Cancel,
                        GestureActivity.GestureSettingType.AppType
                    )

                } else {
                    GestureActivity.actionStartForResult(
                        this@SettingFragment,
                        GestureActivity.GestureState.Setting,
                        GestureActivity.GestureSettingType.AppType
                    )
                }

            }
        }



        fragment_gesture_lay.setOnClickListener {

                lifecycleScope.launch {
                    val appGestureState = GestureManager.getAppGestureState()
                    if(appGestureState){
                        GestureManager.setSFragmentState()
                        gesture_switch.isChecked = false
                        gesture_switch_by_money.isChecked = true
                        return@launch
                    }
                    if(gesture_switch.isChecked){
                        GestureActivity.actionStartForResult(this@SettingFragment, GestureActivity.GestureState.Cancel,GestureActivity.GestureSettingType.AppType)

                    }else{
                        GestureActivity.actionStartForResult(this@SettingFragment, GestureActivity.GestureState.Setting,GestureActivity.GestureSettingType.AppType)
                    }
                }

        }
         lifecycleScope.launch{
             isGestureCheck =  GestureManager.getGestureState()
             gesture_switch.isChecked = GestureManager.getAppGestureState()
             gesture_switch_by_money.isChecked = GestureManager.getFragmentGestureState()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            AppCompatActivity.RESULT_FIRST_USER -> if(resultCode == Activity.RESULT_OK){
                lifecycleScope.launch{
                    val state = GestureManager.getGestureState()
                    EasyLog.e(TAG,"state = $state")
                    gesture_switch.isChecked = GestureManager.getAppGestureState()
                    gesture_switch_by_money.isChecked = GestureManager.getFragmentGestureState()
                }
            }
        }
    }



    companion object {

        public const val TAG  = "SettingFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
