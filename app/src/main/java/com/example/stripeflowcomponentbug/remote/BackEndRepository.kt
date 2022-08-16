package com.example.stripeflowcomponentbug.remote

import com.example.stripeflowcomponentbug.data.SetupIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackEndRepository @Inject constructor(
    private val backendApi: BackendApi
) {
    fun createSetupIntent(
        country: String,
        customerId: String? = null
    ): Flow<SetupIntent> {
        return flow {
            val setupIntent = backendApi.createSetupIntent(
                mutableMapOf("country" to country)
                    .plus(
                        customerId?.let {
                            mapOf("customer_id" to it)
                        }.orEmpty()
                    )
                    .toMutableMap()
            )
            emit(setupIntent)
        }
    }

    fun createEphemeralKey() = flow {
        emit(
            backendApi.createEphemeralKey(
                hashMapOf("api_version" to "2020-03-02", "customer_id" to "cus_LfEtBZkmUNexYc"),

            )
        )
    }
}