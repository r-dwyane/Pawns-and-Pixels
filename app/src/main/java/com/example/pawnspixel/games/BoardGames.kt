package com.example.pawnspixel.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pawnspixel.R

class BoardGames : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val backButton = view?.findViewById<TextView>(R.id.back)
        backButton?.setOnClickListener {
            activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.GONE

            parentFragmentManager.popBackStack()
        }

        return inflater.inflate(R.layout.fragment_boardgames, container, false)
    }

}