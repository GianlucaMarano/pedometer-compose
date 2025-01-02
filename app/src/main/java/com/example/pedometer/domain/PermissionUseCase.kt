package com.example.pedometer.domain

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.permission.HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_HISTORY
import androidx.health.connect.client.records.StepsRecord
import javax.inject.Inject

class PermissionsUseCase @Inject constructor(
    private val healthConnectClient: HealthConnectClient
) {

    suspend fun checkPermissions(): Boolean {
        val grantedPermissions = healthConnectClient.permissionController.getGrantedPermissions()
        val requiredPermissions = setOf(
            PERMISSION_READ_HEALTH_DATA_HISTORY,
            HealthPermission.getReadPermission(StepsRecord::class),
        )
        return grantedPermissions.containsAll(requiredPermissions)
    }
}