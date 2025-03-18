package com.example.pawnspixel.admin.reservations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminDeclinedDetails : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_declined_details, container, false)

        val roomText: TextView = view.findViewById(R.id.admin_declined_details_room)
        val dateText: TextView = view.findViewById(R.id.admin_declined_details_date)
        val startTimeText: TextView = view.findViewById(R.id.admin_declined_details_start_time)
        val endTimeText: TextView = view.findViewById(R.id.admin_declined_details_end_time)
        val reservationIdText: TextView = view.findViewById(R.id.admin_declined_details_reservation_id)
        val playersText: TextView = view.findViewById(R.id.admin_declined_details_players)
        val createdAtText: TextView = view.findViewById(R.id.admin_declined_details_created_at)
        val emailText: TextView = view.findViewById(R.id.admin_declined_details_email)
        val nameText: TextView = view.findViewById(R.id.admin_declined_details_name)
        val reason: TextView = view.findViewById(R.id.admin_declined_reason)
        val back: ImageView = view.findViewById(R.id.admin_backHome82)

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
                fetchUserIdAndName(reservationId, nameText, emailText, reason)
            }
        }

        return view
    }

    private fun fetchUserIdAndName(reservationId: String, nameText: TextView, emailText: TextView, reasonText: TextView) {
        db.collection("bookings").document(reservationId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userId = document.getString("userId")
                    val reason = document.getString("reason")

                    if (!reason.isNullOrEmpty()) {
                        reasonText.text = reason
                        reasonText.visibility = View.VISIBLE
                    } else {
                        reasonText.visibility = View.GONE
                    }

                    if (!userId.isNullOrEmpty()) {
                        fetchUserName(userId, nameText, emailText)
                    } else {
                        val customerName = document.getString("customerName") ?: "Unknown"
                        nameText.text = customerName
                        emailText.visibility = View.GONE  // Hide email if there's no userId
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
                    val email = document.getString("email")

                    nameText.text = name

                    if (!email.isNullOrEmpty()) {
                        emailText.text = email
                        emailText.visibility = View.VISIBLE
                    } else {
                        emailText.visibility = View.GONE  // Hide email if it's null or empty
                    }
                } else {
                    nameText.text = "Unknown"
                    emailText.visibility = View.GONE  // Hide email if user not found
                    Log.e("Firestore", "No user found for userId: $userId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching user details: ${e.message}")
            }
    }

}