package com.example.pawnspixel.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AdminSignInFragment : BottomSheetDialogFragment() {
    private lateinit var progressContainer: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_sign_in, container, false)
        progressContainer = view?.findViewById(R.id.progressContainer2)!!
        val signButton = view.findViewById<Button>(R.id.admin_button)

        signButton.setOnClickListener {
            progressContainer.visibility = View.VISIBLE

            android.os.Handler().postDelayed({
                progressContainer.visibility = View.GONE
                val intent = Intent(requireContext(), AdminActivity::class.java)
                startActivity(intent)
                dismiss()
            }, 2000)

        }
        return view
    }

}