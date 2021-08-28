package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.mohammadmawed.ebayclonemvvmkotlin.data.AppRepo

open class ViewModel(application: Application) : AndroidViewModel(application) {

    private var appRepo: AppRepo? = null
    private var userMutableLiveData: MutableLiveData<FirebaseUser>? = null
    private var loggedOutMutableLiveData: MutableLiveData<Boolean>? = null
    private var documentSnapshotMutableLiveData: MutableLiveData<String>? = null
    private var uriProfilePocMutableLiveData: MutableLiveData<Uri>? = null

    open fun ViewModel(application: Application) {

        appRepo = AppRepo(application)
        userMutableLiveData = appRepo!!.getUserMutableLiveData()
        loggedOutMutableLiveData = appRepo!!.getLoggedOutMutableLiveData()
        documentSnapshotMutableLiveData = appRepo!!.getUsernameMutableLiveData()
        uriProfilePocMutableLiveData = appRepo!!.getUriProfilePicMutableLiveData()
    }
    @RequiresApi(Build.VERSION_CODES.P)
    open fun login(email: String?, password: String?) {
        appRepo!!.loginUsers(email, password)
    }

    open fun getUserMutableLiveData(): MutableLiveData<FirebaseUser>? {
        return userMutableLiveData
    }

    open fun getUsernameMutableLiveData(): MutableLiveData<String>? {
        return documentSnapshotMutableLiveData
    }

    open fun getUriProfilePocMutableLiveData(): MutableLiveData<Uri>? {
        return uriProfilePocMutableLiveData
    }


}