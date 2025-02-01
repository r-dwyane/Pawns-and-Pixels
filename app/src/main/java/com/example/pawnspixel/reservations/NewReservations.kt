package com.example.pawnspixel.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.example.pawnspixel.games.BoardGames
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewReservations : BottomSheetDialogFragment() {
    private lateinit var nintendoButton: FrameLayout
    private lateinit var privateButton: FrameLayout
    private lateinit var pixelsButton1: FrameLayout
    private lateinit var pixelsButton2: FrameLayout
    private lateinit var billiardButton: FrameLayout

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

        nintendoButton = view?.findViewById(R.id.nintendo_roomLayout)!!
        privateButton = view.findViewById(R.id.private_roomLayout)!!
        pixelsButton1 = view.findViewById(R.id.pixels_gaming1Layout)!!
        pixelsButton2 = view.findViewById(R.id.pixels_gaming2Layout)!!
        billiardButton = view.findViewById(R.id.billiardLayout)!!

        nintendoStatus = view.findViewById(R.id.nintendo_status)!!
        privateStatus = view.findViewById(R.id.private_status)!!
        pixelsStatus1 = view.findViewById(R.id.pixelsGaming1_status)!!
        pixelsStatus2 = view.findViewById(R.id.pixelsGaming2_status)!!
        billiardStatus = view.findViewById(R.id.billiard_status)!!

        nintendoButton.setOnClickListener {
            roomName("nitendo")
        }
        privateButton.setOnClickListener {
            roomName("private_room")
        }
        pixelsButton1.setOnClickListener {
            roomName("pixels_gaming1")
        }
        pixelsButton2.setOnClickListener {
            roomName("pixels_gaming2")
        }
        billiardButton.setOnClickListener {
            roomName("billiard")
        }
        return view
    }

    private fun roomName(roomName: String){
        val bundle = Bundle()
        if(roomName == "nitendo"){
            room = "Nintendo Switch Game Room"
        }
        else if(roomName == "private_room"){
            room = "Private Room"
        }
        else if(roomName == "pixels_gaming1"){
            room = "Pixels Gaming (TV 1)"
        }
        else if(roomName == "pixels_gaming2"){
            room = "Pixels Gaming (TV 2)"
        }
        else if(roomName == "billiard"){
            room = "Billiard Table"
        }

        bundle.putString("room", room)
        parentFragmentManager.setFragmentResult("requestKey", bundle)
        dismiss()
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
    }

}