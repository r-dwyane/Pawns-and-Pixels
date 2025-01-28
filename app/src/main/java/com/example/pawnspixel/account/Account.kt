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
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.SharedPrefManager
import com.example.pawnspixel.StartActivity
import com.example.pawnspixel.databinding.FragmentAccountBinding
import com.example.pawnspixel.games.XboxGames
import com.google.firebase.auth.FirebaseAuth

class Account : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var cancelButton: Button
    private lateinit var confirmButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.accountName.text = SessionManager.name.toString()
        binding.accountEmail.text = SessionManager.email.toString()

        val sharedPrefManager = SharedPrefManager(requireContext())

        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE

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
}
