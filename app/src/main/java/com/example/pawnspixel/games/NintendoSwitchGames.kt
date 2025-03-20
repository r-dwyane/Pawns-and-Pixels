package com.example.pawnspixel.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NintendoSwitchGames : Fragment() {

    private lateinit var nintendoGamesRecyclerView: RecyclerView
    private lateinit var boardGamesAdapter: GamesAdapter
    private val gameList = mutableListOf<Game>()
    private val db = FirebaseFirestore.getInstance()
    private var gamesListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nintendo_switch_games, container, false)
        nintendoGamesRecyclerView = view.findViewById(R.id.nintendoGamesRecyclerView)

        val backButton = view.findViewById<ImageView>(R.id.backHome1)
        backButton?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()
        listenForNintendoGamesUpdates()

        return view
    }

    private fun setupRecyclerView() {
        nintendoGamesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        boardGamesAdapter = GamesAdapter(gameList)
        nintendoGamesRecyclerView.adapter = boardGamesAdapter
    }

    private fun listenForNintendoGamesUpdates() {
        gamesListener = db.collection("games")
            .whereEqualTo("gameType", "Nintendo Switch Games")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error fetching games: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    gameList.clear()
                    for (document in snapshots) {
                        val game = document.toObject(Game::class.java)
                        gameList.add(game)
                    }
                    boardGamesAdapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gamesListener?.remove()
    }
}
