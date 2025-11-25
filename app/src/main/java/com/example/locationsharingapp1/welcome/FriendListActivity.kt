package com.example.locationsharingapp1.welcome

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.locationsharingapp1.R
import com.example.locationsharingapp1.adapter.UserAdapter
import com.example.locationsharingapp1.data.User
import com.example.locationsharingapp1.databinding.ActivityFriendListBinding
import com.example.locationsharingapp1.viewModel.AuthenticationViewModel
import com.example.locationsharingapp1.viewModel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class FriendListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendListBinding
    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: List<User>

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        isGranted ->
        if(isGranted.all { it.value }) {
            locationViewModel.startLocationUpdates()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_friend_list)
        
    }
}