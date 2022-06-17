package com.android.simplechat.ui.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.android.simplechat.R
import com.android.simplechat.ui.activities.MainActivity
import com.android.simplechat.utils.APP_ACTIVITY
import com.android.simplechat.utils.hideKeyboard

abstract class BaseChangeFragment(layout: Int) : Fragment(layout) {

    abstract val binding: ViewBinding

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.appDrawer.disableDrawer()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> changeParams()
        }
        return true
    }

    abstract fun changeParams()
}