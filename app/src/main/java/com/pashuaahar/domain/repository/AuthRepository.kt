package com.pashuaahar.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun authStateChanges(): Flow<FirebaseUser?>
    suspend fun signInAnonymously(): Result<Unit>
    suspend fun signInWithCredential(credential: AuthCredential): Result<Unit>
    suspend fun signOut()
}
