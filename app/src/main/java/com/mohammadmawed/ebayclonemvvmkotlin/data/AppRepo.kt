package com.mohammadmawed.ebayclonemvvmkotlin.data

import android.app.Application
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import java.util.*

class AppRepo {

    private var application: Application? = null

    val userMutableLiveData: MutableLiveData<FirebaseUser>? = null
    val loggedOutMutableLiveData: MutableLiveData<Boolean>? = null
    val usernameMutableLiveData: MutableLiveData<String>? = null
    val uriProfilePicMutableLiveData: MutableLiveData<Uri>? = null


    private var firebaseAuth: FirebaseAuth? = null

    constructor(application: Application?) {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {

            val userID = Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!
                .uid

            userMutableLiveData!!.postValue(firebaseAuth!!.currentUser)


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    fun loginUsers(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(
            application!!.mainExecutor,
            { task ->
                if (task.isSuccessful) {
                    userMutableLiveData!!.postValue(firebaseAuth!!.currentUser)
                } else {
                    Toast.makeText(
                        application,
                        "Logging in failed: " + task.exception,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    @JvmName("getUserMutableLiveData1")
    fun getUserMutableLiveData(): MutableLiveData<FirebaseUser>? {
        return userMutableLiveData
    }

    @JvmName("getLoggedOutMutableLiveData1")
    fun getLoggedOutMutableLiveData(): MutableLiveData<Boolean>? {
        return loggedOutMutableLiveData
    }

    @JvmName("getUsernameMutableLiveData1")
    fun getUsernameMutableLiveData(): MutableLiveData<String>? {
        return usernameMutableLiveData
    }

    @JvmName("getUriProfilePicMutableLiveData1")
    fun getUriProfilePicMutableLiveData(): MutableLiveData<Uri>? {
        return uriProfilePicMutableLiveData
    }

}