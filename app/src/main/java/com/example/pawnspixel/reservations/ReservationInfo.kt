package com.example.pawnspixel.reservations

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import com.example.pawnspixel.GetDetails
import com.example.pawnspixel.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReservationInfo : Fragment() {
    private lateinit var roomText: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var spinnerStartTime: Spinner
    private lateinit var spinnerEndTime: Spinner
    private lateinit var players: EditText
    private lateinit var selectedDate: String
    private lateinit var num_players: String
    private lateinit var reserveButton: Button
    private lateinit var room: String

    private val availableTimes = listOf(
        "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM",
        "04:00 PM", "05:00 PM", "06:00 PM", "07:00 PM",
        "08:00 PM", "09:00 PM", "10:00 PM", "11:00 PM",
        "12:00 AM"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey") { _, bundle ->
            room = bundle.getString("room").toString()
            val oneString = "Room: $room"
            roomText.text = oneString.trim()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_info, container, false)
        roomText = view?.findViewById(R.id.roomText)!!
        calendarView = view.findViewById(R.id.calendarView)
        spinnerStartTime = view.findViewById(R.id.spinnerStartTime)
        spinnerEndTime = view.findViewById(R.id.spinnerEndTime)
        players = view.findViewById(R.id.num_players)
        reserveButton = view.findViewById(R.id.reserve_button)

        // Get the current date and calculate the next day
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)  // Add one day to the current date

        // Format the next day's date
        val nextDayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        selectedDate = nextDayDate

        // Set the CalendarView's date to the next day
        calendarView.date = calendar.timeInMillis

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selected = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            selectedDate = selected
            updateDefaultEndTime(year, month, dayOfMonth)
        }

        val backButton = view.findViewById<ImageView>(R.id.backHome4)
        backButton?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupStartTimeSpinner()

        reserveButton.setOnClickListener {
            val bundle = Bundle()
            num_players = players.text.toString().trim()
            val selectedStartTime = spinnerStartTime.selectedItem.toString()
            val selectedEndTime = spinnerEndTime.selectedItem.toString()

            if (num_players.isNotEmpty()) {
                if (selectedStartTime.isNotEmpty() && selectedEndTime.isNotEmpty()) {
                    GetDetails.room = room
                    GetDetails.date = selectedDate
                    GetDetails.players = num_players
                    GetDetails.startTime = selectedStartTime
                    GetDetails.endTime = selectedEndTime

                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                        .replace(R.id.nav_host_fragment, ReservationSummary())
                        .addToBackStack(null)
                        .commit()

                } else {
                    Toast.makeText(requireContext(), "Please select start and end time.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultStartTime()
    }

    private fun setDefaultStartTime() {
        val startTimeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, availableTimes)
        spinnerStartTime.adapter = startTimeAdapter
        spinnerStartTime.setSelection(availableTimes.indexOf("12:00 PM"))
    }

    private fun updateDefaultEndTime(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val defaultEndTime = when (dayOfWeek) {
            Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY -> "10:00 PM"
            Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY -> "12:00 AM"
            else -> "10:00 PM"
        }

        setDefaultEndTime(defaultEndTime)
    }

    private fun setDefaultEndTime(defaultEndTime: String) {
        val endTimeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, availableTimes)
        spinnerEndTime.adapter = endTimeAdapter

        val defaultEndTimePosition = availableTimes.indexOf(defaultEndTime)
        if (defaultEndTimePosition != -1) {
            spinnerEndTime.setSelection(defaultEndTimePosition)
        }
    }

    private fun setupStartTimeSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, availableTimes)
        spinnerStartTime.adapter = adapter

        spinnerStartTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTime = availableTimes[position]
                updateEndTimeSpinner(selectedTime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateEndTimeSpinner(startTime: String) {
        var filteredTimes: List<String>

        if (startTime == "12:00 PM") {
            // If start time is 12:00 PM, show all times from 1:00 PM onwards
            filteredTimes = availableTimes.subList(1, availableTimes.size)
        } else {
            // Filter out times that are less than or equal to the selected start time
            filteredTimes = availableTimes.filter { it > startTime }
        }

        // Remove 11:00 PM and 12:00 AM if the selected start time is 10:00 PM
        if (startTime == "10:00 PM") {
            filteredTimes = filteredTimes.filterNot { it == "11:00 PM" || it == "12:00 AM" }
        }

        // Ensure 12:00 PM is never in the filtered list
        val filteredTimesWithout12PM = filteredTimes.filter { it != "12:00 PM" }

        if (filteredTimesWithout12PM.isEmpty()) {
            // Disable the end time spinner if no valid options are available
            spinnerEndTime.isEnabled = false
        } else {
            // Enable the end time spinner and update the adapter
            spinnerEndTime.isEnabled = true
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filteredTimesWithout12PM)
            spinnerEndTime.adapter = adapter
        }
    }

}
