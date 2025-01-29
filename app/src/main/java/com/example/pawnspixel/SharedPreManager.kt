package com.example.pawnspixel

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_EMAIL = "user_email"
        private const val USER_NAME = "user_name"
        private const val USER_ID = "user_id"
        private const val USER_CONTACT = "contact_number"
    }

    // Save user data to SharedPreferences
    fun saveUserData(email: String, name: String, contactNumber: String, userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_NAME, name)
        editor.putString(USER_CONTACT, contactNumber)
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    fun setUserEmail(email: String){
        val editor = sharedPreferences.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun setUserName(name: String){
        val editor = sharedPreferences.edit()
        editor.putString(USER_NAME, name)
        editor.apply()
    }

    fun getUserEmail(): String? = sharedPreferences.getString(USER_EMAIL, null)
    fun getUserNumber(): String? = sharedPreferences.getString(USER_CONTACT, null)
    fun getUserName(): String? = sharedPreferences.getString(USER_NAME, null)
    fun getUserId(): String? = sharedPreferences.getString(USER_ID, null)

    // Clear user data when logging out
    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

}
