package com.example.stripeflowcomponentbug

import android.app.Application
import com.stripe.android.PaymentConfiguration

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51IT5xuE3DVYz6xayAC2jdR863HcehUS7805HCTXYtJcOg3sgJVGVymzZUKS7r2TcgAJ6pJAXlXJTdkznGlBJwAf900TNHNwvuL"
        )
    }
}