package com.wfs.truthsearch
import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyApplication : Application(), LifecycleObserver {

    val tag = "MyApp"

    var isAppInForeground = false
        private set

    var browserResetListener: (() -> Unit)? = null

    override fun onCreate() {
        super.onCreate()

        // Add this class as an observer to the app's lifecycle
        Log.d("IntentDebug", "MyApplication onCreate: Application started")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        isAppInForeground = true
        // App enters the foreground
        Log.d(tag, "IS IN foreground" )
        browserResetListener?.invoke() // Notify the activity
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        isAppInForeground = false
        Log.d(tag, "IS IN background" )
        // App enters the background
    }

}
