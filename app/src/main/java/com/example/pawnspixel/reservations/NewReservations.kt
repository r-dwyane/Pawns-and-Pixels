package com.example.pawnspixel.reservations

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.pawnspixel.GetDetails
import com.example.pawnspixel.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class NewReservations : BottomSheetDialogFragment() {
    private lateinit var nintendoButton: FrameLayout
    private lateinit var privateButton: FrameLayout
    private lateinit var pixelsButton1: FrameLayout
    private lateinit var pixelsButton2: FrameLayout
    private lateinit var billiardButton: FrameLayout
    private lateinit var progressContainer: RelativeLayout
    private lateinit var nintendoStatus: TextView
    private lateinit var privateStatus: TextView
    private lateinit var pixelsStatus1: TextView
    private lateinit var pixelsStatus2: TextView
    private lateinit var billiardStatus: TextView
    private lateinit var room: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_reservations, container, false)

        nintendoButton = view.findViewById(R.id.nintendo_roomLayout)
        privateButton = view.findViewById(R.id.private_roomLayout)
        pixelsButton1 = view.findViewById(R.id.pixels_gaming1Layout)
        pixelsButton2 = view.findViewById(R.id.pixels_gaming2Layout)
        billiardButton = view.findViewById(R.id.billiardLayout)

        nintendoStatus = view.findViewById(R.id.nintendo_status)
        privateStatus = view.findViewById(R.id.private_status)
        pixelsStatus1 = view.findViewById(R.id.pixelsGaming1_status)
        pixelsStatus2 = view.findViewById(R.id.pixelsGaming2_status)
        billiardStatus = view.findViewById(R.id.billiard_status)

        progressContainer = view.findViewById(R.id.progressContainerNew)

        fetchRoomStatuses()
        GetDetails.clearSession()
        nintendoButton.setOnClickListener { roomName("nitendo") }
        privateButton.setOnClickListener { roomName("private_room") }
        pixelsButton1.setOnClickListener { roomName("pixels_gaming1") }
        pixelsButton2.setOnClickListener { roomName("pixels_gaming2") }
        billiardButton.setOnClickListener { roomName("billiard") }

        return view
    }

    private fun fetchRoomStatuses() {
        val db = FirebaseFirestore.getInstance()
        progressContainer.visibility = View.VISIBLE

        db.collection("rooms")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val roomName = document.getString("room_name") ?: "Unknown"
                    val status = document.getString("status") ?: "Unknown"

                    when (roomName.lowercase(Locale.ROOT)) {
                        "nintendo_room" -> updateRoomUI(nintendoButton, nintendoStatus, status)
                        "private_room" -> updateRoomUI(privateButton, privateStatus, status)
                        "pixels_gaming1" -> updateRoomUI(pixelsButton1, pixelsStatus1, status)
                        "pixels_gaming2" -> updateRoomUI(pixelsButton2, pixelsStatus2, status)
                        "billiard" -> updateRoomUI(billiardButton, billiardStatus, status)
                    }
                    progressContainer.visibility = View.GONE
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching room statuses", e)
            }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateRoomUI(button: FrameLayout, statusView: TextView, status: String) {
        statusView.text = "Status: $status"

        if (status.equals("Maintenance", ignoreCase = true)) {
            button.isEnabled = false
            button.alpha = 0.5f
        } else {
            button.isEnabled = true
            button.alpha = 1.0f
        }
    }


    private fun roomName(roomName: String) {
        val bundle = Bundle()
        room = when (roomName) {
            "nitendo" -> "Nintendo Switch Game Room"
            "private_room" -> "Private Room"
            "pixels_gaming1" -> "Pixels Gaming (TV 1)"
            "pixels_gaming2" -> "Pixels Gaming (TV 2)"
            "billiard" -> "Billiard Table"
            else -> "Unknown Room"
        }

        bundle.putString("room", room)
        parentFragmentManager.setFragmentResult("requestKey", bundle)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.nav_host_fragment, ReservationInfo())
            .addToBackStack(null)
            .commit()
        dismiss()
    }
}
