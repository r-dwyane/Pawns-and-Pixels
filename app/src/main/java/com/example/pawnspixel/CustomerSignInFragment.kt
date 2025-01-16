package com.example.pawnspixel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomerSignInFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUp = view.findViewById<TextView>(R.id.signup_portal)
        signUp.setOnClickListener {
            dismiss()
            val fragment = CustomerSignUpFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }

        val forgot_password = view.findViewById<TextView>(R.id.forgot_password)
        forgot_password.setOnClickListener {
            dismiss()
            val fragment = ForgotPasswordFragment()
            fragment.show(parentFragmentManager, "Android Center")
        }
    }
}
