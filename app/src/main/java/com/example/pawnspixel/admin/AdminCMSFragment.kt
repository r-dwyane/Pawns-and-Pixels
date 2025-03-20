package com.example.pawnspixel.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.pawnspixel.R
import com.example.pawnspixel.admin.games.AdminBoardGamesFragment
import com.example.pawnspixel.admin.offers.AdminNewOffers
import com.example.pawnspixel.admin.rooms.AdminBilliardTable
import com.example.pawnspixel.admin.rooms.AdminNintendoSwitchGameRoomFragment
import com.example.pawnspixel.admin.rooms.AdminPixelsGaming
import com.example.pawnspixel.admin.rooms.AdminPrivateRoom

class AdminCMSFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_c_m_s, container, false)
        val boardGames: ImageButton = view?.findViewById(R.id.boardGamesButton)!!
        val xboxButton: ImageButton = view.findViewById(R.id.xboxButton)!!
        val pixelsButton: FrameLayout = view.findViewById(R.id.admin_pixels_gaming)!!
        val billiardButton: FrameLayout = view.findViewById(R.id.admin_billiard_table)!!
        val privateButton: FrameLayout = view.findViewById(R.id.admin_private_room)!!
        val nintendoButton: FrameLayout = view.findViewById(R.id.admin_nintendo_rooms)!!
        val addOffer: ImageButton = view.findViewById(R.id.new_special_offers_cms)!!


        addOffer.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminNewOffers())
                .addToBackStack(null)
                .commit()
        }

        //GAMES
        boardGames.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminBoardGamesFragment())
                .addToBackStack(null)
                .commit()
        }

        //ROOMS
        nintendoButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminNintendoSwitchGameRoomFragment())
                .addToBackStack(null)
                .commit()
        }

        privateButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminPrivateRoom())
                .addToBackStack(null)
                .commit()
        }

        pixelsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminPixelsGaming())
                .addToBackStack(null)
                .commit()
        }

        billiardButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminBilliardTable())
                .addToBackStack(null)
                .commit()
        }
        return view;
    }
}