package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.model.Doctor
import com.example.model.HealthVideo

/**
 * Doctor Directory Home Screen / User Panel
 * Completely replaced with the modern reference design.
 */
@Composable
fun UserPanelScreen(
    doctors: List<Doctor> = emptyList(),
    approvedVideos: List<HealthVideo> = emptyList(),
    onOpenMasterDocs: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    HomeScreen(modifier = modifier)
}
