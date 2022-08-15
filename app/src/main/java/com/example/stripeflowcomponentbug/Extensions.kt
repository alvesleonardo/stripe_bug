package com.example.stripeflowcomponentbug

enum class SnackbarStatus(val background: Int) {
    SUCCESS(com.stripe.android.paymentsheet.R.color.stripe_paymentsheet_primary_button_success_background),
    ERROR(androidx.appcompat.R.color.error_color_material_light),
    WARNING(androidx.appcompat.R.color.error_color_material_dark),
    NEUTRAL(R.color.black)
}