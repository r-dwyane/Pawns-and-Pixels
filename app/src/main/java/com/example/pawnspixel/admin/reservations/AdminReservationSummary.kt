package com.example.pawnspixel.admin.reservations

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
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
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager
import com.example.pawnspixel.admin.AdminActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminReservationSummary : Fragment() {
    private lateinit var room: TextView
    private lateinit var date: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var players: TextView
    private lateinit var name: TextView
    private lateinit var confirm: Button
    private lateinit var progressContainer: RelativeLayout
    private lateinit var popup: Dialog

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_reservation_summary, container, false)

        room = view.findViewById(R.id.admin_room)
        date = view.findViewById(R.id.admin_date)
        startTime = view.findViewById(R.id.admin_startTime)
        endTime = view.findViewById(R.id.admin_endTime)
        players = view.findViewById(R.id.admin_players)
        name = view.findViewById(R.id.admin_customerName2)
        confirm = view.findViewById(R.id.admin_confirm)
        progressContainer = view.findViewById(R.id.admin_progressContainerSummary)

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

        val dateFormat = SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss a", Locale.getDefault())
        val formattedTimestamp = dateFormat.format(Date())

        val bookingData = hashMapOf(
            "room" to GetDetails.room,
            "startTime" to GetDetails.startTime,
            "endTime" to GetDetails.endTime,
            "date" to GetDetails.date,
            "customerName" to SessionManager.name,
            "status" to "Ongoing",
            "createdAt" to formattedTimestamp,
            "players" to GetDetails.players
        )

        db.collection("bookings")
            .add(bookingData)
            .addOnSuccessListener {
                SessionManager.clearSession()
                GetDetails.clearSession()
                progressContainer.visibility = View.GONE

                Toast.makeText(requireContext(), "Customer Added Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), AdminActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                requireActivity().finish()
            }
            .addOnFailureListener { e ->
                progressContainer.visibility = View.GONE
                Log.w("Firestore", "Error adding booking", e)
                Toast.makeText(context, "Failed to add booking. Try again later.", Toast.LENGTH_SHORT).show()
            }
    }

}