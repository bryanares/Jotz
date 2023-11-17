package com.brian.jotz.features.auth.domain.model

//create a data class to hold the state of the UI
class AuthUiState (
    var isLoading: Boolean = false,
    var isSuccessful: Boolean = false,
    var error: String? = null,
    var userId: String? = null
)