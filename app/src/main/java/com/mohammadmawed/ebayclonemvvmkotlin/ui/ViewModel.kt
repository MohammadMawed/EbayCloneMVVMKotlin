package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mohammadmawed.ebayclonemvvmkotlin.data.AppRepo

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private var passwordMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var appRepo: AppRepo? = null

    init {
       appRepo = AppRepo(application)
    }

    val userData: LiveData<FirebaseUser>
        get() = userMutableLiveData

    val password: LiveData<Boolean>
        get() = passwordMutableLiveData

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
}