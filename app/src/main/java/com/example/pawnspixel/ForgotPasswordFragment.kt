package com.example.pawnspixel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUp = view.findViewById<TextView>(R.id.signup_portal)
        signUp.setOnClickListener {
            dismiss()
            val fragment = CustomerSignUpFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }

        val emailEditText = view.findViewById<EditText>(R.id.email_field)
        val forgotPasswordButton = view.findViewById<TextView>(R.id.send_link)

        forgotPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()

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
                            dismiss()
                        } else {
                            val errorMessage = task.exception?.message
                            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
