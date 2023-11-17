package com.brian.jotz.data.repository

import com.brian.jotz.data.local.User
import com.brian.jotz.data.utils.Rezults
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : MainRepository {

    //sign in functionality, return user, or error
    override suspend fun signIn(email: String, password: String): Flow<Rezults<User>> {
        return callbackFlow {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this.trySend(
                            Rezults.Success(
                                User(
                                    firebaseAuth.currentUser!!.uid
                                )
                            )
                        ).isSuccess


                    } else {
                        this.trySend(
                            Rezults.Error(
                                "",
                                task.exception!!
                            )
                        ).isSuccess
                    }
                }
            awaitClose { this.cancel() }
        }
    }

    //sign up functionality, create user, add user to database, and return user, or error
    override suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): Flow<Rezults<User>> {
        return callbackFlow {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this.trySend(
                            Rezults.Success(
                                User(
                                    firebaseAuth.currentUser!!.uid
                                )
                            )
                        ).isSuccess

                    } else {
                        this.trySend(
                            Rezults.Error(
                                "",
                                task.exception!!
                            )
                        ).isSuccess
                    }
                }
            awaitClose { this.cancel() }
        }
    }

}