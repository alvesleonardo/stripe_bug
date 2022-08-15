# Demonstration for stripe bug at flow controller.
The bug is reported at the ticket: https://github.com/stripe/stripe-android/issues/5372

- If the user uses the default payment method returned by the flow controller payment sheet(buy dismissing the dialog or selecting the same used in the last time) the confirm action will not attach a payment method to the payment intent. 