# APP MASTER DOCUMENTATION
**PROJECT:** Docroot  
**DOMAIN:** Doctor Directory Platform (ডাক্তার ডিরেক্টরি প্ল্যাটফর্ম)  
**VERSION:** 1.0.0-PROTOTYPE  
**LAST UPDATED:** 2026-10-27  
**MAINTAINER:** Kushank Enterprise / AI Studio Engineering  

---

## 🎯 OBJECTIVE (উদ্দেশ্য)
অ্যাপের প্রতিটি Panel, Firebase Connection, Button, Feature, Database এবং Approval System-এর জন্য আলাদা Documentation সংরক্ষণ করা। প্রতিটি পয়েন্টের একটি নির্দিষ্ট নাম ও Unique ID থাকবে। 
ভবিষ্যতে যেকোনো পয়েন্টের নাম বা ID উল্লেখ করলে, সেই পয়েন্টের সম্পূর্ণ নিয়ম, পূর্ববর্তী Configuration এবং সংশ্লিষ্ট Connection প্রদান করা হবে।

---

## 📋 STATUS CODES DEFINITION
- `VERIFIED`: বাস্তবে কোডে ও আর্কিটেকচারে সম্পূর্ণ পরীক্ষিত ও কার্যকর (Active & Working).
- `PENDING_CONFIGURATION`: আর্কিটেকচার ও স্কিমা প্রস্তুত, ক্লাউড/প্রজেক্ট ক্রেডেনশিয়াল বা সার্ভিস কানেকশন অপেক্ষা করছে (Ready for Binding).
- `NOT_IMPLEMENTED`: ভবিষ্যতের জন্য পরিকল্পিত, এখনো কার্যকর করা হয়নি (Planned).

---

## POINT 01: APP MASTER CONTROL
**ID:** `APP-001`  
**STATUS:** `VERIFIED`  
**SCOPE & ARCHITECTURE:**
1. সম্পূর্ণ অ্যাপের মূল কাঠামো ও Architecture: Jetpack Compose + MVVM + StateFlow + Centralized Repository Layer.
2. প্যানেলসমূহ:
   - User Panel (`PANEL-USER-001`): পাবলিক ডিরেক্টরি এবং হেলথ ভিডিও বক্স।
   - Customer Panel (`PANEL-CUSTOMER-001`): ডাক্তার/ক্লিনিক প্রোফাইল ম্যানেজমেন্ট ও ভিডিও সাবমিশন।
   - Admin Panel (`PANEL-ADMIN-001`): অনুমোদন কেন্দ্র (Approval Queue) এবং ডিরেক্ট পাবলিশার।
   - Master Control & Status Dashboard (`DOC-RETRIEVAL-001`): লাইভ পয়েন্ট অডিট, বাটন রেজিস্ট্রি এবং সিস্টেম হেলথ।
3. ইন্টারকানেকশন রুল: কোনো ফিচার পরিবর্তনের পূর্বে নির্ভরশীল অন্যান্য পয়েন্ট স্বয়ংক্রিয়ভাবে অডিট করা হয়।

---

## POINT 02: FIREBASE CONNECTION
**ID:** `FB-001`  
**STATUS:** `PENDING_CONFIGURATION`  
**SCOPE & SPECIFICATION:**
1. **Firebase Authentication:**
   - User Sign-In / Doctor Auth via Jetpack Credential Manager (Google Sign-In).
   - Role-based authentication (Public User, Registered Doctor/Customer, System Administrator).
2. **Firestore Database:**
   - ডাটাবেস স্কিমা ও রিলেশনশিপ `DB-001` এ সংজ্ঞায়িত।
   - রিড/রাইট রুলস: Public Read on approved items; Authenticated write on customer drafts; Admin-only write on approvals.
3. **Firebase Storage:**
   - স্টোরেজ পাথ: `/doctors/{doctorId}/photos/` এবং `/videos/{videoId}/content.mp4`.
   - ভিডিও আপলোড সরাসরি Cloud Storage-এ সংরক্ষিত হবে এবং URL Firestore-এ যুক্ত হবে।
4. **Current State:** 
   - Local reactive state flow ও structured repository সক্রিয়।
   - Firebase libraries pre-configured in Gradle BOM; Live cloud binding awaiting user prompt activation.

---

## POINT 03: ADMIN PANEL
**ID:** `PANEL-ADMIN-001`  
**STATUS:** `VERIFIED`  
**SCOPE & CAPABILITIES:**
1. **UI & Menus:**
   - Pending Video Approval Queue (`LIST-PENDING-VIDEOS`).
   - Doctor Verification / Registration Queue (`LIST-PENDING-DOCTORS`).
   - Admin Direct Video Publisher (`FORM-ADMIN-DIRECT-PUBLISH`).
   - Feature Toggles & Platform Statistics (`DASHBOARD-STATS`).
2. **Actions & Permissions:**
   - `BTN-ADM-APPROVE`: Customer সাবমিশন তাৎক্ষণিকভাবে Approve করে এবং User Panel-এ দৃশ্যমান করে।
   - `BTN-ADM-REJECT`: রিজেক্টের কারণসহ স্ট্যাটাস পরিবর্তন করে; ইউজার প্যানেল থেকে প্রত্যাহার করে।
   - `BTN-ADM-PUBLISH-DIRECT`: কোনো মধ্যবর্তী অনুমোদন ছাড়াই সরাসরি ভিডিও পাবলিশ করে।
3. **Database Targets:**
   - Collections: `videos`, `doctors`, `audit_logs`.

---

## POINT 04: USER PANEL
**ID:** `PANEL-USER-001`  
**STATUS:** `VERIFIED`  
**SCOPE & CAPABILITIES:**
1. **Doctor Directory Screen:**
   - সার্চ বার: ডাক্তার নাম, ক্লিনিক বা হাসপাতালের নাম দ্বারা তাৎক্ষণিক অনুসন্ধান (`INPUT-USER-SEARCH`).
   - স্পেশালিটি ফিল্টার চিপস: Cardiology, Neurology, Pediatrics, Dermatology, General Medicine, Orthopedics, Gynecology (`CHIP-SPECIALTY`).
   - শহর/লোকেশন ফিল্টার: ঢাকা, চট্টগ্রাম, সিলেট, রাজশাহী, খুলনা ইত্যাদি (`CHIP-CITY`).
   - ডাক্তার কার্ড: নাম, ডিগ্রি, চেম্বার, ভিজিট ফি, রেটিং, অ্যাপয়েন্টমেন্ট বাটন (`CARD-DOCTOR-ITEM`).
2. **Health Video Showcase (ভিডিও বক্স):**
   - শুধুমাত্র Admin অনুমোদিত (`APPROVED`) অথবা Admin কর্তৃক সরাসরি আপলোডকৃত ভিডিও প্রদর্শিত হয়।
   - কোনো `PENDING_APPROVAL` বা `REJECTED` ভিডিও ইউজার প্যানেলে দেখতে পায় না।
3. **Database Connection:**
   - `videos` collection query: `status == "APPROVED" && isEnabled == true`.
   - `doctors` collection query: `isVerified == true`.

---

## POINT 05: CUSTOMER PANEL (DOCTOR PORTAL)
**ID:** `PANEL-CUSTOMER-001`  
**STATUS:** `VERIFIED`  
**SCOPE & CAPABILITIES:**
1. **Doctor / Clinic Portal UI:**
   - Customer ID & Verification Status Badge (`CUST-ID-BADGE`).
   - Profile Editor: চেম্বার ঠিকানা, ভিজিট ফি, অভিজ্ঞতার বছর, স্পেশালিটি (`FORM-CUST-PROFILE`).
   - Video Submission Form (`FORM-CUST-VIDEO-SUBMIT`): Title, Health Category, Video URL/Storage ref, Description.
2. **Security & Approval Constraints:**
   - কাস্টমার শুধুমাত্র নিজের আপলোড করা কন্টেন্ট দেখতে ও এডিট করতে পারবে।
   - কাস্টমার নিজের approval status পরিবর্তন করতে পারবে না (Locked to `PENDING_APPROVAL`).
   - সাবমিট করার পর স্বয়ংক্রিয়ভাবে Admin Review Queue (`PANEL-ADMIN-001`)-এ জমা হবে।

---

## POINT 06: VIDEO UPLOAD & APPROVAL
**ID:** `VIDEO-APPROVAL-001`  
**STATUS:** `VERIFIED`  
**LIFECYCLE & STATE MACHINE:**
```
[Customer Upload] ──────► (Status: PENDING_APPROVAL) 
                               │
               ┌───────────────┴───────────────┐
               ▼                               ▼
       [Admin: Approve]                [Admin: Reject]
               │                               │
               ▼                               ▼
      (Status: APPROVED)              (Status: REJECTED)
               │                               │
               ▼                               ▼
  Visible in User Panel Video Box      Hidden from Public
```
- **Admin Direct Upload Path:** Admin সরাসরি আপলোড করলে তাৎক্ষণিক `APPROVED` ফ্ল্যাগসহ User Panel-এ প্রদর্শিত হবে।
- **Storage Policy:** Metadata in Firestore `videos`; Video payload in Cloud Storage / streaming links.

---

## POINT 07: DATABASE STRUCTURE
**ID:** `DB-001`  
**STATUS:** `VERIFIED`  
**COLLECTIONS & SCHEMA:**

### 1. Collection: `doctors`
- `id` (String): ইউনিক ডাক্তার আইডি (যেমন: `doc_101`)
- `name` (String): ডাক্তারের পূর্ণ নাম
- `specialty` (String): বিশেষজ্ঞ বিভাগ (Cardiology, Dermatology, etc.)
- `degrees` (String): শিক্ষাগত যোগ্যতা (MBBS, FCPS, MD)
- `hospital` (String): হাসপাতাল বা চেম্বার
- `city` (String): জেলা/শহর (Dhaka, Chittagong, etc.)
- `consultationFee` (Int): পরামর্শ ফি (টাকা)
- `rating` (Double): গড় রেটিং (৪.৮, ৫.০)
- `reviewsCount` (Int): মোট রিভিউ
- `phone` (String): চেম্বার সিরিয়াল নম্বর
- `isVerified` (Boolean): অ্যাডমিন ভেরিফিকেশন স্ট্যাটাস
- `availability` (String): চেম্বার সময়সূচী

### 2. Collection: `videos`
- `id` (String): ইউনিক ভিডিও আইডি (যেমন: `vid_301`)
- `title` (String): ভিডিও শিরোনাম
- `description` (String): স্বাস্থ্য বার্তা ও বিবরণ
- `category` (String): টপিক (Heart Health, Nutrition, Child Care)
- `duration` (String): সময়কাল (যেমন: `3:45`)
- `videoUrl` (String): স্টোরেজ বা স্ট্রিমিং লিঙ্ক
- `thumbnailUrl` (String): থাম্বনেইল
- `uploadedByRole` (String): `"CUSTOMER"` বা `"ADMIN"`
- `uploaderName` (String): উপস্থাপক বা ডাক্তারের নাম
- `uploaderId` (String): আপলোডকারীর ইউনিক আইডি
- `status` (String): `"PENDING_APPROVAL"`, `"APPROVED"`, `"REJECTED"`
- `rejectionReason` (String, Optional): রিজেক্টের কারণ
- `createdAt` (Long): টাইমস্ট্যাম্প
- `isEnabled` (Boolean): পাবলিক ডিসপ্লে টগল

### 3. Collection: `audit_logs`
- `logId` (String): অডিট লগ ট্র্যাকার
- `timestamp` (Long): ঘটনার সময়
- `pointId` (String): সংশ্লিষ্ট পয়েন্ট আইডি (যেমন `VIDEO-APPROVAL-001`)
- `action` (String): সম্পাদিত অ্যাকশন (APPROVE, REJECT, UPDATE, CREATE)
- `actorRole` (String): ADMIN / CUSTOMER / SYSTEM
- `details` (String): বিস্তারিত তথ্য

---

## POINT 08: BUTTON & FEATURE CONNECTION
**ID:** `UI-CONNECTION-001`  
**STATUS:** `VERIFIED`  
**REGISTRY TABLE:**

| Button ID | Screen / Panel | Action / Description | Target Service / State |
| :--- | :--- | :--- | :--- |
| `BTN-NAV-USER` | Global Navigation | Switch to User Panel (Directory) | Nav State: `USER_PANEL` |
| `BTN-NAV-CUSTOMER` | Global Navigation | Switch to Doctor/Customer Portal | Nav State: `CUSTOMER_PANEL` |
| `BTN-NAV-ADMIN` | Global Navigation | Switch to Admin Approval Panel | Nav State: `ADMIN_PANEL` |
| `BTN-NAV-MASTER` | Global Navigation | Switch to Master Control & Doc Hub | Nav State: `MASTER_HUB` |
| `BTN-USER-SEARCH` | User Panel | Filter doctor list by keyword | ViewModel: `onSearchQueryChange` |
| `BTN-USER-APPOINT` | User Panel | Open appointment booking modal | Dialog: Chamber Booking |
| `BTN-USER-WATCH-VID` | User Panel | Open video playback modal | Dialog: Video Box Player |
| `BTN-CUST-SUBMIT-VID` | Customer Panel | Submit video for Admin Review | Repository: `submitCustomerVideo` (Pending) |
| `BTN-CUST-SAVE-PROF` | Customer Panel | Update doctor chamber details | Repository: `updateDoctorProfile` |
| `BTN-ADM-APPROVE` | Admin Panel | Approve pending video to User Panel | Repository: `approveVideo(id)` |
| `BTN-ADM-REJECT` | Admin Panel | Reject pending video with remarks | Repository: `rejectVideo(id)` |
| `BTN-ADM-DIRECT-PUB` | Admin Panel | Direct publish video without review | Repository: `publishAdminVideo` |
| `BTN-DOC-SEARCH` | Master Hub | Lookup Point ID or Bengali keyword | ViewModel: `filterDocumentation` |
| `BTN-DOC-STATUS-TOGGLE` | Master Hub | Filter by status (Verified/Pending) | ViewModel: `setDocStatusFilter` |

---

## POINT 09: CHANGE HISTORY
**ID:** `CHANGELOG-001`  
**STATUS:** `VERIFIED`  
**HISTORY ENTRIES:**
- **v1.0.0 (2026-10-27):**
  - Initial creation of Docroot Master Documentation Architecture.
  - Verification of 4 distinct functional panels: User Panel, Customer Panel, Admin Panel, and Master Hub.
  - Implementation of Video Approval State Machine (`VIDEO-APPROVAL-001`).
  - Mapping of all 12 Master Points (`APP-001` to `DOC-STORAGE-001`) with live status dashboard.
  - Setup of Button Registry with unique `UI-CONNECTION-001` testTags and IDs.

---

## POINT 10: DOCUMENTATION RETRIEVAL
**ID:** `DOC-RETRIEVAL-001`  
**STATUS:** `VERIFIED`  
**USAGE PROTOCOL:**
- যেকোনো মুহূর্তে ইউজার `"FB-001 দেখাও"`, `"PANEL-USER-001"`, `"VIDEO-APPROVAL-001"` অথবা সংশ্লিষ্ট কীওয়ার্ড বললে Master Hub এবং কোডবেস থেকে তৎক্ষণাৎ সঠিক পয়েন্টের রুলস, কানেকশন এবং বর্তমান স্ট্যাটাস উপস্থাপিত হয়।
- অ্যাপের ভিতরে একটি সার্বক্ষণিক সার্চ ও ফিল্টার বার দেওয়া হয়েছে যাতে তাত্ক্ষণিকভাবে যেকোনো আইডি সার্চ করা যায়।

---

## POINT 11: SAFE UPDATE RULES
**ID:** `SAFE-UPDATE-001`  
**STATUS:** `VERIFIED`  
**MANDATORY SAFETY PROTOCOL:**
1. কোনো পরিবর্তনের পূর্বে সংশ্লিষ্ট পয়েন্টের ডকুমেন্টেশন ও বর্তমান কোড যাচাই করতে হবে।
2. কোনো পয়েন্ট আপডেট করার সময় অন্য পয়েন্টের কার্যকর ব্যবস্থা অক্ষুণ্ণ রাখতে হবে।
3. ইউজারের অনুমতি ব্যতীত বিদ্যমান ডাটাবেজ স্ট্রাকচার বা ফিচার মুছে ফেলা যাবে না।
4. কোড পরিবর্তনের পর `compile_applet` চালিয়ে যাচাই নিশ্চিত করতে হবে।

---

## POINT 12: MASTER DOCUMENTATION STORAGE
**ID:** `DOC-STORAGE-001`  
**STATUS:** `VERIFIED`  
**LOCATION & REPOSITORIES:**
1. ফাইল স্টোরেজ: `/MASTER_DOCUMENTATION.md` (Root Canonical Reference)
2. ইন-অ্যাপ কোড রেজিস্ট্রি: `com.example.data.MasterDocumentationRepository`
3. লাইভ কন্ট্রোল ড্যাশবোর্ড: Docroot Master Control & Audit Dashboard UI
