package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.mohammadmawed.ebayclonemvvmkotlin.R


class MainUIFragment : Fragment() {

    private lateinit var usernameTextView: TextView
    private lateinit var profilePic: ImageView
    private lateinit var addNewOfferButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var offerAdapter: OfferAdapter


    private lateinit var viewModel: ViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_u_i, container, false)

        usernameTextView = view.findViewById(R.id.usernameTextView)
        profilePic = view.findViewById(R.id.profilePic)
        addNewOfferButton = view.findViewById(R.id.addButton)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeLayout = view.findViewById(R.id.swipeLayout)

        val context: Context = container!!.context

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        profilePic.setOnClickListener {
            findNavController().navigate(R.id.action_mainUIFragment_to_settingFragment)
        }
        usernameTextView.setOnClickListener {
            findNavController().navigate(R.id.action_mainUIFragment_to_settingFragment)
        }
        addNewOfferButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainUIFragment_to_postNewOfferFragment)
        }

        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            viewModel.loadUserInfo()
            viewModel.loadData()
        }

        //Loading user's username
        viewModel.usernameLiveData.observe(viewLifecycleOwner, { username ->
            usernameTextView.text = username
        })

        //Loading user's profile picture
        viewModel.userProfileUriLiveData.observe(viewLifecycleOwner, { profile ->
            Glide.with(context).load(profile).into(profilePic)
        })

        swipeLayout.setOnRefreshListener {

            viewModel.loadData()
            viewModel.listLiveData.observe(viewLifecycleOwner, { arrayList ->
                offerAdapter = OfferAdapter(arrayList)
                recyclerView.adapter = offerAdapter
                offerAdapter.notifyDataSetChanged()
            })

            swipeLayout.isRefreshing = false
        }

        viewModel.listLiveData.observe(viewLifecycleOwner, { arrayList ->
            offerAdapter = OfferAdapter(arrayList)
            recyclerView.adapter = offerAdapter
            offerAdapter.notifyDataSetChanged()
        })

        return view
    }
}
