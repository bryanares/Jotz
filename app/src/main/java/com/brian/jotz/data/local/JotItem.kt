package com.brian.jotz.data.local

data class JotItem (
    val id: String? = null,
    val userId: String? = null,
    val date: Long? = null,
    val title: String? = null,
    val category: String ?= null,
    val body: String? = null,
    var createdTime: Long? = null,
    var updatedTime: Long? = null,
)