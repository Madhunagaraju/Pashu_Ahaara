package com.pashuaahar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pashuaahar.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authState: StateFlow<FirebaseUser?> = authRepository.authStateChanges()
        .stateIn(viewModelScope, SharingStarted.Eagerly, authRepository.currentUser)

    fun signInAnonymously() {
        viewModelScope.launch {
            authRepository.signInAnonymously()
        }
    }
}
