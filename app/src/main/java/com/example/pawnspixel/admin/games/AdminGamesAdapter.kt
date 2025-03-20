package com.example.pawnspixel.admin.games

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawnspixel.R

class AdminGamesAdapter(
    private val gamesList: List<AdminGame>,
    private val onEditClick: (AdminGame) -> Unit
) : RecyclerView.Adapter<AdminGamesAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameTitle: TextView = view.findViewById(R.id.admin_gameName)
        val gameDesc: TextView = view.findViewById(R.id.admin_gameDescription)
        val gameImage: ImageView = view.findViewById(R.id.admin_gameImage)
        val editGame: ImageView = view.findViewById(R.id.editGame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_games, parent, false)
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

        holder.editGame.setOnClickListener {
            onEditClick(game)
        }
    }

    override fun getItemCount(): Int = gamesList.size
}
