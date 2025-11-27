package com.example.locationsharingapp1.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp1.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.locationsharingapp1.databinding.ActivityMapsBinding
import com.example.locationsharingapp1.viewModel.FireStoreViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fireStoreViewModel: FireStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireStoreViewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Add a marker in Sydney and move the camera
        fireStoreViewModel.getAllUser { userList ->
            for(user in userList) {
                val userLocation = user.location
                if(userLocation.isNotEmpty()) {
                    val latLng = parseLocation(userLocation)
                    val markerOption = MarkerOptions().position(latLng).title("${user.displayName}\n${user.email}")
                    googleMap.addMarker(markerOption)
                }
            }
        }
    }

    private fun parseLocation(location: String): LatLng {
        val latLngSplit = location.split(",")
        val latitude = latLngSplit[0].substringAfter("Lat: ").toDouble()
        val logitude = latLngSplit[1].substringAfter("Long: ").toDouble()

        return LatLng(latitude, logitude)
    }

}
