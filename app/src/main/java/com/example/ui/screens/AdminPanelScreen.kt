package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocrootRepository
import com.example.model.AuditLog
import com.example.model.HealthVideo
import com.example.model.VideoStatus
import com.example.ui.components.VideoStatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    videos: List<HealthVideo>,
    totalDoctorsCount: Int,
    auditLogs: List<AuditLog>,
    onStateChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeAdminTab by remember { mutableIntStateOf(0) }
    var videoToReject by remember { mutableStateOf<HealthVideo?>(null) }
    var rejectionReasonInput by remember { mutableStateOf("") }

    var directTitle by remember { mutableStateOf("") }
    var directCategory by remember { mutableStateOf("Cardiology") }
    var directUrl by remember { mutableStateOf("") }
    var directDescription by remember { mutableStateOf("") }
    var directSuccessMessage by remember { mutableStateOf<String?>(null) }

    val pendingVideos = remember(videos) { videos.filter { it.status == VideoStatus.PENDING_APPROVAL } }
    val approvedVideos = remember(videos) { videos.filter { it.status == VideoStatus.APPROVED } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "অ্যাডমিন অনুমোদন ও কন্ট্রোল সেন্টার",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "POINT 03: PANEL-ADMIN-001 • মাস্টার পলিসি অথরিটি",
                                fontSize = 11.sp,
                                color = DocPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusPendingBg
                        ) {
                            Text(
                                text = "Admin Root Access",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusPending,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(
                            label = "পেন্ডিং রিভিউ",
                            value = pendingVideos.size.toString(),
                            accent = StatusPending,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "অনুমোদিত ভিডিও",
                            value = approvedVideos.size.toString(),
                            accent = StatusVerified,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "মোট ডাক্তার",
                            value = totalDoctorsCount.toString(),
                            accent = DocSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    PrimaryTabRow(selectedTabIndex = activeAdminTab) {
                        Tab(
                            selected = activeAdminTab == 0,
                            onClick = { activeAdminTab = 0 },
                            text = { Text("অনুমোদন কিউ (${pendingVideos.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            icon = {
                                BadgedBox(badge = { if (pendingVideos.isNotEmpty()) Badge { Text(pendingVideos.size.toString()) } }) {
                                    Icon(Icons.Filled.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            },
                            modifier = Modifier.testTag("tab_adm_queue")
                        )
                        Tab(
                            selected = activeAdminTab == 1,
                            onClick = { activeAdminTab = 1 },
                            text = { Text("ডিরেক্ট পাবলিশার", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.Publish, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_adm_direct")
                        )
                        Tab(
                            selected = activeAdminTab == 2,
                            onClick = { activeAdminTab = 2 },
                            text = { Text("অডিট লগ (${auditLogs.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.History, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_adm_audit")
                        )
                    }
                }
            }

            when (activeAdminTab) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        if (pendingVideos.isEmpty()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(Icons.Filled.TaskAlt, contentDescription = null, tint = StatusVerified, modifier = Modifier.size(44.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("সকল ভিডিও পর্যালোচিত!", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("এই মুহূর্তে কোনো কাস্টমার ভিডিও অনুমোদনের অপেক্ষায় নেই।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        } else {
                            items(pendingVideos, key = { it.id }) { video ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Filled.HourglassTop, contentDescription = null, tint = StatusPending, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(video.category, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DocPrimary)
                                            }
                                            VideoStatusBadge(status = video.status)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(video.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(video.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text("আপলোডকারী: ${video.uploaderName} (${video.uploaderId})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                                Text("স্টোরেজ ফাইল: ${video.videoUrl}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    videoToReject = video
                                                    rejectionReasonInput = ""
                                                },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRejected),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .testTag("BTN-ADM-REJECT-${video.id}")
                                            ) {
                                                Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("বাতিল (Reject)", fontSize = 12.sp)
                                            }

                                            Button(
                                                onClick = {
                                                    DocrootRepository.approveVideo(video.id)
                                                    onStateChanged()
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = StatusVerified),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .weight(1.3f)
                                                    .testTag("BTN-ADM-APPROVE-${video.id}")
                                            ) {
                                                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("অনুমোদন দিন (Approve)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Surface(
                                color = DocPrimaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(Icons.Filled.Publish, contentDescription = null, tint = DocPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "অ্যাডমিন ডিরেক্ট পাবলিশার (Rule VIDEO-APPROVAL-001 Point 7-8)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = DocOnPrimaryContainer
                                        )
                                        Text(
                                            text = "অ্যাডমিন প্যানেল থেকে সরাসরি আপলোড করা ভিডিও অতিরিক্ত অনুমোদন ছাড়াই সরাসরি APPROVED হয়ে User Panel ভিডিও বক্সে প্রকাশিত হয়।",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        directSuccessMessage?.let { msg ->
                            item {
                                Surface(
                                    color = StatusVerifiedBg,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = msg,
                                        fontSize = 12.sp,
                                        color = StatusVerified,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = directTitle,
                                onValueChange = { directTitle = it },
                                label = { Text("ভিডিওর শিরোনাম (Official Medical Reel)") },
                                placeholder = { Text("যেমন: ডেঙ্গু জ্বরে প্লেটলেট কমলে করণীয়") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = directCategory,
                                onValueChange = { directCategory = it },
                                label = { Text("মেডিকেল ক্যাটাগরি") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = directUrl,
                                onValueChange = { directUrl = it },
                                label = { Text("স্টোরেজ ফাইল পাথ / ভিডিও লিঙ্ক") },
                                placeholder = { Text("https://storage.googleapis.com/docroot-videos/dengue_care.mp4") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = directDescription,
                                onValueChange = { directDescription = it },
                                label = { Text("স্বাস্থ্য নির্দেশিকা ও তথ্য বিবরণ") },
                                placeholder = { Text("অফিসিয়াল মেডিকেল বোর্ডের দিকনির্দেশনা...") },
                                minLines = 3,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    if (directTitle.isNotBlank()) {
                                        val url = if (directUrl.isBlank()) "https://storage.googleapis.com/docroot-videos/admin_${System.currentTimeMillis()}.mp4" else directUrl
                                        DocrootRepository.publishAdminDirectVideo(
                                            title = directTitle,
                                            category = directCategory,
                                            videoUrl = url,
                                            description = directDescription.ifBlank { "মেডিকেল বোর্ডের অফিসিয়াল স্বাস্থ্য বার্তা।" }
                                        )
                                        directSuccessMessage = "সরাসরি সফলভাবে প্রকাশিত হয়েছে! ভিডিওটি তাৎক্ষণিকভাবে User Panel-এ দৃশ্যমান।"
                                        directTitle = ""
                                        directUrl = ""
                                        directDescription = ""
                                        onStateChanged()
                                    }
                                },
                                enabled = directTitle.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = DocPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("BTN-ADM-DIRECT-PUB")
                            ) {
                                Icon(Icons.Filled.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("তাৎক্ষণিক প্রকাশ করুন (Publish Directly)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                2 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Text("সিস্টেম পরিবর্তন ও অনুমোদন অডিট লগ (CHANGELOG-001)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        items(auditLogs, key = { it.id }) { log ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(log.pointId, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DocPrimary, modifier = Modifier.padding(4.dp))
                                        }

                                        Text(DocrootRepository.formatTime(log.timestamp), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${log.action} • ${log.actorRole}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(log.details, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    videoToReject?.let { targetVideo ->
        AlertDialog(
            onDismissRequest = { videoToReject = null },
            title = { Text("ভিডিও বাতিলের কারণ উল্লেখ করুন", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(targetVideo.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rejectionReasonInput,
                        onValueChange = { rejectionReasonInput = it },
                        label = { Text("বাতিলের কারণ ও সংশোধনী পরামর্শ") },
                        placeholder = { Text("যেমন: অডিও স্পষ্ট নয় বা বাণিজ্যিক বিজ্ঞাপন রয়েছে।") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val reason = rejectionReasonInput.ifBlank { "মেডিকেল মানদণ্ড পূরণ না করায় বাতিল করা হয়েছে।" }
                        DocrootRepository.rejectVideo(targetVideo.id, reason)
                        videoToReject = null
                        onStateChanged()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRejected)
                ) {
                    Text("বাতিল নিশ্চিত করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { videoToReject = null }) {
                    Text("ফিরে যান")
                }
            }
        )
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = accent)
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}
