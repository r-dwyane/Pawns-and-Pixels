package com.example.pawnspixel.admin.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.example.pawnspixel.R

class AdminNintendoGames : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_nintendo_games, container, false)
        val backBtnAdminBoardGames: ImageButton = view.findViewById(R.id.backBtnAdminBoardGames)!!
        val addGame: ImageView = view.findViewById(R.id.addNintendoGame)

        backBtnAdminBoardGames.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        addGame.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.admin_nav_host_fragment, AdminAddGame())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

}