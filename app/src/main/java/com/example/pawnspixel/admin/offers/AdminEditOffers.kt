package com.example.pawnspixel.admin.offers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminEditOffers : Fragment() {

    private lateinit var offerNameEditText: EditText
    private lateinit var offerDescriptionEditText: EditText
    private lateinit var saveOfferButton: Button
    private lateinit var deleteOfferButton: Button
    private lateinit var backBtn: ImageButton
    private lateinit var progressContainer: RelativeLayout

    private val db = FirebaseFirestore.getInstance()
    private var offerId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_edit_offers, container, false)

        backBtn = view.findViewById(R.id.backBtnAdminBoardGames)
        offerNameEditText = view.findViewById(R.id.editOfferName)
        offerDescriptionEditText = view.findViewById(R.id.editOfferDescription)
        saveOfferButton = view.findViewById(R.id.saveOfferButton)
        deleteOfferButton = view.findViewById(R.id.deleteOfferButton)
        progressContainer = view.findViewById(R.id.admin_progressContainerNew) // Loader

        loadOfferData()

        backBtn.setOnClickListener { parentFragmentManager.popBackStack() }
        saveOfferButton.setOnClickListener { updateOffer() }
        deleteOfferButton.setOnClickListener { showDeletePopup() }

        return view
    }

    private fun loadOfferData() {
        arguments?.let {
            offerId = it.getString("offer_id")
            offerNameEditText.setText(it.getString("offer_name", ""))
            offerDescriptionEditText.setText(it.getString("offer_description", ""))
        }
    }

    private fun updateOffer() {
        val updatedName = offerNameEditText.text.toString().trim()
        val updatedDescription = offerDescriptionEditText.text.toString().trim()

        if (offerId.isNullOrEmpty()) return

        showLoader(true) // Show loading

        db.collection("offers").document(offerId!!)
            .update(mapOf("name" to updatedName, "description" to updatedDescription))
            .addOnSuccessListener {
                showLoader(false) // Hide loading
                Toast.makeText(requireContext(), "Offer updated successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                showLoader(false) // Hide loading
                Toast.makeText(requireContext(), "Error updating offer: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDeletePopup() {
        val deletePopup = AdminDeleteOfferPopup().apply {
            arguments = Bundle().apply { putString("offer_id", offerId) }
        }
        deletePopup.show(parentFragmentManager, "AdminDeleteOfferPopup")
    }

    private fun showLoader(isLoading: Boolean) {
        progressContainer.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
