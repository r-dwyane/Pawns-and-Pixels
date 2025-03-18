package com.example.pawnspixel.admin.rooms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminNintendoSwitchGameRoomFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var descriptionEditText: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var updateButton: Button

    private val roomName = "nintendo_room"
    private var documentId: String? = null
    private var currentDescription: String = ""
    private var currentStatus: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_nintendo_switch_game_room, container, false)
        val backBtnAdminRoomsEquipments: ImageButton = view.findViewById(R.id.backBtnAdminRoomsEquipments)

        backBtnAdminRoomsEquipments.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        db = FirebaseFirestore.getInstance()

        descriptionEditText = view.findViewById(R.id.adminNintendoRoomsEquipmentsDesc)
        statusSpinner = view.findViewById(R.id.adminRoomsEquipmentsStatus)
        updateButton = view.findViewById(R.id.admin_nintendoUpdate)

        val statusOptions = arrayOf("Available", "Maintenance")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        statusSpinner.adapter = adapter

        loadRoomDetails()

        updateButton.setOnClickListener {
            validateAndUpdateRoomStatus()
        }

        return view
    }

    private fun loadRoomDetails() {
        db.collection("rooms")
            .whereEqualTo("room_name", roomName)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    documentId = document.id

                    currentDescription = document.getString("description") ?: "No description available"
                    currentStatus = document.getString("status") ?: "Available"

                    descriptionEditText.setText(currentDescription)
                    val statusIndex = if (currentStatus == "Maintenance") 1 else 0
                    statusSpinner.setSelection(statusIndex)
                } else {
                    Toast.makeText(requireContext(), "Room not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to load room details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateAndUpdateRoomStatus() {
        val newDescription = descriptionEditText.text.toString().trim()
        val newStatus = statusSpinner.selectedItem.toString()

        if (newDescription.isEmpty()) {
            descriptionEditText.error = "Description cannot be empty"
            descriptionEditText.requestFocus()
            return
        }

        if (newDescription == currentDescription && newStatus == currentStatus) {
            Toast.makeText(requireContext(), "No changes detected", Toast.LENGTH_SHORT).show()
            return
        }

        updateRoomStatus(newDescription, newStatus)
    }

    private fun updateRoomStatus(newDescription: String, newStatus: String) {
        val updates = hashMapOf<String, Any>(
            "description" to newDescription,
            "status" to newStatus
        )
        val progressContainer: RelativeLayout = requireView().findViewById(R.id.admin_progressContainerNew)

        progressContainer.visibility = View.VISIBLE
        updateButton.isEnabled = false

        db.collection("rooms")
            .whereEqualTo("room_name", "nintendo_room")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val documentId = document.id

                        db.collection("rooms").document(documentId)
                            .update(updates)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Update successful!")
                                Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT).show()
                                currentDescription = newDescription
                                currentStatus = newStatus
                                parentFragmentManager.popBackStack()
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Update failed: ${e.message}")
                                Toast.makeText(requireContext(), "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                            .addOnCompleteListener {
                                progressContainer.visibility = View.GONE
                                updateButton.isEnabled = true
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Room not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching room: ${e.message}")
                Toast.makeText(requireContext(), "Error fetching room: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
