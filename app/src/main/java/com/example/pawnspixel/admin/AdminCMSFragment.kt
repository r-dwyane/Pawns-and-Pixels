package com.example.pawnspixel.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.AdminOffer
import com.example.pawnspixel.Offer
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.games.AdminBoardGamesFragment
import com.example.pawnspixel.admin.games.AdminNintendoGames
import com.example.pawnspixel.admin.games.AdminXboxGames
import com.example.pawnspixel.admin.offers.AdminEditOffers
import com.example.pawnspixel.admin.offers.AdminNewOffers
import com.example.pawnspixel.admin.offers.AdminOffersAdapter
import com.example.pawnspixel.admin.rooms.AdminBilliardTable
import com.example.pawnspixel.admin.rooms.AdminNintendoSwitchGameRoomFragment
import com.example.pawnspixel.admin.rooms.AdminPixelsGaming
import com.example.pawnspixel.admin.rooms.AdminPrivateRoom
import com.google.firebase.firestore.FirebaseFirestore

class AdminCMSFragment : Fragment() {

    private lateinit var offersRecyclerView: RecyclerView
    private lateinit var offersAdapter: AdminOffersAdapter
    private lateinit var noOffersTextView: TextView
    private val offersList = mutableListOf<AdminOffer>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_c_m_s, container, false)
        val boardGames: ImageButton = view?.findViewById(R.id.boardGamesButton)!!
        val xboxButton: ImageButton = view.findViewById(R.id.admin_xboxButton)!!
        val nintendoGames: ImageButton = view.findViewById(R.id.admin_nintendoButton)!!
        val pixelsButton: FrameLayout = view.findViewById(R.id.admin_pixels_gaming)!!
        val billiardButton: FrameLayout = view.findViewById(R.id.admin_billiard_table)!!
        val privateButton: FrameLayout = view.findViewById(R.id.admin_private_room)!!
        val nintendoButton: FrameLayout = view.findViewById(R.id.admin_nintendo_rooms)!!
        val addOffer: ImageButton = view.findViewById(R.id.new_special_offers_cms)!!

        offersRecyclerView = view.findViewById(R.id.admin_offersRecyclerView)
        noOffersTextView = view.findViewById(R.id.admin_noOffersTextView)

        offersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        offersAdapter = AdminOffersAdapter(offersList, object : AdminOffersAdapter.OnItemClickListener {
            override fun onOfferClick(offerId: String, offerName: String, offerDescription: String) {
                val bundle = Bundle().apply {
                    putString("offer_id", offerId)
                    putString("offer_name", offerName)
                    putString("offer_description", offerDescription)
                }

                val fragment = AdminEditOffers()
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.admin_nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        offersRecyclerView.adapter = offersAdapter

        fetchOffersFromFirestore()

        //GAMES
        boardGames.setOnClickListener { navigateToFragment(AdminBoardGamesFragment()) }
        xboxButton.setOnClickListener { navigateToFragment(AdminXboxGames()) }
        nintendoGames.setOnClickListener { navigateToFragment(AdminNintendoGames()) }

        //ROOMS
        nintendoButton.setOnClickListener { navigateToFragment(AdminNintendoSwitchGameRoomFragment()) }
        privateButton.setOnClickListener { navigateToFragment(AdminPrivateRoom()) }
        pixelsButton.setOnClickListener { navigateToFragment(AdminPixelsGaming()) }
        billiardButton.setOnClickListener { navigateToFragment(AdminBilliardTable()) }

        addOffer.setOnClickListener { navigateToFragment(AdminNewOffers()) }

        return view;
    }

    private fun fetchOffersFromFirestore() {

        db.collection("offers").addSnapshotListener { snapshots, error ->

            if (error != null) {
                Log.e("Firestore", "Error fetching offers", error)
                return@addSnapshotListener
            }

            if (snapshots == null || snapshots.isEmpty) {
                offersList.clear()
                noOffersTextView.visibility = View.VISIBLE
                offersRecyclerView.visibility = View.GONE
            } else {
                offersList.clear()
                for (document in snapshots.documents) {
                    val id = document.id
                    val name = document.getString("name") ?: "No Name"
                    val description = document.getString("description") ?: "No Description"
                    offersList.add(AdminOffer(id, name, description))
                }
                noOffersTextView.visibility = View.GONE
                offersRecyclerView.visibility = View.VISIBLE
            }

            offersAdapter.notifyDataSetChanged()
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.admin_nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

}