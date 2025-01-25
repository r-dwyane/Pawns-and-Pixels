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

class PixelsGamingFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pixels_gaming, container, false)

        val items = arrayOf("Php 99 | 1 pax per hour", "Php 69 | 2 pax per hour", "Php 59 | 3-4 pax per hour", "Php 69 | 5+ pax per hour")

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)
        val adapterItems = ArrayAdapter(requireContext(), R.layout.list_item, items)
        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                // Get the selected item value
                val item = adapterView.getItemAtPosition(position).toString()
            }
        val add = view?.findViewById<Button>(R.id.reserve_now3)
        add?.setOnClickListener {
            val fragment = NewReservations()
            fragment.show(parentFragmentManager, "Android Center")
            dismiss()
        }

        return view
    }
}