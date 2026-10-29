package com.example.ui.components

import android.media.MediaPlayer
import android.net.Uri
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Online Video Player component with blue border, Play/Pause and Mute/Unmute buttons.
 * Streams video directly from Firebase Cloud / Online CDN.
 * No local video files stored in the software.
 */
@Composable
fun OnlineFirebaseVideoPlayer(
    modifier: Modifier = Modifier,
    videoUrl: String = "https://raw.githubusercontent.com/cseitz/sample-files/main/assets/video/mp4/bbb_short.mp4",
    borderColor: Color = Color(0xFF2563EB),
    onVideoClick: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var showControls by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        onDispose {
            try {
                videoViewRef?.stopPlayback()
                mediaPlayerRef?.release()
            } catch (_: Exception) {}
            mediaPlayerRef = null
            videoViewRef = null
        }
    }

    val cornerRadius = 20.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(BorderStroke(2.5.dp, borderColor), RoundedCornerShape(cornerRadius))
            .background(Color(0xFF0F172A))
            .clickable {
                showControls = !showControls
                onVideoClick()
            }
            .testTag("FIREBASE_ONLINE_VIDEO_PLAYER"),
        contentAlignment = Alignment.Center
    ) {
        // 1. Native VideoView streaming online video without bundling offline assets
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    try {
                        setVideoURI(Uri.parse(videoUrl))
                    } catch (_: Exception) {
                        isLoading = false
                        hasError = true
                    }
                    setOnPreparedListener { mp ->
                        mediaPlayerRef = mp
                        mp.isLooping = true
                        try {
                            if (isMuted) {
                                mp.setVolume(0f, 0f)
                            } else {
                                mp.setVolume(1f, 1f)
                            }
                        } catch (_: Exception) {}
                        isLoading = false
                        hasError = false
                        if (isPlaying) {
                            try {
                                start()
                            } catch (_: Exception) {}
                        }
                    }
                    setOnErrorListener { _, _, _ ->
                        isLoading = false
                        hasError = true
                        true
                    }
                    videoViewRef = this
                }
            },
            update = { view ->
                try {
                    if (isPlaying) {
                        if (!view.isPlaying && !isLoading && !hasError) {
                            view.start()
                        }
                    } else {
                        if (view.isPlaying) {
                            view.pause()
                        }
                    }
                    mediaPlayerRef?.let { mp ->
                        if (isMuted) {
                            mp.setVolume(0f, 0f)
                        } else {
                            mp.setVolume(1f, 1f)
                        }
                    }
                } catch (_: Exception) {}
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Loading state while buffering online stream
        if (isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xCC090D16))
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Connecting to Firebase Stream...",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 3. Error / Network retry state
        if (hasError) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xEE090D16))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Firebase Live Video Stream",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Waiting for online stream connection...",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FilledTonalButton(
                    onClick = {
                        hasError = false
                        isLoading = true
                        videoViewRef?.setVideoURI(Uri.parse(videoUrl))
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reconnect", fontSize = 11.sp)
                }
            }
        }

        // 4. Top Status Header (Always visible or toggleable)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x99000000), Color.Transparent)
                    )
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live Firebase Cloud indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0x66000000), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "ONLINE • FIREBASE STREAM",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Quality Badge
            Text(
                text = "HD 1080p",
                color = Color(0xFF93C5FD),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(Color(0x661E3A8A), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        // 5. Player Controls Overlay (Pause & Mute buttons)
        AnimatedVisibility(
            visible = showControls || !isPlaying,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xB3000000))
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pause / Play Button
                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        if (isPlaying) {
                            videoViewRef?.start()
                        } else {
                            videoViewRef?.pause()
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0x801E293B), CircleShape)
                        .testTag("BTN_VIDEO_PLAY_PAUSE")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause Video" else "Play Video",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Center Title hint
                Text(
                    text = "Healthcare Stream",
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                // Mute / Unmute Button
                IconButton(
                    onClick = {
                        isMuted = !isMuted
                        mediaPlayerRef?.let { mp ->
                            if (isMuted) {
                                mp.setVolume(0f, 0f)
                            } else {
                                mp.setVolume(1f, 1f)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0x801E293B), CircleShape)
                        .testTag("BTN_VIDEO_MUTE_UNMUTE")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = if (isMuted) "Unmute Audio" else "Mute Audio",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
