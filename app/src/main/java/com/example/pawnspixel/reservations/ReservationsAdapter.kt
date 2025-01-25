package com.example.pawnspixel.reservations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R

class ReservationsAdapter(
    private val reservationList: List<ReservationsDataClass>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationStatus: TextView = itemView.findViewById(R.id.reservation_status)
        val reservationRoom: TextView = itemView.findViewById(R.id.reservation_room)
        val reservationDate: TextView = itemView.findViewById(R.id.reservation_date)
        val cardView: FrameLayout = itemView.findViewById(R.id.frameLayoutItems)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pending, parent, false)
        return ReservationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.reservationStatus.text = reservation.status
        holder.reservationRoom.text = reservation.room
        holder.reservationDate.text = reservation.date

        holder.cardView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = reservationList.size
}
