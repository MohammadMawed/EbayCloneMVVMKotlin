package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.mohammadmawed.ebayclonemvvmkotlin.R
import com.squareup.picasso.Picasso

class SettingFragment : Fragment() {

    private lateinit var logoutButton: Button
    private lateinit var deleteAccountButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var profilePic: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var offerAdapter: OfferAdapter
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switch: Switch

    private lateinit var viewModel: ViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        logoutButton = view.findViewById(R.id.logoutButton)
        deleteAccountButton = view.findViewById(R.id.deleteAccountButton)
        usernameTextView = view.findViewById(R.id.usernameTextViewSetting)
        userEmailTextView = view.findViewById(R.id.userEmailTextView)
        profilePic = view.findViewById(R.id.profilePicSetting)
        recyclerView = view.findViewById(R.id.favorite_recyclerView)
        switch = view.findViewById(R.id.switch1)

        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        logoutButton.setOnClickListener(View.OnClickListener {
            viewModel.logUserOut()
            viewModel.logoutCheckLiveData.observe(viewLifecycleOwner, { logoutCheck ->
                if (logoutCheck == true) {
                    findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
                }
            })
        })

        deleteAccountButton.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Deleting Account")
            alertDialogBuilder.setMessage("You are about to delete your account\nAre you sure you want to delete your account?")
            alertDialogBuilder.setIcon(R.drawable.trash)
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            alertDialogBuilder.setPositiveButton("Yes, Sure") { dialog, which ->
                viewModel.deleteAccount()
                viewModel.deleteAccountSucceedLiveData.observe(viewLifecycleOwner, {
                    findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
                })
            }
            alertDialogBuilder.show()
        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                if (recyclerView.adapter == null){
                    viewModel.loadOwnOffer()
                }
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            viewModel.loadUserInfo()
        }
        viewModel.savedItemListLiveData.observe(viewLifecycleOwner, { arrayList ->
            offerAdapter = OfferAdapter(arrayList)
            recyclerView.adapter = offerAdapter
            offerAdapter.notifyDataSetChanged()
        })
        viewModel.usernameLiveData.observe(viewLifecycleOwner, { username ->
            usernameTextView.text = username
        })

        viewModel.userProfileUriLiveData.observe(viewLifecycleOwner, { profile ->
            Picasso.get().load(profile).into(profilePic)
        })

        viewModel.userEmailLiveData.observe(viewLifecycleOwner, { userEmail ->
            userEmailTextView.text = userEmail
        })

        return view
    }
}