package com.example.stripeflowcomponentbug

import android.app.Application
import com.example.stripeflowcomponentbug.contants.Keys
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            Keys.STIPE_KEY
        )
    }
}