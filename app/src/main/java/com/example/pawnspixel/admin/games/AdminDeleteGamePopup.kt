package com.example.pawnspixel.admin.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminDeleteGamePopup : DialogFragment() {

    private lateinit var confirmDeleteButton: Button
    private lateinit var cancelDeleteButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_admin_delete_game_popup, container, false)

        val gameId = arguments?.getString("game_id")

        confirmDeleteButton = view.findViewById(R.id.confirmDelete)
        cancelDeleteButton = view.findViewById(R.id.noConfirm)

        confirmDeleteButton.setOnClickListener {
            gameId?.let { id ->
                deleteGame(id)
                Toast.makeText(requireContext(), "Game deleted successfully.", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(requireContext(), "Error: Game ID is missing", Toast.LENGTH_SHORT).show()
        }

        cancelDeleteButton.setOnClickListener { dismiss() }

        return view
    }

    private fun deleteGame(gameId: String) {
        db.collection("games").document(gameId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Game deleted successfully!", Toast.LENGTH_SHORT).show()
                dismiss()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to delete game", Toast.LENGTH_SHORT).show()
            }
    }
}