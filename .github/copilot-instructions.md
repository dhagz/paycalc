# PayPal Calc - Android App

## Architecture Overview

This is a single-activity Android app using **MVP (Model-View-Presenter)** pattern:
- **Models** ([models/Currency.java](../app/src/main/java/com/walng/dhagz/paypalcalc/models/Currency.java)): Data classes (e.g., `Currency` with PayPal fees)
- **Views** ([views/PayPalCalcView.java](../app/src/main/java/com/walng/dhagz/paypalcalc/views/PayPalCalcView.java)): Interfaces defining UI contract
- **Presenters** ([presenters/PayPalCalcPresenter.java](../app/src/main/java/com/walng/dhagz/paypalcalc/presenters/PayPalCalcPresenter.java)): Business logic, implemented as **singletons**

Activities implement view interfaces. [MainActivity.java](../app/src/main/java/com/walng/dhagz/paypalcalc/MainActivity.java) demonstrates this pattern - binds to presenter in `onCreate()` and implements all view callbacks.

## Version 2.0 Update (2026)

The app has been fully modernized to meet Google Play requirements:
- **Target SDK 34** (Android 14) - required by Google Play for 2026
- **Migrated to AndroidX** - modern Android libraries
- **ViewBinding** - replaced ButterKnife for type-safe view references
- **Firebase Analytics** - replaced legacy Google Analytics
- **Gradle 8.6** - modern build system
- **Latest dependencies** - all libraries updated to current versions

See [UPDATE_SUMMARY.md](../UPDATE_SUMMARY.md) and [MIGRATION_GUIDE.md](../MIGRATION_GUIDE.md) for complete details.

## Key Patterns & Conventions

### Singleton Pattern (Critical)
All managers and presenters use lazy singleton initialization:
```java
private static PayPalCalcPresenter instance;
public static PayPalCalcPresenter getInstance(Context context) {
    if (instance == null) {
        instance = new PayPalCalcPresenter(context);
    }
    return instance;
}
```
See: [AnalyticsManager.java](../app/src/main/java/com/walng/dhagz/paypalcalc/managers/AnalyticsManager.java), [CurrencyListProvider.java](../app/src/main/java/com/walng/dhagz/paypalcalc/providers/CurrencyListProvider.java)

### Data Persistence
Uses **SharedPreferences + Gson** for storing currency data. Abstract base class [ListProvider.java](../app/src/main/java/com/walng/dhagz/paypalcalc/providers/ListProvider.java) handles serialization:
- Override `getPreferenceKey()` and `getType()` (TypeToken for Gson)
- Default values: `getDefaultCurrencyList()` in [CurrencyListProvider.java](../app/src/main/java/com/walng/dhagz/paypalcalc/providers/CurrencyListProvider.java) defines 26 currencies with PayPal fees

### View Binding
Uses **ViewBinding** (Android native) for type-safe view access. Bind in `onCreate()`:
```java
binding = ActivityMainBinding.inflate(getLayoutInflater());
setContentView(binding.getRoot());
// Access views with binding.viewId
binding.amount.setText("value");
```

## Build & Development

### SDK/Tooling
- **compileSdk**: 34 (Android 14)
- **targetSdk**: 34 (Android 14)
- **minSdk**: 21 (Android 5.0)
- **Gradle**: 8.6 with Android Gradle Plugin 8.2.2
- **Build Commands**:
  - Build APK: `.\gradlew assembleRelease` (or `assembleDebug`)
  - Build App Bundle: `.\gradlew bundleRelease` (recommended for Play Store)

### Dependencies (v2.0)
- **AndroidX**: AppCompat 1.6.1, Material 1.11.0, CardView 1.0.0, RecyclerView 1.3.2
- **Google Play Services**: AdMob 22.6.0
- **Firebase**: Analytics via BOM 32.7.1
- **Gson**: 2.10.1 for JSON serialization

### Configuration
- **google-services.json** required in `app/` for Firebase/Analytics
- **Key Store**: Alias `PayPal Calc`, passwords `pay.pal.calc`
- **AdMob App ID**: Currently using test ID in AndroidManifest.xml - replace with production ID before publishing

## Firebase Analytics Integration

App-level tracker configured via [PayPalCalcApplication.java](../app/src/main/java/com/walng/dhagz/paypalcalc/PayPalCalcApplication.java) (custom Application class):
```java
mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
```
Track screens/events through [AnalyticsManager.java](../app/src/main/java/com/walng/dhagz/paypalcalc/managers/AnalyticsManager.java) singleton:
```java
AnalyticsManager.getInstance(getApplication()).setScreen(TAG);
```

## UI Components

- **Custom Dialogs**: [ChangeDialog.java](../app/src/main/java/com/walng/dhagz/paypalcalc/ChangeDialog.java) - reusable input dialog with TextInputLayout
- **Currency Adapter**: [CurrencyListAdapter.java](../app/src/main/java/com/walng/dhagz/paypalcalc/adapters/CurrencyListAdapter.java) for Spinner
- **AdMob Banner**: Loaded in MainActivity with `AdRequest.Builder()` and `MobileAds.initialize()`

## Application Logic

PayPal fee calculation formula (see [PayPalCalcPresenter.java](../app/src/main/java/com/walng/dhagz/paypalcalc/presenters/PayPalCalcPresenter.java#L74-L91)):
```java
totalPrice = ((amount + additional) / ((100 - percent) / 100));
addToAmount = totalPrice - amount;
```
Where `additional` is fixed fee, `percent` is percentage fee (e.g., 4.4% for most currencies).

## Publishing to Google Play

The app now meets all Google Play requirements:
1. **Target SDK 34** ✅ (required for 2026)
2. **AndroidX** ✅ (required)
3. **64-bit support** ✅ (handled by modern Gradle)
4. **App Bundle** ✅ (use `bundleRelease` task)

Before publishing:
- Update AdMob App ID in AndroidManifest.xml
- Verify google-services.json configuration
- Test on Android 5.0 through Android 14
- Generate signed App Bundle (.aab)
