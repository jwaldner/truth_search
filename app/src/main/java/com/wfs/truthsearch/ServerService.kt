package com.wfs.truthsearch

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.wfs.truthsearch.webserver.WebServerManager
import java.io.File

class ServerService : Service() {

    private var webServerManager: WebServerManager? = null
    private val tag = "ServerService"

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "Service created")

        // Start the server
        val cacheDir = applicationContext.cacheDir
        webServerManager = WebServerManager(cacheDir, applicationContext)
        webServerManager?.startServer()

        // Start the foreground notification
        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "ServerServiceChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Web Server Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Web Server Running")
            .setContentText("The web server is active and running.")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "Service destroyed")
        webServerManager?.stopServer()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
