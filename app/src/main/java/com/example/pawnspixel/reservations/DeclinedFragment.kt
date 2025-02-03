package com.example.pawnspixel.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class DeclinedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationsAdapter
    private val reservationList = mutableListOf<ReservationsDataClass>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var progressContainer: RelativeLayout
    private lateinit var defaultText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pending, container, false)

        recyclerView = view.findViewById(R.id.pendingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        progressContainer = view.findViewById(R.id.progressPending)
        defaultText = view.findViewById(R.id.noPendings)

        adapter = ReservationsAdapter(reservationList, object : ReservationsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedReservation = reservationList[position]
                val details = """
                    Status: ${clickedReservation.status}
                    Room: ${clickedReservation.room}
                    Date: ${clickedReservation.date}
                    Start Time: ${clickedReservation.startTime}
                    End Time: ${clickedReservation.endTime}
                    Reservation ID: ${clickedReservation.reservationId}
                    Number of Players: ${clickedReservation.numberOfPlayers}
                    Created At: ${clickedReservation.createdAt}
                """.trimIndent()
                Toast.makeText(requireContext(), details, Toast.LENGTH_LONG).show()
            }
        })

        recyclerView.adapter = adapter

        fetchPendingReservations()

        return view
    }

    private fun fetchPendingReservations() {
        val currentUser = auth.currentUser?.uid ?: return

        db.collection("bookings")
            .whereEqualTo("userId", currentUser)
            .whereEqualTo("status", "Declined")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    reservationList.clear()
                    defaultText.visibility = View.GONE

                    for (document in snapshots) {
                        val reservation = document.toObject(ReservationsDataClass::class.java).copy(
                            reservationId = document.id
                        )
                        reservationList.add(reservation)
                    }

                    adapter.notifyDataSetChanged()
                } else {
                    defaultText.visibility = View.VISIBLE
                }

            }
    }


}
