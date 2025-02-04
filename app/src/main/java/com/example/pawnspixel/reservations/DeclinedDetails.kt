package com.example.pawnspixel.reservations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class DeclinedDetails : Fragment() {

    private val db = FirebaseFirestore.getInstance() // Firestore instance

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_declined_details, container, false)

        val statusText: TextView = view.findViewById(R.id.declined_status)
        val roomText: TextView = view.findViewById(R.id.declined_room)
        val dateText: TextView = view.findViewById(R.id.declined_date)
        val startTimeText: TextView = view.findViewById(R.id.declined_start_time)
        val endTimeText: TextView = view.findViewById(R.id.declined_end_time)
        val reservationIdText: TextView = view.findViewById(R.id.declined_reservation_id)
        val playersText: TextView = view.findViewById(R.id.declined_players)
        val createdAtText: TextView = view.findViewById(R.id.declined_created_at)
        val email: TextView = view.findViewById(R.id.declined_email)
        val name: TextView = view.findViewById(R.id.declined_name)
        val reasonText: TextView = view.findViewById(R.id.declined_reason)
        val back: ImageView = view.findViewById(R.id.backHome84)

        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        arguments?.let {
            statusText.text = "Status: ${it.getString("status")}"
            roomText.text = "Room: ${it.getString("room")}"
            dateText.text = "Date: ${it.getString("date")}"
            startTimeText.text = "Start Time: ${it.getString("startTime")}"
            endTimeText.text = "End Time: ${it.getString("endTime")}"
            reservationIdText.text = "Reservation ID: ${it.getString("reservationId")}"
            playersText.text = "Players: ${it.getString("numberOfPlayers")}"
            createdAtText.text = "Created At: ${it.getString("createdAt")}"
            name.text = SessionManager.name
            email.text = SessionManager.email

            val reservationId = it.getString("reservationId")
            if (!reservationId.isNullOrEmpty()) {
                fetchDeclinedReason(reservationId, reasonText)
            }
        }

        return view
    }

    private fun fetchDeclinedReason(reservationId: String, reasonText: TextView) {
        db.collection("bookings").document(reservationId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val reason = document.getString("reason") ?: "No reason provided"
                    reasonText.text = reason
                } else {
                    reasonText.text = "Reason: Not Found"
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
