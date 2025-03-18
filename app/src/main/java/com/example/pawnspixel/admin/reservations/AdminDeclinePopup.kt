package com.example.pawnspixel.admin.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminDeclinePopup : DialogFragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var reasonEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_decline_popup, container, false)

        reasonEditText = view.findViewById(R.id.reason)
        val reservationId = arguments?.getString("reservationId")

        val backToHomeButton: Button = view.findViewById(R.id.noConfirm)
        backToHomeButton.setOnClickListener {
            dismiss()
        }

        val cancel: Button = view.findViewById(R.id.confirmCancel)
        cancel.setOnClickListener {
            reservationId?.let { id ->
                validateAndCancel(id)
            }
        }

        return view
    }

    private fun validateAndCancel(reservationId: String) {
        val reason = reasonEditText.text.toString().trim()

        when {
            reason.isEmpty() -> {
                reasonEditText.error = "Reason is required!"
                reasonEditText.requestFocus()
            }
            reason.length < 5 -> {
                reasonEditText.error = "Reason must be at least 5 characters long!"
                reasonEditText.requestFocus()
            }
            else -> {
                cancelReservation(reservationId, reason)
            }
        }
    }

    private fun cancelReservation(reservationId: String, reason: String) {
        val updates = mapOf(
            "status" to "Declined",
            "reason" to reason
        )

        db.collection("bookings").document(reservationId)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Reservation Declined", Toast.LENGTH_SHORT).show()
                dismiss()
                val intent = Intent(requireContext(), AdminActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                requireActivity().finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
