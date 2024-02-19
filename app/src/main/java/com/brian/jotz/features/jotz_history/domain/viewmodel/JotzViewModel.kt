package com.brian.jotz.features.jotz_history.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.jotz.data.database.dao.JotzDao
import com.brian.jotz.data.local.JotItem
import com.brian.jotz.data.repository.MainRepository
import com.brian.jotz.data.utils.Rezults
import com.brian.jotz.features.jotz_history.domain.model.JotItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JotzViewModel
@Inject constructor
    (
    private val jotzDao: JotzDao,
    private val repository: MainRepository
) : ViewModel() {
    private val _jotItemUiState = MutableStateFlow(JotItemUiState())
    val jotItemUiState = _jotItemUiState.asStateFlow()

    fun resetState() {
        _jotItemUiState.update { JotItemUiState() }
    }

    fun addJotItem(userId: String, date: Long, title: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.addOrUpdateJotz(
                userId,
                null,
                JotItem(date = date, title = title, body = body)
            ).collectLatest { rezult ->
                when (rezult) {
                    is Rezults.Success -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = true,
                                createdJotItem = rezult.data
                            )
                        }
                    }

                    is Rezults.Error -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = false,
                                error = rezult.exception?.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateJotItem(
        userId: String,
        jotItemId: String,
        date: Long? = null,
        title: String? = null,
        body: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldJotItem = _jotItemUiState.value.singleJotItem ?: return@launch
            val newJotItem = oldJotItem.copy(
                date = date ?: oldJotItem.date,
                title = title ?: oldJotItem.title,
                body = body ?: oldJotItem.body
            )

            repository.addOrUpdateJotz(
                userId,
                jotItemId,
                newJotItem
            )
                .collectLatest { rezult ->
                    when (rezult) {
                        is Rezults.Success -> {
                            _jotItemUiState.update {
                                JotItemUiState(
                                    isLoading = false,
                                    isSuccessful = true,
                                    updatedJotItem = rezult.data
                                )
                            }
                        }

                        is Rezults.Error -> {
                            _jotItemUiState.update {
                                JotItemUiState(
                                    isLoading = false,
                                    isSuccessful = false,
                                    error = rezult.exception?.message
                                )
                            }
                        }
                    }
                }
        }
    }

    fun getSingleJotItem(
        userId: String,
        jotItemId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.getSingleJotItem(
                userId,
                jotItemId
            ).collectLatest { rezult ->
                when (rezult) {
                    is Rezults.Success -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = true,
                                singleJotItem = rezult.data
                            )
                        }
                    }

                    is Rezults.Error -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = false,
                                error = rezult.exception?.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun getAllJotItems(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.getAllJotItems(userId).collectLatest { rezult ->

                when (rezult) {
                    is Rezults.Success -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = true,
                                jotItemList = rezult.data
                            )
                        }
                    }

                    is Rezults.Error -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = false,
                                error = rezult.exception?.message
                            )
                        }
                    }
                }
            }
        }
    }

    //delete jot item
    fun deleteSingleJotItem(userId: String, jotItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSingleJotItem(userId, jotItemId).collectLatest { result ->
                when (result) {
                    is Rezults.Success -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = true,
                                jotItemList = result.data
                            )
                        }
                    }

                    is Rezults.Error -> {
                        _jotItemUiState.update {
                            JotItemUiState(
                                isLoading = false,
                                isSuccessful = false,
                                error = result.exception?.message
                            )
                        }
                    }
                }
            }
        }
    }

//    val allJotz: LiveData<List<Jotz>> = jotzDao.getItems().asLiveData()
//    private val _authUiState = MutableStateFlow(AuthUiState())
//    val authUiState = _authUiState.asStateFlow()

//    fun resetState() {
//        _authUiState.value = AuthUiState()
//    }

//    //call dao method to insert data to db
//    private fun insertJotz(jotz: Jotz) {
//        viewModelScope.launch {
//            jotzDao.insert(jotz)
//        }
//    }
//
//    //get new jotz details and define the item
//    private fun getNewJotzEntry(jotzTitle: String, jotzBody: String): Jotz {
//        return Jotz(
//            jotzTitle = jotzTitle.toString(),
//            jotzBody = jotzBody.toString()
//        )
//    }
//
//    private fun updateJotz(jotz: Jotz) {
//        viewModelScope.launch {
//            jotzDao.update(jotz)
//        }
//    }
//
//    //get updated Jotz
//    private fun getUpdatedJotzEntry(
//        jotzId: Int,
//        jotzTitle: String,
//        jotzBody: String
//    ): Jotz {
//        return Jotz(
//            id = jotzId,
//            jotzTitle = jotzTitle,
//            jotzBody = jotzBody
//        )
//    }
//
//    //add the new jotz to db using the insert method
//    fun addNewJotz(jotzTitle: String, jotzBody: String) {
//        val newJotz = getNewJotzEntry(jotzTitle, jotzBody)
//        insertJotz(newJotz)
//    }
//
//    //check if entry is valid(not blank)
//    fun isJotzEntryValid(jotzTitle: String, jotzBody: String): Boolean {
//        if (jotzTitle.isBlank() || jotzBody.isBlank()) {
//            return false
//        }
//        return true
//    }
//
//    //retrieve Jotz item using id
//    fun retrieveJotz(id: Int): LiveData<Jotz> {
//        return jotzDao.getItem(id).asLiveData()
//    }
//
//    //delete Jotz item using id
//    fun deleteJotz(jotz: Jotz) {
//        viewModelScope.launch {
//            jotzDao.delete(jotz)
//        }
//    }
//
//    //update the jotz entry
//    fun updateJotzEntry(
//        jotzId: Int,
//        jotzTitle: String,
//        jotzBody: String
//    ) {
//        val updatedJotz = getUpdatedJotzEntry(jotzId, jotzTitle, jotzBody)
//        updateJotz(updatedJotz)
//    }


}

//class JotzViewModelFactory(private val jotzDao: JotzDao) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(JotzViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return JotzViewModel(jotzDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}