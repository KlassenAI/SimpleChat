package com.android.simplechat.ui.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentSettingsBinding
import com.android.simplechat.ui.activities.MainActivity
import com.android.simplechat.ui.activities.RegisterActivity
import com.android.simplechat.utils.*
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override val binding: FragmentSettingsBinding by viewBinding()

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        APP_ACTIVITY.title = "Настройки"
        initFields()
    }

    private fun initFields() = with(binding) {
        settingsUserPhoto.loadImage(USER.photoUrl)
        settingsBio.text = USER.bio
        settingsFullName.text = USER.fullname
        settingsPhoneNumber.text = USER.phone
        settingsStatus.text = USER.state
        settingsUsername.text = USER.username
        settingsBtnChangeUsername.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        settingsBtnChangeBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settingsChangePhoto.setOnClickListener { changePhoto() }
    }

    private fun changePhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = STORAGE.child(FOLDER_PROFILE_IMAGE).child(UID)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        binding.settingsUserPhoto.loadImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.appDrawer.updateHeader()
                    }
                }
            }
        }
    }
}