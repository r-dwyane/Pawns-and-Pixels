package com.example.pawnspixel.admin.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminBoardGamesFragment : Fragment() {

    private lateinit var boardGamesRecyclerView: RecyclerView
    private lateinit var boardGamesAdapter: AdminGamesAdapter
    private val gameList = mutableListOf<AdminGame>()
    private val db = FirebaseFirestore.getInstance()
    private var gamesListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_board_games, container, false)
        val backBtnAdminBoardGames: ImageButton = view.findViewById(R.id.backBtnAdminBoardGames)
        val addGame: ImageView = view.findViewById(R.id.addBoardGame)
        boardGamesRecyclerView = view.findViewById(R.id.admin_boardGamesRecyclerView)

        backBtnAdminBoardGames.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        addGame.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminAddGame())
                .addToBackStack(null)
                .commit()
        }

        setupRecyclerView()
        listenForBoardGamesUpdates()

        return view
    }

    private fun setupRecyclerView() {
        boardGamesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        boardGamesAdapter = AdminGamesAdapter(gameList) { game ->
            val gameId = game.gameId

            val editFragment = AdminEditGame().apply {
                arguments = Bundle().apply {
                    putString("gameId", gameId)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.admin_nav_host_fragment, editFragment)
                .addToBackStack(null)
                .commit()
        }

        boardGamesRecyclerView.adapter = boardGamesAdapter
    }

    private fun listenForBoardGamesUpdates() {
        gamesListener = db.collection("games")
            .whereEqualTo("gameType", "Board Games")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error fetching games: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    gameList.clear()
                    for (document in snapshots) {
                        val game = document.toObject(AdminGame::class.java).apply {
                            gameId = document.id
                        }
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
