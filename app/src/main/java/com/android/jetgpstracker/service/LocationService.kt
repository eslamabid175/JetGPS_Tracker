package com.android.jetgpstracker.service

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.jetgpstracker.CHANNEL_ID
import com.android.jetgpstracker.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationService : Service() {
// by lazy{} is used to initialize the variable only when it is first used and not when it is defined
    // LocationRequest is used to get the location
    private val locationRequest by lazy{
    LocationRequest.Builder(10000).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setIntervalMillis(10000).build()
}
    private val locationCallback by lazy {
        object : LocationCallback(){
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }
            override fun onLocationResult(location: LocationResult) {
                // val lat is used to get the latitude
val lat=location.lastLocation?.latitude.toString()
                val lng=location.lastLocation?.longitude.toString()
                StartForgroundService(lat,lng)
            }
        }
    }

    // lifeCycle of service

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        //for bounded service only
        return null
    }

    // this fun is for starting service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
LocationUpdates()
        // START_STICKY this flag is meaning that if the service is killed by the system, it will be restarted
        // and start not sticky is meaning that if the service is killed by the system, it will not be restarted
        // and start redeliver intent is meaning that if the service is killed by the system, it will be restarted
        // and the last intent will be delivered

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun LocationUpdates(){
        //for getting location
        val fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback,null
        )
    }
    private fun StartForgroundService(lat:String,lng:String){
        val notification=NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("LocationUpdates")
            .setContentText("$lat - $lng")
            .build()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this,POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED){
startForeground(1,notification)
            }
        }else {
            startForeground(1,notification)

        }
    }
}