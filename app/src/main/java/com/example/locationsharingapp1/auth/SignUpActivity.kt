package com.example.locationsharingapp1.auth

import android.content.Intent
import com.example.locationsharingapp1.databinding.ActivitySignUpBinding
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp1.viewModel.AuthenticationViewModel
import com.example.locationsharingapp1.viewModel.FireStoreViewModel
import com.example.locationsharingapp1.welcome.FriendListActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fireStoreViewModel: FireStoreViewModel
    private lateinit var authViewModel: AuthenticationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        fireStoreViewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()

            authViewModel.signUp(
                email,
                password,
                onSuccess = {
                    val displayName =
                        binding.etFirstName.text.toString() + " " + binding.etLastName.text.toString()
                    val location = "Location not available"
                    fireStoreViewModel.saveUser(
                        authViewModel.getCurrentUserId(),
                        displayName,
                        email,
                        location
                    )
                    startActivity(Intent(this, FriendListActivity::class.java))
                    finish()
                },
                onFailure = { errorMassage ->
                    Toast.makeText(this, errorMassage, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.tvGoToSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser != null) {
            startActivity(Intent(this, FriendListActivity::class.java))
            finish()
        }
    }
}
