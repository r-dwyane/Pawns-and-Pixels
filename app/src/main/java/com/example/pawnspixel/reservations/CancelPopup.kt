package com.example.pawnspixel.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pawnspixel.HomeActivity
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class CancelPopup : DialogFragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cancel_popup, container, false)

        val reservationId = arguments?.getString("reservationId")

        val backToHomeButton: Button = view.findViewById(R.id.noConfirm)
        backToHomeButton.setOnClickListener {
            dismiss()
        }

        val cancel: Button = view.findViewById(R.id.confirmCancel)
        cancel.setOnClickListener {
            reservationId?.let { id ->
                cancelReservation(id)
            }
        }
        return view
    }

    private fun cancelReservation(reservationId: String) {
        db.collection("bookings").document(reservationId)
            .update("status", "Cancelled")
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show()
                dismiss()
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                requireActivity().finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
