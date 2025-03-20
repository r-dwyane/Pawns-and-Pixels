package com.example.pawnspixel.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeFragment : Fragment() {

    private lateinit var stats: ImageButton
    private lateinit var fb: TextView
    private lateinit var totalBookingsText: TextView
    private lateinit var totalPlayersText: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)
        stats = view.findViewById(R.id.statisticsBtn)
        fb = view.findViewById(R.id.userFbViewMore)
        totalBookingsText = view.findViewById(R.id.totalBookingsText)
        totalPlayersText = view.findViewById(R.id.totalPlayersText)

        stats.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminStatisticsFragment())
                .addToBackStack(null)
                .commit()
        }

        fb.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminUserFeedbackFragment())
                .addToBackStack(null)
                .commit()
        }

        listenToCompletedBookings()

        return view
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.admin_nav_host_fragment)?.visibility = View.VISIBLE
    }

    private fun listenToCompletedBookings() {
        val progressContainer = view?.findViewById<View>(R.id.admin_progressContainerNew)

        progressContainer?.visibility = View.VISIBLE

        db.collection("bookings")
            .whereEqualTo("status", "Completed")
            .get()
            .addOnSuccessListener { snapshots ->
                val totalBookings = snapshots.size()
                var totalPlayers = 0

                for (document in snapshots.documents) {
                    val playersString = document.getString("players") ?: "0"
                    val players = playersString.toIntOrNull() ?: 0
                    totalPlayers += players
                }

                totalBookingsText.text = totalBookings.toString()
                totalPlayersText.text = totalPlayers.toString()
            }
            .addOnFailureListener {
                totalBookingsText.text = "Failed to load bookings"
                totalPlayersText.text = "Failed to load players"
            }
            .addOnCompleteListener {
                progressContainer?.visibility = View.GONE
            }
    }


}
