package com.example.stripeflowcomponentbug.data

data class EphemeralKey(
    val associated_objects: List<AssociatedObject>,
    val created: Int,
    val expires: Int,
    val id: String,
    val livemode: Boolean,
    val secret: String
)