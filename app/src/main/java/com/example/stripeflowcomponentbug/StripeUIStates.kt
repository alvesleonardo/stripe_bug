package com.example.stripeflowcomponentbug

sealed class StripeUIStates {
    object Loading: StripeUIStates()
    data class Success(val secretKey: String, val clientKey: String?, val ephemeralKey: String?): StripeUIStates()
    data class Error(val error: Throwable): StripeUIStates()
}