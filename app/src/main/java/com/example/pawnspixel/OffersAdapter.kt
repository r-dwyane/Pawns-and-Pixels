package com.example.pawnspixel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OffersAdapter(private val offersList: List<Offer>) :
    RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val offerName: TextView = itemView.findViewById(R.id.specialOffersName)
        val offerDescription: TextView = itemView.findViewById(R.id.specialOffersDescription)
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
