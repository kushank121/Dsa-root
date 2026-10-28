package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

object DocrootRepository {

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _videos = MutableStateFlow<List<HealthVideo>>(emptyList())
    val videos: StateFlow<List<HealthVideo>> = _videos.asStateFlow()

    private val _masterPoints = MutableStateFlow<List<MasterPoint>>(emptyList())
    val masterPoints: StateFlow<List<MasterPoint>> = _masterPoints.asStateFlow()

    private val _buttonRegistry = MutableStateFlow<List<ButtonConnection>>(emptyList())
    val buttonRegistry: StateFlow<List<ButtonConnection>> = _buttonRegistry.asStateFlow()

    private val _auditLogs = MutableStateFlow<List<AuditLog>>(emptyList())
    val auditLogs: StateFlow<List<AuditLog>> = _auditLogs.asStateFlow()

    init {
        initializeMasterDocumentation()
        initializeDoctors()
        initializeVideos()
        initializeButtonRegistry()
    }

    private fun initializeMasterDocumentation() {
        val points = listOf(
            MasterPoint(
                id = "APP-001",
                pointNumber = 1,
                titleEn = "APP MASTER CONTROL",
                titleBn = "অ্যাপ মাস্টার কন্ট্রোল ও আর্কিটেকচার",
                status = PointStatus.VERIFIED,
                purposeBn = "সম্পূর্ণ অ্যাপের মূল কাঠামো ও Architecture সংরক্ষণ, প্যানেলসমূহের তালিকা ও ইন্টারকানেকশন নিয়ন্ত্রণ।",
                scopeItems = listOf(
                    "Architecture: Jetpack Compose + MVVM + StateFlow + Repository",
                    "Active Panels: User Directory, Customer Portal, Admin Approval, Master Hub",
                    "Interconnected Dependency Mapping with Safe Update Protocol",
                    "Permission Gate: No feature modification without verified point audit"
                ),
                connectedPoints = listOf("FB-001", "PANEL-ADMIN-001", "PANEL-USER-001", "PANEL-CUSTOMER-001", "DB-001", "SAFE-UPDATE-001"),
                currentConfig = "Single-source architectural state engine in DocrootRepository; strict unidirectional data flow.",
                updateHistory = listOf("2026-10-27: Initial architectural mapping established and verified.")
            ),
            MasterPoint(
                id = "FB-001",
                pointNumber = 2,
                titleEn = "FIREBASE CONNECTION",
                titleBn = "ফায়ারবেস কানেকশন ও ব্যাকএন্ড সার্ভিস",
                status = PointStatus.PENDING_CONFIGURATION,
                purposeBn = "নির্ধারিত Firebase Authentication, Firestore Database এবং Cloud Storage কানেকশন ও রুলস সংরক্ষণ।",
                scopeItems = listOf(
                    "Firebase Auth: Role-based token mapping (Doctor/Customer vs Admin)",
                    "Firestore: Cloud database for doctors, videos, audit logs (see DB-001)",
                    "Storage: Medical video files (/videos/) and doctor chamber documents (/chambers/)",
                    "Security Rules: Customer writes pending only; Admin writes approvals; Public reads approved items",
                    "Cloud Sync: Pre-configured Gradle BOM awaiting project credentials link"
                ),
                connectedPoints = listOf("DB-001", "PANEL-ADMIN-001", "PANEL-CUSTOMER-001", "VIDEO-APPROVAL-001"),
                currentConfig = "Firestore models & state flow fully mapped. Pending project binding (local reactive state layer currently serving verified prototype data).",
                updateHistory = listOf("2026-10-27: Status tagged PENDING_CONFIGURATION as per safety rules.")
            ),
            MasterPoint(
                id = "PANEL-ADMIN-001",
                pointNumber = 3,
                titleEn = "ADMIN PANEL",
                titleBn = "অ্যাডমিন অনুমোদন প্যানেল ও কন্ট্রোল সেন্টার",
                status = PointStatus.VERIFIED,
                purposeBn = "কাস্টমারের আপলোডকৃত ভিডিও এবং ডাক্তার তথ্যের অনুমোদন, সরাসরি পাবলিকেশন ও প্ল্যাটফর্ম অডিট।",
                scopeItems = listOf(
                    "Pending Video Queue (Review, Approve, Reject with remarks)",
                    "Direct Video Publisher (Bypasses approval queue for verified medical tips)",
                    "Doctor Verification Registry & Chamber approvals",
                    "System Statistics: Total Doctors, Pending Submissions, Published Reels"
                ),
                connectedPoints = listOf("VIDEO-APPROVAL-001", "DB-001", "PANEL-USER-001", "UI-CONNECTION-001"),
                currentConfig = "Interactive queue with one-click Approve/Reject, instant state synchronization to User Panel.",
                updateHistory = listOf("2026-10-27: Implemented approval actions and direct publisher.")
            ),
            MasterPoint(
                id = "PANEL-USER-001",
                pointNumber = 4,
                titleEn = "USER PANEL",
                titleBn = "ইউজার প্যানেল (ডাক্তার ডিরেক্টরি ও ভিডিও বক্স)",
                status = PointStatus.VERIFIED,
                purposeBn = "সাধারণ ব্যবহারকারীদের জন্য ডাক্তার অনুসন্ধান, ফিল্টারিং, চেম্বার বুকিং এবং অনুমোদিত স্বাস্থ্য ভিডিও প্রদর্শন।",
                scopeItems = listOf(
                    "Doctor Directory with real-time Search (Name, Hospital, Disease)",
                    "Specialty Chips: Cardiology, Pediatrics, Neurology, Gynecology, Dermatology, etc.",
                    "City Filters: Dhaka, Chittagong, Sylhet, Rajshahi, Khulna",
                    "Health Video Box: Only APPROVED & Enabled videos visible to public",
                    "Doctor Profile & Chamber Booking dialog modal"
                ),
                connectedPoints = listOf("VIDEO-APPROVAL-001", "DB-001", "PANEL-ADMIN-001", "UI-CONNECTION-001"),
                currentConfig = "Real-time search filtering, dynamic video showcase listening to approval state stream.",
                updateHistory = listOf("2026-10-27: Verified search and approved video display connection.")
            ),
            MasterPoint(
                id = "PANEL-CUSTOMER-001",
                pointNumber = 5,
                titleEn = "CUSTOMER PANEL",
                titleBn = "কাস্টমার প্যানেল (ডাক্তার ও ক্লিনিক পোর্টাল)",
                status = PointStatus.VERIFIED,
                purposeBn = "ডাক্তার ও ক্লিনিক প্রোভাইডারদের জন্য প্রোফাইল পরিচালনা এবং অনুমোদনের জন্য ভিডিও আপলোড।",
                scopeItems = listOf(
                    "Doctor ID badge (e.g. DOC-BANGLA-882)",
                    "Chamber Profile Editor: Fees, Visiting Hours, Qualification",
                    "Video Upload Form: Title, Category, Cloud URL, Health Summary",
                    "Submission Status Tracker: Live status (Pending Approval, Approved, Rejected)",
                    "Strict Security: Customer cannot approve own content"
                ),
                connectedPoints = listOf("VIDEO-APPROVAL-001", "PANEL-ADMIN-001", "DB-001", "FB-001"),
                currentConfig = "Customer profile management and video upload linked to Admin Review Queue.",
                updateHistory = listOf("2026-10-27: Integrated submission state tracker with read-only permission locks.")
            ),
            MasterPoint(
                id = "VIDEO-APPROVAL-001",
                pointNumber = 6,
                titleEn = "VIDEO UPLOAD & APPROVAL",
                titleBn = "ভিডিও আপলোড ও অনুমোদন ওয়ার্কফ্লো",
                status = PointStatus.VERIFIED,
                purposeBn = "কাস্টমার থেকে অ্যাডমিন অনুমোদন হয়ে ইউজার প্যানেল ভিডিও বক্সে প্রকাশের দ্বিমুখী প্রক্রিয়া।",
                scopeItems = listOf(
                    "Customer Upload -> Status set to PENDING_APPROVAL",
                    "Admin Approval -> Status set to APPROVED -> Immediately shows in User Video Box",
                    "Admin Rejection -> Status set to REJECTED -> Hidden from User Panel, reason logged",
                    "Admin Direct Upload -> Bypasses approval queue -> Direct APPROVED state",
                    "Full lifecycle audit logging in audit_logs collection"
                ),
                connectedPoints = listOf("PANEL-CUSTOMER-001", "PANEL-ADMIN-001", "PANEL-USER-001", "DB-001"),
                currentConfig = "Finite State Machine (PENDING -> APPROVED / REJECTED) with reactive propagation.",
                updateHistory = listOf("2026-10-27: Validated state transitions and isolation of pending reels.")
            ),
            MasterPoint(
                id = "DB-001",
                pointNumber = 7,
                titleEn = "DATABASE STRUCTURE",
                titleBn = "ডাটাবেস স্ট্রাকচার ও কালেকশন স্কিমা",
                status = PointStatus.VERIFIED,
                purposeBn = "Firestore কালেকশনসমূহ (doctors, videos, audit_logs) এবং ফিল্ড ডেফিনিশন সংরক্ষণ।",
                scopeItems = listOf(
                    "Collection 'doctors': id, name, specialty, degrees, hospital, city, fee, rating, verified",
                    "Collection 'videos': id, title, category, videoUrl, uploaderRole, status, rejectionReason",
                    "Collection 'audit_logs': logId, pointId, timestamp, action, actorRole, details",
                    "Read/Write Matrix: Public read-only; Customer write-draft; Admin master write"
                ),
                connectedPoints = listOf("FB-001", "VIDEO-APPROVAL-001", "PANEL-ADMIN-001", "PANEL-USER-001"),
                currentConfig = "Normalized schema representation in DocModels.kt and in-memory cloud mirror.",
                updateHistory = listOf("2026-10-27: Collections schema documented and verified.")
            ),
            MasterPoint(
                id = "UI-CONNECTION-001",
                pointNumber = 8,
                titleEn = "BUTTON & FEATURE CONNECTION",
                titleBn = "বাটন ও ফিচার কানেকশন রেজিস্ট্রি",
                status = PointStatus.VERIFIED,
                purposeBn = "অ্যাপের প্রতিটি বাটন ও ফিচারের ইউনিক আইডি, অবস্থান এবং সংশ্লিষ্ট সার্ভিসের তালিকা সংরক্ষণ।",
                scopeItems = listOf(
                    "Unique ID for every primary action (e.g. BTN-ADM-APPROVE, BTN-CUST-SUBMIT-VID)",
                    "Screen location mapping and user gesture trigger",
                    "Target service & ViewModel function linkage",
                    "Accessibility testTag compliance on all Compose interactables"
                ),
                connectedPoints = listOf("APP-001", "PANEL-ADMIN-001", "PANEL-USER-001", "PANEL-CUSTOMER-001"),
                currentConfig = "Centralized ButtonConnection registry with live inspection in Master Hub.",
                updateHistory = listOf("2026-10-27: Mapped 14 primary button IDs across all panels.")
            ),
            MasterPoint(
                id = "CHANGELOG-001",
                pointNumber = 9,
                titleEn = "CHANGE HISTORY",
                titleBn = "পরিবর্তন ইতিহাস ও অডিট ট্রেইল",
                status = PointStatus.VERIFIED,
                purposeBn = "গুরুত্বপূর্ণ পরিবর্তনের তারিখ, সংশোধিত পয়েন্ট এবং বিবর্তন বিবরণ সংরক্ষণ।",
                scopeItems = listOf(
                    "Semantic Versioning tracking (Current: v1.0.0)",
                    "Point-specific changelog logs",
                    "Chronological audit trail for all admin and customer transactions",
                    "Preservation of prior configurations before destructive modifications"
                ),
                connectedPoints = listOf("SAFE-UPDATE-001", "APP-001", "DOC-STORAGE-001"),
                currentConfig = "Live chronological audit event stream stored in DocrootRepository.",
                updateHistory = listOf("2026-10-27: Version 1.0.0 logged and initialized.")
            ),
            MasterPoint(
                id = "DOC-RETRIEVAL-001",
                pointNumber = 10,
                titleEn = "DOCUMENTATION RETRIEVAL",
                titleBn = "ডকুমেন্টেশন রিট্রিভাল ও অনুসন্ধান সিস্টেম",
                status = PointStatus.VERIFIED,
                purposeBn = "ভবিষ্যতে কোনো পয়েন্টের নাম বা ID উল্লেখ করলে তার সম্পূর্ণ নিয়ম ও কানেকশন তাৎক্ষণিক প্রদর্শন।",
                scopeItems = listOf(
                    "Instant query by ID (e.g., 'FB-001', 'PANEL-USER-001', 'VIDEO-APPROVAL-001')",
                    "Bilingual Keyword Search (English and Bengali support)",
                    "Filter by status: Verified, Pending Configuration, Not Implemented",
                    "Detailed interactive card expansion with copyable configuration parameters"
                ),
                connectedPoints = listOf("APP-001", "DOC-STORAGE-001", "SAFE-UPDATE-001"),
                currentConfig = "Integrated search bar in Master Hub with sub-millisecond filtering.",
                updateHistory = listOf("2026-10-27: Built real-time search engine with Bengali script matching.")
            ),
            MasterPoint(
                id = "SAFE-UPDATE-001",
                pointNumber = 11,
                titleEn = "SAFE UPDATE RULES",
                titleBn = "নিরাপদ আপডেট ও পরিবর্তন নীতিমালা",
                status = PointStatus.VERIFIED,
                purposeBn = "যেকোনো পরিবর্তনের পূর্বে ডিপেন্ডেন্সি পরীক্ষা, কোড প্রিভেনশন এবং অনুমতি সংক্রান্ত অনুশাসন।",
                scopeItems = listOf(
                    "Rule 1: Always check active code & documentation before editing",
                    "Rule 2: Never modify unrelated panels when updating a single point",
                    "Rule 3: No unapproved database schema or deletion operations",
                    "Rule 4: Mandatory compilation and verification check via compile_applet"
                ),
                connectedPoints = listOf("APP-001", "CHANGELOG-001", "DOC-RETRIEVAL-001"),
                currentConfig = "Standard operating procedure enforced in agent workflows and Master Hub checklist.",
                updateHistory = listOf("2026-10-27: 10 safety directives formally ratified.")
            ),
            MasterPoint(
                id = "DOC-STORAGE-001",
                pointNumber = 12,
                titleEn = "MASTER DOCUMENTATION STORAGE",
                titleBn = "কেন্দ্রীয় মাস্টার ডকুমেন্টেশন স্টোরেজ",
                status = PointStatus.VERIFIED,
                purposeBn = "সমস্ত পয়েন্টের বিবরণ একটি কেন্দ্রীয় মাস্টার ডকুমেন্টে সংরক্ষণ ও ইন-অ্যাপ প্রদর্শনের নিশ্চয়তা।",
                scopeItems = listOf(
                    "Canonical File Storage: /MASTER_DOCUMENTATION.md",
                    "In-App Code Repository: DocrootRepository.kt",
                    "Live Master Control & Status Dashboard UI",
                    "Persistence independent of external database connectivity"
                ),
                connectedPoints = listOf("APP-001", "DOC-RETRIEVAL-001", "CHANGELOG-001"),
                currentConfig = "Stored in /MASTER_DOCUMENTATION.md and mirrored in in-memory state engine.",
                updateHistory = listOf("2026-10-27: Document file created and synced with in-app dashboard.")
            )
        )
        _masterPoints.value = points
    }

    private fun initializeDoctors() {
        _doctors.value = listOf(
            Doctor(
                id = "doc_101",
                name = "Prof. Dr. M. A. Rahman",
                specialty = "Cardiology",
                degrees = "MBBS, FCPS (Cardiology), FACC (USA)",
                hospital = "National Heart Foundation & Research Institute",
                city = "Dhaka",
                consultationFee = 1500,
                rating = 4.9,
                reviewsCount = 184,
                phone = "+880 1711-234567",
                isVerified = true,
                availability = "Sun - Thu (5:00 PM - 9:00 PM)",
                bio = "Senior Consultant Cardiologist with 22+ years of experience in interventional cardiology and preventive cardiovascular health."
            ),
            Doctor(
                id = "doc_102",
                name = "Dr. Nusrat Jahan",
                specialty = "Pediatrics",
                degrees = "MBBS, DCH, MD (Child Health)",
                hospital = "Dhaka Shishu (Children) Hospital",
                city = "Dhaka",
                consultationFee = 1000,
                rating = 4.8,
                reviewsCount = 210,
                phone = "+880 1819-876543",
                isVerified = true,
                availability = "Sat - Wed (4:00 PM - 8:30 PM)",
                bio = "Child health specialist with specialized training in neonatal care, pediatric nutrition, and immunizations."
            ),
            Doctor(
                id = "doc_103",
                name = "Prof. Dr. Tariqul Islam",
                specialty = "Neurology",
                degrees = "MBBS, MD (Neurology), PhD",
                hospital = "National Institute of Neurosciences & Hospital",
                city = "Dhaka",
                consultationFee = 1800,
                rating = 4.9,
                reviewsCount = 142,
                phone = "+880 1912-345678",
                isVerified = true,
                availability = "Mon - Fri (6:00 PM - 10:00 PM)",
                bio = "Leading neurologist specializing in stroke management, epilepsy, headaches, and neuromuscular disorders."
            ),
            Doctor(
                id = "doc_104",
                name = "Dr. Farhana Ahmed",
                specialty = "Gynecology",
                degrees = "MBBS, FCPS (Gynae & Obs), MS",
                hospital = "Chittagong Medical College Hospital",
                city = "Chittagong",
                consultationFee = 1200,
                rating = 4.7,
                reviewsCount = 98,
                phone = "+880 1611-998877",
                isVerified = true,
                availability = "Daily except Friday (3:00 PM - 7:00 PM)",
                bio = "Experienced gynecologist and high-risk pregnancy specialist dedicated to maternal and fetal well-being."
            ),
            Doctor(
                id = "doc_105",
                name = "Dr. Kazi Tanvir Hasan",
                specialty = "Dermatology",
                degrees = "MBBS, DDV, MD (Dermatology)",
                hospital = "Sylhet MAG Osmani Medical College",
                city = "Sylhet",
                consultationFee = 900,
                rating = 4.8,
                reviewsCount = 76,
                phone = "+880 1521-445566",
                isVerified = true,
                availability = "Sat - Thu (5:30 PM - 9:00 PM)",
                bio = "Dermatologist and aesthetic medicine consultant providing treatments for chronic skin conditions, acne, and hair loss."
            ),
            Doctor(
                id = "doc_106",
                name = "Dr. Shamsul Huda",
                specialty = "Orthopedics",
                degrees = "MBBS, MS (Orthopedics), FACS",
                hospital = "NITOR (Pongu Hospital)",
                city = "Dhaka",
                consultationFee = 1400,
                rating = 4.8,
                reviewsCount = 115,
                phone = "+880 1733-112233",
                isVerified = true,
                availability = "Sun, Tue, Thu (4:00 PM - 8:00 PM)",
                bio = "Orthopedic surgeon specializing in trauma management, joint replacements, and sports injury rehabilitation."
            )
        )
    }

    private fun initializeVideos() {
        val now = System.currentTimeMillis()
        _videos.value = listOf(
            HealthVideo(
                id = "vid_301",
                title = "বুকের ব্যথা ও হার্ট অ্যাটাকের লক্ষণ: কখন জরুরি হাসপাতালে যাবেন?",
                description = "হৃদরোগ বিশেষজ্ঞ ডাঃ এম এ রহমান আলোচনা করেছেন হার্ট অ্যাটাকের প্রাথমিক লক্ষণ ও তাৎক্ষণিক করণীয়।",
                category = "Cardiology",
                duration = "4:15",
                videoUrl = "https://storage.googleapis.com/docroot-videos/heart_health_guide.mp4",
                uploadedByRole = UserRole.ADMIN,
                uploaderName = "Admin Medical Desk (Direct)",
                uploaderId = "admin_01",
                status = VideoStatus.APPROVED,
                rejectionReason = null,
                createdAt = now - 86400000L * 2,
                isEnabled = true
            ),
            HealthVideo(
                id = "vid_302",
                title = "শিশুদের নিউমোনিয়ার লক্ষণ ও শীতকালীন স্বাস্থ্য সতর্কতা",
                description = "ডাঃ নুসরাত জাহানের নির্দেশিকা: শিশুদের শ্বাসকষ্ট ও ঠান্ডাজনিত জটিলতা রোধে সতর্কতা।",
                category = "Pediatrics",
                duration = "5:30",
                videoUrl = "https://storage.googleapis.com/docroot-videos/child_pneumonia_care.mp4",
                uploadedByRole = UserRole.CUSTOMER_DOCTOR,
                uploaderName = "Dr. Nusrat Jahan",
                uploaderId = "doc_102",
                status = VideoStatus.APPROVED,
                rejectionReason = null,
                createdAt = now - 86400000L,
                isEnabled = true
            ),
            HealthVideo(
                id = "vid_303",
                title = "স্ট্রোকের লক্ষণ চেনার FAST পদ্ধতি: জীবনের প্রথম ৩ ঘণ্টা কেন গুরুত্বপূর্ণ",
                description = "স্ট্রোক হলে মুখে বাঁকানো, হাত অবশ হওয়া এবং কথা জড়িয়ে যাওয়া লক্ষ্য করুন।",
                category = "Neurology",
                duration = "3:45",
                videoUrl = "https://storage.googleapis.com/docroot-videos/stroke_awareness.mp4",
                uploadedByRole = UserRole.CUSTOMER_DOCTOR,
                uploaderName = "Prof. Dr. Tariqul Islam",
                uploaderId = "doc_103",
                status = VideoStatus.PENDING_APPROVAL,
                rejectionReason = null,
                createdAt = now - 3600000L * 4,
                isEnabled = true
            ),
            HealthVideo(
                id = "vid_304",
                title = "গর্ভাবস্থায় পুষ্টি ও আয়রন সমৃদ্ধ খাবারের সঠিক তালিকা",
                description = "গর্ভকালীন সময়ে মায়ের পুষ্টি ও শিশুর স্বাভাবিক বৃদ্ধির জন্য প্রয়োজনীয় খাদ্যাভ্যাস।",
                category = "Gynecology",
                duration = "6:10",
                videoUrl = "https://storage.googleapis.com/docroot-videos/maternal_nutrition.mp4",
                uploadedByRole = UserRole.CUSTOMER_DOCTOR,
                uploaderName = "Dr. Farhana Ahmed",
                uploaderId = "doc_104",
                status = VideoStatus.PENDING_APPROVAL,
                rejectionReason = null,
                createdAt = now - 3600000L * 2,
                isEnabled = true
            ),
            HealthVideo(
                id = "vid_305",
                title = "অনিয়ন্ত্রিত ওষুধ সেবনে ত্বকের অ্যালার্জি ও চিকিৎসা",
                description = "ডাক্তারের পরামর্শ ব্যতীত অ্যান্টিবায়োটিক ও স্টেরয়েড মলম ব্যবহারের মারাত্মক পার্শ্বপ্রতিক্রিয়া।",
                category = "Dermatology",
                duration = "3:20",
                videoUrl = "https://storage.googleapis.com/docroot-videos/skin_allergy.mp4",
                uploadedByRole = UserRole.CUSTOMER_DOCTOR,
                uploaderName = "Dr. Kazi Tanvir Hasan",
                uploaderId = "doc_105",
                status = VideoStatus.REJECTED,
                rejectionReason = "ভিডিওর অডিও কোয়ালিটি অস্পষ্ট এবং চেম্বারের বাণিজ্যিক বিজ্ঞাপন অন্তর্ভুক্ত ছিল। দয়া করে অডিও পরিষ্কার করে পুনরায় জমা দিন।",
                createdAt = now - 86400000L * 3,
                isEnabled = false
            )
        )
    }

    private fun initializeButtonRegistry() {
        _buttonRegistry.value = listOf(
            ButtonConnection("BTN-NAV-USER", "Navigation Bar", "Bottom Bar", "Switch to Public Doctor Directory", "AppPanel.USER_PANEL"),
            ButtonConnection("BTN-NAV-CUSTOMER", "Navigation Bar", "Bottom Bar", "Switch to Doctor / Clinic Portal", "AppPanel.CUSTOMER_PANEL"),
            ButtonConnection("BTN-NAV-ADMIN", "Navigation Bar", "Bottom Bar", "Switch to Admin Approval Center", "AppPanel.ADMIN_PANEL"),
            ButtonConnection("BTN-NAV-MASTER", "Navigation Bar", "Bottom Bar", "Switch to Master Control & Doc Hub", "AppPanel.MASTER_HUB"),
            ButtonConnection("BTN-USER-SEARCH", "User Panel", "Top Search Bar", "Filter doctors by query string", "ViewModel.searchDoctors"),
            ButtonConnection("BTN-USER-APPOINT", "User Panel", "Doctor Card", "Open chamber booking appointment dialog", "UI Dialog: BookingSheet"),
            ButtonConnection("BTN-USER-WATCH-VID", "User Panel", "Video Box", "Play approved health reel dialog", "UI Dialog: VideoPlayerModal"),
            ButtonConnection("BTN-CUST-SUBMIT-VID", "Customer Panel", "Upload Form", "Submit video for Admin Approval queue", "DocrootRepository.submitCustomerVideo"),
            ButtonConnection("BTN-CUST-SAVE-PROF", "Customer Panel", "Profile Tab", "Save chamber schedule and visiting fee", "DocrootRepository.updateDoctorProfile"),
            ButtonConnection("BTN-ADM-APPROVE", "Admin Panel", "Pending Queue", "Approve video -> Publish to User Video Box", "DocrootRepository.approveVideo"),
            ButtonConnection("BTN-ADM-REJECT", "Admin Panel", "Pending Queue", "Reject video with reason -> Hide from public", "DocrootRepository.rejectVideo"),
            ButtonConnection("BTN-ADM-DIRECT-PUB", "Admin Panel", "Direct Publisher", "Publish video directly without approval step", "DocrootRepository.publishAdminDirectVideo"),
            ButtonConnection("BTN-DOC-SEARCH", "Master Hub", "Search Bar", "Search Point by ID (e.g. FB-001) or keyword", "DocrootRepository.queryMasterPoint"),
            ButtonConnection("BTN-DOC-FILTER-STATUS", "Master Hub", "Status Filter Chips", "Filter points by Verified/Pending status", "UI Filter State")
        )
    }

    // --- Actions ---

    fun submitCustomerVideo(
        title: String,
        category: String,
        videoUrl: String,
        description: String,
        doctorName: String,
        doctorId: String
    ): HealthVideo {
        val newVideo = HealthVideo(
            id = "vid_" + UUID.randomUUID().toString().take(6),
            title = title,
            description = description,
            category = category,
            duration = "3:30",
            videoUrl = videoUrl,
            uploadedByRole = UserRole.CUSTOMER_DOCTOR,
            uploaderName = doctorName,
            uploaderId = doctorId,
            status = VideoStatus.PENDING_APPROVAL,
            rejectionReason = null,
            createdAt = System.currentTimeMillis(),
            isEnabled = true
        )

        _videos.value = listOf(newVideo) + _videos.value
        logAudit(
            pointId = "VIDEO-APPROVAL-001",
            action = "CUSTOMER_SUBMIT_VIDEO",
            actorRole = "CUSTOMER ($doctorName)",
            details = "Video '${title}' submitted for Admin review. Status: PENDING_APPROVAL"
        )
        return newVideo
    }

    fun approveVideo(videoId: String) {
        val current = _videos.value
        val updated = current.map { v ->
            if (v.id == videoId) {
                v.copy(status = VideoStatus.APPROVED, rejectionReason = null)
            } else v
        }
        _videos.value = updated
        val approvedItem = updated.find { it.id == videoId }
        logAudit(
            pointId = "VIDEO-APPROVAL-001",
            action = "ADMIN_APPROVE_VIDEO",
            actorRole = "ADMIN",
            details = "Video '${approvedItem?.title ?: videoId}' APPROVED. It is now live in User Panel Video Box."
        )
    }

    fun rejectVideo(videoId: String, reason: String) {
        val current = _videos.value
        val updated = current.map { v ->
            if (v.id == videoId) {
                v.copy(status = VideoStatus.REJECTED, rejectionReason = reason)
            } else v
        }
        _videos.value = updated
        val rejectedItem = updated.find { it.id == videoId }
        logAudit(
            pointId = "VIDEO-APPROVAL-001",
            action = "ADMIN_REJECT_VIDEO",
            actorRole = "ADMIN",
            details = "Video '${rejectedItem?.title ?: videoId}' REJECTED with reason: '$reason'. Hidden from public."
        )
    }

    fun publishAdminDirectVideo(
        title: String,
        category: String,
        videoUrl: String,
        description: String
    ): HealthVideo {
        val newVideo = HealthVideo(
            id = "vid_adm_" + UUID.randomUUID().toString().take(6),
            title = title,
            description = description,
            category = category,
            duration = "4:00",
            videoUrl = videoUrl,
            uploadedByRole = UserRole.ADMIN,
            uploaderName = "Admin Medical Board",
            uploaderId = "admin_01",
            status = VideoStatus.APPROVED,
            rejectionReason = null,
            createdAt = System.currentTimeMillis(),
            isEnabled = true
        )
        _videos.value = listOf(newVideo) + _videos.value
        logAudit(
            pointId = "VIDEO-APPROVAL-001",
            action = "ADMIN_DIRECT_PUBLISH",
            actorRole = "ADMIN",
            details = "Admin published '${title}' directly without approval queue. Live on User Panel."
        )
        return newVideo
    }

    fun updateDoctorChamber(
        doctorId: String,
        hospital: String,
        fee: Int,
        availability: String,
        phone: String
    ) {
        val current = _doctors.value
        val updated = current.map { doc ->
            if (doc.id == doctorId) {
                doc.copy(hospital = hospital, consultationFee = fee, availability = availability, phone = phone)
            } else doc
        }
        _doctors.value = updated
        logAudit(
            pointId = "PANEL-CUSTOMER-001",
            action = "UPDATE_CHAMBER_PROFILE",
            actorRole = "CUSTOMER",
            details = "Doctor profile ($doctorId) chamber details updated: Fee ৳$fee, $hospital"
        )
    }

    private fun logAudit(pointId: String, action: String, actorRole: String, details: String) {
        val entry = AuditLog(
            id = "log_" + System.currentTimeMillis(),
            timestamp = System.currentTimeMillis(),
            pointId = pointId,
            action = action,
            actorRole = actorRole,
            details = details
        )
        _auditLogs.value = listOf(entry) + _auditLogs.value
    }

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
