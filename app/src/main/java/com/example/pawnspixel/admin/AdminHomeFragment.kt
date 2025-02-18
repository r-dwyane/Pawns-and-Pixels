package com.example.pawnspixel.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.pawnspixel.R
import com.example.pawnspixel.StartActivity
import com.example.pawnspixel.games.XboxGames

class AdminHomeFragment : Fragment() {
    private lateinit var stats: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)
        stats = view?.findViewById(R.id.statisticsBtn)!!

        stats.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, 
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, AdminStatisticsFragment())
                .addToBackStack(null)
                .commit()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE
    }
}