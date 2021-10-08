package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mohammadmawed.ebayclonemvvmkotlin.R

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        Handler().postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is signed in
                findNavController().navigate(R.id.action_splashScreenFragment_to_mainUIFragment)
            } else {
                // No user is signed in
                findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
            }
        }, 500)

        return view
    }
}