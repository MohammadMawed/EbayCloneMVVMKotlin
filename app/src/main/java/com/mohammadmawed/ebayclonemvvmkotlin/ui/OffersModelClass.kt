package com.mohammadmawed.ebayclonemvvmkotlin.ui

data class OffersModelClass(
    var title: String? = null,
    var description: String? = null,
    var Time: String? = null,
    var price: String? = null,
    var city: String? = null,
    var imageID: String? = null,
    var category: String? = null,
    var userID: String? = null,
    var ImageUri: String? = null
){
    /*var ImageUri: String? = null
        get() = field                     // getter
        set(value) { field = value }      // setter

    var Time: String? = null
        get() = field                     // getter
        set(value) { field = value }*/     // setter

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass){
            return false
        }
        other as OffersModelClass

        if (title != other.title){
            return false
        }
        if (description != other.description){
            return false
        }
        if (Time != other.Time){
            return false
        }
        if (price != other.price){
            return false
        }
        if (city != other.city){
            return false
        }
        if (imageID!= other.imageID){
            return false
        }
        if (category != other.category){
            return false
        }
        if (userID != other.userID){
            return false
        }
        if (ImageUri != other.ImageUri){
            return false
        }
        return true
    }
}

