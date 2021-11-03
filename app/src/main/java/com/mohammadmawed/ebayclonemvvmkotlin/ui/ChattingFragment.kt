package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.mohammadmawed.ebayclonemvvmkotlin.R


class ChattingFragment : Fragment() {

    private lateinit var profilePic: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var sendMessagesEditText: EditText
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var sendMessageButton: ImageButton
    private lateinit var chatAdapter: ChatMessagesAdapter

    private val args: ChattingFragmentArgs by navArgs()
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatting, container, false)

        profilePic = view.findViewById(R.id.profilePicChat)
        usernameTextView = view.findViewById(R.id.usernameTextViewChat)
        sendMessagesEditText = view.findViewById(R.id.editTextTextChat)
        chatRecyclerView = view.findViewById(R.id.chat_recyclerView)
        sendMessageButton = view.findViewById(R.id.sendMessagesButton)

        val context: Context = container!!.context
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        val receiverUserID = args.userID

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            viewModel.loadUserInfo(receiverUserID)
            viewModel.loadMessages(receiverUserID)
        }
        //Loading user's username
        viewModel.usernameLiveData.observe(viewLifecycleOwner, { username ->
            usernameTextView.text = username
        })

        sendMessageButton.setOnClickListener {
            val message = sendMessagesEditText.text.toString()
            viewModel.sendMessage(message, receiverUserID)
        }
        //Loading user's profile picture
        viewModel.userProfileUriLiveData.observe(viewLifecycleOwner, { profile ->
            Glide.with(context).load(profile).into(profilePic)
        })
        viewModel.messageSentSuccessfullyLiveData.observe(viewLifecycleOwner, { status ->
            if (status){
                sendMessagesEditText.setText("")
            }
        })
        viewModel.messageLivData.observe(viewLifecycleOwner, { arrayList ->
            chatAdapter = ChatMessagesAdapter(arrayList)
            chatRecyclerView.adapter = chatAdapter
            chatAdapter.notifyDataSetChanged()
            chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        })
        return view
    }

}