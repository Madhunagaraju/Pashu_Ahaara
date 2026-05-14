# Product Requirements Document (PRD)
## Pashu-Aahar: Smart Cattle Nutrition Calculator

---

## 1. Product Vision & Objective
**Objective:** To empower small-scale dairy farmers by providing an accessible, offline-first mobile application that calculates scientifically balanced, cost-effective cattle feed recipes using locally available grains.
**Vision:** To reduce farmer dependency on expensive commercial cattle feed, optimize cattle nutrition to maximize milk yield, and provide clear visibility into daily, monthly, and yearly financial savings.

---

## 2. Target Audience
- **Primary Users:** Small to medium-scale dairy farmers in rural and semi-urban areas.
- **Secondary Users:** Veterinary students, dairy consultants, and agricultural extension workers assisting farmers.
- **User Personas:** 
  - *Ramesh (Dairy Farmer):* Owns 5 cows. Wants to reduce the cost of feeding his cows but doesn't know the exact proportion of grains to mix to achieve the right protein and energy levels. Has a basic Android smartphone and limited internet connectivity.

---

## 3. Key Features & User Stories

### 3.1 Authentication & Onboarding
- **Feature:** Secure login via Google or Anonymous Guest mode.
- **User Story:** As a farmer, I want to log in quickly without a complex password so I can access my saved cattle profiles across devices, or skip login entirely if I just want to test the app.

### 3.2 Cow Profile Management
- **Feature:** Create, edit, and delete detailed cattle profiles.
- **Data Points:** Name, Breed (Desi, Jersey, HF, Crossbreed), Age, Body Weight (kg), Daily Milk Yield (Liters).
- **User Story:** As a farmer, I want to save individual profiles for each of my cows because a high-yielding Jersey requires a different nutritional mix than a low-yielding Desi cow.

### 3.3 Dynamic Recipe Generator (Core Engine)
- **Feature:** Algorithm-driven feed formulation based on Dry Matter Intake (DMI), Crude Protein (CP), and Total Digestible Nutrients (TDN) formulas.
- **User Story:** As a farmer, I want the app to tell me exactly how many kilograms of Maize, Cottonseed Cake, and Wheat Bran I should mix to fulfill my cow's specific daily nutritional requirement.

### 3.4 Cost & Savings Analytics
- **Feature:** Dynamic inputs for market feed price and local grain prices, paired with visual charts (Bar, Pie, Line) using MPAndroidChart.
- **User Story:** As a farmer, I want to visually compare what I spend on commercial feed versus what I would spend on the home-made recipe, so I can see my exact monthly and yearly savings.

### 3.5 Veterinary Tips Library
- **Feature:** Curated list of best practices regarding hygiene, disease prevention, and fodder storage. Includes offline bookmarking.
- **User Story:** As a farmer with poor internet, I want to bookmark important veterinary advice so I can read it later while working in the shed.

---

## 4. Technical Requirements & Architecture

- **Platform:** Native Android (Min SDK 24, Target SDK 34)
- **UI Toolkit:** Jetpack Compose (Material Design 3)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) + Clean Architecture
- **Dependency Injection:** Dagger-Hilt
- **Local Persistence:** Room Database (Offline-first data strategy)
- **Preferences/Bookmarks:** DataStore Preferences
- **Charting Library:** MPAndroidChart
- **Authentication:** Firebase Auth

---

## 5. Non-Functional Requirements (NFRs)
- **Offline Capability:** The core algorithm, profile management, and saved recipes must function 100% offline.
- **Performance:** Complex nutritional calculations and recipe matrix generations must occur off the main thread (using Coroutines) to prevent UI freezing.
- **Usability:** The UI must be highly intuitive, utilizing large buttons, clear iconography, and simple terminology suitable for agricultural workers.
- **Reliability:** App must survive process death and gracefully handle schema migrations without losing farmer data.

---

## 6. Future Scope (V2.0)
- **Localization:** Full support for regional languages (Hindi, Kannada, Telugu, etc.).
- **Cloud Sync:** Sync local Room database to Firebase Firestore for cross-device cloud backup.
- **Notification Reminders:** Reminders for vaccinations, deworming, and milking schedules.
- **Community Forum:** A feature allowing farmers to ask questions to local veterinary experts.
