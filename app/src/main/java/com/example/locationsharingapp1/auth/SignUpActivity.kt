package com.example.locationsharingapp1.auth

import com.example.locationsharingapp1.databinding.ActivitySignUpBinding

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationsharingapp1.welcome.FriendListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Go to Sign In
        binding.tvGoToSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Sign Up Button Click
        binding.btnSignUp.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val email = binding.etEmailSignUp.text.toString().trim()
            val password = binding.etPasswordSignUp.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInput(firstName, lastName, email, password, confirmPassword)) {
                createAccount(firstName, lastName, email, password)
            }
        }
    }

    // ---------------------------
    // VALIDATION
    // ---------------------------

    private fun validateInput(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPass: String
    ): Boolean {

        if (firstName.isEmpty()) {
            binding.etFirstName.error = "First name required"
            binding.etFirstName.requestFocus()
            return false
        }

        if (lastName.isEmpty()) {
            binding.etLastName.error = "Last name required"
            binding.etLastName.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            binding.etEmailSignUp.error = "Email required"
            binding.etEmailSignUp.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailSignUp.error = "Enter valid email"
            binding.etEmailSignUp.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.etPasswordSignUp.error = "Minimum 6 characters"
            binding.etPasswordSignUp.requestFocus()
            return false
        }

        if (password != confirmPass) {
            binding.etConfirmPassword.error = "Password doesn't match"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    // ---------------------------
    // FIREBASE SIGN UP
    // ---------------------------
    private fun createAccount(firstName: String, lastName: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->

                val userId = authResult.user!!.uid
                val fullName = "$firstName $lastName"

                val userMap = hashMapOf(
                    "userId" to userId,
                    "userEmail" to email,
                    "displayName" to fullName,
                    "latitude" to 0.0,    // later update by location
                    "longitude" to 0.0
                )

                db.collection("AppUsers")
                    .document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, FriendListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save user", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Sign Up Failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}
