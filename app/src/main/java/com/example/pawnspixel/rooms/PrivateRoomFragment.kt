package com.example.pawnspixel.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.example.pawnspixel.R
import com.example.pawnspixel.reservations.NewReservations
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PrivateRoomFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_private_room, container, false)

        val add = view?.findViewById<Button>(R.id.reserve_now)
        add?.setOnClickListener {
            val fragment = NewReservations()
            fragment.show(parentFragmentManager, "Android Center")
            dismiss()
        }

        return view
    }
}