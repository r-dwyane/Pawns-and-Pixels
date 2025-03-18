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

class AdminPixelsGaming : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var descriptionEditText: EditText
    private lateinit var statusSpinner1: Spinner
    private lateinit var statusSpinner2: Spinner
    private lateinit var updateButton: Button
    private lateinit var progressContainer: RelativeLayout

    private var currentDescription: String = ""
    private var currentStatus1: String = ""
    private var currentStatus2: String = ""

    private var documentId1: String? = null
    private var documentId2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_pixels_gaming, container, false)

        val backBtn: ImageButton = view.findViewById(R.id.backBtnAdminRoomsEquipments)
        descriptionEditText = view.findViewById(R.id.adminPixelsGamingRoomsEquipmentsDesc)
        statusSpinner1 = view.findViewById(R.id.adminPixelsGaming1RoomsEquipmentsStatus)
        statusSpinner2 = view.findViewById(R.id.adminPixelsGaming2RoomsEquipmentsStatus)
        updateButton = view.findViewById(R.id.admin_pixelsUpdate)
        progressContainer = view.findViewById(R.id.admin_progressContainerNew)

        backBtn.setOnClickListener { parentFragmentManager.popBackStack() }

        db = FirebaseFirestore.getInstance()

        val statusOptions = arrayOf("Available", "Maintenance")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        statusSpinner1.adapter = adapter
        statusSpinner2.adapter = adapter

        loadRoomDetails("pixels_gaming1") { id, desc, status ->
            documentId1 = id
            currentDescription = desc
            currentStatus1 = status
            statusSpinner1.setSelection(if (status == "Maintenance") 1 else 0)
        }

        loadRoomDetails("pixels_gaming2") { id, _, status ->
            documentId2 = id
            currentStatus2 = status
            statusSpinner2.setSelection(if (status == "Maintenance") 1 else 0)
        }

        updateButton.setOnClickListener { validateAndUpdateRoomStatus() }

        return view
    }

    private fun loadRoomDetails(roomName: String, callback: (String?, String, String) -> Unit) {
        db.collection("rooms")
            .whereEqualTo("room_name", roomName)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val docId = document.id
                    val desc = document.getString("description") ?: "No description available"
                    val status = document.getString("status") ?: "Available"

                    callback(docId, desc, status)
                    if (roomName == "pixels_gaming1") descriptionEditText.setText(desc)
                } else {
                    showToast("Room '$roomName' not found")
                }
            }
            .addOnFailureListener { e -> showToast("Failed to load '$roomName': ${e.message}") }
    }

    private fun validateAndUpdateRoomStatus() {
        val newDescription = descriptionEditText.text.toString().trim()
        val newStatus1 = statusSpinner1.selectedItem.toString()
        val newStatus2 = statusSpinner2.selectedItem.toString()

        if (newDescription.isEmpty()) {
            descriptionEditText.error = "Description cannot be empty"
            descriptionEditText.requestFocus()
            return
        }

        if (newDescription == currentDescription && newStatus1 == currentStatus1 && newStatus2 == currentStatus2) {
            showToast("No changes detected")
            return
        }

        progressContainer.visibility = View.VISIBLE
        updateButton.isEnabled = false

        val updates = mutableListOf<Pair<String?, HashMap<String, Any>>>()

        if (newDescription != currentDescription) {
            updates.add(documentId1 to hashMapOf("description" to newDescription))
        }
        if (newStatus1 != currentStatus1) {
            updates.add(documentId1 to hashMapOf("status" to newStatus1))
        }
        if (newStatus2 != currentStatus2) {
            updates.add(documentId2 to hashMapOf("status" to newStatus2))
        }

        updateRooms(updates)
    }

    private fun updateRooms(updates: List<Pair<String?, HashMap<String, Any>>>) {
        var updatesCompleted = 0

        updates.forEach { (docId, data) ->
            if (docId != null) {
                db.collection("rooms").document(docId)
                    .update(data)
                    .addOnSuccessListener {
                        updatesCompleted++
                        if (updatesCompleted == updates.size) onAllUpdatesComplete()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Update failed: ${e.message}")
                        showToast("Update failed: ${e.message}")
                        progressContainer.visibility = View.GONE
                        updateButton.isEnabled = true
                    }
            }
        }
    }

    private fun onAllUpdatesComplete() {
        showToast("Updated successfully!")
        progressContainer.visibility = View.GONE
        updateButton.isEnabled = true
        parentFragmentManager.popBackStack()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
