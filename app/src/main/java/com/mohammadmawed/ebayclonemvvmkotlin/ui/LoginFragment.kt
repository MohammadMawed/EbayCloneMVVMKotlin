package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mohammadmawed.ebayclonemvvmkotlin.R

abstract class LoginFragment : Fragment(R.layout.fragment_login) {

    private var emailEditText: EditText? = null
    private var passwordEditText:EditText? = null
    private var loginButton: Button? = null
    private var signupButton: Button? = null
    private var resetPasswordButton:Button? = null

    private var viewModel: ViewModel? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailEditText = view.findViewById(R.id.username)
        passwordEditText = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.login)

        loginButton!!.setOnClickListener(View.OnClickListener {
            val email = emailEditText!!.getText().toString().trim { it <= ' ' }
            val password = passwordEditText!!.getText().toString().trim { it <= ' ' }
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel!!.login(email, password)
            }
        })

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel!!.getUserMutableLiveData()!!.observe(this,
            { firebaseUser ->
                if (firebaseUser != null) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment3_to_mainUIFragment)
                }
            })
    }
}