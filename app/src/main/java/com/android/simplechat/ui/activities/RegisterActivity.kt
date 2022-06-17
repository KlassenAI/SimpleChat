package com.android.simplechat.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.ActivityRegisterBinding
import com.android.simplechat.utils.replaceFragment
import com.android.simplechat.ui.fragments.EnterPhoneFragment
import com.android.simplechat.utils.initFirebase

class RegisterActivity : AppCompatActivity(R.layout.activity_register) {

    private val binding: ActivityRegisterBinding by viewBinding(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneFragment(), false)
        initFirebase()
    }
}