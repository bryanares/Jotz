package com.brian.jotz.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.brian.jotz.data.local.JotItem
import com.brian.jotz.data.local.User
import com.brian.jotz.data.utils.JotFirebaseDocument
import com.brian.jotz.data.utils.Rezults
import com.brian.jotz.data.utils.toLong
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDateTime
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val jotFirebaseFirestore: FirebaseFirestore
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addOrUpdateJotz(
        userId: String,
        jotItemId: String?,
        jotItem: JotItem
    ): Flow<Rezults<JotItem>> = callbackFlow {

        //create unique id
        val fileJotItemId = jotItemId ?: createUniqueID(
            userId,
            LocalDateTime.now().toLong(), "jotz"
        )
        //update item created time, if it is a new item, create unique id and assign it
        jotItem.updatedTime = LocalDateTime.now().toLong()
        if (jotItemId == null)
            jotItem.createdTime = jotItem.updatedTime

        //perform the update or addition of item to firestore
        jotFirebaseFirestore.collection(userId).document(JotFirebaseDocument.JOTZ)
            .collection(JotFirebaseDocument.JOTZ)
            .document(fileJotItemId).set(jotItem)
            .addOnSuccessListener { documentReference ->

                var newJotItem = jotItem.copy(id = fileJotItemId)

                this.trySend(
                    Rezults.Success(
                        newJotItem
                    )
                ).isSuccess
            }
            .addOnFailureListener { e ->
                this.trySend(
                    Rezults.Error(
                        "",
                        e
                    )
                ).isSuccess
            }
        awaitClose { this.cancel() }
    }

    override suspend fun getAllJotItems(userId: String): Flow<Rezults<List<JotItem>>> =
        callbackFlow {
            //get all items in the firestore collection
            jotFirebaseFirestore.collection(userId).document(JotFirebaseDocument.JOTZ)
                .collection(JotFirebaseDocument.JOTZ).get()
                .addOnSuccessListener { documentReference ->
                    //create an instance of list of JotItems

                    var jotItemList = mutableListOf<JotItem>()

                    //loop through the documents in the collection, get them via id and add them to the JotItemList
                    for (document in documentReference.documents) {
                        var jotItem = document.toObject(JotItem::class.java)?.copy(
                            id = document.id
                        )
                        jotItemList.add(jotItem!!)
                    }
                    this.trySend(
                        Rezults.Success(
                            jotItemList
                        )
                    )
                }
            awaitClose { this.cancel() }
        }

    override suspend fun getSingleJotItem(
        userId: String,
        jotItemId: String
    ): Flow<Rezults<JotItem>> = callbackFlow {
        jotFirebaseFirestore.collection(userId).document(JotFirebaseDocument.JOTZ)
            .collection(JotFirebaseDocument.JOTZ)
            .document(jotItemId).get()
            //null pointer exemption from here that I don't understand
            .addOnSuccessListener { documentReference ->
                var jotItem = documentReference.toObject(JotItem::class.java)?.copy(
                    id = documentReference.id
                )
                Log.d("Tag", "${documentReference.id}")
                Log.d("Tag", "${userId}")
                Log.d("Tag", "${jotItemId}")
                this.trySend(
                    Rezults.Success(
                        jotItem!!
                    )
                ).isSuccess
            }
            .addOnFailureListener { e ->
                this.trySend(
                    Rezults.Error(
                        "",
                        e
                    )
                ).isSuccess
            }
        awaitClose { this.cancel() }
    }


    //create unique Id using userId, timesStamp, key and key2 variables
    private fun createUniqueID(
        userId: String,
        timeStamp: Long,
        key: String,
        key2: String? = null
    ): String {
        return if (key2 != null) {
            "$userId$timeStamp$key$key2"
        } else {
            "$userId$timeStamp$key"
        }
    }

}