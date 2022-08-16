package com.example.stripeflowcomponentbug

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stripeflowcomponentbug.domain.StripeViewModel
import com.example.stripeflowcomponentbug.ui.theme.StripeFlowComponentBugTheme
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.model.PaymentOption
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: StripeViewModel by viewModels()

    private var paymentSelected: MutableState<PaymentOption?> = mutableStateOf(null)
    private var ready: MutableState<Boolean> = mutableStateOf(false)
    private var response: MutableState<String> = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flowController = PaymentSheet.FlowController.create(
            this,
            { selected -> paymentSelected.value = selected },
            { result ->
                when (result) {
                    is PaymentSheetResult.Completed -> {
                        response.value = "SUCCESS"
                    }
                    is PaymentSheetResult.Failed -> {
                        response.value = result.error.message ?: ""
                    }
                    else -> {}
                }
            }
        )
        setContent {
            val scope = rememberCoroutineScope()
            fun setupStripeSdk(
                secretKey: String,
                customerId: String?,
                ephemeralKey: String?,
            ) {
                Log.e("KEY", secretKey)
                val customer =
                    if (!customerId.isNullOrBlank() && !ephemeralKey.isNullOrBlank()) {
                        PaymentSheet.CustomerConfiguration(customerId, ephemeralKey)
                    } else {
                        null
                    }
                flowController.configureWithSetupIntent(
                    setupIntentClientSecret = secretKey,
                    configuration = PaymentSheet.Configuration(
                        merchantDisplayName = "Doji",
                        customer = customer,
                        allowsDelayedPaymentMethods = false,
                    )
                ) { isReady, error ->
                    if (error != null) {
                        error.printStackTrace()
                        response.value = error.message ?: ""
                    } else if (isReady) {
                        ready.value = true
                        flowController.presentPaymentOptions()
                    }
                }
            }

            val state = viewModel.state.collectAsState(initial = StripeUIStates.Loading)
            LaunchedEffect(key1 = !ready.value, block = {
                viewModel.prepareScreen()
            })

            LaunchedEffect(key1 = (state.value is StripeUIStates.Success && !ready.value), block = {
                if (state.value !is StripeUIStates.Success) return@LaunchedEffect
                setupStripeSdk(
                    (state.value as StripeUIStates.Success).secretKey,
                    (state.value as StripeUIStates.Success).clientKey,
                    (state.value as StripeUIStates.Success).ephemeralKey
                )
            })

            StripeFlowComponentBugTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = response.value,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.onSurface
                            ),
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                        )
                        AnimatedVisibility(visible = paymentSelected.value != null) {
                            Row(Modifier.fillMaxWidth()) {
                                Image(
                                    imageVector = ImageVector.vectorResource(
                                        id = paymentSelected.value?.drawableResourceId
                                            ?: com.stripe.android.R.drawable.stripe_ic_visa
                                    ), contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .size(48.dp, 32.dp)
                                        .border(
                                            1.dp,
                                            Color(0XFFD9D9D9),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .clip(RoundedCornerShape(4.dp))
                                )
                                Text(
                                    text = paymentSelected.value?.label ?: "",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.onSurface
                                    ),
                                    modifier = Modifier
                                        .weight(1F)
                                        .padding(top = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }
                        Button(
                            enabled = ready.value,
                            onClick = { flowController.confirm() }) {
                            Text(
                                text = "Reserve",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(top = 4.dp),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}