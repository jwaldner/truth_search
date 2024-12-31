package com.wfs.truthsearch.utils

object AppCloser {
    private var closeAppCallback: (() -> Unit)? = null

    fun setCloseAppCallback(callback: () -> Unit) {
        closeAppCallback = callback
    }

    fun closeApp() {
        closeAppCallback?.invoke()
    }
}
