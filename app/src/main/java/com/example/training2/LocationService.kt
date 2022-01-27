package com.example.training2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*
import android.R

import android.view.LayoutInflater
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class LocationService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "LOCATION"
    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_media_play)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timer = Timer()
        var k = 0
        val refreshTime = intent.getIntExtra("refresh_time", 1000)
        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                    mLocation = location
                    mLocation?.let {
                        Log.d(TAG, "onLocationChanged: Latitude ${it.latitude} , Longitude ${it.longitude}")
                        if (isServiceStarted){
                            k+=1
                            val BROADCAST_ACTION = "AFFICHER";
                            val intent1 = Intent(BROADCAST_ACTION)
                            val str = "Current location :  " + it.latitude + "," + it.longitude + "\nRefresh time : "+refreshTime+"ms"
                            //Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
                            intent1.putExtra("text", str)
                            intent1.putExtra("action", "AFFICHER")
                            intent1.putExtra("location", it.toString())
                            LocalBroadcastManager.getInstance(applicationContext)
                                .sendBroadcast(intent1)
                            Log.d("AFFICHER", "Ca devrait afficher")
                        } else
                            k = 0
                    }
                }
            }, refreshTime)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false
        stopSelf()

    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }
}