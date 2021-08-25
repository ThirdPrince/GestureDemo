package com.android.gesture.app.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope

import com.android.gesture.R
import com.android.gesture.app.OnStartGestureLock
import com.android.gesture.app.activity.GESTURE_FOR_RESULT
import com.android.gesture.app.activity.GESTURE_SETTING_TYPE
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.activity.IsOpenHandLock
import com.android.gesture.app.life.GestureLife
import com.android.gesture.app.util.GestureManager
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 设置手势密码页面
 */
class SettingFragment : Fragment(),OnStartGestureLock {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  var isCheck = false


    public  var waitingGesture  = false

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
            GestureActivity.actionStartForResult(activity!!, GestureActivity.GestureType.Modify,GestureActivity.GestureSettingType.AppType)
        }
         lifecycleScope.launch{
             isCheck =  GestureManager.getGestureState()
             gesture_switch.isChecked = isCheck
             gesture_switch.setOnCheckedChangeListener { _, isChecked ->
                 if (!isChecked) {
                     GestureActivity.actionStartForResult(this@SettingFragment, GestureActivity.GestureType.Cancel,GestureActivity.GestureSettingType.AppType)
                 }else{
                     gesture_switch_by_money.isChecked = false
                     GestureActivity.actionStartForResult(activity!!, GestureActivity.GestureType.Setting,GestureActivity.GestureSettingType.AppType)
                 }

             }
             gesture_switch_by_money.setOnCheckedChangeListener { buttonView, isChecked ->
                 if (!isChecked) {
                     GestureActivity.actionStartForResult(this@SettingFragment, GestureActivity.GestureType.Cancel,GestureActivity.GestureSettingType.FragmentType)
                 }else{
                     gesture_switch.isChecked = false
                     GestureActivity.actionStartForResult(activity!!, GestureActivity.GestureType.Setting,GestureActivity.GestureSettingType.FragmentType)
                 }
             }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            AppCompatActivity.RESULT_FIRST_USER -> if(resultCode == Activity.RESULT_OK){
                lifecycleScope.launch{
                   GestureManager.setGestureState(false)
                    gesture_switch.isChecked = false
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



    override fun onStartGesture() {
        GestureActivity.actionStart(activity!!,GestureActivity.GestureType.Verify)
    }
}
