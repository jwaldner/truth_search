package com.wfs.truthsearch.webserver

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import android.content.Context


class WebServerManager(private val cacheDir: File, private val context: Context) {
    private var server: LocalServer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val tag = "ServerManager" // Tag for logging

    fun startServer() {
        Thread {
            try {
                server = LocalServer(cacheDir, context).apply {
                    start()
                }
                Log.d(tag, "Server started successfully on port 8080")
                startPinging()
            } catch (e: Exception) {
                Log.e(tag, "Error starting server: ${e.message}")
            }
        }.start()
    }

    fun stopServer() {
        handler.removeCallbacksAndMessages(null) // Stop pings
        try {
            server?.stop()
            Log.d(tag, "Server stopped successfully")
        } catch (e: Exception) {
            Log.e(tag, "Error stopping server: ${e.message}")
        }
    }

    private fun startPinging() {
        val pingRunnable = object : Runnable {
            override fun run() {
                Thread {
                    try {
                        // Ping the server
                        val url = URL("http://127.0.0.1:8080/")
                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"
                            connectTimeout = 5000
                            readTimeout = 5000
                            inputStream.bufferedReader().use { it.readText() } // Optional: Read response
                        }
                        Log.d(tag, "Ping successful at ${System.currentTimeMillis()}")
                    } catch (e: Exception) {
                        Log.e(tag, "Ping failed: ${e.message}")
                    }
                }.start() // Run the ping logic on a separate thread
                handler.postDelayed(this, 30000) // Schedule the next ping
            }
        }
        handler.post(pingRunnable) // Start the first ping
    }

}