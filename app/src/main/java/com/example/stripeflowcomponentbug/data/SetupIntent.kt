package com.example.stripeflowcomponentbug.data

data class SetupIntent(
    val intent: String,
    val secret: String,
    val status: String
)