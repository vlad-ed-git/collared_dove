package com.dev_vlad.collared_doves.utils

import android.util.Log

object MyLogger {
    fun logThis(tag: String, location: String, message: String, exception: Exception? = null) {
        Log.d(tag, "@ $location says $message", exception)
    }
}