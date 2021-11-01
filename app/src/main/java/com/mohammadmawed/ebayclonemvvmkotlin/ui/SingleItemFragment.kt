package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

import com.mohammadmawed.ebayclonemvvmkotlin.R

class SingleItemFragment : Fragment() {

    private lateinit var descriptionTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var providerUsernameTextView: TextView
    private lateinit var profilePic: ImageView
    private lateinit var imageView: ImageView
    private lateinit var saveButton: ImageButton
    private lateinit var rootLayout: ConstraintLayout

    private val args: SingleItemFragmentArgs by navArgs()
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_item, container, false)


        val location = args.location
        val time = args.time
        val description = args.description
        val imageID = args.imageID
        val price = args.price
        val userID = args.userID
        val category = args.category
        val title = args.title

        providerUsernameTextView = view.findViewById(R.id.providerUsername)
        descriptionTextView = view.findViewById(R.id.text_single_view_activity)
        titleTextView = view.findViewById(R.id.titleTextView)
        timeTextView = view.findViewById(R.id.timeTextView)
        cityTextView = view.findViewById(R.id.locationView)
        categoryTextView = view.findViewById(R.id.categoryView)
        priceTextView = view.findViewById(R.id.priceView)
        imageView = view.findViewById(R.id.imageViewSingleItem)
        profilePic = view.findViewById(R.id.profilePicSingleView)
        saveButton = view.findViewById(R.id.saveButton)
        rootLayout = view.findViewById(R.id.rootLayoutSingleItem)

        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        val context: Context = container!!.context

        saveButton.setOnClickListener {
            viewModel.saveFavoriteItems(imageID)
            viewModel.savedItemSuccessfullyLiveData.observe(viewLifecycleOwner, { status ->
                if (status){
                    saveButton.tag = "Saved"
                    saveButton.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    Snackbar.make(rootLayout, "Item saved successfully", Snackbar.LENGTH_LONG).show()
                }
            })
        }

        //We get the data from the args and set it in the TextView
        descriptionTextView.text = description
        cityTextView.text = location
        categoryTextView.text = category
        titleTextView.text = title
        priceTextView.text = price
        timeTextView.text = time

        viewModel.loadImagesSingleItem(imageID)
        viewModel.loadProviderData(userID)

        viewModel.uriSingleItemLiveData.observe(viewLifecycleOwner, { uri ->
            Glide.with(context).load(uri).into(imageView)
        })

        viewModel.providerUriLiveData.observe(viewLifecycleOwner, { uri ->
            Glide.with(context).load(uri).into(profilePic)
        })
        viewModel.providerUsernameLiveData.observe(viewLifecycleOwner, { username ->
            providerUsernameTextView.text = username
        })

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_singleItemFragment_to_mainUIFragment)
            // Handle the back button event
        }

        return view

    }

}