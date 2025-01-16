package com.example.pawnspixel

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var cancelButton: Button
    private lateinit var confirmButton: Button
    private lateinit var logOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        logOutButton = findViewById(R.id.logout)

        val sharedPrefManager = SharedPrefManager(this)
        val email = sharedPrefManager.getUserEmail()
        val name = sharedPrefManager.getUserName()
        val id = sharedPrefManager.getUserId()

        if (email != null && name != null && id != null) {
            SessionManager.email = email
            SessionManager.name = name
            SessionManager.userId = id
        }

        val userName = SessionManager.name
        val userEmail = SessionManager.email

        if (userName != null && userEmail != null) {
            Toast.makeText(this, "Name: $userName \n Email: $userEmail", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "User details not available", Toast.LENGTH_SHORT).show()
        }

        dialog = Dialog(this)
        dialog.setContentView(R.layout.fragment_popup)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        cancelButton = dialog.findViewById(R.id.cancel)
        confirmButton = dialog.findViewById(R.id.confirm)

        logOutButton.setOnClickListener{
            dialog.show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        confirmButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            sharedPrefManager.clearUserData()

            SessionManager.clearSession()

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Sign Out Successfully", Toast.LENGTH_SHORT).show()

            finish()
        }

    }
}