package com.example.pawnspixel.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R

class XboxGames : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_xbox_games, container, false)

        val backButton = view?.findViewById<ImageView>(R.id.backHome3)
        backButton?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

}