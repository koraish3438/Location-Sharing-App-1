package com.example.locationsharingapp1.viewModel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
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
        try {
            fusedLocationClient?.let { client ->
                val context = getApplication<Application>().applicationContext
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    callback("Location permission not granted")
                    return
                }

                client.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val lastLocation = task.result
                        val latitude = lastLocation.latitude
                        val longitude = lastLocation.longitude
                        callback("Lat: $latitude, Long: $longitude")
                    } else {
                        callback("Location not available")
                    }
                }
            } ?: callback("Location client not initialized")
        } catch (e: SecurityException) {
            callback("Location access denied: ${e.message}")
        }
    }

}
