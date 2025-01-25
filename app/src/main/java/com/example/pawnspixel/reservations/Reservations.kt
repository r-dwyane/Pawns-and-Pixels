package com.example.pawnspixel.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pawnspixel.R
import com.google.android.material.tabs.TabLayout

class Reservations : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservations, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.tablayout2)

        tabLayout?.apply {
            addTab(newTab().setText("Pending"))
            addTab(newTab().setText("Accepted"))
            addTab(newTab().setText("Ongoing"))
            addTab(newTab().setText("Completed"))
            addTab(newTab().setText("Declined"))
            addTab(newTab().setText("Cancelled"))
        }

        showFragment(PendingFragment())

        tabLayout?.getTabAt(0)?.select()

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showFragment(PendingFragment())
                    1 -> showFragment(AcceptedFragment())
                    2 -> showFragment(OngoingFragment())
                    3 -> showFragment(CompletedFragment())
                    4 -> showFragment(DeclinedFragment())
                    5 -> showFragment(CancelledFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view
    }

    private fun showFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.reservations_layout, fragment)
            .commit()
    }
}
