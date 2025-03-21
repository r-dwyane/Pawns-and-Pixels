package com.example.pawnspixel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pawnspixel.account.Account
import com.example.pawnspixel.reservations.Reservations

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int):
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Reservations()
            1 -> Home()
            2 -> Account()
            else -> Home()
        }
    }

}