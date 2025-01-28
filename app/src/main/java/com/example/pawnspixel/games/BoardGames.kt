package com.example.pawnspixel.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.pawnspixel.R

class BoardGames : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_boardgames, container, false)
        val backButton = view?.findViewById<ImageView>(R.id.backHome2)
        backButton?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

}