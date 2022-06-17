package com.android.simplechat.ui.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentChangeNameBinding
import com.android.simplechat.utils.*

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    override val binding: FragmentChangeNameBinding by viewBinding()

    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            binding.settingsInputName.setText(fullnameList[0])
            binding.settingsInputSurname.setText(fullnameList[1])
        } else binding.settingsInputName.setText(fullnameList[0])
    }

    override fun changeParams() {
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()){
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            DB.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
                .setValue(fullname).addOnCompleteListener {
                    if (it.isSuccessful){
                        showToast(getString(R.string.toast_data_update))
                        USER.fullname = fullname
                        APP_ACTIVITY.appDrawer.updateHeader()
                        fragmentManager?.popBackStack()
                    }
                }
        }
    }
}