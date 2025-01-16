package com.example.pawnspixel

object SessionManager {
    var userId: String? = null
    var email: String? = null
    var name: String? = null
    var contactNumber: String? = null
    var password: String? = null

    fun clearSession() {
        email = null
        name = null
        contactNumber = null
        password = null
    }


}