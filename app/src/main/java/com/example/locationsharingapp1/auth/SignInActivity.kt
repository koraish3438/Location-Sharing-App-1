package com.example.locationsharingapp1.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationsharingapp1.databinding.ActivitySignInBinding
import com.example.locationsharingapp1.welcome.FriendListActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        if (auth.currentUser != null) {
            startActivity(Intent(this, FriendListActivity::class.java))
            finish()
        }

        // Button Click: Sign In
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmailSignIn.text.toString().trim()
            val password = binding.etPasswordSignIn.text.toString().trim()

            if (validateInput(email, password)) {
                signInUser(email, password)
            }
        }

        // Text Click: Go to Sign Up
        binding.tvGoToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    // Input Validation
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmailSignIn.error = "Email required"
            binding.etEmailSignIn.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailSignIn.error = "Enter valid email"
            binding.etEmailSignIn.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.etPasswordSignIn.error = "Password required"
            binding.etPasswordSignIn.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.etPasswordSignIn.error = "Password should be at least 6 characters"
            binding.etPasswordSignIn.requestFocus()
            return false
        }

        return true
    }

    // Firebase Sign In
    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, FriendListActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
