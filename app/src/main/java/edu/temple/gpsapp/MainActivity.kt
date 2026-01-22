package edu.temple.gpsapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    var previousLocation: Location? = null

    lateinit var mapView : MapView
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)


        locationManager = getSystemService(LocationManager::class.java)

        locationListener = object: LocationListener {
            override fun onLocationChanged(location: Location) {

                previousLocation?.run {
                    val distance = this.distanceTo(location)
                    val timeDif = (location.time - this.time) / 1000.0
                    Log.d("Time dif", timeDif.toString())
                    if (distance > 1) {
                        val calcSpeed = distance / timeDif
                        Log.d("Speed", "Reported: ${location.speed} - Calculated: ${calcSpeed}")
                    }
                }

                previousLocation = location
            }
        }

        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            123
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)


    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        locationManager.removeUpdates(locationListener)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }



    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }


}