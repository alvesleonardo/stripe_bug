package com.example.stripeflowcomponentbug.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stripeflowcomponentbug.StripeUIStates
import com.example.stripeflowcomponentbug.contants.Keys
import com.example.stripeflowcomponentbug.remote.BackEndRepository
import com.example.stripeflowcomponentbug.remote.BackendApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StripeViewModel @Inject constructor(
    private val repository: BackEndRepository
) : ViewModel() {
    private val _state = MutableStateFlow<StripeUIStates>(StripeUIStates.Loading)
    val state: StateFlow<StripeUIStates> = _state

    @OptIn(FlowPreview::class)
    fun prepareScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            val flow = repository.createEphemeralKey().flatMapConcat { eKey->
                repository.createSetupIntent("us", eKey.associated_objects.first { it.type == "customer" }.id)
                    .map {
                        StripeUIStates.Success(it.secret, eKey.associated_objects.first { it.type == "customer" }.id, eKey.secret)
                    }
            }.catch { it.printStackTrace() }
            flow.collect {
                withContext(Dispatchers.Main) {
                    _state.value = StripeUIStates.Success(it.secretKey, it.clientKey , it.ephemeralKey)
                }
            }
        }
    }
}