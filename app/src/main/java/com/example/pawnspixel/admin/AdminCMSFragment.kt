package com.example.pawnspixel.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.pawnspixel.R
import com.example.pawnspixel.games.NintendoSwitchGames

class AdminCMSFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_c_m_s, container, false)
        val boardGames: ImageButton = view?.findViewById(R.id.boardGamesButton)!!
        val xboxButton: ImageButton = view.findViewById(R.id.xboxButton)!!
        val nintendoButton: ImageButton = view.findViewById(R.id.nintendoButton)!!

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
        return view;
    }
}