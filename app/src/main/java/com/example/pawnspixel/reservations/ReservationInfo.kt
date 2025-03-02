package com.example.pawnspixel.reservations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.setFragmentResultListener
import com.example.pawnspixel.GetDetails
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ReservationInfo : Fragment() {
    private lateinit var roomText: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var spinnerStartTime: Spinner
    private lateinit var spinnerEndTime: Spinner
    private lateinit var players: EditText
    private lateinit var reserveButton: Button
    private var room: String = ""
    private val availableTimes = listOf(
        "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM",
        "04:00 PM", "05:00 PM", "06:00 PM", "07:00 PM",
        "08:00 PM", "09:00 PM", "10:00 PM", "11:00 PM",
        "12:00 AM"
    )
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey") { _, bundle ->
            val receivedRoom = bundle.getString("room")
            if (!receivedRoom.isNullOrEmpty()) {
                room = receivedRoom
                if (::roomText.isInitialized) {
                    roomText.text = "Room: $room".trim()
                }
                fetchUnavailableTimes()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_reservation_info, container, false)
        roomText = view.findViewById(R.id.roomText)
        calendarView = view.findViewById(R.id.calendarView)
        spinnerStartTime = view.findViewById(R.id.spinnerStartTime)
        spinnerEndTime = view.findViewById(R.id.spinnerEndTime)
        players = view.findViewById(R.id.num_players)
        reserveButton = view.findViewById(R.id.reserve_button)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        updateSelectedDate(calendar)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(year, month, dayOfMonth)
            if (tempCalendar.before(Calendar.getInstance())) {
                moveToNextAvailableDate()
            } else {
                updateSelectedDate(tempCalendar)
            }
        }

        view.findViewById<ImageView>(R.id.backHome4)?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupStartTimeSpinner()

        reserveButton.setOnClickListener {
            val numPlayers = players.text.toString().trim()
            val selectedStartTime = spinnerStartTime.selectedItem?.toString() ?: ""
            val selectedEndTime = spinnerEndTime.selectedItem?.toString() ?: ""

            if (numPlayers.isNotEmpty() && selectedStartTime.isNotEmpty() && selectedEndTime.isNotEmpty()) {
                GetDetails.room = room
                GetDetails.date = selectedDate
                GetDetails.players = numPlayers
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
                Toast.makeText(requireContext(), "Please enter all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun fetchUnavailableTimes() {
        if (room.isEmpty() || selectedDate.isEmpty()) return

        val db = FirebaseFirestore.getInstance()
        db.collection("bookings")
            .whereEqualTo("date", selectedDate)
            .whereEqualTo("room", room)
            .whereIn("status", listOf("Ongoing", "Accepted"))
            .get()
            .addOnSuccessListener { documents ->
                val unavailableTimes = mutableSetOf<String>()

                // Identify booked time slots
                documents.forEach { document ->
                    val startTime = document.getString("startTime")
                    val endTime = document.getString("endTime")

                    if (startTime != null && endTime != null) {
                        val startIndex = availableTimes.indexOf(startTime)
                        val endIndex = availableTimes.indexOf(endTime)

                        if (startIndex != -1 && endIndex != -1) {
                            for (i in startIndex..endIndex) {
                                unavailableTimes.add(availableTimes[i])
                            }
                        }
                    }
                }

                // Get max allowed end time based on the day
                val maxEndTime = getMaxEndTime(selectedDate)
                val maxEndIndex = availableTimes.indexOf(maxEndTime)

                // Filter available time slots based on bookings and max end time
                val filteredTimes = availableTimes
                    .filterIndexed { index, time ->
                        time !in unavailableTimes && index <= maxEndIndex
                    }

                if (filteredTimes.isEmpty()) {
                    moveToNextAvailableDate()
                } else {
                    updateStartTimeSpinner(filteredTimes)
                }
            }
    }

    private fun getMaxEndTime(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        return try {
            calendar.time = dateFormat.parse(date)!!
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek in Calendar.FRIDAY..Calendar.SUNDAY) "12:00 AM" else "10:00 PM"
        } catch (e: Exception) {
            "10:00 PM" // Default fallback
        }
    }


    private fun updateStartTimeSpinner(filteredTimes: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filteredTimes)
        spinnerStartTime.adapter = adapter
        spinnerStartTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateEndTimeSpinner(filteredTimes[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateEndTimeSpinner(startTime: String) {
        val startIndex = availableTimes.indexOf(startTime)
        val maxEndTime = getMaxEndTime(selectedDate)
        val maxEndIndex = availableTimes.indexOf(maxEndTime)

        if (startIndex != -1) {
            val filteredTimes = availableTimes
                .subList(startIndex + 1, maxEndIndex + 1)

            if (filteredTimes.isNotEmpty()) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filteredTimes)
                spinnerEndTime.adapter = adapter
                spinnerEndTime.isEnabled = true
            } else {
                spinnerEndTime.adapter = null
                spinnerEndTime.isEnabled = false
                Toast.makeText(requireContext(), "No valid end times available.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDefaultEndTime(calendar: Calendar) {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val defaultEndTime = if (dayOfWeek in Calendar.FRIDAY..Calendar.SUNDAY) "12:00 AM" else "10:00 PM"

        val endTimeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf(defaultEndTime))
        spinnerEndTime.adapter = endTimeAdapter
        spinnerEndTime.isEnabled = true
    }

    private fun setupStartTimeSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, availableTimes)
        spinnerStartTime.adapter = adapter
        spinnerStartTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateEndTimeSpinner(availableTimes[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateSelectedDate(calendar: Calendar) {
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        calendarView.date = calendar.timeInMillis
        updateDefaultEndTime(calendar)
        fetchUnavailableTimes()
    }

    private fun moveToNextAvailableDate() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarView.date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        updateSelectedDate(calendar)
        Toast.makeText(requireContext(), "No available slots. Moving to next available date.", Toast.LENGTH_SHORT).show()
    }
}
