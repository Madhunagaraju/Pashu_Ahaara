# Pashu-Aahar - Cattle Nutrition Calculator

Pashu-Aahar is an offline-first Android app built for small dairy farmers to prepare balanced cattle feed using local grains at lower cost.

## Problem Statement

Small farmers often buy expensive branded feed without guidance for balanced home-made feed. This app helps generate practical feed recipes and compares home-made cost vs market feed cost.

## Tech Stack

- Kotlin 1.9.x
- Android minSdk 24, targetSdk 34
- Jetpack Compose (Material Design 3)
- MVVM + Clean Architecture
- Hilt (Dependency Injection)
- Room (offline local database)
- Kotlin Coroutines + Flow
- Navigation Compose
- MPAndroidChart
- DataStore Preferences (offline bookmarks)

## Project Structure

```text
com.pashuaahar
├── data
│   ├── local
│   │   ├── dao
│   │   ├── database
│   │   └── entity
│   └── repository
├── domain
│   ├── model
│   ├── repository
│   └── usecase
├── presentation
│   ├── components
│   ├── navigation
│   ├── screens
│   │   ├── home
│   │   ├── cow
│   │   ├── recipe
│   │   └── tips
│   └── theme
├── di
└── util
```

## Features Implemented

1. **Home Dashboard**
   - Welcome message
   - Total cows
   - Monthly savings estimate
   - Quick actions
   - Recent recipe activity

2. **Cow Profile Management**
   - Add cow profile with Stepper UI
   - Edit existing cow profile
   - List saved cows from Room
   - Swipe-to-delete card action
   - Fields: name, breed, age, weight, milk yield

3. **Recipe Generator**
   - Dynamic recipe generation based on selected cow and grain data
   - Uses nutrition requirements and local grain nutrient profiles
   - Displays recipe text, protein %, TDN, cost/kg, daily cost

4. **Cost Tracker**
   - User market feed price input
   - Grain-wise editable price input
   - Home-made vs market daily cost
   - Monthly and yearly savings

5. **Charts (MPAndroidChart)**
   - Bar chart (daily comparison)
   - Pie chart (monthly feed cost vs savings)
   - Line chart (yearly cumulative savings trend)

6. **Veterinary Tips**
   - Category-based tips
   - Search
   - Expandable cards
   - Bookmark support (persisted offline with DataStore)
   - Video links

## Business Logic

### Dry Matter Intake (DMI)

`DMI = BodyWeight × 0.03`

### Crude Protein Requirement (CP)

`CP = (MilkYield × 80g) + (BodyWeight × 0.08kg)`

### TDN Requirement

`TDN = (MilkYield × 50) + (BodyWeight × 0.05)`

The recipe engine allocates mix quantities from locally available grains and computes final nutrition and cost outputs.

## Default Grain Data

- Maize: Protein 9%, TDN 80%, Price Rs 25/kg
- Cottonseed Cake: Protein 23%, TDN 75%, Price Rs 35/kg
- Mustard Cake: Protein 38%, TDN 70%, Price Rs 40/kg
- Wheat Bran: Protein 15%, TDN 65%, Price Rs 20/kg
- Rice Bran: Protein 12%, TDN 70%, Price Rs 18/kg
- Soybean Meal: Protein 45%, TDN 85%, Price Rs 55/kg

## App Flow

1. User opens app to Home dashboard.
2. User adds cows in My Cows tab using stepper form.
3. User opens Recipe tab, selects cow, sets market price, and generates recipe.
4. App computes nutrition + cost and renders charts for savings.
5. User explores tips in Tips tab and bookmarks useful items for offline access.

## Architecture Explanation

- **Presentation**
  - Compose screens + ViewModels
  - Immutable UI state via `StateFlow`
  - Sealed UI events for user actions
- **Domain**
  - Pure models, repository contracts, and use cases
  - Business rules and calculations
- **Data**
  - Room entities/DAO/database
  - Repository implementations map data models to domain models
  - Offline-first storage and retrieval

## Setup Instructions

1. Open project in Android Studio Hedgehog or newer.
2. Ensure Android SDK 34 and JDK 17 are available.
3. If Gradle wrapper files are missing, generate wrapper:
   - `gradle wrapper`
4. Sync Gradle and run app on emulator/device (API 24+).

## Offline Behavior

- Cow, grain, and recipe data is stored in Room.
- Default grains are seeded into local DB on first launch.
- Tips bookmarks are stored in DataStore preferences.
- Core calculations run locally without internet.

## Notes for Internship Evaluation

- Uses Kotlin + Compose + Material 3 only.
- Uses MVVM + Clean Architecture.
- Dynamic recipe changes with cow profile and milk yield.
- App design is simple and farmer-friendly for practical field use.
