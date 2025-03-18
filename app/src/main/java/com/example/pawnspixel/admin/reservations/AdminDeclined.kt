package com.example.pawnspixel.admin.reservations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FieldPath

class AdminDeclined : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminReservationAdapter
    private val reservationList = mutableListOf<AdminReservationsDataClass>()
    private val filteredList = mutableListOf<AdminReservationsDataClass>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var progressContainer: RelativeLayout
    private lateinit var defaultText: TextView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_pending, container, false)

        recyclerView = view.findViewById(R.id.admin_pendingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        progressContainer = view.findViewById(R.id.admin_progressPending)
        defaultText = view.findViewById(R.id.admin_noPendings)
        searchView = view.findViewById(R.id.searchView)!!

        adapter = AdminReservationAdapter(filteredList, object : AdminReservationAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedReservation = filteredList[position]

                val bundle = Bundle().apply {
                    putString("status", clickedReservation.status)
                    putString("room", clickedReservation.room)
                    putString("date", formatDate(clickedReservation.date))
                    putString("startTime", clickedReservation.startTime)
                    putString("endTime", clickedReservation.endTime)
                    putString("reservationId", clickedReservation.reservationId)
                    putString("numberOfPlayers", clickedReservation.players)
                    putString("createdAt", clickedReservation.createdAt)
                }

                val detailsFragment = AdminDeclinedDetails()
                detailsFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    .replace(R.id.admin_nav_host_fragment, detailsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })

        recyclerView.adapter = adapter
        fetchPendingReservations()
        setupSearchListener()

        return view
    }

    private fun fetchPendingReservations() {
        db.collection("bookings")
            .whereEqualTo("status", "Declined")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    Log.d("Error", error.message ?: "Unknown error")
                    return@addSnapshotListener
                }

                reservationList.clear()

                if (snapshots != null && !snapshots.isEmpty) {
                    defaultText.visibility = View.GONE

                    val tempReservations = mutableListOf<AdminReservationsDataClass>()
                    val userIds = mutableSetOf<String>()

                    for (document in snapshots) {
                        val reservation = document.toObject(AdminReservationsDataClass::class.java).copy(
                            reservationId = document.id
                        )
                        tempReservations.add(reservation)
                        reservation.userId?.let { userIds.add(it) }
                    }

                    if (userIds.isNotEmpty()) {
                        fetchCustomerNames(tempReservations, userIds)
                    } else {
                        reservationList.addAll(tempReservations)
                        filterReservations("")
                    }
                } else {
                    defaultText.visibility = View.VISIBLE
                    filterReservations("")
                }
            }
    }

    private fun fetchCustomerNames(reservations: List<AdminReservationsDataClass>, userIds: Set<String>) {
        db.collection("users")
            .whereIn(FieldPath.documentId(), userIds.toList())  // Query by document ID
            .get()
            .addOnSuccessListener { userSnapshots ->
                val userMap = mutableMapOf<String, String>()

                for (userDoc in userSnapshots) {
                    val userId = userDoc.id  // Get document ID (which matches userId in bookings)
                    val name = userDoc.getString("name") ?: "Unknown"  // Ensure the field name is correct
                    userMap[userId] = name
                }

                for (reservation in reservations) {
                    reservation.customerName = userMap[reservation.userId] ?: "Unknown"

                    if (reservation.customerName == "Unknown") {
                        Log.e("Firestore", "No customer name found for userId: ${reservation.userId}")
                    }
                }

                reservationList.addAll(reservations)
                filterReservations("")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching user names: ${e.message}")
                reservationList.addAll(reservations)
                filterReservations("")
            }
    }

    private fun setupSearchListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterReservations(newText.orEmpty())
                return true
            }
        })
    }

    private fun filterReservations(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())

        filteredList.clear()
        if (lowerCaseQuery.isEmpty()) {
            filteredList.addAll(reservationList)
        } else {
            filteredList.addAll(reservationList.filter { reservation ->
                reservation.customerName.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            })
        }

        adapter.notifyDataSetChanged()

        defaultText.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return dateString)
        } catch (e: Exception) {
            dateString
        }
    }
}
