# User Interface Documentation - PayPal Calc

## UI Architecture

### Single Activity Design
- **MainActivity**: The only activity in the app
- **No Fragments**: Simple, single-screen layout
- **No Navigation**: App has one screen with all functionality

### Layout Structure
```
activity_main.xml (LinearLayout vertical)
├── ScrollView (for small screens)
│   └── RelativeLayout (main content)
│       ├── Spinner (currency selection)
│       ├── TextInputLayout + EditText (amount input)
│       ├── CardView (fee breakdown)
│       │   ├── Percentage charge display
│       │   ├── Fixed charge display
│       │   └── Total charge display
│       ├── CardView (result)
│       │   └── Total amount display
│       └── TextView (rate app prompt)
└── AdView (banner ad at bottom)
```

## Screen Components

### 1. Currency Spinner (Top)
**ID**: `@+id/currency_spinner`
**Type**: `android.widget.Spinner`
**Purpose**: Select currency and fee structure

**Data Source**: `CurrencyListAdapter`
- 26 predefined currencies
- Custom currency option (last item)

**Item Layout**: `listitem_currency.xml`
```
Name (Symbol)
Percentage% | Symbol Amount
```

**Example Display**:
```
Philippine Peso (PHP)
4.40% | PHP 15.00
```

**Behavior**:
- Auto-selects first item (PHP) on launch
- OnItemSelected → updates presenter → saves to SharedPreferences
- Displays current fee structure for selected currency

### 2. Amount Input Field
**ID**: `@+id/amount`
**Type**: `EditText` within `TextInputLayout`
**Properties**:
```xml
android:hint="@string/amount"
android:inputType="numberDecimal"
android:digits="1234567890."
android:textSize="@dimen/text_headline"
```

**Behavior**:
- Decimal keyboard input only
- Real-time calculation via TextWatcher
- No validation (accepts empty, 0, invalid decimals)
- Clears previous results when text changes

### 3. Fee Breakdown Card
**ID**: `@+id/amount_with_transaction_charge_card`
**Type**: `CardView`

**Contains Three Rows**:

#### a) Percentage Charge Row (Clickable)
**Container ID**: `@+id/percentage_container`
**Display ID**: `@+id/paypal_transaction_charge_percentage`
**Format**: `"4.40%"`
**OnClick**: Opens dialog to modify percentage

#### b) Fixed Charge Row (Clickable)
**Container ID**: `@+id/additional_container`
**Display ID**: `@+id/paypal_transaction_charge_additional`
**Format**: `"PHP 15.00"` (with currency symbol)
**OnClick**: Opens dialog to modify fixed fee

#### c) Total Transaction Charge Row
**Display ID**: `@+id/paypal_transaction_charge_total`
**Format**: `"PHP 4.98"` (calculated fee amount)
**Note**: This is the fee you'll pay, not including the base amount

### 4. Result Card
**Type**: `CardView`

**Total Amount Display**:
**ID**: `@+id/total_amount`
**Format**: `"PHP 104.98"`
**Purpose**: Shows total amount to charge (base + fees)

**This is the answer**: What customer should pay

### 5. Rate App Prompt
**ID**: `@+id/send_some_love`
**Type**: `TextView` (clickable)
**Content**: HTML formatted with bold, colored "LOVE"
```xml
<![CDATA[Send some <b><font color="#FF4081">LOVE</font></b>!<br/>
Rate this app with <b>5 stars</b>!]]>
```

**Behavior**: OnClick → Opens Play Store listing

### 6. AdMob Banner (Bottom)
**ID**: `@+id/ad_view`
**Type**: `com.google.android.gms.ads.AdView`
**Position**: Fixed at bottom, outside ScrollView
**Size**: Standard banner (320x50)

## Dialogs

### ChangeDialog (Custom)
**Layout**: `dialog_change.xml`
**Type**: `AlertDialog` with custom view

**Components**:
- `TextInputLayout` (Material Design input wrapper)
- `EditText` (numerical input)
- OK button (saves change)
- Cancel button (dismisses)

**Usage**:
```java
ChangeDialog.show(context, R.string.dialog_change_percentage_label, "4.4",
    (dialog, userInput) -> {
        // Handle new value
    }
);
```

**Two Use Cases**:
1. **Change Percentage**: Label "Percentage Charge (%)"
2. **Change Fixed Fee**: Label "Additional Charge"

## Material Design Elements

### CardView
- Elevation: Default shadow
- Corner radius: Default
- Background: White
- Padding: Defined in dimens.xml

### TextInputLayout
- Material Design floating labels
- Hint animation
- Used for amount input and dialog input

### Colors (values/colors.xml)
- **Primary Color**: Defined in theme
- **Accent Color**: `#FF4081` (pink, used in "LOVE" text)
- Theme: `@style/AppTheme` (Material Design)

### Typography
- **Headline**: Large text for amount input and results
- **Body**: Regular text for labels
- **Subhead**: Currency names in spinner

## Dimensions (values/dimens.xml)
```xml
<dimen name="horizontal_margin">16dp</dimen>
<dimen name="vertical_margin">16dp</dimen>
<dimen name="text_headline">34sp</dimen>
<!-- Card margins, padding, etc. -->
```

## Theme Configuration

### AppTheme (values/styles.xml)
- Extends: `Theme.AppCompat.Light.DarkActionBar`
- Primary color: Material blue
- Accent color: Pink (#FF4081)
- Status bar color: Darker primary

**No Custom ActionBar**: Uses default Material toolbar

## Responsive Design

### Screen Size Support
- **Small screens**: ScrollView ensures all content accessible
- **Large screens** (w820dp): Increased margins via values-w820dp/dimens.xml
- **Orientation**: Portrait only (no landscape-specific layout)

### Density Support
Launcher icons for all densities:
- mdpi (48x48)
- hdpi (72x72)
- xhdpi (96x96)
- xxhdpi (144x144)
- xxxhdpi (192x192)

## User Interactions

### 1. Calculate Flow (Primary Use Case)
```
1. User selects currency from Spinner
   ↓
2. User types amount in EditText
   ↓ (TextWatcher triggers)
3. Presenter calculates fees
   ↓
4. UI updates:
   - Transaction charge total
   - Amount total (result)
```

**Example**:
```
Selected: Philippine Peso (PHP 4.4% + 15)
Typed: 100
Result: 
  - Transaction charge: PHP 19.96
  - Amount total: PHP 119.96
```

### 2. Customize Fees Flow
```
1. User clicks percentage or additional charge container
   ↓
2. ChangeDialog appears with current value
   ↓
3. User enters new value → OK
   ↓
4. Presenter updates currency
   ↓
5. Saves to SharedPreferences
   ↓
6. Recalculates with new fees
   ↓
7. UI updates with new values
```

### 3. Rate App Flow
```
1. User clicks "Send some LOVE" text
   ↓
2. Opens Play Store app listing
   (or web URL if Play Store not available)
```

## Spinner Adapter Implementation

### CurrencyListAdapter
**Extends**: `BaseAdapter`

**Data**: `LinkedList<Currency>`

**getView() Layout**: `listitem_currency.xml`

**Layout Structure**:
```xml
<LinearLayout vertical>
    <TextView id="@+id/name" />      <!-- "Philippine Peso (PHP)" -->
    <LinearLayout horizontal>
        <TextView id="@+id/percent" /> <!-- "4.40%" -->
        <TextView id="@+id/symbol" />  <!-- "PHP" -->
        <TextView id="@+id/amount" />  <!-- "15.00" -->
    </LinearLayout>
</LinearLayout>
```

**Formatting**:
- Percentage: `String.format("%.2f", percent) + "%"`
- Amount: `DecimalFormat("#,###,###.00").format(amount)`

**No ViewHolder**: Inflates view on every getView() call (inefficient)

## Accessibility

**Current State**: Minimal accessibility support
- Text fields have content descriptions from android:hint
- Clickable areas (buttons, spinners) have default descriptions
- No custom TalkBack messages
- No accessibility labels on icons

**Missing**:
- Content descriptions on CardViews
- Announcement for calculation results
- Minimum touch target sizes (some may be < 48dp)

## Localization

**Current State**: English only

**Localizable Strings**: All in `res/values/strings.xml`
- 9 user-facing strings
- Ready for translation (no hardcoded strings in Java)

**Non-Translatable**: `res/values/donottranslate.xml`
- Ad unit IDs
- Analytics ID
- SharedPreferences key

**Currency Names**: Hardcoded in `CurrencyListProvider.java`
- Would need refactoring for localization

## Animation

**Current State**: No custom animations

**Default Android Animations**:
- TextInputLayout hint floating
- Spinner dropdown
- Dialog enter/exit
- ScrollView scrolling

## Performance Considerations

### Calculation Triggers
- **Per Keystroke**: TextWatcher fires on every character
- **No Debouncing**: Immediate calculation (acceptable for simple math)

### View Inflation
- **CurrencyListAdapter**: Inflates on every getView() (inefficient)
- **Improvement**: Implement ViewHolder pattern

### Memory
- **Static Presenter**: Retains view reference (potential leak)
- **Mitigation**: Single activity app, less concern

## Error States

### No Error Messages
- Empty input → Shows 0.00 in results
- Invalid decimal → Caught in try-catch, defaults to 0
- No currency selected → Impossible (Spinner always has selection)

### No Loading States
- Calculations instant (no async operations)
- SharedPreferences reads/writes synchronous

## Edge Cases Handled

1. **Empty Amount**: Defaults to 0, shows "0.00"
2. **Multiple Decimals**: EditText digits filter prevents
3. **Currency is null**: Adapter checks and provides default
4. **No saved currencies**: Provider returns defaults
5. **Play Store not installed**: Falls back to web URL

## Edge Cases NOT Handled

1. **Very Large Numbers**: No max value validation
2. **Negative Numbers**: Digits filter prevents, but no explicit check
3. **Configuration Changes**: Presenter survives but EditText text lost (no savedInstanceState)
4. **No Internet**: AdMob fails silently, no error message

## Visual Design

### Color Scheme
- **Primary**: Blue (AppCompat default)
- **Accent**: Pink #FF4081
- **Card Background**: White
- **Text**: Dark gray (default)

### Spacing
- **Card Margins**: 16dp horizontal, 16dp vertical
- **Internal Padding**: 16dp
- **Between Elements**: 8dp

### Typography Hierarchy
1. **Headline** (34sp): Amount input, total result
2. **Title** (20sp): Card labels
3. **Body** (14sp): Fee details
4. **Caption** (12sp): Currency symbols

### Elevation
- **Cards**: 2dp elevation (subtle shadow)
- **AppBar**: 4dp elevation
- **Dialogs**: 24dp elevation

## AdMob Integration

### Banner Configuration
```java
AdRequest adRequest = new AdRequest.Builder().build();
mAdView.loadAd(adRequest);
```

**Placement**: Bottom of screen, always visible (not in ScrollView)

**Ad Unit ID**: From `donottranslate.xml`
```xml
<string name="banner_ad_unit_id">ca-app-pub-8581143305013649/2357796582</string>
```

**Ad Size**: BANNER (320x50)

**Behavior**:
- Loads in onCreate()
- Refreshes automatically (AdMob default: 60 seconds)
- No click tracking (handled by AdMob)

## Future UI Improvements

Potential enhancements (not implemented):
1. **Dark Mode**: Theme switching
2. **History**: Recent calculations
3. **Multiple Amounts**: Batch calculation
4. **Copy Button**: Copy result to clipboard
5. **Share**: Share calculation
6. **Favorites**: Star frequently used currencies
7. **Currency Search**: Filter spinner items
8. **Haptic Feedback**: On button presses
9. **Animations**: Result reveal animation
10. **Tablet Layout**: Two-column layout for large screens
