package com.example.pawnspixel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawnspixel.authentication.CustomerSignInFragment
import com.example.pawnspixel.games.BoardGames
import com.example.pawnspixel.games.NintendoSwitchGames
import com.example.pawnspixel.games.XboxGames
import com.example.pawnspixel.rooms.BilliardTableFragment
import com.example.pawnspixel.rooms.NintendoRoomFragment
import com.example.pawnspixel.rooms.PixelsGamingFragment
import com.example.pawnspixel.rooms.PrivateRoomFragment
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment() {

    private lateinit var offersRecyclerView: RecyclerView
    private lateinit var offersAdapter: OffersAdapter
    private lateinit var noOffersTextView: TextView
    private val offersList = mutableListOf<Offer>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val nintendo_rooms = view?.findViewById<FrameLayout>(R.id.nintendo_rooms)
        val private_rooms = view?.findViewById<FrameLayout>(R.id.private_room)
        val pixel_gamings = view?.findViewById<FrameLayout>(R.id.pixels_gaming)
        val billiard_table = view?.findViewById<FrameLayout>(R.id.billiard_table)

        val boardGames = view?.findViewById<ImageButton>(R.id.boardGamesButton)
        val nintendoSwitchGames = view?.findViewById<ImageButton>(R.id.nintendoButton)
        val xboxGames = view?.findViewById<ImageButton>(R.id.xboxButton)

        offersRecyclerView = view.findViewById(R.id.offersRecyclerView)
        noOffersTextView = view.findViewById(R.id.noOffersTextView)

        offersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        offersAdapter = OffersAdapter(offersList)
        offersRecyclerView.adapter = offersAdapter

        fetchOffersFromFirestore()

        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE

        boardGames?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, BoardGames())
                .addToBackStack(null)
                .commit()
        }

        xboxGames?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, XboxGames())
                .addToBackStack(null)
                .commit()
        }

        nintendoSwitchGames?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, NintendoSwitchGames())
                .addToBackStack(null)
                .commit()
        }

        nintendo_rooms?.setOnClickListener{
            val fragment = NintendoRoomFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }
        private_rooms?.setOnClickListener{
            val fragment = PrivateRoomFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }
        pixel_gamings?.setOnClickListener{
            val fragment = PixelsGamingFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }
        billiard_table?.setOnClickListener{
            val fragment = BilliardTableFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }
        val sharedPrefManager = SharedPrefManager(requireContext())
        SessionManager.contactNumber = sharedPrefManager.getUserNumber()
        return view
    }

    private fun fetchOffersFromFirestore() {
        db.collection("offers").addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.e("Firestore", "Error fetching offers", error)
                return@addSnapshotListener
            }

            offersList.clear()
            if (snapshots != null) {
                for (document in snapshots.documents) {
                    val name = document.getString("name") ?: "No Name"
                    val description = document.getString("description") ?: "No Description"
                    offersList.add(Offer(name, description))
                }
            }

            if (offersList.isEmpty()) {
                noOffersTextView.visibility = View.VISIBLE
                offersRecyclerView.visibility = View.GONE
            } else {
                noOffersTextView.visibility = View.GONE
                offersRecyclerView.visibility = View.VISIBLE
            }

            offersAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE
    }
}
