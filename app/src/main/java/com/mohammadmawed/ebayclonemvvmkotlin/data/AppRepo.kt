package com.mohammadmawed.ebayclonemvvmkotlin.data

import android.app.Application
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AppRepo(application: Application) {

    private var application: Application? = null
    private var userMutableLiveData: MutableLiveData<FirebaseUser>? = null
    private var loggedOutMutableLiveData: MutableLiveData<Boolean>? = null
    private var usernameMutableLiveData: MutableLiveData<String>? = null
    private var uriProfilePicMutableLiveData: MutableLiveData<Uri>? = null


    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    @RequiresApi(api = Build.VERSION_CODES.P)
    fun loginUsers(email: String?, password: String?) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(
            application!!.mainExecutor, OnCompleteListener {
            if (it.isSuccessful){
                userMutableLiveData!!.postValue(firebaseAuth.currentUser)
            }else{
                Toast.makeText(application, "Login Failed please try again!", Toast.LENGTH_LONG).show()
            }
        })
        /*firebaseAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(
            Activity()
        ) {
            if (it.isSuccessful) {
                userMutableLiveData!!.postValue(firebaseAuth.currentUser)
            } else {
                Toast.makeText(
                    Activity(),
                    "Logging in failed: " + it.exception,
                    Toast.LENGTH_LONG
                ).show()
            }
        }*/
    }
}
