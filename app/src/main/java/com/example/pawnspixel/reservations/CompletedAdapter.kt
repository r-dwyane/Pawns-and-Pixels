package com.example.pawnspixel.reservations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R
import java.text.SimpleDateFormat
import java.util.Locale

class CompletedAdapter(
    private val reservationList: List<ReservationsDataClass>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CompletedAdapter.ReservationViewHolder>() { // Fixed Adapter Name

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationStatus: TextView = itemView.findViewById(R.id.completed_status)
        val reservationRoom: TextView = itemView.findViewById(R.id.completed_room)
        val reservationDate: TextView = itemView.findViewById(R.id.completed_date)
        val cardView: FrameLayout = itemView.findViewById(R.id.frameLayoutComplete)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_completed, parent, false) // Ensure this is correct
        return ReservationViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.reservationStatus.text = "Status: ${reservation.status}"
        holder.reservationRoom.text = reservation.room
        holder.reservationDate.text = "${formatDate(reservation.date)} | ${reservation.startTime} - ${reservation.endTime}"

        holder.cardView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = reservationList.size

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Invalid Date"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return dateString)
        } catch (e: Exception) {
            dateString
        }
    }
}
