package com.example.pawnspixel.admin.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminNewOffers : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var offerNameEditText: EditText
    private lateinit var offerDescriptionEditText: EditText
    private lateinit var addOfferButton: Button
    private lateinit var progressContainer: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_new_offers, container, false)

        val backBtn: ImageButton = view.findViewById(R.id.backBtnAdminBoardGames)
        offerNameEditText = view.findViewById(R.id.newOfferName)
        offerDescriptionEditText = view.findViewById(R.id.adminBilliardRoomsEquipmentsDesc)
        addOfferButton = view.findViewById(R.id.addOfferButton)
        progressContainer = view.findViewById(R.id.admin_progressContainerNew)

        db = FirebaseFirestore.getInstance()

        backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        addOfferButton.setOnClickListener {
            saveOffer()
        }

        return view
    }

    private fun saveOffer() {
        val offerName = offerNameEditText.text.toString().trim()
        val offerDescription = offerDescriptionEditText.text.toString().trim()

        if (offerName.isEmpty()) {
            offerNameEditText.error = "Offer name is required"
            offerNameEditText.requestFocus()
            return
        }

        if (offerDescription.isEmpty()) {
            offerDescriptionEditText.error = "Description is required"
            offerDescriptionEditText.requestFocus()
            return
        }

        if (offerDescription.length > 50) {
            offerDescriptionEditText.error = "Description cannot exceed 50 characters"
            offerDescriptionEditText.requestFocus()
            return
        }

        val offer = hashMapOf(
            "name" to offerName,
            "description" to offerDescription,
            "timestamp" to System.currentTimeMillis()
        )

        progressContainer.visibility = View.VISIBLE
        addOfferButton.isEnabled = false

        db.collection("offers")
            .add(offer)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Offer added successfully!", Toast.LENGTH_SHORT).show()
                offerNameEditText.text.clear()
                offerDescriptionEditText.text.clear()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to add offer: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressContainer.visibility = View.GONE
                addOfferButton.isEnabled = true
            }
    }
}
