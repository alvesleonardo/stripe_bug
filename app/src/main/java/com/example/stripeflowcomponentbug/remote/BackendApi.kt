package com.example.stripeflowcomponentbug.remote

import com.example.stripeflowcomponentbug.data.EphemeralKey
import com.example.stripeflowcomponentbug.data.SetupIntent
import okhttp3.ResponseBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BackendApi {

    @FormUrlEncoded
    @POST("ephemeral_keys")
    suspend fun createEphemeralKey(@FieldMap apiVersionMap: HashMap<String, String>): EphemeralKey

    @FormUrlEncoded
    @POST("create_payment_intent")
    suspend fun createPaymentIntent(@FieldMap params: MutableMap<String, String>): ResponseBody

    @FormUrlEncoded
    @POST("create_setup_intent")
    suspend fun createSetupIntent(@FieldMap params: MutableMap<String, String>): SetupIntent
}
