package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ButtonConnection
import com.example.model.MasterPoint
import com.example.model.PointStatus
import com.example.ui.components.PointStatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterHubScreen(
    masterPoints: List<MasterPoint>,
    buttonRegistry: List<ButtonConnection>,
    onSelectPoint: (MasterPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf<PointStatus?>(null) }
    var activeSubView by remember { mutableIntStateOf(0) } // 0: Points Directory, 1: Button Registry (UI-CONNECTION-001), 2: Safe Rules (SAFE-UPDATE-001)
    var expandedPointId by remember { mutableStateOf<String?>("APP-001") }

    val verifiedCount = remember(masterPoints) { masterPoints.count { it.status == PointStatus.VERIFIED } }
    val pendingCount = remember(masterPoints) { masterPoints.count { it.status == PointStatus.PENDING_CONFIGURATION } }
    val notImplementedCount = remember(masterPoints) { masterPoints.count { it.status == PointStatus.NOT_IMPLEMENTED } }

    val filteredPoints = remember(masterPoints, searchQuery, selectedStatusFilter) {
        masterPoints.filter { pt ->
            val matchesSearch = searchQuery.isBlank() ||
                    pt.id.contains(searchQuery, ignoreCase = true) ||
                    pt.titleEn.contains(searchQuery, ignoreCase = true) ||
                    pt.titleBn.contains(searchQuery, ignoreCase = true) ||
                    pt.purposeBn.contains(searchQuery, ignoreCase = true) ||
                    pt.currentConfig.contains(searchQuery, ignoreCase = true)

            val matchesStatus = selectedStatusFilter == null || pt.status == selectedStatusFilter

            matchesSearch && matchesStatus
        }
    }

    val quickQueryTags = listOf("FB-001", "PANEL-USER-001", "PANEL-CUSTOMER-001", "PANEL-ADMIN-001", "VIDEO-APPROVAL-001", "DB-001", "UI-CONNECTION-001")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header: Master Control Hub & Status Dashboard (DOC-RETRIEVAL-001)
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
                                text = "Docroot Master Architecture",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "POINT 10 & 12: DOC-RETRIEVAL-001 • MASTER DOCUMENTATION",
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
                                text = "12 Points Live",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DocPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Metrics Summary Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricBadgeCard(
                            title = "Verified",
                            count = verifiedCount,
                            bg = StatusVerifiedBg,
                            textCol = StatusVerified,
                            isSelected = selectedStatusFilter == PointStatus.VERIFIED,
                            onClick = {
                                selectedStatusFilter = if (selectedStatusFilter == PointStatus.VERIFIED) null else PointStatus.VERIFIED
                            },
                            modifier = Modifier.weight(1f)
                        )
                        MetricBadgeCard(
                            title = "Pending Config",
                            count = pendingCount,
                            bg = StatusPendingBg,
                            textCol = StatusPending,
                            isSelected = selectedStatusFilter == PointStatus.PENDING_CONFIGURATION,
                            onClick = {
                                selectedStatusFilter = if (selectedStatusFilter == PointStatus.PENDING_CONFIGURATION) null else PointStatus.PENDING_CONFIGURATION
                            },
                            modifier = Modifier.weight(1.3f)
                        )
                        MetricBadgeCard(
                            title = "Unimplemented",
                            count = notImplementedCount,
                            bg = StatusNotImplementedBg,
                            textCol = StatusNotImplemented,
                            isSelected = selectedStatusFilter == PointStatus.NOT_IMPLEMENTED,
                            onClick = {
                                selectedStatusFilter = if (selectedStatusFilter == PointStatus.NOT_IMPLEMENTED) null else PointStatus.NOT_IMPLEMENTED
                            },
                            modifier = Modifier.weight(1.1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    PrimaryTabRow(selectedTabIndex = activeSubView) {
                        Tab(
                            selected = activeSubView == 0,
                            onClick = { activeSubView = 0 },
                            text = { Text("মাস্টার পয়েন্ট ডিরেক্টরি", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.AccountTree, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_master_points")
                        )
                        Tab(
                            selected = activeSubView == 1,
                            onClick = { activeSubView = 1 },
                            text = { Text("বাটন রেজিস্ট্রি (UI-001)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.TouchApp, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_master_buttons")
                        )
                        Tab(
                            selected = activeSubView == 2,
                            onClick = { activeSubView = 2 },
                            text = { Text("নিরাপদ নীতিমালা", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            icon = { Icon(Icons.Filled.Security, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.testTag("tab_master_safety")
                        )
                    }
                }
            }

            // Body
            when (activeSubView) {
                0 -> {
                    // Search & Instant ID Query Bar (POINT 10: DOC-RETRIEVAL-001)
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("পয়েন্ট ID (যেমন: FB-001, PANEL-USER-001) বা কিওয়ার্ড লিখুন...", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(Icons.Filled.Search, contentDescription = null, tint = DocPrimary)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Filled.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("INPUT-DOC-SEARCH")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Quick tags
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            contentPadding = PaddingValues(vertical = 2.dp)
                        ) {
                            items(quickQueryTags) { tag ->
                                SuggestionChip(
                                    onClick = { searchQuery = tag },
                                    label = { Text(tag, fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.testTag("CHIP-QUICK-TAG-$tag")
                                )
                            }
                        }
                    }

                    // Master Points List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredPoints, key = { it.id }) { pt ->
                            MasterPointCard(
                                point = pt,
                                isExpanded = expandedPointId == pt.id,
                                onToggleExpand = {
                                    expandedPointId = if (expandedPointId == pt.id) null else pt.id
                                    onSelectPoint(pt)
                                }
                            )
                        }
                    }
                }

                1 -> {
                    // Button & Feature Connection Registry (POINT 08: UI-CONNECTION-001)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Surface(
                                color = DocPrimaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "UI & BUTTON CONNECTION REGISTRY (ID: UI-CONNECTION-001)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = DocOnPrimaryContainer
                                    )
                                    Text(
                                        text = "অ্যাপের প্রতিটি বাটন ও ফিচারের Unique ID, অবস্থান, ক্লিক অ্যাকশন এবং সংশ্লিষ্ট ব্যাকএন্ড সার্ভিসের পূর্ণ বিবরণ।",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        items(buttonRegistry, key = { it.buttonId }) { btn ->
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
                                            Text(
                                                text = btn.buttonId,
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                color = DocPrimary,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Text(
                                            text = btn.panel,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(btn.action, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("অবস্থান: ${btn.screenLocation}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("সার্ভিস/টার্গেট: ${btn.targetService}", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = DocSecondary)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Safe Update Rules (POINT 11: SAFE-UPDATE-001)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                tonalElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Shield, contentDescription = null, tint = DocPrimary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "POINT 11: SAFE UPDATE RULES (SAFE-UPDATE-001)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DocPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Docroot প্ল্যাটফর্মের যেকোনো কোড বা ডাটাবেজ পরিবর্তনের পূর্বে বাধ্যতামূলক ১০টি অনুশাসন:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        val safeRules = listOf(
                            "১. যেকোনো পরিবর্তনের আগে বর্তমান Project Structure ও কোড পরীক্ষা করতে হবে।",
                            "২. সংশ্লিষ্ট Point-এর Documentation পড়তে হবে এবং ইন্টারকানেকশন বুঝতে হবে।",
                            "৩. সেই Feature-এর সঙ্গে যুক্ত Firebase Connection ও অন্য Feature পরীক্ষা করতে হবে।",
                            "৪. ইউজারের নির্দেশ অনুযায়ী শুধু প্রয়োজনীয় নির্দিষ্ট পরিবর্তনটি করতে হবে।",
                            "৫. অন্য Panel বা Feature-এর কার্যকর ব্যবস্থা কোনো অবস্থাতেই নষ্ট করা যাবে না।",
                            "৬. বিদ্যমান কার্যকর Code অপ্রয়োজনীয়ভাবে Rewrite করা যাবে না।",
                            "৭. Firebase Security Rules ও User Permissions অক্ষুণ্ণ রাখতে হবে।",
                            "৮. পরিবর্তনের পরে compile_applet চালিয়ে সংশ্লিষ্ট Feature পরীক্ষা করতে হবে।",
                            "৯. Documentation ও Changelog-এ পরিবর্তনের বিবরণ যথাযথভাবে যোগ করতে হবে।",
                            "১০. কোনো কাজ বাস্তবায়িত না হলে সেটিকে সম্পূর্ণ বা Verified বলে উপস্থাপন করা যাবে না।"
                        )

                        items(safeRules) { rule ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = null, tint = StatusVerified, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(rule, fontSize = 12.sp, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricBadgeCard(
    title: String,
    count: Int,
    bg: Color,
    textCol: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) bg.copy(alpha = 0.9f) else bg.copy(alpha = 0.4f),
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) CardDefaults.outlinedCardBorder() else null,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(count.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textCol)
            Text(title, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = textCol, maxLines = 1)
        }
    }
}

@Composable
fun MasterPointCard(
    point: MasterPoint,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("CARD-POINT-${point.id}")
            .clickable { onToggleExpand() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: ID, Name, Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = point.id,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DocPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "POINT ${point.pointNumber.toString().padStart(2, '0')}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

                PointStatusBadge(status = point.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${point.titleEn} (${point.titleBn})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = point.purposeBn,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )

            // Interconnected Points chips
            if (point.connectedPoints.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("সংযুক্ত পয়েন্ট: ", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(point.connectedPoints) { dep ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = dep,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Expandable Details
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("নিয়মাবলী ও কার্যপরিধি (Rules & Scope):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DocPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    point.scopeItems.forEach { scope ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text("• ", fontSize = 11.sp, color = DocPrimary, fontWeight = FontWeight.Bold)
                            Text(scope, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("বাস্তব কনফিগারেশন স্ট্যাটাস (Real State):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DocPrimary)
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = point.currentConfig,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    if (point.updateHistory.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("ইতিহাস (Changelog):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        point.updateHistory.forEach { hist ->
                            Text(hist, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "সংক্ষেপ করুন ▲" else "সম্পূর্ণ নিয়ম দেখুন ▼",
                    fontSize = 11.sp,
                    color = DocPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
