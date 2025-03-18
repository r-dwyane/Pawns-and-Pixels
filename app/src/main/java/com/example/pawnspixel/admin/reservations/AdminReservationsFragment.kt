package com.example.pawnspixel.admin.reservations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.pawnspixel.R
import com.google.android.material.tabs.TabLayout

class AdminReservationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_reservations, container, false)
        val add = view.findViewById<ImageButton>(R.id.add_reservations)!!

        add.setOnClickListener {
            val fragment = AdminNewReservation()
            fragment.show(parentFragmentManager, "Android Center")
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.admin_tablayout2)

        tabLayout?.apply {
            addTab(newTab().setText("Pending"))
            addTab(newTab().setText("Accepted"))
            addTab(newTab().setText("Ongoing"))
            addTab(newTab().setText("Completed"))
            addTab(newTab().setText("Declined"))
            addTab(newTab().setText("Cancelled"))
        }

        showFragment(AdminPending())

        tabLayout?.getTabAt(0)?.select()

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showFragment(AdminPending())
                    1 -> showFragment(AdminAccepted())
                    2 -> showFragment(AdminOngoing())
                    3 -> showFragment(AdminCompleted())
                    4 -> showFragment(AdminDeclined())
                    5 -> showFragment(AdminCancelled())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view
    }

    private fun showFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.admin_reservations_layout, fragment)
            .commit()
    }

}