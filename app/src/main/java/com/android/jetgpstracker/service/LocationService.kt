package com.android.jetgpstracker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LocationService : Service() {
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
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}