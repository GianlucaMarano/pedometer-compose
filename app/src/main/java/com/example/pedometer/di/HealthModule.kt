package com.example.pedometer.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.pedometer.data.network.HealthDataSource
import com.example.pedometer.data.HealthRepository
import com.example.pedometer.feature.home.domain.ReadStepsUseCase
import com.example.pedometer.domain.WriteStepsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object HealthModule {

    @Provides
    @Singleton
    fun provideHealthConnectClient(@ApplicationContext context: Context): HealthConnectClient {
        return HealthConnectClient.getOrCreate(context)
    }

    @Provides
    @Singleton
    fun provideHealthDataSource(
        healthConnectClient: HealthConnectClient
    ): HealthDataSource {
        return HealthDataSource(healthConnectClient)
    }

    @Provides
    @Singleton
    fun provideHealthRepository(
        healthDataSource: HealthDataSource
    ): HealthRepository {
        return HealthRepository(healthDataSource)
    }

    @Provides
    @Singleton
    fun provideReadStepsUseCase(
        healthRepository: HealthRepository
    ): ReadStepsUseCase {
        return ReadStepsUseCase(healthRepository)
    }

    @Provides
    @Singleton
    fun provideWriteStepsUseCase(
        healthRepository: HealthRepository
    ): WriteStepsUseCase {
        return WriteStepsUseCase(healthRepository)
    }
}