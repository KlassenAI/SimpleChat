package com.android.simplechat.models

data class Common(
    val id: String = "",
    var fullname: String = "",
    var phone: String = "",
    var state: String = "",
    var photoUrl: String = "",
    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timestamp: Any = ""
)