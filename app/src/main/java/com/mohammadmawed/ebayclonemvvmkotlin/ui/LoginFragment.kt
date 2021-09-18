package com.mohammadmawed.ebayclonemvvmkotlin.ui


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mohammadmawed.ebayclonemvvmkotlin.R

class LoginFragment : Fragment() {

    private var emailEditText: EditText? = null
    private lateinit var passwordEditText: EditText
    private var loginButton: Button? = null
    private var signupButton: Button? = null
    private var resetPasswordButton:Button? = null


    private lateinit var viewModel: ViewModel


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


        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        loginButton?.setOnClickListener(View.OnClickListener {
            val email = emailEditText!!.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)
        })

        viewModel.userData.observe(viewLifecycleOwner, {
             firebaseUser ->
                if (firebaseUser != null) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment3_to_mainUIFragment)
                }
            })
        viewModel.password.observe(viewLifecycleOwner,{
            password ->
            if (password == false){
                Toast.makeText(activity, "Login Failed", Toast.LENGTH_LONG).show()
            }
        })
        return view
    }

}