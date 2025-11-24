package com.example.locationsharingapp1.viewModel

import androidx.lifecycle.ViewModel
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
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                // Handle failure
            }


    }

}