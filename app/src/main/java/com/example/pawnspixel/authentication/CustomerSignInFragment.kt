package com.example.pawnspixel.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.pawnspixel.HomeActivity
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.SharedPrefManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustomerSignInFragment : BottomSheetDialogFragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPrefManager = SharedPrefManager(requireContext())

        val signUp = view.findViewById<TextView>(R.id.signup_portal)
        signUp.setOnClickListener {
            dismiss()
            val fragment = CustomerSignUpFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }

        val forgotPassword = view.findViewById<TextView>(R.id.forgot_password)
        forgotPassword.setOnClickListener {
            dismiss()
            val fragment = ForgotPasswordFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }

        val button = view.findViewById<Button>(R.id.login)
        button.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.email_field).text.toString()
            val password = view.findViewById<EditText>(R.id.password_field).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        fetchUserDetails(email)
                    } else {
                        Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Empty fields are invalid.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserDetails(email: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first()
                    val name = document.getString("name") ?: "Unknown"
                    val contactNumber = document.getString("contact_number") ?: "Not Available"
                    val userId = document.id

                    // Save user data to SharedPreferences
                    sharedPrefManager.saveUserData(email, name, contactNumber, userId)

                    // Update session manager
                    SessionManager.userId = userId
                    SessionManager.email = email
                    SessionManager.name = name

                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching user details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
