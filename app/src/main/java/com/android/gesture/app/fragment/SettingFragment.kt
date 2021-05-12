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
import androidx.lifecycle.lifecycleScope

import com.android.gesture.R
import com.android.gesture.app.activity.GESTURE_FOR_RESULT
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.activity.IsOpenHandLock
import com.android.gesture.app.util.GestureManager
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
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  var isCheck = false

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

    // TODO: Rename method, update argument and hook method into UI event


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gesture_password.setOnClickListener {
            var intent = Intent(activity, GestureActivity::class.java)
            intent.putExtra("openHandLock", true)
            activity?.startActivity(intent)
        }
         lifecycleScope.launch{
             isCheck =  GestureManager.getGestureState()
             gesture_switch.isChecked = isCheck
             gesture_switch.setOnCheckedChangeListener { buttonView, isChecked ->
                 if (!isChecked) {
                     GestureActivity.actionStartForResult(this@SettingFragment, GestureActivity.GestureType.Verify)
                 }else{
                     GestureActivity.actionStartForResult(activity!!, GestureActivity.GestureType.Setting)
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
}
