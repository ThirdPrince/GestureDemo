package com.android.gesture.app.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*


object FragmentUtil {

   lateinit var view:View


   fun getFragments(fm: FragmentManager): List<Fragment>? {
      val fragments: List<Fragment> = fm.getFragments()
      return if (fragments == null || fragments.isEmpty()) Collections.emptyList() else fragments
   }




}