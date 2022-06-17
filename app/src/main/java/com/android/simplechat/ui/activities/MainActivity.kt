package com.android.simplechat.ui.activities

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.ActivityMainBinding
import com.android.simplechat.utils.AppDrawer
import com.android.simplechat.ui.fragments.ChatsFragment
import com.android.simplechat.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val binding: ActivityMainBinding by viewBinding(R.id.container)
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            CoroutineScope(IO).launch { initContacts() }
            initFields()
            initFunc()
        }
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(binding.toolbar)
            appDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        appDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            CoroutineScope(IO).launch { initContacts() }
        }
    }
}