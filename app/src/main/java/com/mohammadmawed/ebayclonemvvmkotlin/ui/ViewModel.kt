package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener

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

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var passwordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var usernameMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var uriProfilePicMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
    private var loggedOutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var userEmailMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var deleteAccountSucceedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var providerUsernameMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var providerUriProfilePicMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
    private var singleItemURIMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
    private var uploadSuccessfulMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var listMutableLiveData: MutableLiveData<ArrayList<OffersModelClass>> =
        MutableLiveData()
    var arrayList: ArrayList<OffersModelClass> = ArrayList()

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("offers")
    private val owmItemRef = FirebaseDatabase.getInstance().reference.child("ownItems")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private val firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()


    val userDataLiveData: LiveData<FirebaseUser>
        get() = userMutableLiveData

    val passwordLiveData: LiveData<Boolean>
        get() = passwordMutableLiveData

    val usernameLiveData: LiveData<String>
        get() = usernameMutableLiveData

    val userProfileUriLiveData: LiveData<Uri>
        get() = uriProfilePicMutableLiveData

    val logoutCheckLiveData: LiveData<Boolean>
        get() = loggedOutMutableLiveData

    val userEmailLiveData: LiveData<String>
        get() = userEmailMutableLiveData

    val deleteAccountSucceedLiveData: LiveData<Boolean>
        get() = deleteAccountSucceedMutableLiveData

    val listLiveData: LiveData<ArrayList<OffersModelClass>>
        get() = listMutableLiveData

    val uriSingleItemLiveData: LiveData<Uri>
        get() = singleItemURIMutableLiveData

    val providerUsernameLiveData: LiveData<String>
        get() = providerUsernameMutableLiveData

    val providerUriLiveData: LiveData<Uri>
        get() = providerUriProfilePicMutableLiveData

    val successfulUploadLiveData: LiveData<Boolean>
        get() = uploadSuccessfulMutableLiveData

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            Activity()
        ) {
            if (it.isSuccessful) {
                userMutableLiveData.postValue(firebaseAuth.currentUser)
            } else {
                passwordMutableLiveData.postValue(false)
            }
        }
    }

    fun loadUserInfo() {
        val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
        val user = FirebaseAuth.getInstance().currentUser

        //Getting the user's email
        if (user != null) {
            // User is signed in
            val documentReference: DocumentReference =
                firebaseStore.collection("users").document(userID)
            documentReference.addSnapshotListener { value, _ ->
                usernameMutableLiveData.postValue(value!!.getString("username"))
                val userEmail: String = FirebaseAuth.getInstance().currentUser?.email.toString()
                userEmailMutableLiveData.postValue(userEmail)
            }
        }
        //Getting the user's profile pic
        val fileRef =
            storageReference.child("users/" + firebaseAuth.currentUser?.uid + "/profile.jpg")
        fileRef.downloadUrl.addOnSuccessListener { uri ->
            uriProfilePicMutableLiveData.postValue(uri)
        }
    }

    fun logUserOut() {
        firebaseAuth.signOut()
        loggedOutMutableLiveData.postValue(true)
    }

    fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deleteAccountSucceedMutableLiveData.postValue(true)
            }
        }
    }

    fun loadData(reload: Boolean) {
        if (reload) {
            databaseReference.addValueEventListener(object : ValueEventListener {
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
                            arrayList.add(model as OffersModelClass)

                            listMutableLiveData.postValue(arrayList)

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun loadImagesSingleItem(imageID: String) {

        //Getting the images for a single post
        val fileRef11 = FirebaseStorage.getInstance().reference.child(
            "offers/$imageID.jpg"
        )
        fileRef11.downloadUrl.addOnSuccessListener { uri ->

            singleItemURIMutableLiveData.postValue(uri)
        }

    }

    fun loadProviderData(userID: String) {

        //Getting the username of the provider
        // User is signed in
        val documentReference: DocumentReference =
            firebaseStore.collection("users").document(userID)
        documentReference.addSnapshotListener { value, _ ->
            providerUsernameMutableLiveData.postValue(value!!.getString("username"))
        }
        val fileRef =
            storageReference.child("users/$userID/profile.jpg")
        fileRef.downloadUrl.addOnSuccessListener { uri ->
            providerUriProfilePicMutableLiveData.postValue(uri)
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

                    databaseReference.child(imageID).setValue(hashMap)
                        .addOnSuccessListener(OnSuccessListener<Void?> {
                            owmItemRef.child(firebaseAuth.currentUser!!.uid).child(imageID)
                                .setValue(hashMap).addOnCompleteListener {
                                    uploadSuccessfulMutableLiveData.postValue(true)
                                }
                        })
                }
            }

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

