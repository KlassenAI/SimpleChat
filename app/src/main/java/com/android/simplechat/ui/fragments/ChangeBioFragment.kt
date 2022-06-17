package com.android.simplechat.ui.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentChangeBioBinding
import com.android.simplechat.utils.*

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override val binding: FragmentChangeBioBinding by viewBinding()

    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun changeParams() {
        val newBio = binding.settingsInputBio.text.toString()
        DB.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    USER.bio = newBio
                    fragmentManager?.popBackStack()
                }
            }
    }
}