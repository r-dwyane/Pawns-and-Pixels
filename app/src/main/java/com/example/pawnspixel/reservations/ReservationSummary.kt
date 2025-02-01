package com.example.pawnspixel.reservations

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.example.pawnspixel.GetDetails
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class ReservationSummary : Fragment() {
    private lateinit var room : TextView
    private lateinit var date : TextView
    private lateinit var startTime : TextView
    private lateinit var endTime : TextView
    private lateinit var players : TextView
    private lateinit var name : TextView
    private lateinit var email : TextView
    private lateinit var confirm: Button

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_summary, container, false)
        room = view.findViewById(R.id.room)
        date = view.findViewById(R.id.date)
        startTime = view.findViewById(R.id.startTime)
        endTime = view.findViewById(R.id.endTime)
        players = view.findViewById(R.id.players)
        name = view.findViewById(R.id.customerName)
        email = view.findViewById(R.id.customerEmail)
        confirm = view.findViewById(R.id.confirm)

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatDate = GetDetails.date?.let { inputFormat.parse(it) }

        if (formatDate != null) {
            val outputFormat = SimpleDateFormat("yyyy MMMM dd", Locale.getDefault())
            val formattedDate = outputFormat.format(formatDate)

            room.text = "Room: ${GetDetails.room}"
            date.text = formattedDate
            startTime.text = "Start Time: ${GetDetails.startTime}"
            endTime.text = "End Time: ${GetDetails.endTime}"
            players.text = "Number of Players: ${GetDetails.players}"
            name.text = "Name: ${SessionManager.name}"
            email.text = "Email: ${SessionManager.email}"
        } else {
            date.text = "Invalid date format"
        }

        confirm.setOnClickListener {
            confirmReservation()
        }

        return view
    }

    private fun confirmReservation(){
        val db = FirebaseFirestore.getInstance()
        val bookingData = hashMapOf(
            "userId" to SessionManager.userId,
            "startTime" to GetDetails.startTime,
            "endTime" to GetDetails.endTime,
            "date" to GetDetails.date,
            "status" to "Pending",
            "createdAt" to FieldValue.serverTimestamp(),
            "players" to  GetDetails.players
        )

        val bookingRef = db.collection("bookings").document()
        val statusRef = bookingRef.collection("Pending")

        statusRef.add(bookingData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Reservation Requested", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding booking", e)
            }
    }

}
