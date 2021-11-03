package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.mohammadmawed.ebayclonemvvmkotlin.R

class ChatMessagesAdapter(private val dataList: ArrayList<ChatContentModelClass>) :

    RecyclerView.Adapter<ChatMessagesAdapter.mViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_single_view, parent,
            false
        )
        return mViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: mViewHolder, position: Int) {

        val data: ChatContentModelClass = dataList[position]

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val userID: String? = data.senderID
        val message: String? = data.message
        val time: String? = data.time

        if (currentUserID == userID){
            holder.messageReceived.visibility = View.GONE
            holder.messageSent.text = message
            holder.messageTime.text = time
        }else{
            holder.messageSent.visibility = View.GONE
            holder.messageReceived.text = message
            holder.messageTime.text = time
        }
    }

    override fun getItemCount(): Int {

        return dataList.size
    }

    class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageReceived: TextView = itemView.findViewById(R.id.message_received)
        var messageSent: TextView = itemView.findViewById(R.id.message_sent)
        var messageTime: TextView = itemView.findViewById(R.id.messageTime)
    }
}