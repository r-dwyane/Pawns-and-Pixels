package com.example.pawnspixel.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.pawnspixel.R

class PrivacyPolicy : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_privacy_policy, container, false)

        val button = view?.findViewById<ImageView>(R.id.backAccount3)

        button?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return  view
    }

}