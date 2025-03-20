package com.example.pawnspixel.games

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawnspixel.R

class GamesAdapter(private val gamesList: List<Game>) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameTitle: TextView = view.findViewById(R.id.boardGameTitle)
        val gameDesc: TextView = view.findViewById(R.id.boardGameDesc)
        val gameImage: ImageView = view.findViewById(R.id.boardGameImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_games, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]
        holder.gameTitle.text = game.gameName
        holder.gameDesc.text = game.gameDescription

        Glide.with(holder.itemView.context)
            .load(game.imageUrl)
            .placeholder(R.drawable.logo_notification)
            .into(holder.gameImage)
    }

    override fun getItemCount(): Int = gamesList.size
}
