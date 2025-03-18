package com.example.pawnspixel.admin.reservations

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

class AdminReservationAdapter(
    private val reservationList: List<AdminReservationsDataClass>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AdminReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationStatus: TextView = itemView.findViewById(R.id.admin_customer_name)
        val reservationRoom: TextView = itemView.findViewById(R.id.admin_reservation_room)
        val reservationDate: TextView = itemView.findViewById(R.id.admin_reservation_date)
        val cardView: FrameLayout = itemView.findViewById(R.id.frameLayoutItems)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_item_pending, parent, false)
        return ReservationViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.reservationStatus.text ="Name: " + reservation.customerName
        holder.reservationRoom.text = reservation.room
        holder.reservationDate.text = formatDate(reservation.date) + " | " + reservation.startTime + " - " + reservation.endTime

        holder.cardView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = reservationList.size

    private fun formatDate(dateString: String): String {
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
