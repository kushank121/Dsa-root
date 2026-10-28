package com.example.model

enum class PointStatus(val label: String, val labelBn: String) {
    VERIFIED("Verified", "যাচাইকৃত ও সক্রিয়"),
    PENDING_CONFIGURATION("Pending Configuration", "কনফিগারেশন অপেক্ষমান"),
    NOT_IMPLEMENTED("Not Implemented", "পরিকল্পিত / অপ্রস্তুত")
}

enum class VideoStatus(val label: String, val labelBn: String) {
    PENDING_APPROVAL("Pending Approval", "অনুমোদনের অপেক্ষায়"),
    APPROVED("Approved", "অনুমোদিত ও প্রকাশিত"),
    REJECTED("Rejected", "বাতিলকৃত")
}

enum class UserRole {
    PUBLIC_USER,
    CUSTOMER_DOCTOR,
    ADMIN
}

enum class AppPanel(val title: String, val titleBn: String) {
    USER_PANEL("User Directory", "ইউজার প্যানেল"),
    CUSTOMER_PANEL("Doctor Portal", "কাস্টমার প্যানেল"),
    ADMIN_PANEL("Admin Approval", "অ্যাডমিন প্যানেল"),
    MASTER_HUB("Master Control", "মাস্টার ড্যাশবোর্ড")
}

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val degrees: String,
    val hospital: String,
    val city: String,
    val consultationFee: Int,
    val rating: Double,
    val reviewsCount: Int,
    val phone: String,
    val isVerified: Boolean = true,
    val availability: String,
    val bio: String
)

data class HealthVideo(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val duration: String,
    val videoUrl: String,
    val uploadedByRole: UserRole,
    val uploaderName: String,
    val uploaderId: String,
    val status: VideoStatus,
    val rejectionReason: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isEnabled: Boolean = true
)

data class MasterPoint(
    val id: String,
    val pointNumber: Int,
    val titleEn: String,
    val titleBn: String,
    val status: PointStatus,
    val purposeBn: String,
    val scopeItems: List<String>,
    val connectedPoints: List<String>,
    val currentConfig: String,
    val updateHistory: List<String>
)

data class ButtonConnection(
    val buttonId: String,
    val panel: String,
    val screenLocation: String,
    val action: String,
    val targetService: String
)

data class AuditLog(
    val id: String,
    val timestamp: Long,
    val pointId: String,
    val action: String,
    val actorRole: String,
    val details: String
)
