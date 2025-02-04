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

class CancelledFragment : Fragment() {

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

                val bundle = Bundle().apply {
                    putString("status", clickedReservation.status)
                    putString("room", clickedReservation.room)
                    putString("date", clickedReservation.date)
                    putString("startTime", clickedReservation.startTime)
                    putString("endTime", clickedReservation.endTime)
                    putString("reservationId", clickedReservation.reservationId)
                    putString("numberOfPlayers", clickedReservation.players)
                    putString("createdAt", clickedReservation.createdAt)
                }

                val detailsFragment = OngoingDetails()
                detailsFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.nav_host_fragment, detailsFragment)
                    .addToBackStack(null)
                    .commit()
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
            .whereEqualTo("status", "Cancelled")
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
                    adapter.notifyDataSetChanged()
                }

            }
    }


}
