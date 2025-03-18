package com.example.pawnspixel.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pawnspixel.admin.reservations.AdminReservationsFragment

class AdminTabPageAdapter(activity: FragmentActivity, private val tabCount: Int):
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AdminHomeFragment()
            1 -> AdminReservationsFragment()
            2 -> AdminCMSFragment()
            else -> AdminHomeFragment()
        }
    }

}