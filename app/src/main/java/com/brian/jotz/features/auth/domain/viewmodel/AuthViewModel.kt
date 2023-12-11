package com.brian.jotz.features.auth.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.jotz.data.repository.MainRepository
import com.brian.jotz.data.utils.Rezults
import com.brian.jotz.features.auth.domain.model.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    fun resetState() {
        _authUiState.update { AuthUiState() }
    }

    //login functionality, return user, or error
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO){
            if (email.isEmpty() || password.isEmpty()) {
                _authUiState.update {
                    AuthUiState(isLoading= false, isSuccessful = false, error = "Please fill all the fields")
                }
                return@launch
            }
            repository.signIn(email, password).collectLatest { rezult ->
                when(rezult){
                    is Rezults.Success -> {
                        _authUiState.update {
                            AuthUiState(isLoading = false, isSuccessful = true, userId = rezult.data.id)
                        }
                    }
                    is Rezults.Error -> {
                        _authUiState.update {
                            AuthUiState(isLoading = false, isSuccessful = false, error = rezult.exception?.message)
                        }
                    }

                }
            }
        }
    }

    fun signup(email: String, password: String, confirmPassword: String, name: String) {
        viewModelScope.launch(Dispatchers.IO){
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
                _authUiState.update {
                    AuthUiState(isLoading = false, isSuccessful = false, error = "Please fill all the fields")
                }
                return@launch
            }
            if (password != confirmPassword) {
                _authUiState.update {
                    AuthUiState(isLoading = false, isSuccessful = false, error = "Passwords do not match")
                }
                return@launch
            }
            repository.signUp(email, password, name).collectLatest { rezult ->
                when(rezult){
                    is Rezults.Success -> {
                        _authUiState.update {
                            AuthUiState(isLoading = false, isSuccessful = true, userId = rezult.data.id)
                        }
                    }
                    is Rezults.Error -> {
                        _authUiState.update {
                            AuthUiState(isLoading = false, isSuccessful = false, error = rezult.exception?.message)
                        }
                    }

                }
            }
        }
    }
}