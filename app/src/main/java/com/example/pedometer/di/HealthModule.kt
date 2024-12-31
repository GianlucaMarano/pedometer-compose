package com.example.pedometer.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Importa le classi dei tuoi repository, use case e data source
import com.example.pedometer.data.HealthDataSource
import com.example.pedometer.data.HealthRepository
import com.example.pedometer.feature.home.domain.ReadStepsUseCase
import com.example.pedometer.domain.WriteStepsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object HealthModule {

    // Fornisce il client di Health Connect
    @Provides
    @Singleton
    fun provideHealthConnectClient(@ApplicationContext context: Context): HealthConnectClient {
        return HealthConnectClient.getOrCreate(context)
    }

    // Fornisce il data source
    @Provides
    @Singleton
    fun provideHealthDataSource(
        healthConnectClient: HealthConnectClient
    ): HealthDataSource {
        return HealthDataSource(healthConnectClient)
    }

    // Fornisce il repository
    @Provides
    @Singleton
    fun provideHealthRepository(
        healthDataSource: HealthDataSource
    ): HealthRepository {
        return HealthRepository(healthDataSource)
    }

    // Fornisce il UseCase per leggere i passi
    @Provides
    @Singleton
    fun provideReadStepsUseCase(
        healthRepository: HealthRepository
    ): ReadStepsUseCase {
        return ReadStepsUseCase(healthRepository)
    }

    // Fornisce il UseCase per scrivere i passi
    @Provides
    @Singleton
    fun provideWriteStepsUseCase(
        healthRepository: HealthRepository
    ): WriteStepsUseCase {
        return WriteStepsUseCase(healthRepository)
    }
}