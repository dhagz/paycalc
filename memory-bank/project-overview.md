# Project Overview - PayPal Calc

## What is PayPal Calc?

PayPal Calc is an Android calculator app that helps users determine the total amount they need to charge to cover PayPal's transaction fees. When a user wants to receive a specific amount after fees, the app calculates how much extra needs to be added to cover PayPal's percentage and fixed charges.

## Project Identity

- **Package Name**: `com.walng.dhagz.paypalcalc`
- **App Name**: PayCalc
- **Current Version**: 1.4 (versionCode: 5)
- **Play Store**: [Published and Active](https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc)
- **Author**: Dhagz (@walng)
- **Created**: March 2016

## Core Purpose

The app solves a specific problem: **How much should I charge to receive X amount after PayPal fees?**

### Example Calculation
If you want to receive $100 and PayPal charges 4.4% + $0.30:
- You need to charge: $104.98
- PayPal takes: $4.98 (fee)
- You receive: $100.00 (desired amount)

## Technical Stack

### Platform
- **Target SDK**: Android 6.0 (API 23)
- **Min SDK**: Android 4.0.3 (API 15)
- **Build System**: Gradle 1.5.0
- **Language**: Java

### Key Technologies
1. **MVP Architecture** - Model-View-Presenter pattern
2. **ButterKnife 7.0.1** - View injection
3. **Gson** - JSON serialization for data persistence
4. **SharedPreferences** - Local data storage
5. **Google Play Services**:
   - AdMob for banner ads
   - Google Analytics for tracking

### Project Structure
```
PayCalc/
├── app/                          # Main application module
│   ├── src/main/java/.../paypalcalc/
│   │   ├── models/              # Data classes (Currency)
│   │   ├── views/               # View interfaces (MVP)
│   │   ├── presenters/          # Business logic (MVP)
│   │   ├── providers/           # Data providers (persistence)
│   │   ├── managers/            # Utility managers (Analytics)
│   │   ├── adapters/            # UI adapters (Spinner)
│   │   ├── MainActivity.java   # Single activity app
│   │   └── ChangeDialog.java   # Custom dialog utility
│   ├── src/main/res/            # Android resources
│   └── build.gradle             # App dependencies
├── build.gradle                  # Project build config
├── .github/
│   └── copilot-instructions.md  # AI agent guidance
└── memory-bank/                  # Comprehensive documentation
```

## App Features

### Primary Features
1. **Currency Selection** - 26 predefined currencies with PayPal fee structures
2. **Fee Calculation** - Real-time calculation as user types amount
3. **Custom Fees** - Users can modify percentage and fixed fees per currency
4. **Persistent Storage** - User modifications saved locally
5. **Custom Currency** - Option to create custom fee structure

### Supported Currencies
26 major currencies including:
- Philippine Peso (PHP) - Default selection
- US Dollar (USD), Euro (EUR), British Pound (GBP)
- Australian Dollar (AUD), Canadian Dollar (CAD)
- Japanese Yen (JPY) - Different fee structure (3.9% + 40 JPY)
- Plus 19 other international currencies
- Custom currency option

### UI Components
- **Currency Spinner** - Dropdown selection
- **Amount Input** - Decimal number input with validation
- **Fee Display Cards** - Shows percentage, fixed, and total fees
- **Result Display** - Shows total amount to charge
- **Rate Prompt** - Encourages 5-star ratings
- **AdMob Banner** - Monetization

## Development History

Based on code timestamps:
- **Initial Development**: March 25, 2016
- **Last Major Update**: March 27, 2016
- **Active Status**: Published on Google Play Store

## Monetization Strategy

1. **AdMob Banner Ads** - Bottom banner in main activity
2. **Google Analytics** - User behavior tracking
3. **Play Store Ratings** - Prominent "Rate 5 stars" call-to-action

## Key Design Decisions

1. **Single Activity Design** - Simplified navigation
2. **Singleton Pattern** - All managers and presenters use lazy initialization
3. **MVP Pattern** - Clear separation of concerns
4. **SharedPreferences Storage** - Lightweight, no database needed
5. **ButterKnife** - Reduces boilerplate for view binding
6. **No Testing Framework** - JUnit dependencies commented out
7. **No ProGuard** - Minification disabled in release builds
8. **Custom Application Class** - For Google Analytics tracker initialization
