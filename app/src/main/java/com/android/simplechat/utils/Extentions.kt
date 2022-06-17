package com.android.simplechat.utils

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.simplechat.R
import com.android.simplechat.models.Common
import com.android.simplechat.models.User
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.fragment, fragment).commit()
    } else {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    (activity as AppCompatActivity).replaceFragment(fragment, addStack)
}

fun Fragment.replaceActivity(activity: AppCompatActivity) {
    (requireActivity() as AppCompatActivity).replaceActivity(activity)
}

fun hideKeyboard() {
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.loadImage(url: String) {
    Picasso.get().load(url.ifEmpty { "empty" }).fit().placeholder(R.drawable.default_photo).into(this)
}

fun getClearPhoneNumber(phoneNumber: String): String {
    val npn = phoneNumber.replace(Regex("[\\s-+,()]"), "")
    return if (npn[0] == '8') npn.replaceFirst("8", "7") else npn
}

fun DataSnapshot.getCommon() = this.getValue(Common::class.java) ?: Common()
fun DataSnapshot.getUser() = this.getValue(User::class.java) ?: User()

fun initContacts() {
    if (checkPermission(READ_CONTACTS)) {
        val contacts = arrayListOf<Common>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val phoneReg = """[+]?[78][ ]\d{3}[ ]\d{3}[-]\d{2}[-]\d{2}""".toRegex()
                val fullname =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phoneNumber = phoneNumber.replace("(", "")
                phoneNumber = phoneNumber.replace(")", "")
                if (phoneReg.containsMatchIn(phoneNumber)) {
                    val clearPhoneNumber = getClearPhoneNumber(phoneNumber)
                    val contact = Common()
                    contact.fullname = fullname
                    contact.phone = clearPhoneNumber
                    contacts.add(contact)
                }
            }
        }
        cursor?.close()
        updatePhonesToDatabase(contacts)
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}
