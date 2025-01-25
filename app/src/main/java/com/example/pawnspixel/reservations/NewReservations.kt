package com.example.pawnspixel.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewReservations : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_reservations, container, false)

        return view
    }

}