package com.example.locationsharingapp1.auth

import com.example.locationsharingapp1.databinding.ActivitySignUpBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp1.viewModel.AuthenticationViewModel
import com.example.locationsharingapp1.viewModel.FireStoreViewModel

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
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()


        }
    }
}
