# External Dependencies and Integrations - PayPal Calc

## Google Play Services Integration

### Overview
The app integrates two Google Play Services modules:
1. **AdMob** - Banner advertisements for monetization
2. **Google Analytics** - User behavior tracking

### Configuration Files

#### build.gradle (Project Level)
```gradle
dependencies {
    classpath 'com.google.gms:google-services:2.0.0-alpha6'
}
```

#### build.gradle (App Level)
```gradle
apply plugin: 'com.google.gms.google-services'  // At end of file

dependencies {
    compile 'com.google.android.gms:play-services-ads:8.3.0'
    compile 'com.google.android.gms:play-services-analytics:8.3.0'
}
```

#### google-services.json
**Location**: `app/google-services.json`
**Purpose**: Firebase/Google Services configuration
**Contains**:
- Project ID
- Client ID
- API keys
- Package name validation

**Security**: Should be in `.gitignore` but often committed for convenience

## AdMob Integration

### Banner Ad Configuration

**Ad Unit ID** (from `values/donottranslate.xml`):
```xml
<string name="banner_ad_unit_id">ca-app-pub-8581143305013649/2357796582</string>
```

**Format Breakdown**:
- `ca-app-pub`: AdMob prefix
- `8581143305013649`: Publisher ID
- `2357796582`: Specific ad unit

### Implementation

**Layout** (`activity_main.xml`):
```xml
<com.google.android.gms.ads.AdView
    android:id="@+id/ad_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    ads:adSize="BANNER"
    ads:adUnitId="@string/banner_ad_unit_id" />
```

**Java** (`MainActivity.onCreate()`):
```java
@Bind(R.id.ad_view) AdView mAdView;

AdRequest adRequest = new AdRequest.Builder().build();
mAdView.loadAd(adRequest);
```

### Ad Behavior

**Load Timing**: On activity creation
**Refresh Rate**: Automatic (AdMob default: 60 seconds)
**Ad Size**: BANNER (320x50dp)
**Position**: Bottom of screen, fixed outside ScrollView
**Fallback**: Empty space if ads fail to load

### Test Ads

**For Development**: Use test device IDs
```java
AdRequest adRequest = new AdRequest.Builder()
    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
    .addTestDevice("YOUR_DEVICE_ID_HERE")
    .build();
```

**Not Implemented**: Uses real ads in all builds

## Google Analytics Integration

### Configuration

**Analytics ID** (from `values/donottranslate.xml`):
```xml
<string name="google_analytics_id">UA-75626424-1</string>
<bool name="ga_reportUncaughtExceptions">true</bool>
```

**Format**: Universal Analytics (UA-XXXXXXXX-X)
**Note**: UA deprecated in 2023, should migrate to GA4

### Tracker Configuration

**XML Configuration** (`res/xml/global_tracker.xml`):
Referenced by `PayPalCalcApplication.getDefaultTracker()`
```java
mTracker = analytics.newTracker(R.xml.global_tracker);
```

**File Structure** (typical):
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="ga_trackingId">@string/google_analytics_id</string>
    <bool name="ga_autoActivityTracking">true</bool>
    <bool name="ga_reportUncaughtExceptions">@bool/ga_reportUncaughtExceptions</bool>
</resources>
```

### Application Class Integration

**PayPalCalcApplication.java**:
```java
public class PayPalCalcApplication extends Application {
    private Tracker mTracker;
    
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
```

**AndroidManifest.xml Declaration**:
```xml
<application android:name=".PayPalCalcApplication" ...>
```

### AnalyticsManager Wrapper

**Purpose**: Simplify analytics calls throughout app

**Implementation**:
```java
public class AnalyticsManager {
    private static AnalyticsManager instance;
    private Tracker mTracker;
    
    public static AnalyticsManager getInstance(Application application) {
        if (instance == null) {
            instance = new AnalyticsManager(application);
        }
        return instance;
    }
    
    public void setScreen(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    
    public void sendEvent(String category, String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }
}
```

### Usage in MainActivity

**Screen Tracking**:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    // ...
    AnalyticsManager.getInstance(getApplication()).setScreen(TAG);
}
```

**TAG Value**: `"MainActivity"`

### Analytics Events

**Currently Tracked**:
- Screen views (MainActivity only)
- Uncaught exceptions (automatic)

**NOT Tracked** (opportunities):
- Currency selection changes
- Fee customization events
- Calculation frequency
- Amount ranges
- "Rate app" clicks
- Dialog dismissals

### Debug Analytics

**Enable Debug Logging**:
```bash
adb shell setprop log.tag.GAv4 DEBUG
```

**View Logs**:
```bash
adb logcat -s GAv4
```

## Third-Party Libraries

### ButterKnife 7.0.1

**Purpose**: View injection and binding
**Website**: https://jakewharton.github.io/butterknife/

**Dependency**:
```gradle
compile 'com.jakewharton:butterknife:7.0.1'
```

**Usage**:
```java
@Bind(R.id.amount) EditText mAmount;
@Bind(R.id.currency_spinner) Spinner mCurrencySpinner;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);  // Initializes all @Bind fields
}
```

**ProGuard Rules** (in `proguard-rules.pro`):
```proguard
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

**Version Note**: 7.0.1 is very old (2015)
- Current version: 10.x
- Modern alternative: View Binding (built into Android)

### Gson (Implicit)

**Purpose**: JSON serialization for SharedPreferences
**Dependency**: Included transitively via Google Play Services

**Usage in ListProvider**:
```java
// Serialize
Gson gson = new Gson();
String json = gson.toJson(items);
editor.putString(key, json);

// Deserialize
String json = sharedPreferences.getString(key, null);
Gson gson = new Gson();
Type type = new TypeToken<LinkedList<Currency>>(){}.getType();
return gson.fromJson(json, type);
```

**TypeToken**: Resolves generic types at runtime

### Android Support Libraries

**Dependency Versions**: 23.1.0

**AppCompat**:
```gradle
compile 'com.android.support:appcompat-v7:23.1.0'
```
- Material Design components
- Backward compatibility for modern features
- `AppCompatActivity` base class

**Design Library**:
```gradle
compile 'com.android.support:design:23.1.0'
```
- TextInputLayout (floating labels)
- Coordinator layouts
- Material animations

**CardView**:
```gradle
compile 'com.android.support:cardview-v7:23.1.0'
```
- Elevated card containers
- Rounded corners
- Shadow rendering

**RecyclerView**:
```gradle
compile 'com.android.support:recyclerview-v7:23.1.0'
```
- **NOT USED** in current app
- Included but not utilized
- Potential for future features

## Android Permissions

### Declared in AndroidManifest.xml

**INTERNET**:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
**Purpose**: Load AdMob ads and send Analytics data
**Risk Level**: Normal (auto-granted)

**ACCESS_NETWORK_STATE**:
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
**Purpose**: Check network availability before ad/analytics requests
**Risk Level**: Normal (auto-granted)

### No Runtime Permissions

**Target SDK**: 23 (Marshmallow)
- Introduced runtime permissions
- This app only uses normal permissions
- No runtime permission requests needed

## Play Store Integration

### App Listing

**Package Name**: `com.walng.dhagz.paypalcalc`
**URL**: https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc

### Rating Prompt

**Implementation** (`MainActivity.java`):
```java
private View.OnClickListener mSendSomeLoveClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final String appPackageName = getPackageName();
        try {
            // Try Play Store app
            startActivity(new Intent(Intent.ACTION_VIEW, 
                Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            // Fallback to web browser
            startActivity(new Intent(Intent.ACTION_VIEW, 
                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
};
```

**Triggers**:
- User clicks "Send some LOVE" text
- Opens Play Store app (or web if not available)
- Directly to app's listing page

**No Smart Prompting**:
- Always visible (not based on usage)
- No dismissal tracking
- No A/B testing

## External Data Sources

### None Currently

**App is Self-Contained**:
- No API calls
- No remote configuration
- No content updates
- All currency data hardcoded

**Potential Integrations**:
1. **PayPal API**: Fetch real-time fee structures
2. **Exchange Rate API**: Convert amounts between currencies
3. **Firebase Remote Config**: Update fees without app update
4. **Cloud Storage**: Backup user customizations

## Build-Time Dependencies

### Gradle Plugin
```gradle
classpath 'com.android.tools.build:gradle:1.5.0'
```
**Very Old**: From 2015, pre-Android Studio 2.0

### Google Services Plugin
```gradle
classpath 'com.google.gms:google-services:2.0.0-alpha6'
```
**Alpha Version**: Unusual for production app

### Build Tools
```gradle
buildToolsVersion "23.0.1"
```
**Old But Stable**: Should still work on modern systems

## Network Error Handling

### AdMob Failures
**Behavior**: Silent failure, empty space shown
**No User Feedback**: No error messages
**Retry**: Automatic via AdMob refresh

### Analytics Failures
**Behavior**: Silent failure, data not sent
**No User Feedback**: Invisible to user
**Buffering**: Analytics batches and retries automatically

### Network Detection
**Uses**: `ACCESS_NETWORK_STATE` permission
**Google Play Services**: Handles network checks internally
**App Logic**: No explicit network state checking

## Security Considerations

### API Keys in Code

**Current State**: All keys in `donottranslate.xml`
```xml
<string name="banner_ad_unit_id">ca-app-pub-8581143305013649/2357796582</string>
<string name="google_analytics_id">UA-75626424-1</string>
```

**Risk Level**: Low
- AdMob ad unit IDs are public (shown in HTML)
- Analytics IDs are public (sent to browser)
- No authentication keys or secrets

**Best Practice**: Still okay for these specific keys

### google-services.json

**Contains**:
- API keys (some restricted by package name)
- OAuth client IDs
- Project identifiers

**Risk Level**: Low-Medium
- Keys restricted to package name
- No server-side secrets
- Common to commit to repo for convenience

**Best Practice**: Could use in `.gitignore` and CI/CD secrets

### No Backend

**Advantage**: No server to secure
**Limitation**: Can't update fees without app update
**Trade-off**: Simpler architecture, less attack surface

## Dependency Update Path

### Current Challenges

**Very Old Dependencies**:
- Gradle 1.5.0 → Should update to 7.x+
- Support Libraries 23.1.0 → Migrate to AndroidX
- ButterKnife 7.0.1 → Update to 10.x or use View Binding
- Google Play Services 8.3.0 → Update to 20.x+
- Universal Analytics → Migrate to GA4

**Breaking Changes**: Major refactoring required
1. AndroidX migration (namespace changes)
2. ButterKnife API changes
3. GA4 API completely different
4. Gradle build script updates

**Recommended Path**:
1. Update Gradle to 4.x first
2. Migrate to AndroidX
3. Replace ButterKnife with View Binding
4. Update Google Play Services
5. Migrate Analytics to GA4

## Testing External Dependencies

### AdMob Testing

**Test Ads**:
```java
AdRequest adRequest = new AdRequest.Builder()
    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
    .build();
```

**Verify**:
- Ad loads successfully
- Test ad banner shows "Test Ad"
- Click doesn't count as revenue

### Analytics Testing

**Debug Mode**:
```bash
adb shell setprop log.tag.GAv4 DEBUG
adb logcat -s GAv4
```

**Verify**:
- Screen view hits sent
- Event tracking works
- Data appears in Analytics console (24-48hr delay)

### Network Testing

**Airplane Mode Test**:
1. Enable airplane mode
2. Launch app
3. Verify: App works, no crashes, ads fail gracefully

**Slow Connection Test**:
1. Throttle network (Charles Proxy, Android tools)
2. Verify: App remains responsive, ads load eventually

## Third-Party Service Dashboards

### AdMob Console
- URL: https://apps.admob.com/
- View: Revenue, impressions, fill rate
- Manage: Ad units, blocking controls

### Google Analytics Console
- URL: https://analytics.google.com/
- View: Screen views, events, user demographics
- Manage: Goals, filters, custom reports

### Play Console
- URL: https://play.google.com/console/
- View: Installs, ratings, crashes
- Manage: Releases, store listing, pricing

## Monitoring and Alerts

### Current Monitoring: None

**No Crash Reporting**: Beyond analytics uncaught exceptions
**No Performance Monitoring**: No Firebase Performance
**No Custom Alerts**: No revenue/user drop notifications

**Recommended Additions**:
1. **Firebase Crashlytics**: Detailed crash reports
2. **Firebase Performance**: App startup time, network latency
3. **AdMob Revenue Alerts**: Notify on significant drops
4. **Analytics Custom Alerts**: User engagement thresholds
