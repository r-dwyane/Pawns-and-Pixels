package com.example.pawnspixel.reservations

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.pawnspixel.GetDetails
import com.example.pawnspixel.Home
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.authentication.CustomerSignInFragment
import com.example.pawnspixel.games.BoardGames
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReservationSummary : Fragment() {
    private lateinit var room: TextView
    private lateinit var date: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var players: TextView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var confirm: Button
    private lateinit var backToHome: Button
    private lateinit var progressContainer: RelativeLayout
    private lateinit var popup: Dialog

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
        progressContainer = view.findViewById(R.id.progressContainerSummary)

        popup = Dialog(requireContext())

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

    private fun confirmReservation() {
        progressContainer.visibility = View.VISIBLE
        val db = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            progressContainer.visibility = View.GONE
            Toast.makeText(context, "Please sign in to make a reservation", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss a", Locale.getDefault())
        val formattedTimestamp = dateFormat.format(Date())

        // Ensure GetDetails and SessionManager are not null before using them
        val bookingData = hashMapOf(
            "userId" to SessionManager.userId,
            "room" to GetDetails.room,
            "startTime" to GetDetails.startTime,
            "endTime" to GetDetails.endTime,
            "date" to GetDetails.date,
            "status" to "Pending",
            "createdAt" to formattedTimestamp,
            "players" to GetDetails.players
        )

        db.collection("bookings")
            .add(bookingData)
            .addOnSuccessListener {
                progressContainer.visibility = View.GONE
                val popupFragment = ReservationPopup()
                popupFragment.show(parentFragmentManager, "ReservationPopup")
            }
            .addOnFailureListener { e ->
                progressContainer.visibility = View.GONE
                Log.w("Firestore", "Error adding booking", e)
                Toast.makeText(context, "Failed to add booking. Try again later.", Toast.LENGTH_SHORT).show()
            }
    }


}
