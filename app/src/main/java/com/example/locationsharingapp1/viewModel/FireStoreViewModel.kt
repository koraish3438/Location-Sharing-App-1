package com.example.locationsharingapp1.viewModel

import androidx.lifecycle.ViewModel
import com.example.locationsharingapp1.data.User
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun saveUser(userId: String, displayName: String, email: String, location: String) {
        val user = hashMapOf(
            "userId" to userId,
            "displayName" to displayName,
            "email" to email,
            "location" to location
        )

        usersCollection.document(userId).set(user)
            .addOnSuccessListener { }
            .addOnFailureListener {e->}
    }

    fun getAllUser(callback: (List<User>) -> Unit) {
        usersCollection.get()
            .addOnSuccessListener { result ->
                val userList = mutableListOf<User>()
                for (document in result) {
                    val userId = document.id
                    val displayName = document.getString("displayName")?: ""
                    val email = document.getString("email")?: ""
                    val location = document.getString("location")?: ""

                    userList.add(User(userId, displayName, email, location))
                }
                callback(userList)
            }
            .addOnFailureListener { e ->
                callback(emptyList())
            }
    }

    fun updateUser(userId: String,displayName: String, email: String, location: String?) {
        val user = hashMapOf(
            "displayName" to displayName,
            "location" to location
        )

        val userMap = user.toMap()
        usersCollection.document(userId).update(userMap)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    fun updateUserLocation(userId: String, location: String) {
        if(userId.isEmpty()) {
            return
        }
        val user = hashMapOf(
            "location" to location
        )
        val userMap = user.toMap()
        usersCollection.document(userId).update(userMap)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    fun getUser(userId: String, callback: (User?) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getUserLocation(userId: String, callback: (String) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val location = documentSnapshot.getString("location") ?: ""
                callback(location)
            }
            .addOnFailureListener {
                callback("")
            }
    }
}