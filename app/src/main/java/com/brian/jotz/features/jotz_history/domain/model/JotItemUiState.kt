package com.brian.jotz.features.jotz_history.domain.model

import com.brian.jotz.data.local.JotItem

data class JotItemUiState (
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val error: String? = null,
    val updatedJotItem: JotItem? = null,
    val createdJotItem: JotItem? = null,
    val singleJotItem: JotItem? = null,
    val jotItemList: List<JotItem>? = null,
)