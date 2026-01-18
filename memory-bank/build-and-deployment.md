# Build and Deployment - PayPal Calc

## Build System Configuration

### Gradle Versions
- **Android Gradle Plugin**: 1.5.0 (Classic, pre-AndroidX)
- **Gradle Wrapper**: As defined in gradle-wrapper.properties
- **Build Tools**: 23.0.1

### SDK Configuration
```gradle
compileSdkVersion 23        // Android 6.0 Marshmallow
buildToolsVersion "23.0.1"

defaultConfig {
    applicationId "com.walng.dhagz.paypalcalc"
    minSdkVersion 15        // Android 4.0.3 Ice Cream Sandwich
    targetSdkVersion 23     // Android 6.0 Marshmallow
    versionCode 5
    versionName "1.4"
}
```

**Device Coverage**:
- Minimum API 15 supports ~99% of devices (as of 2016)
- Target API 23 enables Material Design features

## Build Commands

### Windows (PowerShell/CMD)
```powershell
# Debug build
.\gradlew assembleDebug

# Release build (unsigned)
.\gradlew assembleRelease

# Clean build
.\gradlew clean

# Install to connected device
.\gradlew installDebug
.\gradlew installRelease
```

### Linux/Mac
```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

### Output Locations
Debug APK: `app/build/outputs/apk/app-debug.apk`
Release APK: `app/build/outputs/apk/app-release-1.4.apk`

**Note**: Build script customizes APK filename to include version:
```gradle
applicationVariants.all { variant ->
    variant.outputs.each { output ->
        def file = output.outputFile
        output.outputFile = new File(
            file.parent, 
            file.name.replace(".apk", "-" + defaultConfig.versionName + ".apk")
        )
    }
}
```

## Dependencies

### Core Android Libraries
```gradle
compile 'com.android.support:appcompat-v7:23.1.0'
compile 'com.android.support:design:23.1.0'        // Material Design
compile 'com.android.support:cardview-v7:23.1.0'   // CardView widget
compile 'com.android.support:recyclerview-v7:23.1.0'
```

### Google Play Services
```gradle
compile 'com.google.android.gms:play-services-ads:8.3.0'       // AdMob
compile 'com.google.android.gms:play-services-analytics:8.3.0'  // Analytics
```

**Google Services Plugin**:
```gradle
apply plugin: 'com.google.gms.google-services'  // At bottom of app/build.gradle
```

### Third-Party Libraries
```gradle
compile 'com.jakewharton:butterknife:7.0.1'  // View injection
```

**No Explicit Gson Dependency**: Included transitively via Google Play Services

### Testing Dependencies
```gradle
// testCompile 'junit:junit:4.12'  // COMMENTED OUT - No tests
```

## ProGuard Configuration

### Build Configuration
```gradle
buildTypes {
    release {
        minifyEnabled false  // ProGuard disabled
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 
                      'proguard-rules.pro'
    }
}
```

**Why Disabled**: Small app size, no sensitive code, avoids ButterKnife reflection issues

### ProGuard Rules (app/proguard-rules.pro)
Despite `minifyEnabled false`, rules are documented for future use:

```proguard
# ButterKnife-specific rules
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
```

**Purpose**: Preserve ButterKnife's annotation-generated view binding classes

## Signing Configuration

### Key Store Details (from README.md)
```
Alias: PayPal Calc
Key Store Password: pay.pal.calc
Key Password: pay.pal.calc
```

**Location**: Not in source control (security best practice)

### Signing Setup (Manual)
```gradle
// Add to app/build.gradle for automated signing
android {
    signingConfigs {
        release {
            storeFile file("path/to/keystore.jks")
            storePassword "pay.pal.calc"
            keyAlias "PayPal Calc"
            keyPassword "pay.pal.calc"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

## Required Configuration Files

### google-services.json
**Location**: `app/google-services.json`
**Purpose**: Firebase/Google Services configuration
**Contains**:
- Project ID
- API keys for AdMob and Analytics
- Package name validation

**Must Match**: Package name `com.walng.dhagz.paypalcalc`

### Analytics Configuration
**Location**: `app/src/main/res/xml/global_tracker.xml`
**Referenced by**: `PayPalCalcApplication.getDefaultTracker()`

**IDs in donottranslate.xml**:
```xml
<string name="banner_ad_unit_id">ca-app-pub-8581143305013649/2357796582</string>
<string name="google_analytics_id">UA-75626424-1</string>
<bool name="ga_reportUncaughtExceptions">true</bool>
```

## Lint Configuration

```gradle
lintOptions {
    disable 'InvalidPackage'  // Ignore package naming issues
}
```

## Packaging Options

```gradle
packagingOptions {
    exclude 'META-INF/services/javax.annotation.processing.Processor'
}
```

**Purpose**: Avoid duplicate file conflicts from annotation processors (ButterKnife)

## Local Build Setup

### Prerequisites
1. **Android Studio** 1.5+ or command line tools
2. **JDK** 7 or 8
3. **Android SDK** with API 23 installed

### First-Time Setup
```powershell
# Clone repository
git clone <repo-url>
cd PayCalc

# Add google-services.json to app/ directory
# Add keystore file (for release builds)

# Build
.\gradlew assembleDebug
```

### Local Properties
**File**: `local.properties` (auto-generated, gitignored)
```properties
sdk.dir=C\:\\Users\\username\\AppData\\Local\\Android\\Sdk
```

**Purpose**: Points to Android SDK installation

## Play Store Deployment

### Current Listing
**URL**: https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc

### Release Checklist
1. ✅ Increment `versionCode` in `app/build.gradle`
2. ✅ Update `versionName` for user display
3. ✅ Test on physical device
4. ✅ Generate signed APK with release keystore
5. ✅ Upload to Play Console
6. ✅ Update screenshots/description if UI changed
7. ✅ Test billing/ads in production environment

### Version History
- **1.4 (versionCode 5)**: Current version
- **Previous versions**: 1-4 (codes 1-4)

## Continuous Integration

**Status**: No CI/CD configured
**Potential Setup**: GitHub Actions, Travis CI, or Circle CI could automate:
- Lint checks
- Debug builds on PRs
- Release APK generation
- Play Store deployment

## Common Build Issues

### Issue: Missing google-services.json
**Error**: `File google-services.json is missing`
**Fix**: Add file to `app/` directory from Firebase console

### Issue: ButterKnife Annotation Errors
**Error**: `cannot find symbol @Bind`
**Fix**: Rebuild project (`.\gradlew clean build`)

### Issue: SDK Version Mismatch
**Error**: `Failed to find target with hash string 'android-23'`
**Fix**: Install API 23 via SDK Manager

### Issue: Keystore Not Found
**Error**: `Keystore file not found`
**Fix**: Either:
- Add keystore to specified path
- Remove signing config for debug builds

## Build Performance

**Typical Build Times** (on modern hardware):
- Clean build: ~30-60 seconds
- Incremental build: ~5-10 seconds
- Clean + rebuild: ~45-90 seconds

**Optimization Options** (not currently used):
```properties
# gradle.properties
org.gradle.jvmargs=-Xmx2048m
org.gradle.parallel=true
org.gradle.daemon=true
```

## Gradle Wrapper

**Files**:
- `gradlew` (Linux/Mac shell script)
- `gradlew.bat` (Windows batch file)
- `gradle/wrapper/gradle-wrapper.properties` (version specification)

**Advantage**: Developers don't need Gradle installed globally

## Module Structure

**Single Module App**:
```gradle
// settings.gradle
include ':app'
```

**No Additional Modules**: No separate library modules, wear modules, or feature modules
