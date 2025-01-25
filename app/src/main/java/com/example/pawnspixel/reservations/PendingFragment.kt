package com.example.pawnspixel.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R

class PendingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pending, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.pendingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val reservations = listOf(
            ReservationsDataClass("Pending", "Game Room 1", "2025-01-25"),
            ReservationsDataClass("Pending", "Game Room 2", "2025-01-26"),
            ReservationsDataClass("Pending", "Game Room 3", "2025-01-27"),
            ReservationsDataClass("Pending", "Game Room 4", "2025-01-25")
        )

        recyclerView.adapter = ReservationsAdapter(reservations, object : ReservationsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedReservation = reservations[position]
                Toast.makeText(requireContext(), "Clicked: ${clickedReservation.room}", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}
