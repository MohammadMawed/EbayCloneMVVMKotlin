package com.mohammadmawed.ebayclonemvvmkotlin.ui

import android.net.Uri

data class OffersModelClass(
    var title: String? = null,
    var description: String? = null,
    var time: String? = null,
    var price: String? = null,
    var city: String? = null,
    var imageID: String? = null,
    var category: String? = null,
    var userID: String? = null,
    var imageUri: Uri? = null
)
