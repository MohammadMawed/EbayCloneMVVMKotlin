package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mohammadmawed.ebayclonemvvmkotlin.R

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private var signupButton: Button? = null
    private var resetPasswordButton:Button? = null
    private lateinit var progressBar: ProgressBar

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
        progressBar = view.findViewById(R.id.loading)


        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        loginButton.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()){
                viewModel.login(email, password)
            }else{
                Toast.makeText(activity, "Please enter your credential!", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
        })

        viewModel.userDataLiveData.observe(viewLifecycleOwner, {
             firebaseUser ->
                if (firebaseUser != null) {
                    progressBar.visibility = View.GONE
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment3_to_mainUIFragment)
                }
            })
        viewModel.passwordLiveData.observe(viewLifecycleOwner,{
            password ->
            if (password == false){
                progressBar.visibility = View.GONE
                Toast.makeText(activity, "Login Failed, Please try again!", Toast.LENGTH_LONG).show()
            }
        })
        return view
    }
}