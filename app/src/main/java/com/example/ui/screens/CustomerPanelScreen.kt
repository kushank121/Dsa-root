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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocrootRepository
import com.example.model.Doctor
import com.example.model.HealthVideo
import com.example.model.VideoStatus
import com.example.ui.components.VideoStatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerPanelScreen(
    currentDoctor: Doctor,
    myVideos: List<HealthVideo>,
    onVideoSubmitted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Upload Video, 1: My Submissions, 2: Chamber Profile

    // Video submission form state
    var videoTitle by remember { mutableStateOf("") }
    var videoCategory by remember { mutableStateOf("Cardiology") }
    var videoUrl by remember { mutableStateOf("") }
    var videoDescription by remember { mutableStateOf("") }
    var submissionSuccessMessage by remember { mutableStateOf<String?>(null) }

    // Chamber edit state
    var chamberHospital by remember { mutableStateOf(currentDoctor.hospital) }
    var chamberFee by remember { mutableStateOf(currentDoctor.consultationFee.toString()) }
    var chamberSchedule by remember { mutableStateOf(currentDoctor.availability) }
    var chamberPhone by remember { mutableStateOf(currentDoctor.phone) }
    var profileSavedMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Cardiology", "Pediatrics", "Neurology", "Gynecology", "Dermatology", "General Health")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Info & Notice for Customer Panel (PANEL-CUSTOMER-001)
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
                                text = "ডাক্তার ও প্রোভাইডার পোর্টাল",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "POINT 05: PANEL-CUSTOMER-001 • কাস্টমার আইডি: ${currentDoctor.id.uppercase()}",
                                fontSize = 11.sp,
                                color = DocPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Verified Provider",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DocPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Logged in doctor summary card
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = DocPrimary, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(currentDoctor.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${currentDoctor.specialty} • ${currentDoctor.degrees}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    PrimaryTabRow(selectedTabIndex = activeSubTab) {
                        Tab(
                            selected = activeSubTab == 0,
                            onClick = { activeSubTab = 0 },
                            text = { Text("ভিডিও আপলোড", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_cust_upload")
                        )
                        Tab(
                            selected = activeSubTab == 1,
                            onClick = { activeSubTab = 1 },
                            text = { Text("আমার সাবমিশন (${myVideos.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.Checklist, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_cust_submissions")
                        )
                        Tab(
                            selected = activeSubTab == 2,
                            onClick = { activeSubTab = 2 },
                            text = { Text("চেম্বার প্রোফাইল", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.EditLocation, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_cust_profile")
                        )
                    }
                }
            }

            // Tab Content
            when (activeSubTab) {
                0 -> {
                    // Video Upload Form (POINT 06: VIDEO-APPROVAL-001)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            // Policy notice
                            Surface(
                                color = StatusPendingBg,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(Icons.Filled.Info, contentDescription = null, tint = StatusPending, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "অনুমোদন নীতিমালা (Rule VIDEO-APPROVAL-001)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = StatusPending
                                        )
                                        Text(
                                            text = "কাস্টমার থেকে আপলোডকৃত ভিডিও সরাসরি Pending Approval অবস্থায় Admin Panel-এ জমা হবে। অ্যাডমিন অনুমোদন দিলে তবেই তা User Panel ভিডিও বক্সে প্রকাশিত হবে।",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        submissionSuccessMessage?.let { msg ->
                            item {
                                Surface(
                                    color = StatusVerifiedBg,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = StatusVerified)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(msg, fontSize = 12.sp, color = StatusVerified, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = videoTitle,
                                onValueChange = { videoTitle = it },
                                label = { Text("ভিডিওর শিরোনাম (Title)") },
                                placeholder = { Text("যেমন: হার্ট অ্যাটাকের ৫টি প্রধান পূর্বাভাস") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("INPUT-CUST-VID-TITLE")
                            )
                        }

                        item {
                            Column {
                                Text("স্বাস্থ্য ক্যাটাগরি নির্বাচন করুন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    categories.take(3).forEach { cat ->
                                        FilterChip(
                                            selected = videoCategory == cat,
                                            onClick = { videoCategory = cat },
                                            label = { Text(cat, fontSize = 11.sp) },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    categories.drop(3).forEach { cat ->
                                        FilterChip(
                                            selected = videoCategory == cat,
                                            onClick = { videoCategory = cat },
                                            label = { Text(cat, fontSize = 11.sp) },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = videoUrl,
                                onValueChange = { videoUrl = it },
                                label = { Text("ভিডিও স্টোরেজ URL / ক্লাউড লিঙ্ক") },
                                placeholder = { Text("https://storage.googleapis.com/docroot-videos/sample.mp4") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("INPUT-CUST-VID-URL")
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = videoDescription,
                                onValueChange = { videoDescription = it },
                                label = { Text("ভিডিওর সারসংক্ষেপ ও স্বাস্থ্য বার্তা") },
                                placeholder = { Text("রোগী ও সাধারণ মানুষের জন্য পরামর্শ লিখুন...") },
                                minLines = 3,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("INPUT-CUST-VID-DESC")
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    if (videoTitle.isNotBlank()) {
                                        val url = if (videoUrl.isBlank()) "https://storage.googleapis.com/docroot-videos/${System.currentTimeMillis()}.mp4" else videoUrl
                                        DocrootRepository.submitCustomerVideo(
                                            title = videoTitle,
                                            category = videoCategory,
                                            videoUrl = url,
                                            description = videoDescription.ifBlank { "স্বাস্থ্য পরামর্শমূলক ভিডিও" },
                                            doctorName = currentDoctor.name,
                                            doctorId = currentDoctor.id
                                        )
                                        submissionSuccessMessage = "ভিডিও সফলভাবে জমা হয়েছে! স্ট্যাটাস: PENDING_APPROVAL. অ্যাডমিনের অনুমোদনের পর User Panel-এ প্রদর্শিত হবে।"
                                        videoTitle = ""
                                        videoUrl = ""
                                        videoDescription = ""
                                        onVideoSubmitted()
                                    }
                                },
                                enabled = videoTitle.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = DocPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("BTN-CUST-SUBMIT-VID")
                            ) {
                                Icon(Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("অ্যাডমিন পর্যালোচনার জন্য জমা দিন (Submit for Review)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                1 -> {
                    // Customer Submissions Live Status Tracker (POINT 05 & 06)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Text(
                                text = "আপনার আপলোডকৃত ভিডিও এবং লাইভ অনুমোদন স্ট্যাটাস",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (myVideos.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("এখনও কোনো ভিডিও জমা দেননি।", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
                            items(myVideos, key = { it.id }) { video ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = video.category,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = DocPrimary
                                            )
                                            VideoStatusBadge(status = video.status)
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(video.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(video.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                        // Rejection reason if applicable
                                        if (video.status == VideoStatus.REJECTED && !video.rejectionReason.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Surface(
                                                color = StatusRejectedBg,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(8.dp),
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    Icon(Icons.Filled.Error, contentDescription = null, tint = StatusRejected, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Column {
                                                        Text("অ্যাডমিন ফিডব্যাক / রিজেক্টের কারণ:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusRejected)
                                                        Text(video.rejectionReason, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                                    }
                                                }
                                            }
                                        }

                                        if (video.status == VideoStatus.APPROVED) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Filled.Visibility, contentDescription = null, tint = StatusVerified, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("লাইভ: User Panel ভিডিও বক্সে সক্রিয় আছে", fontSize = 11.sp, color = StatusVerified)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Chamber Profile Editor (POINT 05: Customer Permissions)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Text("চেম্বার ও ভিজিট শিডিউল আপডেট", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("রোগীরা এই তথ্যের ভিত্তিতে সরাসরি চেম্বারে যোগাযোগ করবে।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        profileSavedMessage?.let { msg ->
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
                                value = chamberHospital,
                                onValueChange = { chamberHospital = it },
                                label = { Text("হাসপাতাল / ক্লিনিকের নাম") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = chamberFee,
                                onValueChange = { chamberFee = it },
                                label = { Text("পরামর্শ ফি (টাকা)") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = chamberSchedule,
                                onValueChange = { chamberSchedule = it },
                                label = { Text("চেম্বারের দিন ও সময়") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = chamberPhone,
                                onValueChange = { chamberPhone = it },
                                label = { Text("সিরিয়ালের হটলাইন মোবাইল নম্বর") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    val feeInt = chamberFee.toIntOrNull() ?: currentDoctor.consultationFee
                                    DocrootRepository.updateDoctorChamber(
                                        doctorId = currentDoctor.id,
                                        hospital = chamberHospital,
                                        fee = feeInt,
                                        availability = chamberSchedule,
                                        phone = chamberPhone
                                    )
                                    profileSavedMessage = "চেম্বার তথ্য সফলভাবে সংরক্ষিত হয়েছে এবং ইউজার প্যানেলে আপডেট হয়েছে।"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DocPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("BTN-CUST-SAVE-PROF")
                            ) {
                                Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("পরিবর্তন সংরক্ষণ করুন (Save Profile)")
                            }
                        }
                    }
                }
            }
        }
    }
}
