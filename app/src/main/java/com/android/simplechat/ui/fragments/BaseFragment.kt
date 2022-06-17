package com.android.simplechat.ui.fragments

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.android.simplechat.ui.activities.MainActivity
import com.android.simplechat.utils.APP_ACTIVITY

abstract class BaseFragment(layout: Int) : Fragment(layout) {

    abstract val binding: ViewBinding

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.appDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.appDrawer.enableDrawer()
    }
}