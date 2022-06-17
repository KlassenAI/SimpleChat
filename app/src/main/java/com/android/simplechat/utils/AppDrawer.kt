package com.android.simplechat.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.android.simplechat.ui.activities.MainActivity
import com.android.simplechat.R
import com.android.simplechat.ui.fragments.ContactsFragment
import com.android.simplechat.ui.fragments.SettingsFragment
import com.android.simplechat.utils.USER
import com.android.simplechat.utils.loadImage
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer {

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var profile: ProfileDrawerItem

    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        drawerLayout = drawer.drawerLayout
    }

    fun disableDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        APP_ACTIVITY.binding.toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
    }

    fun enableDrawer() {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        APP_ACTIVITY.binding.toolbar.setNavigationOnClickListener { drawer.openDrawer() }
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
            .withActivity(APP_ACTIVITY)
            .withToolbar(APP_ACTIVITY.binding.toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(header)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName("Создать группу")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_create_groups),
                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName("Создать секретный чат")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_secret_chat),
                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName("Создать канал")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_create_channel),
                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName("Контакты")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_contacts),
                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName("Звонки")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_phone),
                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName("Избранное")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_favorites),
                PrimaryDrawerItem().withIdentifier(106)
                    .withIconTintingEnabled(true)
                    .withName("Настройки")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_settings),
                DividerDrawerItem(),
                PrimaryDrawerItem().withIdentifier(108)
                    .withIconTintingEnabled(true)
                    .withName("Пригласить друзей")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_invate),
                PrimaryDrawerItem().withIdentifier(109)
                    .withIconTintingEnabled(true)
                    .withName("Вопросы о телеграм")
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_help)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    clickToItem(position)
                    return false
                }
            }).build()
    }

    private fun clickToItem(position: Int) {
        when (position) {
            4 -> APP_ACTIVITY.replaceFragment(ContactsFragment())
            7 -> APP_ACTIVITY.replaceFragment(SettingsFragment())
        }
    }

    private fun createHeader() {
        profile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        header = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITY)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(profile)
            .build()
    }

    fun updateHeader() {
        profile.withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
        header.updateProfile(profile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.loadImage(uri.toString())
            }
        })
    }
}