package com.example.locationsharingapp1.viewModel

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener

class LocationViewModel(application: Application): AndroidViewModel(application) {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun initClient(client: FusedLocationProviderClient) {
        fusedLocationClient = client
    }

    fun getLastLocation(callback: (String) -> Unit) {
        val context = getApplication<Application>()

        // Check permission first
        val hasFine = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            callback("Permission not granted")
            return
        }

        fusedLocationClient?.lastLocation
            ?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val lastLocation = task.result
                    val latitude = lastLocation.latitude
                    val longitude = lastLocation.longitude
                    callback("Lat: $latitude, Long: $longitude")
                } else {
                    callback("Location not available")
                }
            })
    }
}
