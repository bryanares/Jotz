package com.brian.jotz.data.utils

//created a sealed Rezults class to handle the response from the api
sealed class Rezults<T> {
    data class Success<T>(val data: T) : Rezults<T>()
    data class Error<T>(val message: String? = null, val exception: Exception? = null) : Rezults<T>()

}