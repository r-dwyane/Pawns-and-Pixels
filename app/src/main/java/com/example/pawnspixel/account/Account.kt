package com.example.pawnspixel.account

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.SharedPrefManager
import com.example.pawnspixel.StartActivity
import com.example.pawnspixel.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Account : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var cancelButton: Button
    private lateinit var confirmButton: Button
    private lateinit var swingRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.accountName.text = SessionManager.name.toString()
        binding.accountEmail.text = SessionManager.email.toString()
        swingRefreshLayout = binding.swipe

        val sharedPrefManager = SharedPrefManager(requireContext())

        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE

        swingRefreshLayout.setOnRefreshListener {
            onRefresh()
        }

        binding.editInfo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                    )
                    .replace(R.id.nav_host_fragment, UpdateDetails())
                    .addToBackStack(null)
                    .commit()
        }

        binding.termsConditions.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, TermsAndConditions())
                .addToBackStack(null)
                .commit()
        }

        binding.privacyPolicy.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, PrivacyPolicy())
                .addToBackStack(null)
                .commit()
        }

        binding.signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            sharedPrefManager.clearUserData()

            SessionManager.clearSession()

            val intent = Intent(requireActivity(), StartActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), "Sign Out Successfully", Toast.LENGTH_SHORT).show()

            requireActivity().finish()
        }


//        dialog = Dialog(requireContext())
//        dialog.setContentView(R.layout.fragment_popup)
//        dialog.window?.setLayout(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialog.setCancelable(false)
//        cancelButton = dialog.findViewById(R.id.cancel)
//        confirmButton = dialog.findViewById(R.id.confirm)
//
//        binding.logout.setOnClickListener {
//            dialog.show()
//        }
//
//        // Cancel logout
//        cancelButton.setOnClickListener {
//            dialog.dismiss()
//        }

        // Confirm logout
//        confirmButton.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//
//            sharedPrefManager.clearUserData()
//
//            SessionManager.clearSession()
//
//            val intent = Intent(requireActivity(), StartActivity::class.java)
//            startActivity(intent)
//            Toast.makeText(requireContext(), "Sign Out Successfully", Toast.LENGTH_SHORT).show()
//
//            requireActivity().finish()
//        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE
    }

    private fun onRefresh() {
        fetchUserDataFromFirebase()

        binding.accountName.text = SessionManager.name.toString()
        binding.accountEmail.text = SessionManager.email.toString()
        swingRefreshLayout.isRefreshing = false
    }

    private fun fetchUserDataFromFirebase() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = SessionManager.userId
        val userRef = userId?.let { FirebaseFirestore.getInstance().collection("users").document(it) }

        userRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val name = documentSnapshot.getString("name")
                val email = documentSnapshot.getString("email")

                val sharedPrefManager = SharedPrefManager(requireContext())
                if (email != null && name != null) {
                    sharedPrefManager.setUserEmail(email)
                    sharedPrefManager.setUserName(name)
                    SessionManager.name = name
                    SessionManager.email = email
                }
            }
        }
    }
}
