package com.example.stripeflowcomponentbug.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteInterfaces {

    @Provides
    fun providesService(remoteClient: RemoteClient): BackendApi =
        remoteClient.create(BackendApi::class.java)

}