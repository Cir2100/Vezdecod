package com.kurilov.vezdecod

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // token expired
        }
    }

    companion object {
        lateinit var appContext: Context
    }
}