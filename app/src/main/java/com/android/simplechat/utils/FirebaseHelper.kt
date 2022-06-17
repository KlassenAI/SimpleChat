package com.android.simplechat.utils

import android.net.Uri
import com.android.simplechat.R
import com.android.simplechat.models.Common
import com.android.simplechat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var DB: DatabaseReference
lateinit var STORAGE: StorageReference
lateinit var USER: User
lateinit var UID: String

const val TYPE_TEXT = "text"

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_MESSAGES = "messages"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timestamp"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    DB = FirebaseDatabase.getInstance().reference
    STORAGE = FirebaseStorage.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    DB.child(NODE_USERS).child(UID).child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}


inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    DB.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(User::class.java) ?: User()
            if (USER.username.isEmpty()) {
                USER.username = UID
            }
            function()
        })
}

fun updatePhonesToDatabase(arrayCommons: ArrayList<Common>) {
    if (AUTH.currentUser != null) {
        DB.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { snapshot ->
                arrayCommons.forEach { contact ->
                    if (snapshot.key == contact.phone) {
                        DB.child(NODE_PHONES_CONTACTS).child(UID)
                            .child(snapshot.value.toString()).child(CHILD_ID)
                            .setValue(snapshot.value.toString())
                            .addOnFailureListener { showToast(it.message.toString()) }
                        DB.child(NODE_PHONES_CONTACTS).child(UID)
                            .child(snapshot.value.toString()).child(CHILD_FULLNAME)
                            .setValue(contact.fullname)
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
                }
            }
        })
    }
}

inline fun sendMessage(message: String, receivingUserid: String, typeText: String, crossinline function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserid"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserid/$UID"
    val messageKey = DB.child(refDialogUser).push().key

    val mapMessage = hashMapOf(
        CHILD_FROM to UID,
        CHILD_TYPE to typeText,
        CHILD_TEXT to message,
        CHILD_TIMESTAMP to ServerValue.TIMESTAMP
    )

    val mapDialog = hashMapOf<String, Any>(
        "$refDialogUser/$messageKey" to mapMessage,
        "$refDialogReceivingUser/$messageKey" to mapMessage,
    )

    DB.updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}
