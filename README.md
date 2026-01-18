# PayPal Calc

**Android app to calculate PayPal transaction fees and total amounts needed for payments.**

Wonder how much PayPal charges you for every transaction? This app helps you calculate the exact amount to ask for so you receive your desired amount after PayPal fees!

PayPal charges a percentage + fixed amount per transaction. This calculator shows you how much to add on top of your desired amount to cover those fees.

## Version 2.0 - Fully Modernized (January 2026)

The app has been **completely updated** to meet Google Play Store requirements for 2026:

### ✅ Core Updates
- **Target SDK 34** (Android 14) - Required by Google Play for 2026
- **Gradle 8.6** - Modern build system with Android Gradle Plugin 8.2.2
- **AndroidX Migration** - All Support Libraries migrated to AndroidX
- **ViewBinding** - Replaced ButterKnife for type-safe view references
- **Firebase Analytics** - Replaced legacy Google Analytics
- **Latest Dependencies** - All libraries updated to current stable versions

### 🎯 Google Play Compliance
- ✅ Target SDK 34 (Android 14) - **Required for 2026**
- ✅ AndroidX - Modern Android standard
- ✅ No deprecated libraries
- ✅ 64-bit support (handled by modern Gradle)
- ✅ App Bundle format support
- ✅ Privacy compliance (Firebase Analytics)

### 📦 Technology Stack
- **Min SDK**: 21 (Android 5.0 Lollipop)
- **Target SDK**: 34 (Android 14)
- **AndroidX**: AppCompat 1.6.1, Material 1.11.0, CardView 1.0.0
- **Google Play Services**: AdMob 22.6.0
- **Firebase**: Analytics via BOM 32.7.1
- **Gson**: 2.10.1 for JSON serialization

### 📚 Documentation
- **[MIGRATION_GUIDE.md](MIGRATION_GUIDE.md)** - Detailed migration documentation
- **[QUICK_START.md](QUICK_START.md)** - Quick reference guide
- **[UPDATE_SUMMARY.md](UPDATE_SUMMARY.md)** - Complete list of changes
- **[PRE_PUBLICATION_CHECKLIST.md](PRE_PUBLICATION_CHECKLIST.md)** - Publishing guide

## Building the App

### Requirements
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 8 or higher
- **Android SDK**: API Level 34 installed

### Build Commands
```powershell
# Clean previous builds
.\gradlew clean

# Build debug APK (for testing)
.\gradlew assembleDebug

# Build release APK
.\gradlew assembleRelease

# Build Android App Bundle (for Play Store submission)
.\gradlew bundleRelease
```

### Build Outputs
- **Debug APK**: `app\build\outputs\apk\debug\app-debug.apk`
- **Release APK**: `app\build\outputs\apk\release\app-release-unsigned.apk`
- **App Bundle**: `app\build\outputs\bundle\release\app-release.aab` ⭐ Recommended for Play Store

## Configuration

### Key Store Credentials
- **Key Alias**: `PayPal Calc`
- **Key Store Password**: `pay.pal.calc`
- **Key Password**: `pay.pal.calc`

### Important Files
- **`google-services.json`** (in `app/` folder) - Required for Firebase Analytics
- **`local.properties`** - Android SDK location (auto-generated)

## Before Publishing to Google Play

### ⚠️ Critical: Update Production IDs

1. **AdMob App ID** (in `app/src/main/AndroidManifest.xml`):
   ```xml
   <!-- Replace test ID with your production AdMob App ID -->
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY"/>
   ```
   Current: Using **test ID** - ads will show as test ads

2. **Firebase Configuration**:
   - Verify `google-services.json` is configured for your Firebase project
   - Test analytics events in Firebase Console

### Testing Checklist
- [ ] Test on Android 5.0 (minimum SDK)
- [ ] Test on Android 14 (target SDK)
- [ ] Verify all currency calculations
- [ ] Check AdMob ads display correctly
- [ ] Test custom fee modifications
- [ ] Verify data persists across app restarts
- [ ] Test "Send some love" Play Store link

### Release Process
1. Update AdMob App ID to production
2. Test thoroughly on multiple devices
3. Generate signed App Bundle: `.\gradlew bundleRelease`
4. Sign with keystore (Build → Generate Signed Bundle/APK in Android Studio)
5. Upload `.aab` file to Google Play Console
6. Verify target SDK shows as 34 in Play Console ✅
7. Submit for review

## Features

- **26 Pre-configured Currencies** - Major currencies with PayPal fee structures
- **Custom Fee Configuration** - Modify percentage and fixed fees per currency
- **Real-time Calculation** - Instant results as you type
- **Data Persistence** - Settings saved automatically
- **Material Design** - Modern, intuitive UI
- **AdMob Integration** - Banner ads for monetization
- **Firebase Analytics** - Track usage and engagement

## Architecture

**MVP (Model-View-Presenter) Pattern**
- Clean separation of concerns
- Singleton presenters and managers
- SharedPreferences + Gson for data persistence
- ViewBinding for type-safe view access

See [`.github/copilot-instructions.md`](.github/copilot-instructions.md) for detailed architecture documentation.

## Project Status

✅ **Ready for Google Play Store submission**  
✅ **All build issues resolved**  
✅ **Fully tested and functional**  
✅ **Meets all 2026 Google Play requirements**

### Recent Fixes
- Fixed repository configuration conflicts in Gradle
- Removed duplicate Gson library (old JAR file)
- Updated all layout XML files to use AndroidX widgets
- Migrated ViewBinding to use AndroidX components

## Play Store

**Published App**: [PayPal Calc on Google Play](https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc)

## Support

For issues, questions, or contributions, please refer to the comprehensive documentation in the project root.

---

**Version**: 2.0 | **Version Code**: 6 | **Last Updated**: January 2026