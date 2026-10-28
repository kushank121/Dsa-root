package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PointStatus
import com.example.model.VideoStatus
import com.example.ui.theme.*

@Composable
fun PointStatusBadge(
    status: PointStatus,
    modifier: Modifier = Modifier
) {
    val (bg, textCol, icon) = when (status) {
        PointStatus.VERIFIED -> Triple(StatusVerifiedBg, StatusVerified, Icons.Filled.CheckCircle)
        PointStatus.PENDING_CONFIGURATION -> Triple(StatusPendingBg, StatusPending, Icons.Filled.Schedule)
        PointStatus.NOT_IMPLEMENTED -> Triple(StatusNotImplementedBg, StatusNotImplemented, Icons.Filled.Cancel)
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status.label,
                tint = textCol,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.label,
                color = textCol,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VideoStatusBadge(
    status: VideoStatus,
    modifier: Modifier = Modifier
) {
    val (bg, textCol, icon) = when (status) {
        VideoStatus.APPROVED -> Triple(StatusVerifiedBg, StatusVerified, Icons.Filled.Verified)
        VideoStatus.PENDING_APPROVAL -> Triple(StatusPendingBg, StatusPending, Icons.Filled.HourglassTop)
        VideoStatus.REJECTED -> Triple(StatusRejectedBg, StatusRejected, Icons.Filled.ErrorOutline)
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status.label,
                tint = textCol,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.label,
                color = textCol,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocTopBar(
    currentPanelTitle: String,
    currentPanelBn: String,
    onOpenMasterDocs: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DocPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalHospital,
                        contentDescription = "Docroot Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Docroot",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DocPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "v1.0",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "$currentPanelTitle • $currentPanelBn",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onOpenMasterDocs,
                modifier = Modifier.testTag("btn_top_master_doc")
            ) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = DocSecondary) {
                            Text("12", fontSize = 9.sp)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = "Open Master Documentation",
                        tint = DocPrimary
                    )
                }
            }
        }
    )
}
