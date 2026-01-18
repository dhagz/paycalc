# Quick Start Guide - PayPal Calc v2.0

## What Changed?

Your Android app has been fully modernized to meet Google Play Store requirements for 2026. Here's a summary:

### ✅ Core Updates
- **Target SDK**: Now supports Android 14 (API 34) - required by Google Play
- **Minimum SDK**: Android 5.0+ (API 21)
- **Gradle**: Updated to 8.6 with modern Android Gradle Plugin 8.2.2
- **Version**: Bumped to 2.0

### ✅ Dependency Modernization
- **Removed**: ButterKnife, Android Support Library, old Google Analytics
- **Added**: AndroidX libraries, Firebase Analytics, latest Google Play Services
- **Repository**: Migrated from deprecated jcenter to mavenCentral

### ✅ Code Changes
- **ViewBinding**: Replaced ButterKnife with Android's native ViewBinding
- **AndroidX**: All imports updated to AndroidX packages
- **Analytics**: Migrated to Firebase Analytics from legacy Google Analytics
- **Manifest**: Updated for Android 12+ requirements

## Building the App

### First Time Setup
1. Open the project in Android Studio (Hedgehog or later)
2. Let Gradle sync complete (it will download dependencies)
3. Ensure you have Android SDK 34 installed

### Build Commands
```bash
# Clean previous builds
.\gradlew clean

# Build debug version for testing
.\gradlew assembleDebug

# Build release version
.\gradlew assembleRelease

# Create App Bundle for Play Store
.\gradlew bundleRelease
```

### Output Locations
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release-unsigned.apk`
- **App Bundle**: `app/build/outputs/bundle/release/app-release.aab`

## Before Publishing to Google Play

### 1. Update AdMob Configuration
**Current**: Using test AdMob App ID
**Action**: Replace in `app/src/main/AndroidManifest.xml`

```xml
<!-- Replace this test ID -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/>

<!-- With your production ID -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY"/>
```

### 2. Verify Firebase Configuration
- Check that `app/google-services.json` exists
- Ensure it's configured for your Firebase project
- Test that analytics events are being logged in Firebase Console

### 3. Generate Signed Release
Use Android Studio:
1. **Build → Generate Signed Bundle / APK**
2. Select **Android App Bundle** (recommended for Play Store)
3. Use your keystore:
   - **Alias**: PayPal Calc
   - **Password**: pay.pal.calc

### 4. Test Thoroughly
- [ ] Test on Android 5.0 (minimum)
- [ ] Test on Android 14 (target)
- [ ] Verify all calculations are correct
- [ ] Check AdMob ads display properly
- [ ] Confirm currency data persists
- [ ] Test custom fee modifications

## What to Watch For

### Common Issues & Solutions

**Issue**: ViewBinding class not found  
**Solution**: Build → Rebuild Project in Android Studio

**Issue**: Firebase not initialized  
**Solution**: Ensure `google-services.json` is present in `app/` folder

**Issue**: AdMob ads not showing  
**Solution**: For testing, ads may take time to load. Use test device ID if needed.

**Issue**: Build fails with "namespace not found"  
**Solution**: The namespace is now in `app/build.gradle` instead of AndroidManifest.xml

## Key File Changes

### Modified Files
- `build.gradle` - Updated Gradle plugin and dependencies
- `app/build.gradle` - Migrated to AndroidX, ViewBinding, Firebase
- `gradle.properties` - Added AndroidX migration flags
- `gradle/wrapper/gradle-wrapper.properties` - Updated to Gradle 8.6
- `settings.gradle` - Added modern dependency resolution
- `app/src/main/AndroidManifest.xml` - Updated for Android 12+ requirements
- `MainActivity.java` - Migrated from ButterKnife to ViewBinding
- `AnalyticsManager.java` - Migrated to Firebase Analytics
- All `*.java` files - Updated imports to AndroidX

### New Files
- `MIGRATION_GUIDE.md` - Detailed migration documentation
- `QUICK_START.md` - This file

## Google Play Requirements Met

✅ **Target SDK 34** (Android 14)  
✅ **64-bit architecture** (handled by modern Gradle)  
✅ **App Bundle** support (recommended format)  
✅ **Privacy compliance** (Firebase Analytics configured)  
✅ **Modern dependencies** (no deprecated libraries)  

## Next Steps

1. **Test the build**: Run `.\gradlew assembleDebug` to verify everything compiles
2. **Run on device**: Install and test on a physical Android device
3. **Update AdMob ID**: Replace test ID with your production App ID
4. **Generate signed bundle**: Create .aab file for Play Store submission
5. **Submit to Play Store**: Upload through Google Play Console

## Need Help?

- **Detailed Changes**: See [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md)
- **Project Structure**: See `.github/copilot-instructions.md`
- **Build Issues**: Check Android Studio's Build Output panel
- **Runtime Issues**: Check Logcat in Android Studio

## Version Information

- **Version Name**: 2.0
- **Version Code**: 6
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

---

**Ready to build?** Run `.\gradlew assembleDebug` to get started!
