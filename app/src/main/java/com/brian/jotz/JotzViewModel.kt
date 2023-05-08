package com.brian.jotz

import androidx.lifecycle.*
import com.brian.jotz.data.Jotz
import com.brian.jotz.data.JotzDao
import kotlinx.coroutines.launch

class JotzViewModel(private val jotzDao: JotzDao) : ViewModel() {

    val allJotz: LiveData<List<Jotz>> = jotzDao.getItems().asLiveData()

    //call dao method to insert data to db
    private fun insertJotz(jotz: Jotz) {
        viewModelScope.launch {
            jotzDao.insert(jotz)
        }
    }

    //get new jotz details and define the item
    private fun getNewJotzEntry(jotzTitle: String, jotzBody: String): Jotz {
        return Jotz(
            jotzTitle = jotzTitle.toString(),
            jotzBody = jotzBody.toString()
        )
    }

    private fun updateJotz(jotz: Jotz) {
        viewModelScope.launch {
            jotzDao.update(jotz)
        }
    }

    //get updated Jotz
    private fun getUpdatedJotzEntry(
        jotzId: Int,
        jotzTitle: String,
        jotzBody: String
    ): Jotz {
        return Jotz(
            id = jotzId,
            jotzTitle = jotzTitle,
            jotzBody = jotzBody
        )
    }

    //add the new jotz to db using the insert method
    fun addNewJotz(jotzTitle: String, jotzBody: String) {
        val newJotz = getNewJotzEntry(jotzTitle, jotzBody)
        insertJotz(newJotz)
    }

    //check if entry is valid(not blank)
    fun isJotzEntryValid(jotzTitle: String, jotzBody: String): Boolean {
        if (jotzTitle.isBlank() || jotzBody.isBlank()) {
            return false
        }
        return true
    }

    //retrieve Jotz item using id
    fun retrieveJotz(id: Int): LiveData<Jotz> {
        return jotzDao.getItem(id).asLiveData()
    }

    //delete Jotz item using id
    fun deleteJotz(jotz: Jotz) {
        viewModelScope.launch {
            jotzDao.delete(jotz)
        }
    }

    //update the jotz entry
    fun updateJotzEntry(
        jotzId: Int,
        jotzTitle: String,
        jotzBody: String
    ) {
        val updatedJotz = getUpdatedJotzEntry(jotzId, jotzTitle, jotzBody)
        updateJotz(updatedJotz)
    }


}

class JotzViewModelFactory(private val jotzDao: JotzDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JotzViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JotzViewModel(jotzDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}