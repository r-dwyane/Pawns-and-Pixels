package com.example.pawnspixel.account

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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.RelativeLayout
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.SharedPrefManager
import com.example.pawnspixel.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateDetails : Fragment() {
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var emailText: EditText
    private lateinit var nameText: EditText
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var progressContainer: RelativeLayout
    private lateinit var progressBar: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_details, container, false)

        val button = view?.findViewById<ImageView>(R.id.update_back)
        emailText = view?.findViewById(R.id.updateEmail)!!
        nameText = view.findViewById(R.id.updateName)!!
        val forgotPassword = view.findViewById<TextView>(R.id.resetPassword)
        val save = view.findViewById<Button>(R.id.updateButton)

        sharedPrefManager = SharedPrefManager(requireContext())

        emailText.setText(SessionManager.email ?: "")
        nameText.setText(SessionManager.name ?: "")

        progressContainer = view.findViewById(R.id.progressContainer)
        progressBar = view.findViewById<ImageView>(R.id.progressImage)

        button?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        forgotPassword?.setOnClickListener {
            val emailInput = emailText.text.toString().trim()

            if (emailInput.isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(emailInput)
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
            val nameInput = nameText.text?.toString()?.trim() ?: ""
            val emailInput = emailText.text?.toString()?.trim() ?: ""

            Log.d("UpdateDetails", "Email: $emailInput, Name: $nameInput")

            if (emailInput.isEmpty()) {
                emailText.error = "Email cannot be empty"
                return@setOnClickListener
            }

            if (nameInput.isEmpty()) {
                nameText.error = "Name cannot be empty"
                return@setOnClickListener
            }

            updateEmail(emailInput, nameInput)
        }

        return view
    }

    private fun updateEmail(newEmail: String, newName: String) {
        val user = FirebaseAuth.getInstance().currentUser

        // Show the ProgressBar with dark translucent background
        progressContainer.visibility = View.VISIBLE

        user?.verifyBeforeUpdateEmail(newEmail)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Please check your email to verify the update", Toast.LENGTH_SHORT).show()

                user.reload().addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful) {
                        if (user.isEmailVerified) {
                            updateFirestore(newEmail, newName)
                        } else {
                            Toast.makeText(requireContext(), "Email not verified yet", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Error:", reloadTask.exception?.message ?: "Reload failed")
                    }

                    progressContainer.visibility = View.GONE
                }
            } else {
                Log.e("Error:", task.exception?.message ?: "Email update failed")

                FirebaseAuth.getInstance().signOut()
                sharedPrefManager.clearUserData()
                SessionManager.clearSession()

                val intent = Intent(requireActivity(), StartActivity::class.java)
                startActivity(intent)
                Toast.makeText(requireContext(), "Session Expired", Toast.LENGTH_SHORT).show()
                requireActivity().finish()

                Toast.makeText(requireContext(), "Please sign in again.", Toast.LENGTH_SHORT).show()

                progressContainer.visibility = View.GONE
            }
        }
    }

    private fun updateFirestore(newEmail: String, newName: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            val updateMap = mapOf(
                "name" to newName,
                "email" to newEmail
            )

            userRef.update(updateMap).addOnCompleteListener { task ->
                progressContainer.visibility = View.GONE

                if (task.isSuccessful) {
                    SessionManager.email = newEmail
                    SessionManager.name = newName
                    sharedPrefManager.setUserEmail(newEmail)
                    sharedPrefManager.setUserName(newName)

                    Toast.makeText(requireContext(), "User details updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Firestore Update", "Failed to update user data: ${task.exception}")
                }
            }
        } else {
            Log.e("Firestore", "User not authenticated")
            progressContainer.visibility = View.GONE
        }
    }
}
