package com.android.simplechat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentEnterPhoneBinding
import com.android.simplechat.utils.replaceActivity
import com.android.simplechat.utils.replaceFragment
import com.android.simplechat.utils.showToast
import com.android.simplechat.ui.activities.MainActivity
import com.android.simplechat.ui.activities.RegisterActivity
import com.android.simplechat.utils.AUTH
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {

    private val binding: FragmentEnterPhoneBinding by viewBinding()

    private lateinit var phoneNumber: String
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                AUTH.signInWithCredential(p0).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Добро пожаловать")
                        (activity as AppCompatActivity).replaceActivity(MainActivity())
                    } else showToast(it.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("TEST", p0.message.toString())
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                (activity as RegisterActivity).replaceFragment(EnterCodeFragment(phoneNumber, id))
            }
        }

        binding.registerBtnNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (binding.registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast("Введите номер телефона")
        } else {
            authUser()
        }
    }

    private fun authUser() {
        phoneNumber = binding.registerInputPhoneNumber.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity as RegisterActivity,
            callback
        )
    }
}