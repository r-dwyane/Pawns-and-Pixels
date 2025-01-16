package com.example.pawnspixel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustomerSignUpFragment : BottomSheetDialogFragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val sharedPrefManager = SharedPrefManager(requireContext())

        val signIn = view.findViewById<TextView>(R.id.signin_portal)
        signIn.setOnClickListener {
            dismiss()
            val fragment2 = CustomerSignInFragment()
            fragment2.show(parentFragmentManager, "Android Center")
        }

        val emailField = view.findViewById<EditText>(R.id.email)
        val nameField = view.findViewById<EditText>(R.id.customer_name)
        val passwordField = view.findViewById<EditText>(R.id.password)
        val confirmPasswordField = view.findViewById<EditText>(R.id.password_2)
        val contactField = view.findViewById<EditText>(R.id.contact_number)
        val signUp = view.findViewById<Button>(R.id.signup)

        signUp.setOnClickListener {
            val email = emailField.text.toString()
            val name = nameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val contactNumber = contactField.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && contactNumber.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = firebaseAuth.currentUser?.uid
                                val user = hashMapOf(
                                    "name" to name,
                                    "email" to email,
                                    "contact_number" to contactNumber
                                )
                                if (userId != null) {
                                    firestore.collection("users").document(userId).set(user)
                                        .addOnSuccessListener {
                                            Toast.makeText(requireContext(), "User registered successfully", Toast.LENGTH_SHORT).show()
                                            sharedPrefManager.saveUserData(email, name, password, contactNumber, userId)

                                            val intent2 = Intent(requireContext(), HomeActivity::class.java)
                                            startActivity(intent2)
                                            requireActivity().finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(requireContext(), "Failed to create account: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
