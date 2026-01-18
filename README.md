# PayPal Calc

Wonder how much PayPal charges you for every transaction? This app can help you!

PayPal will charge for a certain percentage and amount.

This calculator will give you the amount you need on top of the amount for the transaction.

## Version 2.0 - Modern Android Update

The app has been updated to meet Google Play Store requirements for 2026:
- Target SDK 34 (Android 14)
- Migrated to AndroidX
- Updated to modern Gradle 8.6
- Replaced ButterKnife with ViewBinding
- Migrated to Firebase Analytics
- Updated all dependencies to latest versions

See [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) for detailed changes.

## Building the App

### Requirements
- Android Studio Hedgehog or later
- JDK 8 or higher
- Android SDK 34

### Build Commands
```bash
# Clean build
.\gradlew clean

# Build debug APK
.\gradlew assembleDebug

# Build release APK (for testing)
.\gradlew assembleRelease

# Build Android App Bundle (for Play Store)
.\gradlew bundleRelease
```

### Key Store
* **Key Alias:** PayPal Calc
* **Key Store Password:** pay.pal.calc
* **Key Password:** pay.pal.calc

### Before Publishing
1. Update AdMob App ID in AndroidManifest.xml with your production ID
2. Ensure google-services.json is configured with your Firebase project
3. Test on devices running Android 5.0 through Android 14
4. Generate signed App Bundle (.aab) for Play Store submission

### Play Store Link
[https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc](https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc)