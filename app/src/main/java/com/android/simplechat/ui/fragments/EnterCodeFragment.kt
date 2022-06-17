package com.android.simplechat.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentEnterCodeBinding
import com.android.simplechat.ui.activities.MainActivity
import com.android.simplechat.ui.activities.RegisterActivity
import com.android.simplechat.utils.*
import com.google.firebase.auth.PhoneAuthProvider

class EnterCodeFragment(
    private val phoneNumber: String,
    private val id: String
) : Fragment(R.layout.fragment_enter_code) {

    private val binding: FragmentEnterCodeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as RegisterActivity).title = phoneNumber

        binding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = it.toString()
            if (string.length == 6) {
                enterCode(it.toString())
            }
        })
    }

    private fun enterCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val map = mutableMapOf<String, Any>()
                val clearPhoneNumber = getClearPhoneNumber(phoneNumber)
                map[CHILD_ID] = uid
                map[CHILD_PHONE] = clearPhoneNumber
                map[CHILD_USERNAME] = uid
                DB.child(NODE_PHONES).child(clearPhoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        DB.child(NODE_USERS).child(uid).updateChildren(map)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                showToast("Добро пожаловать")
                                (activity as AppCompatActivity).replaceActivity(MainActivity())
                            }
                    }
            } else showToast(task.exception?.message.toString())
        }
    }
}