package com.example.pawnspixel.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AdminSignInFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_sign_in, container, false)

        val signButton = view.findViewById<Button>(R.id.admin_button)

        signButton.setOnClickListener {
            val intent = Intent(requireContext(), AdminActivity::class.java)
            startActivity(intent)
            dismiss()
        }
        return view
    }

}