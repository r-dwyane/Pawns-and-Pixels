package com.example.pawnspixel.admin.reservations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminAcceptedDetails : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_accepted_details, container, false)

        val roomText: TextView = view.findViewById(R.id.admin_accepted_details_room)
        val dateText: TextView = view.findViewById(R.id.admin_accepted_details_date)
        val startTimeText: TextView = view.findViewById(R.id.admin_accepted_details_start_time)
        val endTimeText: TextView = view.findViewById(R.id.admin_accepted_details_end_time)
        val reservationIdText: TextView = view.findViewById(R.id.admin_accepted_details_reservation_id)
        val playersText: TextView = view.findViewById(R.id.admin_accepted_details_players)
        val createdAtText: TextView = view.findViewById(R.id.admin_accepted_details_created_at)
        val emailText: TextView = view.findViewById(R.id.admin_accepted_details_email)
        val nameText: TextView = view.findViewById(R.id.admin_accepted_details_name)
        val back: ImageView = view.findViewById(R.id.admin_backHome82)
        val ongoing: Button = view.findViewById(R.id.ongoingReservation)

        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        arguments?.let {
            roomText.text = "Room: ${it.getString("room")}"
            dateText.text = "Date: ${it.getString("date")}"
            startTimeText.text = "Start Time: ${it.getString("startTime")}"
            endTimeText.text = "End Time: ${it.getString("endTime")}"
            reservationIdText.text = "Reservation ID: ${it.getString("reservationId")}"
            playersText.text = "Players: ${it.getString("numberOfPlayers")}"
            createdAtText.text = "Created At: ${it.getString("createdAt")}"

            val reservationId = it.getString("reservationId")

            if (reservationId != null) {
                fetchUserIdAndName(reservationId, nameText, emailText)

                ongoing.setOnClickListener {
                    db.collection("bookings").document(reservationId)
                        .update("status", "Ongoing")
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Reservation is Ongoing", Toast.LENGTH_SHORT).show()
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
        }

        return view
    }

    private fun fetchUserIdAndName(reservationId: String, nameText: TextView, emailText: TextView) {
        db.collection("bookings").document(reservationId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userId = document.getString("userId")
                    if (userId != null) {
                        fetchUserName(userId, nameText, emailText)
                    } else {
                        nameText.text = "Unknown"
                        emailText.text = "Unknown"
                        Log.e("Firestore", "No userId found for reservationId: $reservationId")
                    }
                } else {
                    Log.e("Firestore", "No document found for reservationId: $reservationId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching userId: ${e.message}")
            }
    }

    private fun fetchUserName(userId: String, nameText: TextView, emailText: TextView) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: "Unknown"
                    val email = document.getString("email") ?: "Unknown"
                    nameText.text = name
                    emailText.text = email
                } else {
                    nameText.text = "Unknown"
                    emailText.text = "Unknown"
                    Log.e("Firestore", "No user found for userId: $userId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching user details: ${e.message}")
            }
    }
}
