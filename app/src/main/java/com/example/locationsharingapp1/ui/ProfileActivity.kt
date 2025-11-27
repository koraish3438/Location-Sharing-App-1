package com.example.locationsharingapp1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp1.auth.SignInActivity
import com.example.locationsharingapp1.databinding.ActivityProfileBinding
import com.example.locationsharingapp1.viewModel.AuthenticationViewModel
import com.example.locationsharingapp1.viewModel.FireStoreViewModel
import com.example.locationsharingapp1.welcome.FriendListActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var fireStoreViewModel: FireStoreViewModel
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModels
        authViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        fireStoreViewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]

        // Update button
        binding.btnUpdateProfile.setOnClickListener {
            val newName = binding.etProfileName.text.toString()
            val newEmail = binding.etProfileEmail.text.toString()
            updateProfile(newName, newEmail)
        }

        // Home & Logout
        binding.ivHome.setOnClickListener {
            startActivity(Intent(this, FriendListActivity::class.java))
            finish()
        }

        binding.ivLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        loadUserInfo()
    }

    private fun updateProfile(newName: String, newEmail: String) {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            fireStoreViewModel.updateUser(userId, newName, newEmail, null)
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserInfo() {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            binding.etProfileEmail.setText(firebaseAuth.currentUser?.email)
            fireStoreViewModel.getUser(currentUser.uid) { user ->
                if (user != null) {
                    binding.etProfileName.setText(user.displayName)
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
}
