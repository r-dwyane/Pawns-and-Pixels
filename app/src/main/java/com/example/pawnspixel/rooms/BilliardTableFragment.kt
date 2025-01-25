package com.example.pawnspixel.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import com.example.pawnspixel.R
import com.example.pawnspixel.R.*
import com.example.pawnspixel.reservations.NewReservations
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BilliardTableFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_billiard_table, container, false)

        val items = arrayOf("Php 89 per person, per hour", "Php 219 3+ person, per hour")

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)
        val adapterItems = ArrayAdapter(requireContext(), layout.list_item, items)
        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                // Get the selected item value
                val item = adapterView.getItemAtPosition(position).toString()
            }

        val add = view?.findViewById<Button>(R.id.reserve_now2)
        add?.setOnClickListener {
            val fragment = NewReservations()
            fragment.show(parentFragmentManager, "Android Center")
            dismiss()
        }

        return view
    }
}