package com.example.pawnspixel

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawnspixel.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth

class Account : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var cancelButton: Button
    private lateinit var confirmButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        val sharedPrefManager = SharedPrefManager(requireContext())

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
            Toast.makeText(requireContext(), "Name: $userName \n Email: $userEmail", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "User details not available", Toast.LENGTH_SHORT).show()
        }

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_popup)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        cancelButton = dialog.findViewById(R.id.cancel)
        confirmButton = dialog.findViewById(R.id.confirm)

        binding.logout.setOnClickListener {
            dialog.show()
        }

        // Cancel logout
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // Confirm logout
        confirmButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            sharedPrefManager.clearUserData()

            SessionManager.clearSession()

            // Use the requireActivity() context for starting the activity
            val intent = Intent(requireActivity(), StartActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), "Sign Out Successfully", Toast.LENGTH_SHORT).show()

            // Finish the activity from the fragment's parent activity
            requireActivity().finish()
        }

        return binding.root
    }
}
