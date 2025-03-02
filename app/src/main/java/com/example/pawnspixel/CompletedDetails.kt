package com.example.pawnspixel.reservations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.example.pawnspixel.SessionManager

class CompletedDetails : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed_details, container, false)

        val statusText: TextView = view.findViewById(R.id.completed_status)
        val roomText: TextView = view.findViewById(R.id.completed_room)
        val dateText: TextView = view.findViewById(R.id.completed_date)
        val startTimeText: TextView = view.findViewById(R.id.completed_start_time)
        val endTimeText: TextView = view.findViewById(R.id.completed_end_time)
        val reservationIdText: TextView = view.findViewById(R.id.completed_reservation_id)
        val playersText: TextView = view.findViewById(R.id.completed_players)
        val createdAtText: TextView = view.findViewById(R.id.completedCreated_at)
        val email: TextView = view.findViewById(R.id.completed_email)
        val name: TextView = view.findViewById(R.id.completed_name)
        val back: ImageView = view.findViewById(R.id.backHome85)

        back.setOnClickListener{
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
        }

        return view
    }
}
