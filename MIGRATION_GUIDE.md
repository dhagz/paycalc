# Migration Guide - PayPal Calc v2.0

## Overview
This document describes the migration from the legacy Android project structure to modern Android development standards required for Google Play Store publishing in 2026.

## Major Changes

### 1. SDK & Build Tools Updates
- **compileSdk**: 23 → 34 (Android 14)
- **targetSdk**: 23 → 34 (Android 14)
- **minSdk**: 15 → 21 (Android 5.0)
- **Gradle**: 2.10 → 8.6
- **Android Gradle Plugin**: 1.5.0 → 8.2.2
- **Version**: 1.4 → 2.0 (versionCode 5 → 6)

### 2. Dependency Migration

#### Removed Dependencies
- ButterKnife 7.0.1 (replaced with ViewBinding)
- Android Support Library (all modules)
- Google Play Services Analytics (replaced with Firebase Analytics)

#### New/Updated Dependencies
- **AndroidX**:
  - appcompat: 1.6.1
  - material: 1.11.0
  - cardview: 1.0.0
  - recyclerview: 1.3.2
  - constraintlayout: 2.1.4

- **Google Play Services**:
  - play-services-ads: 22.6.0

- **Firebase**:
  - firebase-bom: 32.7.1
  - firebase-analytics (latest via BOM)

- **Gson**: 2.10.1

### 3. Repository Migration
- **Removed**: jcenter() (deprecated)
- **Added**: google(), mavenCentral()

### 4. Code Modernization

#### ViewBinding
Replaced ButterKnife with Android's native ViewBinding:
- Enabled in `build.gradle`: `buildFeatures { viewBinding true }`
- MainActivity now uses `ActivityMainBinding`
- All `@Bind` annotations removed
- Manual view binding in onCreate with `binding = ActivityMainBinding.inflate(getLayoutInflater())`

#### AndroidX Migration
All Android Support Library imports updated to AndroidX:
- `android.support.v7.app.AppCompatActivity` → `androidx.appcompat.app.AppCompatActivity`
- `android.support.annotation.Nullable` → `androidx.annotation.Nullable`
- `android.support.design.widget.TextInputLayout` → `com.google.android.material.textfield.TextInputLayout`
- `android.support.v7.app.AlertDialog` → `androidx.appcompat.app.AlertDialog`

#### Analytics Migration
Migrated from Google Analytics to Firebase Analytics:
- Removed `global_tracker.xml`
- Updated `AnalyticsManager` to use `FirebaseAnalytics`
- Updated `PayPalCalcApplication` to use Firebase
- Screen tracking now uses Firebase's `logEvent` with `SCREEN_VIEW`

### 5. Manifest Updates
- Removed `package` attribute (now in build.gradle as `namespace`)
- Added `android:exported="true"` to MainActivity (required for Android 12+)
- Updated AdMob meta-data to use `APPLICATION_ID` (using test ID - needs replacement with production ID)
- Removed legacy Google Play Services version meta-data

### 6. Build Configuration
- Added Java 8 support:
  ```groovy
  compileOptions {
      sourceCompatibility JavaVersion.VERSION_1_8
      targetCompatibility JavaVersion.VERSION_1_8
  }
  ```
- Enabled ViewBinding in buildFeatures
- Updated packaging options syntax for newer Gradle
- Removed deprecated `applicationVariants` output file naming

### 7. Gradle Properties
Added AndroidX migration flags:
- `android.useAndroidX=true`
- `android.enableJetifier=true`
- `android.nonTransitiveRClass=true`

## Next Steps

### Before Publishing to Google Play

1. **Update AdMob App ID**:
   - Replace test ID in AndroidManifest.xml with your production AdMob App ID
   - Current: `ca-app-pub-3940256099942544~3347511713` (test ID)

2. **Test the App**:
   - Run on physical devices with Android 5.0+ through Android 14
   - Verify AdMob ads are displaying correctly
   - Test all currency calculations
   - Verify Firebase Analytics is tracking correctly

3. **Update google-services.json**:
   - Ensure the Firebase configuration file is up to date
   - Verify Firebase Analytics is properly configured in Firebase Console

4. **Generate Signed APK/AAB**:
   - Use Android Studio's Build → Generate Signed Bundle/APK
   - Key Store: alias `PayPal Calc`, password `pay.pal.calc`
   - Generate Android App Bundle (.aab) for Google Play Store

5. **Privacy Policy**:
   - Update privacy policy to reflect Firebase Analytics usage
   - Include AdMob data collection information

6. **Play Store Listing**:
   - Update screenshots for modern Android UI
   - Update app description to mention support for Android 14
   - Target API level 34 satisfies Google Play requirements

## Build Commands

```bash
# Clean build
.\gradlew clean

# Build debug APK
.\gradlew assembleDebug

# Build release APK
.\gradlew assembleRelease

# Build Android App Bundle (for Play Store)
.\gradlew bundleRelease

# Run on connected device
.\gradlew installDebug
```

## Testing Checklist

- [ ] App builds successfully without errors
- [ ] App runs on Android 5.0 (API 21)
- [ ] App runs on Android 14 (API 34)
- [ ] Currency spinner populates correctly
- [ ] PayPal fee calculations are accurate
- [ ] Custom percentage/additional fee changes work
- [ ] AdMob banner displays (with test ads)
- [ ] Firebase Analytics tracks screen views
- [ ] "Send some love" link opens Play Store
- [ ] App persists selected currency across restarts
- [ ] UI displays correctly on various screen sizes

## Known Issues & Solutions

### Issue: Namespace not found
**Solution**: The `namespace` is now defined in `app/build.gradle` instead of AndroidManifest.xml

### Issue: ViewBinding classes not generated
**Solution**: Rebuild project (Build → Rebuild Project in Android Studio)

### Issue: Firebase not initialized
**Solution**: Ensure `google-services.json` is present in `app/` directory

### Issue: AdMob ads not showing
**Solution**: For testing, use test device IDs or test ad units from Google AdMob documentation

## Resources

- [Android 14 Behavior Changes](https://developer.android.com/about/versions/14/behavior-changes-14)
- [AndroidX Migration Guide](https://developer.android.com/jetpack/androidx/migrate)
- [Firebase Analytics for Android](https://firebase.google.com/docs/analytics/android/start)
- [Google Play Target API Level Requirements](https://developer.android.com/google/play/requirements/target-sdk)
- [ViewBinding Documentation](https://developer.android.com/topic/libraries/view-binding)
