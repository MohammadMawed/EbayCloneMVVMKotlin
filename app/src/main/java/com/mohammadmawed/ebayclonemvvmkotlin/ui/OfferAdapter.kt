package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohammadmawed.ebayclonemvvmkotlin.R


class OfferAdapter(private val dataList: ArrayList<OffersModelClass>) :
    RecyclerView.Adapter<OfferAdapter.mViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_view, parent,
            false
        )
        return mViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val oldList = dataList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            OfferItemDiffCallBack(
                oldList,
                dataList
            )
        )
        val data: OffersModelClass = dataList[position]

        diffResult.dispatchUpdatesTo(this)

        val userID: String = data.userID.toString()
        val imageID: String = data.imageID.toString()
        val descriptionArg: String = data.description.toString()
        val categoryArg: String = data.category.toString()
        val titleArg: String = data.title.toString()
        val timeArg: String = data.Time.toString()
        val priceArg: String = data.price.toString()
        val cityArg: String = data.city.toString()
        val uri = Uri.parse(data.ImageUri)

        holder.title.text = titleArg
        holder.time.text = timeArg
        holder.priceView.text = priceArg + "€"
        holder.location.text = cityArg
        Glide.with(holder.itemView).load(uri).into(holder.imageView)

        holder.itemView.setOnClickListener {

            val action = MainUIFragmentDirections.actionMainUIFragmentToSingleItemFragment(
                timeArg,
                titleArg, descriptionArg, imageID, priceArg, cityArg, userID, categoryArg
            )
            Navigation.findNavController(holder.itemView).navigate(action)
            //dataList.clear()
            //navController = Navigation.findNavController(holder.itemView)
            //navController!!.navigate(R.id.action_mainUIFragment_to_singleItemFragment)
        }
    }

    class OfferItemDiffCallBack(
        var oldOfferList: List<OffersModelClass>,
        var newOfferList: List<OffersModelClass>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldOfferList.size
        }

        override fun getNewListSize(): Int {
            return newOfferList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldOfferList[oldItemPosition].imageID == newOfferList[newItemPosition].imageID)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldOfferList[oldItemPosition] == newOfferList[newItemPosition]
        }

    }

    override fun getItemCount(): Int {

        return dataList.size
    }


    class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_single_view)
        var priceView: TextView = itemView.findViewById(R.id.price_single_view)
        var title: TextView = itemView.findViewById(R.id.title_single_View)
        var time: TextView = itemView.findViewById(R.id.time_single_view)
        var location: TextView = itemView.findViewById(R.id.location_single_view)
    }
}