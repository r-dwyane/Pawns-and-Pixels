package com.example.pawnspixel.admin.offers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.AdminOffer
import com.example.pawnspixel.R

class AdminOffersAdapter(
    private val offersList: List<AdminOffer>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AdminOffersAdapter.OfferViewHolder>() {

    interface OnItemClickListener {
        fun onOfferClick(offerId: String, offerName: String, offerDescription: String)
    }

    inner class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val offerName: TextView = itemView.findViewById(R.id.specialOffersName)
        val offerDescription: TextView = itemView.findViewById(R.id.specialOffersDescription)

        init {
            itemView.setOnClickListener {
                val offer = offersList[adapterPosition]
                listener.onOfferClick(offer.id, offer.name, offer.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offers, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offersList[position]
        holder.offerName.text = offer.name
        holder.offerDescription.text = offer.description
    }

    override fun getItemCount(): Int {
        return offersList.size
    }
}
