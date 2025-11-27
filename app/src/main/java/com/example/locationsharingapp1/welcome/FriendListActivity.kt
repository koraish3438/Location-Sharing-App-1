package com.example.locationsharingapp1.welcome

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationsharingapp1.R
import com.example.locationsharingapp1.adapter.UserAdapter
import com.example.locationsharingapp1.auth.SignInActivity
import com.example.locationsharingapp1.databinding.ActivityFriendListBinding
import com.example.locationsharingapp1.map.MapsActivity
import com.example.locationsharingapp1.ui.ProfileActivity
import com.example.locationsharingapp1.viewModel.AuthenticationViewModel
import com.example.locationsharingapp1.viewModel.FireStoreViewModel
import com.example.locationsharingapp1.viewModel.LocationViewModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class FriendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var userAdapter: UserAdapter

    private val authViewModel: AuthenticationViewModel by viewModels()
    private val fireStoreViewModel: FireStoreViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) getLocation()
        else Toast.makeText(this, "Permission Required!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init fusedLocationClient in ViewModel
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationViewModel.initClient(fusedLocationClient)

        setupDrawer()
        setupRecyclerView()
        checkLocationPermission()
    }

    private fun setupDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_profile -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.menu_map -> startActivity(Intent(this, MapsActivity::class.java))
                R.id.menu_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList())
        binding.rvFriendList.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@FriendListActivity)
        }

        fireStoreViewModel.getAllUser { users ->
            userAdapter.updateData(users)
        }
    }

    private fun checkLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getLocation() {
        locationViewModel.getLastLocation { location ->
            val userId = authViewModel.getCurrentUserId()
            if (userId.isNotEmpty()) {
                fireStoreViewModel.updateUserLocation(userId, location)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }
}
