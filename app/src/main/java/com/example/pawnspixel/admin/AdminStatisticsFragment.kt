package com.example.pawnspixel.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminStatisticsFragment : Fragment() {

    private lateinit var nintendoBookingsStatsText:TextView
    private lateinit var nintendoPlayersStatsText:TextView
    private lateinit var pixelsGamingBookingsStatsText:TextView
    private lateinit var pixelsGamingPlayersStatsText:TextView
    private lateinit var billiardTableBookingsStatsText:TextView
    private lateinit var billiardTablePlayersStatsText:TextView
    private lateinit var privateRoomBookingsStatsText:TextView
    private lateinit var privateRoomPlayersStatsText:TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_statistics, container, false)
        val backBtnAdminStats: ImageButton = view.findViewById(R.id.backBtnAdminStats)!!
        nintendoBookingsStatsText = view.findViewById(R.id.nintendoBookingsStatsText)
        nintendoPlayersStatsText = view.findViewById(R.id.nintendoPlayersStatsText)
        pixelsGamingBookingsStatsText = view.findViewById(R.id.pixelsGamingBookingsStatsText)
        pixelsGamingPlayersStatsText = view.findViewById(R.id.pixelsGamingPlayersStatsText)
        billiardTableBookingsStatsText = view.findViewById(R.id.billiardTableBookingsStatsText)
        billiardTablePlayersStatsText = view.findViewById(R.id.billiardTablePlayersStatsText)
        privateRoomBookingsStatsText = view.findViewById(R.id.privateRoomBookingsStatsText)
        privateRoomPlayersStatsText = view.findViewById(R.id.privateRoomPlayersStatsText)

        backBtnAdminStats.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        loadNintendoCount()
        loadBilliardCount()
        loadPrivateRoomCount()
        loadPixelsGamingCount()

        return view
    }

    private fun loadNintendoCount() {
        val bookingsRef = db.collection("bookings")
        bookingsRef.whereEqualTo("room", "Nintendo Switch Game Room")
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { documents ->
                var totalPlayers = 0
                var count = 0

                for (document in documents) {
                    val playersStr = document.getString("players") ?: "0"
                    totalPlayers += playersStr.toIntOrNull() ?: 0
                    count++
                }

                nintendoBookingsStatsText.text = count.toString()
                nintendoPlayersStatsText.text = totalPlayers.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error getting documents: ", exception)
            }
    }

    private fun loadBilliardCount() {
        val bookingsRef = db.collection("bookings")
        bookingsRef.whereEqualTo("room", "Billiard Table")
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { documents ->
                var totalPlayers = 0
                var count = 0

                for (document in documents) {
                    val playersStr = document.getString("players") ?: "0"
                    totalPlayers += playersStr.toIntOrNull() ?: 0
                    count++
                }

                billiardTableBookingsStatsText.text = count.toString()
                billiardTablePlayersStatsText.text = totalPlayers.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error getting documents: ", exception)
            }
    }

    private fun loadPrivateRoomCount() {
        val bookingsRef = db.collection("bookings")
        bookingsRef.whereEqualTo("room", "Private Room")
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { documents ->
                var totalPlayers = 0
                var count = 0

                for (document in documents) {
                    val playersStr = document.getString("players") ?: "0"
                    totalPlayers += playersStr.toIntOrNull() ?: 0
                    count++
                }

                privateRoomBookingsStatsText.text = count.toString()
                privateRoomPlayersStatsText.text = totalPlayers.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error getting documents: ", exception)
            }
    }

    private fun loadPixelsGamingCount(){
        val db = FirebaseFirestore.getInstance()
        val bookingsRef = db.collection("bookings")

        bookingsRef.whereIn("room", listOf("Pixels Gaming (TV 1)", "Pixels Gaming (TV 2)"))
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { documents ->
                var totalPlayers = 0
                var count = 0

                for (document in documents) {
                    val playersStr = document.getString("players") ?: "0"
                    totalPlayers += playersStr.toIntOrNull() ?: 0
                    count++
                }

                pixelsGamingBookingsStatsText.text = count.toString()
                pixelsGamingPlayersStatsText.text = totalPlayers.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error getting documents: ", exception)
            }

    }
}