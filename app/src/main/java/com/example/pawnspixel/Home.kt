package com.example.pawnspixel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.pawnspixel.authentication.CustomerSignInFragment
import com.example.pawnspixel.games.BoardGames
import com.example.pawnspixel.games.NintendoSwitchGames
import com.example.pawnspixel.games.XboxGames
import com.example.pawnspixel.rooms.BilliardTableFragment
import com.example.pawnspixel.rooms.NintendoRoomFragment
import com.example.pawnspixel.rooms.PixelsGamingFragment
import com.example.pawnspixel.rooms.PrivateRoomFragment

class Home : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        return view
    }
}
