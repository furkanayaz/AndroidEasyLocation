package com.fa.usinglocation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.fa.usinglocation.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activityMainObject = this

        inits()

    }

    fun inits() {
        binding.latitude = "Latitude"
        binding.longitude = "Longitude"
        flpc = LocationServices.getFusedLocationProviderClient(this)
    }

    fun getLocation() {
        checkLocation()
    }

    fun checkLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocation(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }else {
                //toSettings() method runs phone location
                getLocationInformation()
            }

        }else {
            TODO("If android version is lower than 'M' it will not work.")
        }
    }

    @SuppressLint("NewApi")
    fun getLocationInformation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationTask = flpc.lastLocation
            locationTask.addOnSuccessListener {
                if (it != null) {
                    binding.latitude = "Latitude: ${it.latitude}"
                    binding.longitude = "Longitude: ${it.longitude}"
                }else {
                    binding.latitude = "Latitude access denied."
                    binding.longitude = "Longitude access denied."
                    Snackbar.make(binding.btnLocation, "Please turn on phone location services.", Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setAction("TURN ON", {toSettings()}).show()
                }
            }
        }
    }

    fun requestLocation(permission: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(permission), 100)
        }else {
            TODO("If android version is lower than 'M' it will not work.")
        }
    }

    fun toSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}