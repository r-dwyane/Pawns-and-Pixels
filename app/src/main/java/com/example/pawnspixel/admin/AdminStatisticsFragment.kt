package com.example.pawnspixel.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.pawnspixel.R

class AdminStatisticsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_statistics, container, false)
        val backBtnAdminStats: ImageButton = view.findViewById(R.id.backBtnAdminStats)!!

        backBtnAdminStats.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

}