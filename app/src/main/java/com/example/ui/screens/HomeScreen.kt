package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

/**
 * Doctor Directory Home Screen
 * Displays the newly uploaded high-resolution image directly with preserved original quality
 * and aspect ratio, while providing full interactive functionality for all buttons and navigation.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDoctor: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showBookingDialog by remember { mutableStateOf(false) }
    var showAiDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showTopDoctorsDialog by remember { mutableStateOf(false) }
    var activeSpecialtyFeedback by remember { mutableStateOf<String?>(null) }
    var activeDoctorName by remember { mutableStateOf("Dr. Emily Chen") }

    val specialtyNames = remember {
        listOf(
            "Cardiologist", "Pulmonologist", "Neurologist",
            "Nephrologist", "Ophthalmologist", "Hepatologist",
            "Gastroenterologist", "Psychologist", "Endocrinologist"
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF4F6)),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val imageWidth = screenWidth
        val imageHeight = screenHeight

        // Fill full screen edge-to-edge, eliminating black bars on sides and empty gaps top/bottom
        Image(
            painter = painterResource(id = R.drawable.img_home_screen),
            contentDescription = "Doctoot Home Screen",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .testTag("HOME_SCREEN_IMAGE")
        )

        // Top-left Doctoot Logo Area
        Box(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .fillMaxHeight(0.06f)
                .align(Alignment.TopStart)
                .offset(
                    x = imageWidth * 0.07f,
                    y = imageHeight * 0.038f
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    activeSpecialtyFeedback = "Doctoot Healthcare"
                }
                .testTag("LOGO-DOCTOOT")
        )

                // 1. Search Bar Touch Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.86f)
                        .fillMaxHeight(0.046f)
                        .align(Alignment.TopCenter)
                        .offset(y = imageHeight * 0.115f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showSearchDialog = true
                        }
                        .testTag("BTN-SEARCH-BAR")
                )

                // Top-right 3D Heart Touch Area (Large 3D Model Quick Cardiologist access)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.26f)
                        .fillMaxHeight(0.145f)
                        .align(Alignment.TopEnd)
                        .offset(
                            x = -imageWidth * 0.069f,
                            y = imageHeight * 0.002f
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            activeSpecialtyFeedback = "Cardiologist (42 Doctors Available)"
                        }
                        .testTag("BTN-TOP-HEART")
                )

                // 2. Hero: "+ Book Appointment" Button Touch Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.24f)
                        .fillMaxHeight(0.035f)
                        .align(Alignment.TopStart)
                        .offset(
                            x = imageWidth * 0.10f,
                            y = imageHeight * 0.312f
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            activeDoctorName = "Specialist Consultant"
                            showBookingDialog = true
                        }
                        .testTag("BTN-HERO-BOOK")
                )

                // 3. Hero: "✨ Ask AI Consultant" Button Touch Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.24f)
                        .fillMaxHeight(0.035f)
                        .align(Alignment.TopStart)
                        .offset(
                            x = imageWidth * 0.355f,
                            y = imageHeight * 0.312f
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showAiDialog = true
                        }
                        .testTag("BTN-HERO-AI")
                )

                // 4. Nine Specialty Card Touch Areas
                val colOffsets = listOf(0.075f, 0.368f, 0.662f)
                val rowOffsets = listOf(0.358f, 0.492f, 0.625f)
                val cardWidthFraction = 0.264f
                val cardHeightFraction = 0.125f

                for (row in 0..2) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        if (index < specialtyNames.size) {
                            val specialty = specialtyNames[index]
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(cardWidthFraction)
                                    .fillMaxHeight(cardHeightFraction)
                                    .align(Alignment.TopStart)
                                    .offset(
                                        x = imageWidth * colOffsets[col],
                                        y = imageHeight * rowOffsets[row]
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        activeSpecialtyFeedback = specialty
                                        onNavigateToDoctor(specialty)
                                    }
                                    .testTag("CARD-SPECIALTY-$specialty")
                            )
                        }
                    }
                }

                // =========================================================================
                // 🟢 NEXT BUTTON SIZE & POSITION / বাটনের সাইজ ও পজিশন (এখান থেকে এডিট করতে পারবেন)
                // =========================================================================
                val nextButtonWidth = 78.dp              // বাটনের প্রস্থ (Width)
                val nextButtonHeight = 28.dp             // বাটনের উচ্চতা (Height)
                val nextButtonFontSize = 13.sp           // অক্ষরের ফন্ট সাইজ (Font Size)
                val nextButtonCornerRadius = 14.dp       // কোণার রাউন্ডনেস (Corner Radius)
                val nextButtonColor = Color(0xFF16A34A)  // গ্রীন কালার (Button Color)
                val nextButtonOffsetX = 0.745f           // ডানে-বামে অবস্থান (X Offset)
                val nextButtonOffsetY = 0.772f           // উপরে-নিচে অবস্থান (Y Offset)
                // =========================================================================

                // 4b. "Next" Green Button (Replacing 'See all' beside Top Rated Doctors)
                Button(
                    onClick = { showTopDoctorsDialog = true },
                    modifier = Modifier
                        .width(nextButtonWidth)
                        .height(nextButtonHeight)
                        .align(Alignment.TopStart)
                        .offset(
                            x = imageWidth * nextButtonOffsetX,
                            y = imageHeight * nextButtonOffsetY
                        )
                        .testTag("BTN-NEXT-TOP-DOCTORS"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = nextButtonColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(nextButtonCornerRadius),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = nextButtonFontSize
                    )
                }

                // 5. Dr. Emily Chen "Book Now" Button Touch Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.23f)
                        .fillMaxHeight(0.035f)
                        .align(Alignment.TopStart)
                        .offset(
                            x = imageWidth * 0.685f,
                            y = imageHeight * 0.854f
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            activeDoctorName = "Dr. Emily Chen"
                            showBookingDialog = true
                        }
                        .testTag("BTN-DOCTOR-BOOK-NOW")
                )

                // 6. Bottom Navigation Bar 5 Tabs Touch Areas
                val tabWidthFraction = 0.2f
                val navBarTopFraction = 0.933f
                val navBarHeightFraction = 0.067f

                for (tabIndex in 0..4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(tabWidthFraction)
                            .fillMaxHeight(navBarHeightFraction)
                            .align(Alignment.TopStart)
                            .offset(
                                x = imageWidth * (tabIndex * tabWidthFraction),
                                y = imageHeight * navBarTopFraction
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedTab = tabIndex
                            }
                            .testTag("BOTTOM-TAB-$tabIndex")
                    )
                }
    }

    // Appointment Booking Dialog
    if (showBookingDialog) {
        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF2563EB)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Book Appointment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = activeDoctorName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Cardiologist • 12 yrs exp • 4.9 ★",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Next Available Slots:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E40AF)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Today at 4:30 PM\n• Tomorrow at 10:00 AM\n• Thursday at 2:00 PM",
                                fontSize = 12.sp,
                                color = Color(0xFF1E3A8A)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showBookingDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirm Slot")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingDialog = false }) {
                    Text("Cancel", color = Color(0xFF64748B))
                }
            }
        )
    }

    // AI Consultant Dialog
    if (showAiDialog) {
        AlertDialog(
            onDismissRequest = { showAiDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF8B5CF6)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Health Consultant",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Describe your symptoms or health questions to find the right specialist immediately.",
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFF5F3FF),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Suggested for you:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6D28D9)
                            )
                            Text(
                                text = "Chest discomfort or heart palpitations -> Consult a Cardiologist.",
                                fontSize = 12.sp,
                                color = Color(0xFF4C1D95)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAiDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Start Consultation")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAiDialog = false }) {
                    Text("Dismiss", color = Color(0xFF64748B))
                }
            }
        )
    }

    // Search Dialog
    if (showSearchDialog) {
        var localQuery by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF2563EB)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Search Doctors",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = localQuery,
                        onValueChange = { localQuery = it },
                        placeholder = { Text("Search specialty, symptoms...") },
                        singleLine = true,
                        trailingIcon = {
                            if (localQuery.isNotEmpty()) {
                                IconButton(onClick = { localQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Quick Specialties:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cardiology • Pulmonology • Neurology • Nephrology",
                        fontSize = 12.sp,
                        color = Color(0xFF2563EB)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSearchDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Search")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text("Close", color = Color(0xFF64748B))
                }
            }
        )
    }

    // Top Rated Doctors ("Next") Dialog
    if (showTopDoctorsDialog) {
        val topDoctorsList = remember {
            listOf(
                Triple("Prof. Dr. M. A. Rahman", "Cardiologist • 22 yrs exp", "4.9 ★ (184 reviews)"),
                Triple("Dr. Nusrat Jahan", "Pediatric Specialist • 14 yrs exp", "4.8 ★ (210 reviews)"),
                Triple("Prof. Dr. Tariqul Islam", "Neurologist • 18 yrs exp", "4.9 ★ (142 reviews)"),
                Triple("Dr. Farhana Ahmed", "Gynecologist • 15 yrs exp", "4.7 ★ (98 reviews)"),
                Triple("Dr. Shamsul Huda", "Orthopedic Surgeon • 16 yrs exp", "4.8 ★ (115 reviews)")
            )
        }
        AlertDialog(
            onDismissRequest = { showTopDoctorsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF16A34A)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Top Rated Specialists",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Next available certified specialists ready for consultation:",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    topDoctorsList.forEach { (name, spec, rating) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    activeDoctorName = name
                                    showTopDoctorsDialog = false
                                    showBookingDialog = true
                                },
                            color = Color(0xFFF0FDF4),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = spec,
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Text(
                                        text = rating,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF16A34A)
                                    )
                                }
                                Button(
                                    onClick = {
                                        activeDoctorName = name
                                        showTopDoctorsDialog = false
                                        showBookingDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Book", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showTopDoctorsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Close")
                }
            }
        )
    }

    // Specialty Selection Snackbar / Notification
    activeSpecialtyFeedback?.let { specialty ->
        LaunchedEffect(specialty) {
            kotlinx.coroutines.delay(2000)
            activeSpecialtyFeedback = null
        }
    }
}
