package com.example.locationsharingapp1.welcome

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.google.android.material.appbar.MaterialToolbar
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
        else Toast.makeText(this, "Location permission is required!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize fusedLocationClient
        locationViewModel.initClient(LocationServices.getFusedLocationProviderClient(this))

        setupToolbarAndDrawer()
        setupRecyclerView()
        setupMapButton()
        checkLocationPermission()
    }

    private fun setupToolbarAndDrawer() {
        val toolbar: MaterialToolbar = binding.topToolbar
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
        fireStoreViewModel.getAllUser { users -> userAdapter.updateData(users) }
    }

    private fun setupMapButton() {
        binding.mapBtn.setOnClickListener { startActivity(Intent(this, MapsActivity::class.java)) }
    }

    private fun checkLocationPermission() {
        val fineLocationGranted = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted || !coarseLocationGranted) {
            locationPermissionRequest.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        } else getLocation()
    }

    private fun getLocation() {
        locationViewModel.getLastLocation { locationString ->
            if (locationString.startsWith("Lat:")) {
                val parts = locationString.replace("Lat: ", "").replace("Long: ", "").split(",")
                val lat = parts[0].toDoubleOrNull()
                val lng = parts[1].toDoubleOrNull()
                if (lat != null && lng != null) {
                    authViewModel.getCurrentUserId()?.let { userId ->
                        fireStoreViewModel.updateUserLocation(userId, "Lat: $lat, Long: $lng")
                    }
                }
            } else {
                Toast.makeText(this, locationString, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

}
