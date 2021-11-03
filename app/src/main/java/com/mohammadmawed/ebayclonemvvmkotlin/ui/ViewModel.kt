package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var _userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var _passwordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var _usernameMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var _uriProfilePicMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
    private var _loggedOutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var _userEmailMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var _deleteAccountSucceedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var _providerUsernameMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var _providerUriProfilePicMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
    private var _singleItemURIMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
    private var _uploadSuccessfulMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var _listMutableLiveData: MutableLiveData<ArrayList<OffersModelClass>> =
        MutableLiveData()
    private var _ownItemListMutableLiveData: MutableLiveData<ArrayList<OffersModelClass>> =
        MutableLiveData()
    private var _savedItemListMutableLiveData: MutableLiveData<ArrayList<OffersModelClass>> =
        MutableLiveData()
    private var _savedItemSuccessfullyMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var _messageSentSuccessfullyMutableLiveData: MutableLiveData<Boolean> =
        MutableLiveData()
    private var _messageMutableLiveData: MutableLiveData<ArrayList<ChatContentModelClass>> = MutableLiveData()

    var arrayListMainUI: ArrayList<OffersModelClass> = ArrayList()
    var arrayListOwnItem: ArrayList<OffersModelClass> = ArrayList()
    var arrayListSavedItem: ArrayList<OffersModelClass> = ArrayList()
    var arrayListMessage: ArrayList<ChatContentModelClass> = ArrayList()

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("offers")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private val firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()


    val userDataLiveData: LiveData<FirebaseUser>
        get() = _userMutableLiveData

    val passwordLiveData: LiveData<Boolean>
        get() = _passwordMutableLiveData

    val usernameLiveData: LiveData<String>
        get() = _usernameMutableLiveData

    val userProfileUriLiveData: LiveData<Uri>
        get() = _uriProfilePicMutableLiveData

    val logoutCheckLiveData: LiveData<Boolean>
        get() = _loggedOutMutableLiveData

    val userEmailLiveData: LiveData<String>
        get() = _userEmailMutableLiveData

    val deleteAccountSucceedLiveData: LiveData<Boolean>
        get() = _deleteAccountSucceedMutableLiveData

    val listLiveData: LiveData<ArrayList<OffersModelClass>>
        get() = _listMutableLiveData

    val ownItemListLiveData: LiveData<ArrayList<OffersModelClass>>
        get() = _ownItemListMutableLiveData

    val savedItemListLiveData: LiveData<ArrayList<OffersModelClass>>
        get() = _savedItemListMutableLiveData

    val uriSingleItemLiveData: LiveData<Uri>
        get() = _singleItemURIMutableLiveData

    val providerUsernameLiveData: LiveData<String>
        get() = _providerUsernameMutableLiveData

    val providerUriLiveData: LiveData<Uri>
        get() = _providerUriProfilePicMutableLiveData

    val successfulUploadLiveData: LiveData<Boolean>
        get() = _uploadSuccessfulMutableLiveData

    val savedItemSuccessfullyLiveData: LiveData<Boolean>
        get() = _savedItemSuccessfullyMutableLiveData

    val messageSentSuccessfullyLiveData: LiveData<Boolean>
        get() = _messageSentSuccessfullyMutableLiveData

    val messageLivData: LiveData<ArrayList<ChatContentModelClass>>
        get() = _messageMutableLiveData

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            Activity()
        ) {
            if (it.isSuccessful) {
                _userMutableLiveData.postValue(firebaseAuth.currentUser)
            } else {
                _passwordMutableLiveData.postValue(false)
            }
        }
    }

    fun loadUserInfo(userID: String) {
        val user = FirebaseAuth.getInstance().currentUser

        //Getting the user's email
        if (user != null) {
            // User is signed in
            val documentReference: DocumentReference =
                firebaseStore.collection("users").document(userID)
            documentReference.addSnapshotListener { value, _ ->
                _usernameMutableLiveData.postValue(value!!.getString("username"))
                val userEmail: String = FirebaseAuth.getInstance().currentUser?.email.toString()
                _userEmailMutableLiveData.postValue(userEmail)
            }
        }
        //Getting the user's profile pic
        val fileRef =
            storageReference.child("users/$userID/profile.jpg")
        fileRef.downloadUrl.addOnSuccessListener { uri ->
            _uriProfilePicMutableLiveData.postValue(uri)
        }
    }

    fun logUserOut() {
        firebaseAuth.signOut()
        _loggedOutMutableLiveData.postValue(true)
    }

    fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _deleteAccountSucceedMutableLiveData.postValue(true)
            }
        }
    }

    fun loadData() {

        //Resetting the ArrayList when recall the function to avoid duplication in the recyclerView
        arrayListMainUI.clear()

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {

                    val model = data.getValue(OffersModelClass::class.java)

                    //Getting the offer ID to load its images
                    val imageID: String = model?.imageID.toString()

                    val fileRef11 = FirebaseStorage.getInstance().reference.child(
                        "offers/$imageID.jpg"
                    )
                    fileRef11.downloadUrl.addOnSuccessListener { uri ->

                        //Assigning the image uri and converting it to string
                        model?.ImageUri = uri.toString()

                        //Assigning the new time format
                        model?.Time = model?.Time?.let { calculateTimeAge(it) }

                        //Adding the data to arraylist as whole to observe it from the fragment

                        arrayListMainUI.add(model as OffersModelClass)
                        _listMutableLiveData.postValue(arrayListMainUI)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun loadOwnOffer() {
        arrayListOwnItem.clear()
        val myUid = firebaseAuth.currentUser?.uid
        val myOffersQuery = databaseReference.orderByChild("userID").equalTo(myUid)
        myOffersQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {

                    val model = data.getValue(OffersModelClass::class.java)

                    //Getting the offer ID to load its images
                    val imageID: String = model?.imageID.toString()

                    Log.d("User's offer --->", imageID)

                    //Getting the offer ID to load its images

                    val fileRef11 = FirebaseStorage.getInstance().reference.child(
                        "offers/$imageID.jpg"
                    )
                    fileRef11.downloadUrl.addOnSuccessListener { uri ->

                        //Assigning the image uri and converting it to string
                        model?.ImageUri = uri.toString()

                        //Assigning the new time format
                        model?.Time = model?.Time?.let { calculateTimeAge(it) }

                        //Adding the data to arraylist as whole to observe it from the fragment
                        arrayListOwnItem.add(model as OffersModelClass)

                        _ownItemListMutableLiveData.postValue(arrayListOwnItem)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    fun saveFavoriteItems(offerID: String) {
        val userID = firebaseAuth.currentUser?.uid.toString()
        val databaseReference1: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("savedItem").child(userID)
        databaseReference1.child(offerID).setValue(offerID).addOnCompleteListener {
            //Notifying the user when the saving process is successfully done
            _savedItemSuccessfullyMutableLiveData.postValue(true)
        }
    }

    fun loadSavedItems() {
        arrayListSavedItem.clear()
        val userID = firebaseAuth.currentUser?.uid.toString()
        val databaseReference1: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("savedItem").child(userID)
        databaseReference1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {

                    val savedOfferID: String = data.value as String

                    //Getting the offer ID to load its images

                    val myOffersQuery =
                        databaseReference.orderByChild("imageID").equalTo(savedOfferID)
                    myOffersQuery.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data in snapshot.children) {

                                val model = data.getValue(OffersModelClass::class.java)

                                //Getting the offer ID to load its images
                                val imageID: String = model?.imageID.toString()

                                Log.d("User's saved offer --->", imageID)

                                //Getting the offer ID to load its images

                                val fileRef11 = FirebaseStorage.getInstance().reference.child(
                                    "offers/$imageID.jpg"
                                )
                                fileRef11.downloadUrl.addOnSuccessListener { uri ->

                                    //Assigning the image uri and converting it to string
                                    model?.ImageUri = uri.toString()

                                    //Assigning the new time format
                                    model?.Time = model?.Time?.let { calculateTimeAge(it) }

                                    //Adding the data to arraylist as whole to observe it from the fragment
                                    arrayListSavedItem.add(model as OffersModelClass)

                                    _savedItemListMutableLiveData.postValue(arrayListSavedItem)

                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun loadImagesSingleItem(imageID: String) {

        //Getting the images for a single post
        val fileRef11 = FirebaseStorage.getInstance().reference.child(
            "offers/$imageID.jpg"
        )
        fileRef11.downloadUrl.addOnSuccessListener { uri ->

            _singleItemURIMutableLiveData.postValue(uri)
        }

    }

    fun loadProviderData(userID: String) {

        //Getting the username of the provider
        // User is signed in
        val documentReference: DocumentReference =
            firebaseStore.collection("users").document(userID)
        documentReference.addSnapshotListener { value, _ ->
            _providerUsernameMutableLiveData.postValue(value!!.getString("username"))
        }
        val fileRef =
            storageReference.child("users/$userID/profile.jpg")
        fileRef.downloadUrl.addOnSuccessListener { uri ->
            _providerUriProfilePicMutableLiveData.postValue(uri)
        }
    }

    @SuppressLint("SimpleDateFormat")

    fun uploadFile(
        title: String,
        description: String,
        price: String,
        city: String,
        category: String,
        imageUri: Uri
    ) {
        val calendar: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MM-yyyy HH:mm a")
        val timeInMillis: Long = calendar.timeInMillis
        val time: String = df.format(calendar.timeInMillis)
        val imageID: String = timeInMillis.toString()
        storageReference.child("offers/").child("$imageID.jpg").putFile(imageUri)
            .addOnSuccessListener {
                storageReference.child("$imageID.jpg").downloadUrl.addOnCompleteListener {
                    val hashMap = HashMap<String, String>()
                    hashMap["title"] = title
                    hashMap["description"] = description
                    hashMap["ImageUri"] = imageUri.toString()
                    hashMap["imageID"] = imageID
                    hashMap["category"] = category
                    hashMap["price"] = price
                    hashMap["city"] = city
                    hashMap["Time"] = time
                    hashMap["userID"] = firebaseAuth.currentUser!!.uid

                    //Notifying user when the upload is finished
                    _uploadSuccessfulMutableLiveData.postValue(true)
                }
            }

    }

    fun sendMessage(message: String, receiverID: String) {

        val senderID = firebaseAuth.currentUser?.uid.toString()

        if (message.isNotEmpty()) {
            val calender: Calendar = Calendar.getInstance()
            @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd-MM-yyyy HH:mm a")
            val time: String = df.format(calender.time)

            //Creating hashmap to upload it to firebase
            val chatHashMap = HashMap<String, String>()
            chatHashMap["message"] = message
            chatHashMap["time"] = time
            chatHashMap["receiverID"] = receiverID
            chatHashMap["senderID"] = senderID

            val databaseReference2: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("Chats")
            databaseReference2.child(receiverID).child(senderID).push().setValue(chatHashMap)
                .addOnCompleteListener {
                    databaseReference2.child(senderID).child(receiverID).push()
                        .setValue(chatHashMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                _messageSentSuccessfullyMutableLiveData.value = true
                            }
                        }
                }
        }
    }
    fun loadMessages(providerID: String){
        arrayListMessage.clear()
        val userID = firebaseAuth.currentUser!!.uid
        val databaseReference3: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Chats").child(userID).child(providerID)

        databaseReference3.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {

                    val model = data.getValue(ChatContentModelClass::class.java)

                    //Getting the messages
                    //val message: String = model?.message.toString()
                    //Log.d("Message From: ", message)

                    arrayListMessage.add(model as ChatContentModelClass)

                    _messageMutableLiveData.postValue(arrayListMessage)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun calculateTimeAge(time: String): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = "ago"
        try {
            @SuppressLint("SimpleDateFormat") val dateFormat = 
                SimpleDateFormat("dd-MM-yyyy HH:mm a")
            val pasTime = dateFormat.parse(time)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second Seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }
}

