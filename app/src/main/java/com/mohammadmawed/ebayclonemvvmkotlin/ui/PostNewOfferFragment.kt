package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mohammadmawed.ebayclonemvvmkotlin.R

class PostNewOfferFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var uploadButton: Button
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var ImageUri: Uri
    var isImageAdd = false

    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_new_offer, container, false)

        imageView = view.findViewById(R.id.imageViewSelect)
        titleEditText = view.findViewById(R.id.editTextTitle)
        priceEditText = view.findViewById(R.id.editTextPrice)
        descEditText = view.findViewById(R.id.descEditText)
        cityEditText = view.findViewById(R.id.editTextCity)
        uploadButton = view.findViewById(R.id.uploadButton)

        val spinner: Spinner = view.findViewById(R.id.spinner)
        val context: Context = container!!.context

        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)



        imageView.setOnClickListener {
            //Open the gallery
            val oenGalleryIntent = Intent()
            oenGalleryIntent.type = "image/*"
            oenGalleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                oenGalleryIntent, PICK_IMAGE_REQUEST
            )
        }

        uploadButton.setOnClickListener {
            val title: String = titleEditText.text.toString()
            val desc: String = descEditText.text.toString()
            val price: String = priceEditText.text.toString()
            val city: String = cityEditText.text.toString()
            val category = spinner.selectedItem.toString()

            viewModel.uploadFile(title, desc, price, city, category, ImageUri)
        }

        viewModel.successfulUploadLiveData.observe(viewLifecycleOwner, {
            if(it == true){
                findNavController().navigate(R.id.action_postNewOfferFragment_to_mainUIFragment)
                Toast.makeText(context, "Upload Done!", Toast.LENGTH_LONG).show()
            }
        })

        val adapter = ArrayAdapter.createFromResource(context,
        R.array.category,
        android.R.layout.simple_spinner_item).apply {

            // Specify the layout to use when the list of choices appears
            this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Apply the adapter to the spinner
            spinner.adapter = this
        }

        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            assert(data != null)
            ImageUri = data!!.data!!
            //Uploading the image to firebase
            imageView.setImageURI(ImageUri)
            isImageAdd = true
        }
    }
}