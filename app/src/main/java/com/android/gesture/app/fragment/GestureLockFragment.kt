package com.android.gesture.app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ProcessLifecycleOwner
import com.android.gesture.app.activity.GestureActivity
import com.android.gesture.app.life.GestureLife

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GestureLockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "GestureLockFragment"

abstract class GestureLockFragment : Fragment() {
    // TODO: Rename and change types of parameters
    public  var waitingGesture  = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(GestureLife(this))
    }


    override fun onResume() {
        super.onResume()
        EasyLog.e(TAG,"onResume")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        EasyLog.e(TAG,"onHiddenChanged")
        if(hidden){

        }else{
            if(waitingGesture){
                waitingGesture = false
                GestureActivity.actionStart(activity!!, GestureActivity.GestureState.Verify)
            }
        }
    }

}