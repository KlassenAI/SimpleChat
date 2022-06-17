package com.android.simplechat.ui.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentChangeUsernameBinding
import com.android.simplechat.utils.AppValueEventListener
import com.android.simplechat.utils.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    override val binding: FragmentChangeUsernameBinding by viewBinding()

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        binding.settingsInputUsername.setText(USER.username)
    }

    override fun changeParams() {
        mNewUsername =
            binding.settingsInputUsername.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            DB.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast("Такой пользователь уже существует")
                    } else {
                        changeUsername()
                    }
                })

        }
    }

    private fun changeUsername() {
        DB.child(NODE_USERNAMES).child(mNewUsername).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        DB.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        DB.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    fragmentManager?.popBackStack()
                    USER.username = mNewUsername
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}