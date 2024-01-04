package com.brian.jotz.data.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

//created a sealed Rezults class to handle the response from the api
sealed class Rezults<T> {
    data class Success<T>(val data: T) : Rezults<T>()
    data class Error<T>(val message: String? = null, val exception: Exception? = null) :
        Rezults<T>()

}

fun LocalDateTime.toLong(zoneId: String = "UTC") =
    this.atZone(ZoneId.of(zoneId)).toInstant().toEpochMilli()

//convert long to local date
fun Long.toLocalDate(zoneId: String = "UTC") =
    Instant.ofEpochMilli(this).atZone(ZoneId.of(zoneId)).toLocalDate()


//get full date from long
fun Long.getFullDateFromLong(): String {
    LocalDateTime.now().toLocalDate()
    if (this.toLocalDate() == LocalDateTime.now().toLocalDate())
        return "Today"
    if (this.toLocalDate() == LocalDateTime.now().toLocalDate().minusDays(1))
        return "Yesterday"
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH)
    sdf.applyPattern("EEE, d MMM yyyy")
    return sdf.format(java.util.Date(this))
}