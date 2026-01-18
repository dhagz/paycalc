# Code Patterns and Conventions - PayPal Calc

## Design Patterns

### 1. Singleton Pattern (Critical)

**Used By**: All managers, presenters, and providers

**Standard Implementation**:
```java
private static ClassName instance;

public static ClassName getInstance(Context context) {
    if (instance == null) {
        instance = new ClassName(context);
    }
    return instance;
}
```

**Classes Using This Pattern**:
- `PayPalCalcPresenter`
- `AnalyticsManager`
- `CurrencyListProvider`

**Why**: 
- Ensures single source of truth for app state
- Avoids recreation on configuration changes
- Simplifies access (no need to pass instances)

**Caution**: Holds Context reference (potential memory leak if not careful)

### 2. MVP (Model-View-Presenter)

**View Interface Contract**:
```java
public interface PayPalCalcView {
    void populateCurrencyList(LinkedList<Currency> currencies);
    void setTransactionTotal(String total);
    // ... other view update methods
}
```

**Activity Implementation**:
```java
public class MainActivity extends AppCompatActivity implements PayPalCalcView {
    private PayPalCalcPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = PayPalCalcPresenter.getInstance(this);
        presenter.bindPayPalCalcView(this);
    }
    
    @Override
    public void setTransactionTotal(String total) {
        mTransactionTotal.setText(total);
    }
}
```

**Benefits**:
- Testable business logic (Presenter has no Android deps)
- Clear separation of concerns
- Easy to swap UI implementations

### 3. Template Method Pattern

**Used By**: `ListProvider<D>` abstract class

```java
public abstract class ListProvider<D> {
    // Template methods
    public void set(LinkedList<D> items) { /* ... */ }
    public LinkedList<D> get() { /* ... */ }
    
    // Hook methods (must implement)
    public abstract String getPreferenceKey();
    public abstract Type getType();
}
```

**Subclass Implementation**:
```java
public class CurrencyListProvider extends ListProvider<Currency> {
    @Override
    public String getPreferenceKey() {
        return "CurrencyListProvider";
    }
    
    @Override
    public Type getType() {
        return new TypeToken<LinkedList<Currency>>(){}.getType();
    }
}
```

### 4. Static Factory Method

**Used By**: `ChangeDialog`

```java
public class ChangeDialog {
    public static void show(Context context, @StringRes int hintLabel, 
                           String defaultText, OnOkClickListener listener) {
        // Build and show AlertDialog
    }
    
    public interface OnOkClickListener {
        void onClick(DialogInterface dialog, String userInput);
    }
}
```

**Benefits**:
- Cleaner API (no constructor + show() calls)
- Encapsulates dialog creation complexity
- Prevents misuse (can't call new ChangeDialog())

### 5. Observer Pattern (Implicit)

**TextWatcher on Amount Field**:
```java
private TextWatcher mAmountTextWatcher = new TextWatcher() {
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateAmount(s.toString());
    }
    // ... other methods
};

mAmount.addTextChangedListener(mAmountTextWatcher);
```

**Why**: Real-time calculation updates as user types

## Naming Conventions

### Variables

**Member Variables**: Prefix with `m`
```java
private EditText mAmount;
private TextView mTransactionTotal;
private PayPalCalcPresenter presenter;  // Exception: no prefix for singletons
```

**Static Members**: ALL_CAPS with underscores
```java
private static final String TAG = "MainActivity";
private static PayPalCalcPresenter instance;
```

**Local Variables**: camelCase, descriptive
```java
float totalPrice;
float addToAmount;
String formattedAmount;
```

**Listener Variables**: Descriptive name + type
```java
private View.OnClickListener mTransactionPercentageClickListener;
private TextWatcher mAmountTextWatcher;
```

### Classes

**Pattern**: PascalCase, descriptive, suffix with type
```java
PayPalCalcPresenter      // Presenter suffix
PayPalCalcView           // View suffix (interface)
CurrencyListAdapter      // Adapter suffix
AnalyticsManager         // Manager suffix
CurrencyListProvider     // Provider suffix
```

### Methods

**Pattern**: camelCase, verb-first for actions
```java
// Getters/Setters
public Currency getCurrency()
public void setCurrency(Currency currency)

// Actions
public void bindPayPalCalcView(PayPalCalcView view)
public void onAmountChanged(float amount)
public void displayPercentageChargeChange()

// View callbacks (implement interface)
@Override
public void populateCurrencyList(LinkedList<Currency> currencies)

// Static factory
public static ClassName getInstance(Context context)
```

### Resources

**String Resources**: snake_case
```xml
<string name="app_name">PayCalc</string>
<string name="dialog_change_percentage_label">Percentage Charge (%)</string>
<string name="send_some_love">...</string>
```

**IDs in Layouts**: snake_case
```xml
<EditText android:id="@+id/amount" />
<TextView android:id="@+id/paypal_transaction_charge_total" />
<Spinner android:id="@+id/currency_spinner" />
```

**Layout Files**: type_purpose
```
activity_main.xml         // Activity layout
dialog_change.xml         // Dialog layout
listitem_currency.xml     // List item layout
```

## Code Organization

### Package Structure
```
com.walng.dhagz.paypalcalc/
├── MainActivity.java           // Root: Activities and App-level classes
├── PayPalCalcApplication.java
├── ChangeDialog.java
├── models/                     // Data classes
├── views/                      // View interfaces
├── presenters/                 // Business logic
├── providers/                  // Data persistence
├── managers/                   // Utility services
└── adapters/                   // UI adapters
```

**Convention**: Related classes grouped by responsibility (not layer type)

### File Organization

**Standard Class Structure**:
```java
// 1. Package declaration
package com.walng.dhagz.paypalcalc.presenters;

// 2. Imports (Android first, then third-party, then internal)
import android.content.Context;
import com.google.gson.Gson;
import com.walng.dhagz.paypalcalc.models.Currency;

// 3. Class Javadoc
/**
 * @author Dhagz
 * @since 2016-03-25
 */
// 4. Class declaration
public class PayPalCalcPresenter {
    
    // 5. Static members
    private static PayPalCalcPresenter instance;
    
    // 6. Instance members
    private Context context;
    private PayPalCalcView payPalCalcView;
    
    // 7. Static methods (getInstance first)
    public static PayPalCalcPresenter getInstance(Context context) { }
    
    // 8. Constructor
    public PayPalCalcPresenter(Context context) { }
    
    // 9. Public methods
    public void bindPayPalCalcView(PayPalCalcView view) { }
    
    // 10. Private methods
    private void updateView() { }
}
```

## Common Patterns

### 1. View Binding with ButterKnife

**MainActivity Pattern**:
```java
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.amount) EditText mAmount;
    @Bind(R.id.currency_spinner) Spinner mCurrencySpinner;
    // ... more bindings
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);  // Single call binds all @Bind fields
        
        // Now mAmount, mCurrencySpinner are initialized
    }
}
```

**No Manual findViewById**: ButterKnife eliminates this boilerplate

### 2. Presenter Binding

**Standard Activity onCreate Flow**:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    
    // Get singleton instance
    presenter = PayPalCalcPresenter.getInstance(this);
    
    // Bind view callbacks
    presenter.bindPayPalCalcView(this);
    
    // Setup listeners
    mAmount.addTextChangedListener(mAmountTextWatcher);
}
```

### 3. Listener Definitions

**Member Variable Pattern** (not anonymous inner classes):
```java
private TextWatcher mAmountTextWatcher = new TextWatcher() {
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateAmount(s.toString());
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        // do nothing
    }
};
```

**Why**: Clearer intent, reusable, easier to test

### 4. Null Safety

**Checking Before Use**:
```java
if (presenter != null) {
    presenter.onAmountChanged(amount);
}

if (payPalCalcView != null) {
    payPalCalcView.setTransactionTotal(total);
}
```

**Provider Fallbacks**:
```java
@Override
public LinkedList<Currency> get() {
    LinkedList<Currency> list = super.get();
    if (list == null) {
        return getDefaultCurrencyList();
    }
    return list;
}
```

### 5. Try-Catch for Parsing

**Safe Float Parsing**:
```java
float amount;
try {
    amount = Float.parseFloat(amountString);
} catch (Exception ex) {
    amount = 0;  // Default fallback
}
```

**Used For**: User input parsing (amount, percentage, fixed fee)

### 6. String Formatting

**DecimalFormat for Currency**:
```java
DecimalFormat formatter = new DecimalFormat(symbol + " #,###,###.00");
String formatted = formatter.format(amount);
// Result: "USD 1,234.56"
```

**String.format for Percentages**:
```java
String percent = String.format("%.2f%%", percentageCharge);
// Result: "4.40%"
```

### 7. Data Persistence

**Save Pattern**:
```java
// In Presenter
public void setCurrency(Currency currency) {
    // Update in-memory list
    mCurrencies.set(index, currency);
    
    // Persist immediately
    CurrencyListProvider.getInstance(context).set(mCurrencies);
    
    // Update view
    payPalCalcView.setTransactionPercentage(formatted);
}
```

**Load Pattern**:
```java
// In Presenter.bindPayPalCalcView()
this.mCurrencies = CurrencyListProvider.getInstance(context).get();
```

## Comments and Documentation

### Javadoc Style

**Class Documentation**:
```java
/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class Currency implements Parcelable { }
```

**Section Comments**:
```java
/**
 * PARCELLING PART
 */
public Currency(Parcel in) { }
```

**Inline Comments**:
```java
// do nothing
// save to preferences
// get dialog_change.xml view
```

**Comment Style**: Minimal, only for non-obvious code

## Error Handling

**Current Approach**: Defensive programming
- Try-catch on user input parsing
- Null checks before method calls
- Default values on exceptions (0 for amounts)
- No custom exception types
- No error dialogs (silent failures)

**Google Analytics**:
```xml
<bool name="ga_reportUncaughtExceptions">true</bool>
```
Uncaught exceptions automatically reported to Analytics

## Testing

**Current State**: No unit tests, no instrumentation tests
**Test Dependencies**: Commented out in build.gradle
```gradle
// testCompile 'junit:junit:4.12'
```

**Testable Design**: MVP pattern supports testing (Presenter has no Android deps)

## Code Style

### Indentation
- **4 spaces** (not tabs)
- Braces on same line (K&R style)

### Braces
```java
if (condition) {
    doSomething();
}

for (int i = 0; i < count; i++) {
    process(i);
}
```

### Line Length
- No strict limit (some lines exceed 100 characters)
- Break at logical points (after operators, commas)

### Blank Lines
- One blank line between methods
- Two blank lines between major sections (static vs instance)

## Android-Specific Conventions

### Context Usage
- Always pass Context to singletons
- Store as member variable
- Use `getApplication()` for Analytics (avoid Activity context)

### Parcelable Implementation
```java
public class Currency implements Parcelable {
    // Constructor from Parcel
    public Currency(Parcel in) { }
    
    @Override
    public void writeToParcel(Parcel parcel, int flags) { }
    
    @Override
    public int describeContents() { return 0; }
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { };
}
```

### String Resources
- All user-facing text in strings.xml
- IDs/keys in donottranslate.xml
- HTML formatting in CDATA: `<![CDATA[...]]>`

### Intent Handling
```java
try {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://...")));
} catch (android.content.ActivityNotFoundException e) {
    // Fallback to web URL
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://...")));
}
```

## Anti-Patterns Present

1. **No ViewHolder in Adapter**: CurrencyListAdapter inflates view every time
2. **Static SharedPreferences**: Could cause issues with multiple contexts
3. **Context in Singletons**: Potential memory leak (though app is single-activity)
4. **No Unbinding**: Presenter holds view reference indefinitely
5. **Magic Numbers**: Percentage and amount charges hardcoded in provider
6. **No Resource Cleanup**: TextWatcher never removed from EditText

**Note**: These are acceptable for a small, single-activity app but would need fixing in larger projects.
