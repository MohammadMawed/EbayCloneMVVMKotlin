package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohammadmawed.ebayclonemvvmkotlin.R
import com.squareup.picasso.Picasso

class OfferAdapter(private val dataList: ArrayList<OffersModelClass>): RecyclerView.Adapter<OfferAdapter.mViewHolder>() {

    private lateinit var viewModel: ViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferAdapter.mViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_view, parent,
            false)
        return mViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OfferAdapter.mViewHolder, position: Int) {
        val data: OffersModelClass = dataList[position]

        val timeAgo: String = data.time?.let { viewModel.calculateTimeAge(it) }.toString()
        holder.title.text = data.title
        holder.time.text = timeAgo
        holder.priceView.text = data.price.toString() + "€"
        holder.location.text = data.city
        val userID: String? = data.userID
        val imageID: String? = data.imageID
        val uri = Uri.parse(imageID)
        Picasso.get().load(uri).into(holder.imageView)

    }

    override fun getItemCount(): Int {

        return dataList.size

    }
    public class mViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView: ImageView = itemView.findViewById(R.id.image_single_view)
        var priceView: TextView = itemView.findViewById(R.id.price_single_view)
        var title: TextView = itemView.findViewById(R.id.title_single_View)
        var time: TextView = itemView.findViewById(R.id.time_single_view)
        var location: TextView = itemView.findViewById(R.id.location_single_view)
    }
}