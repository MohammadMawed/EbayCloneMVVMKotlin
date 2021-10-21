package com.mohammadmawed.ebayclonemvvmkotlin.ui

data class OffersModelClass(
    var title: String? = null,
    var description: String? = null,
    //var time: String? = null,
    var price: String? = null,
    var city: String? = null,
    var imageID: String? = null,
    var category: String? = null,
    var userID: String? = null,
    //var ImageUri: String? = null
){
    var ImageUri: String? = null
        get() = field                     // getter
        set(value) { field = value }      // setter

    var Time: String? = null
        get() = field                     // getter
        set(value) { field = value }      // setter
}

