package com.example.pawnspixel.admin.offers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.AdminActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminDeleteOfferPopup : DialogFragment() {

    private lateinit var confirmDeleteButton: Button
    private lateinit var cancelDeleteButton: Button
    private val db = FirebaseFirestore.getInstance()
    private var offerId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_delete_offer_popup, container, false)

        offerId = arguments?.getString("offer_id")

        confirmDeleteButton = view.findViewById(R.id.confirmCancel)
        cancelDeleteButton = view.findViewById(R.id.noConfirm)

        cancelDeleteButton.setOnClickListener { dismiss() }
        confirmDeleteButton.setOnClickListener { deleteOffer() }

        return view
    }

    private fun deleteOffer() {
        if (offerId.isNullOrEmpty()) return

        db.collection("offers").document(offerId!!)
            .delete()
            .addOnSuccessListener {
                context?.let {
                    Toast.makeText(it, "Offer deleted successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                context?.let {
                    Toast.makeText(it, "Error deleting offer: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        if (isAdded) {
            dismiss()
        }

        context?.let {
            val intent = Intent(it, AdminActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            activity?.finish()
        }
    }

}
