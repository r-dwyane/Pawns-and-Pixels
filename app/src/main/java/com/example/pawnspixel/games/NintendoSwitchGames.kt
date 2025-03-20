package com.example.pawnspixel.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore


class NintendoSwitchGames : Fragment() {

    private lateinit var nintendoGamesRecyclerView: RecyclerView
    private lateinit var boardGamesAdapter: GamesAdapter
    private val gameList = mutableListOf<Game>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nintendo_switch_games, container, false)
        nintendoGamesRecyclerView = view.findViewById(R.id.nintendoGamesRecyclerView)

        val backButton = view?.findViewById<ImageView>(R.id.backHome1)
        backButton?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()
        fetchBoardGames()

        return view
    }

    private fun setupRecyclerView() {
        nintendoGamesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        boardGamesAdapter = GamesAdapter(gameList)
        nintendoGamesRecyclerView.adapter = boardGamesAdapter
    }

    private fun fetchBoardGames() {
        db.collection("games")
            .whereEqualTo("gameType", "Nintendo Switch Games")
            .get()
            .addOnSuccessListener { documents ->
                gameList.clear()
                for (document in documents) {
                    val game = document.toObject(Game::class.java)
                    gameList.add(game)
                }
                boardGamesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch games", Toast.LENGTH_SHORT).show()

            }
    }
}