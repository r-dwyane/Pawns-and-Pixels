package com.example.pawnspixel.admin.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.offers.AdminDeleteOfferPopup
import com.google.firebase.firestore.FirebaseFirestore

class AdminEditGame : Fragment() {

    private var gameId: String? = null
    private lateinit var gameImage: ImageView
    private lateinit var gameNameEdit: EditText
    private lateinit var gameDescEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var progressContainer: RelativeLayout

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameId = arguments?.getString("gameId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_game, container, false)

        gameImage = view.findViewById(R.id.admin_gameImage)
        gameNameEdit = view.findViewById(R.id.editGameName)
        gameDescEdit = view.findViewById(R.id.editGameDescription)
        saveButton = view.findViewById(R.id.saveGameButton)
        deleteButton = view.findViewById(R.id.deleteGameButton)
        progressContainer = view.findViewById(R.id.admin_progressContainerNew)

        fetchGameDetails()

        saveButton.setOnClickListener {
            updateGameDetails()
        }

        deleteButton.setOnClickListener {
            deleteGame()
        }

        return view
    }

    private fun fetchGameDetails() {
        val gameId = arguments?.getString("gameId")

        if (gameId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Error: Invalid game ID", Toast.LENGTH_SHORT).show()
            return
        }

        showLoader()

        db.collection("games").document(gameId)
            .get()
            .addOnSuccessListener { document ->
                hideLoader()

                if (document.exists()) {
                    val game = document.toObject(AdminGame::class.java)
                    if (game != null) {
                        gameNameEdit.setText(game.gameName)
                        gameDescEdit.setText(game.gameDescription)

                        if (game.imageUrl.isNotEmpty()) {
                            Glide.with(requireContext()).load(game.imageUrl).into(gameImage)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Game not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                hideLoader()
                Toast.makeText(requireContext(), "Failed to fetch game: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateGameDetails() {
        val updatedName = gameNameEdit.text.toString().trim()
        val updatedDesc = gameDescEdit.text.toString().trim()

        if (updatedName.isEmpty() || updatedDesc.isEmpty()) {
            Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        showLoader()

        gameId?.let { id ->
            db.collection("games").document(id)
                .update(mapOf(
                    "gameName" to updatedName,
                    "gameDescription" to updatedDesc
                ))
                .addOnSuccessListener {
                    hideLoader()
                    Toast.makeText(requireContext(), "Game updated successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    hideLoader()
                    Toast.makeText(requireContext(), "Failed to update game", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteGame() {
        gameId?.let { id ->
            val deletePopup = AdminDeleteGamePopup().apply {
                arguments = Bundle().apply { putString("game_id", id) }
            }
            deletePopup.show(parentFragmentManager, "AdminDeleteGamePopup")
        }
    }

    private fun showLoader() {
        progressContainer.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressContainer.visibility = View.GONE
    }
}
