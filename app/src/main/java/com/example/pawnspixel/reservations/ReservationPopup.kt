package com.example.pawnspixel.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.pawnspixel.HomeActivity
import com.example.pawnspixel.R

class ReservationPopup : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_sent, container, false)

        val backToHomeButton: Button = view.findViewById(R.id.backToHome)
        backToHomeButton.setOnClickListener {
            dismiss()

            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            requireActivity().finish()
        }

        return view
    }
}
