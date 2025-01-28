package com.example.pawnspixel.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.SharedPrefManager
import com.example.pawnspixel.StartActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateDetails : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_details, container, false)

        val sharedPrefManager = SharedPrefManager(requireContext())

        val button = view?.findViewById<ImageView>(R.id.update_back)
        val emailText = view?.findViewById<EditText>(R.id.updateEmail)
        val nameText = view?.findViewById<EditText>(R.id.updateName)
        val forgotPassword = view?.findViewById<TextView>(R.id.resetPassword)
        val save = view?.findViewById<Button>(R.id.updateButton)

        emailText?.setText(SessionManager.email.toString())
        nameText?.setText(SessionManager.name.toString())

        val email = emailText?.text.toString()
        val name = nameText?.text.toString()
        button?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        forgotPassword?.setOnClickListener {
            if (email.isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Password reset email sent.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val errorMessage = task.exception?.message
                            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
        }

        save?.setOnClickListener {
            updateEmail(email)
            updateName(name)
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun updateEmail(newEmail: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val firestore = FirebaseFirestore.getInstance()
        val sharedPrefManager = SharedPrefManager(requireContext())

        user?.let { currentUser ->
            promptForPassword { password ->
                if (password.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show()
                    return@promptForPassword
                }

                val credential = EmailAuthProvider.getCredential(SessionManager.email.toString(), password)

                currentUser.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
                        currentUser.updateEmail(newEmail).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Firebase", "Email updated successfully")

                                // Update session
                                SessionManager.email = newEmail
                                sharedPrefManager.saveUserData(newEmail, SessionManager.name.toString(), SessionManager.contactNumber.toString(), currentUser.uid.toString())

                                // Update email in Firestore
                                val userId = currentUser.uid
                                val userRef = firestore.collection("users").document(userId)
                                userRef.update("email", newEmail)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Email updated in Firestore")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("Firestore", "Error updating email in Firestore", exception)
                                        Toast.makeText(requireContext(), "Failed to update email in Firestore", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                Log.e("Firebase", "Error updating email in Firebase", task.exception)
                                Toast.makeText(requireContext(), "Failed to update email in Firebase", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("Firebase", "Reauthentication failed", reAuthTask.exception)
                        Toast.makeText(requireContext(), "Reauthentication failed. Please check your password.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun promptForPassword(callback: (String) -> Unit) {
        val passwordEditText = EditText(requireContext())
        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Enter Password")
            .setView(passwordEditText)
            .setPositiveButton("OK") { _, _ ->
                callback(passwordEditText.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                callback("")
                dialog.cancel()
            }
            .create()

        dialog.show()
    }


    private fun updateName(newName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val firestore = FirebaseFirestore.getInstance()
        val sharedPrefManager = SharedPrefManager(requireContext())

        user?.let {
            val userId = it.uid
            val userRef = firestore.collection("users").document(userId)
            val userUpdates = hashMapOf("name" to newName)

            userRef.update(userUpdates as Map<String, String>)
                .addOnSuccessListener {
                    Log.d("Firestore", "User name updated in Firestore")

                    SessionManager.name = newName
                    sharedPrefManager.saveUserData(SessionManager.email.toString(), newName, SessionManager.contactNumber.toString(), userId)

                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error updating user name", exception)
                    Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show()
                }
        }
    }

}