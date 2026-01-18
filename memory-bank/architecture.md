# Architecture - PayPal Calc

## Architectural Pattern: MVP (Model-View-Presenter)

This app uses the Model-View-Presenter pattern to separate concerns and maintain testable code.

## Layer Responsibilities

### Models (`models/`)
**Purpose**: Data structures and business entities

#### Currency.java
```java
public class Currency implements Parcelable {
    private int id;                    // Unique identifier
    private String name;               // Display name (e.g., "US Dollar")
    private String symbol;             // Currency symbol (e.g., "USD")
    private float percentageCharge;    // PayPal percentage (e.g., 4.4)
    private float amountCharge;        // PayPal fixed fee (e.g., 0.3)
}
```

**Key Features**:
- Implements `Parcelable` for efficient inter-component passing
- Immutable ID, mutable fees (user can customize)
- Includes `equals()` and `hashCode()` for collections
- `toString()` returns formatted display: "Currency Name (SYMBOL)"

### Views (`views/`)
**Purpose**: Interface contracts between Activity and Presenter

#### PayPalCalcView.java
```java
public interface PayPalCalcView {
    void populateCurrencyList(LinkedList<Currency> currencies);
    void setTransactionAddition(String transactionAddition);
    void setTransactionPercentage(String transactionPercentage);
    void setTransactionTotal(String transactionTotal);
    void setAmountTotal(String amountTotal);
    void promptPercentageChargeChange(float currentPercentageCharge);
    void promptAdditionalChargeChange(float currentAdditionalCharge);
}
```

**Design Pattern**: Activity implements this interface, allowing Presenter to update UI without Android dependencies.

### Presenters (`presenters/`)
**Purpose**: Business logic and state management

#### PayPalCalcPresenter.java

**Singleton Implementation**:
```java
private static PayPalCalcPresenter instance;

public static PayPalCalcPresenter getInstance(Context context) {
    if (instance == null) {
        instance = new PayPalCalcPresenter(context);
    }
    return instance;
}
```

**Key Responsibilities**:
1. **Currency Management** - Load/save from CurrencyListProvider
2. **Fee Calculation** - Core PayPal formula implementation
3. **View Updates** - Format and push data to view callbacks
4. **User Customization** - Handle fee modifications

**Core Calculation Logic** (Lines 74-91):
```java
totalPrice = ((amount + additional) / ((100 - percent) / 100));
addToAmount = totalPrice - amount;
```

**Formula Explanation**:
- `amount`: Desired receive amount
- `additional`: Fixed PayPal fee
- `percent`: Percentage fee (e.g., 4.4)
- `totalPrice`: Total to charge customer
- `addToAmount`: Extra amount covering fees

**Example**: Receive $100 with 4.4% + $0.30 fees
```
totalPrice = (100 + 0.30) / (95.6 / 100) = 104.98
addToAmount = 104.98 - 100 = 4.98
```

### Providers (`providers/`)
**Purpose**: Data persistence layer

#### ListProvider.java (Abstract Base)
```java
public abstract class ListProvider<D> {
    private static SharedPreferences sharedPreferences;
    
    public void set(LinkedList<D> items);  // Save with Gson
    public LinkedList<D> get();            // Load with Gson
    public abstract String getPreferenceKey();
    public abstract Type getType();        // For Gson TypeToken
}
```

**Storage Pattern**:
1. Uses SharedPreferences for lightweight persistence
2. Gson serializes `LinkedList<T>` to JSON string
3. TypeToken resolves generic type at runtime
4. Abstract methods enforce subclass configuration

#### CurrencyListProvider.java
**Singleton with Default Data**:
```java
public static CurrencyListProvider getInstance(Context context) {
    if (instance == null) {
        instance = new CurrencyListProvider(context);
    }
    return instance;
}
```

**Default Currency Data**:
- 26 predefined currencies with real PayPal fee structures
- Philippine Peso (PHP) as first/default currency
- Japanese Yen (JPY) has unique fee structure (3.9% + 40 JPY)
- Custom currency option (ID 0, empty symbol)

**Key Methods**:
- `getDefaultCurrencyList()`: Hardcoded 26 currencies
- `get()`: Returns stored list or defaults if null
- `set()`: Saves with null-safety (null → defaults)
- `getCurrencyById()`: Static helper for lookups

### Managers (`managers/`)
**Purpose**: Utility services

#### AnalyticsManager.java
**Singleton for Google Analytics**:
```java
private Tracker mTracker;

public void setScreen(String screenName);
public void sendEvent(String category, String action);
```

**Integration**:
- Obtains tracker from `PayPalCalcApplication` custom app class
- Wraps Google Analytics HitBuilders for cleaner API
- Used in MainActivity onCreate: `AnalyticsManager.getInstance(getApplication()).setScreen(TAG)`

### Adapters (`adapters/`)
**Purpose**: Bridge data to UI components

#### CurrencyListAdapter.java
**Classic BaseAdapter for Spinner**:
```java
public class CurrencyListAdapter extends BaseAdapter {
    private LinkedList<Currency> values;
    private LayoutInflater inflater;
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflates listitem_currency.xml
        // Displays: name, symbol, percent, amount
    }
}
```

**Note**: Does NOT implement ViewHolder pattern (potential optimization)

## Activity Layer

### MainActivity.java
**Implements**: `AppCompatActivity`, `PayPalCalcView`

**ButterKnife View Binding**:
```java
@Bind(R.id.currency_spinner) Spinner mCurrencySpinner;
@Bind(R.id.amount) EditText mAmount;
@Bind(R.id.ad_view) AdView mAdView;
// ... 7 more bindings
```

**Lifecycle Setup** (onCreate):
1. Bind views with ButterKnife
2. Set Analytics screen
3. Load AdMob banner
4. Get presenter singleton instance
5. Bind presenter to view (this)
6. Set HTML-formatted "Rate app" text
7. Attach listeners (TextWatcher, OnClickListeners)

**Listeners**:
- `mAmountTextWatcher`: Real-time calculation on text change
- `mTransactionPercentageClickListener`: Open percentage dialog
- `mTransactionAdditionClickListener`: Open fixed fee dialog
- `mSendSomeLoveClickListener`: Open Play Store listing

**View Implementation**:
- `populateCurrencyList()`: Creates CurrencyListAdapter for Spinner
- `setTransaction*()`: Updates TextViews with formatted strings
- `promptPercentageChargeChange()`: Shows ChangeDialog
- `promptAdditionalChargeChange()`: Shows ChangeDialog

## Custom Components

### ChangeDialog.java
**Purpose**: Reusable input dialog for fee customization

**Static Factory Method**:
```java
public static void show(
    Context context,
    @StringRes int hintLabel,
    String defaultText,
    final OnOkClickListener onOkClickListener
)
```

**Features**:
- Uses TextInputLayout (Material Design)
- Pre-fills with current value
- Callback interface for OK button
- Cancel dismisses without action

**Usage Pattern**:
```java
ChangeDialog.show(this, R.string.dialog_change_percentage_label, "4.4",
    (dialog, userInput) -> {
        float percentage = Float.parseFloat(userInput);
        presenter.changePercentageCharge(percentage);
    }
);
```

## Custom Application Class

### PayPalCalcApplication.java
**Purpose**: Initialize app-wide Google Analytics

**Declared in AndroidManifest.xml**:
```xml
<application android:name=".PayPalCalcApplication" ...>
```

**Singleton Tracker**:
```java
synchronized public Tracker getDefaultTracker() {
    if (mTracker == null) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        mTracker = analytics.newTracker(R.xml.global_tracker);
    }
    return mTracker;
}
```

## Data Flow

### Initialization Flow
```
App Launch
  → PayPalCalcApplication.onCreate()
  → MainActivity.onCreate()
    → ButterKnife.bind(this)
    → AnalyticsManager.getInstance().setScreen()
    → PayPalCalcPresenter.getInstance(context)
      → CurrencyListProvider.getInstance(context)
        → Load from SharedPreferences or defaults
      → presenter.bindPayPalCalcView(this)
        → view.populateCurrencyList(currencies)
          → Create CurrencyListAdapter
          → Set Spinner adapter
          → Auto-select first currency (PHP)
```

### User Input Flow
```
User types amount
  → TextWatcher.onTextChanged()
    → MainActivity.updateAmount(string)
      → Float.parseFloat(string)
      → presenter.onAmountChanged(float)
        → Get currency fees
        → Calculate totalPrice and addToAmount
        → Format with DecimalFormat
        → view.setTransactionTotal()
        → view.setAmountTotal()
          → Update TextViews
```

### Currency Change Flow
```
User selects currency
  → Spinner.OnItemSelectedListener
    → presenter.setCurrency(currency)
      → Update mCurrencies list in-memory
      → CurrencyListProvider.getInstance().set(mCurrencies)
        → Gson.toJson(list)
        → SharedPreferences.Editor.putString()
        → .commit()
      → Format display strings
      → view.setTransactionAddition()
      → view.setTransactionPercentage()
      → Clear previous totals
```

### Fee Customization Flow
```
User clicks percentage container
  → OnClickListener
    → presenter.displayPercentageChargeChange()
      → view.promptPercentageChargeChange(currentPercent)
        → ChangeDialog.show()
          User enters new value
          → OnOkClickListener.onClick(userInput)
            → presenter.changePercentageCharge(float)
              → currency.setPercentageCharge()
              → presenter.setCurrency(currency)
                → Save to SharedPreferences
                → Update view
              → updateAmount() to recalculate
```

## Key Design Principles

1. **Singleton Pattern**: Ensures single instance of managers/presenters across app lifecycle
2. **MVP Separation**: View has no business logic, Presenter has no Android dependencies
3. **Lazy Initialization**: Singletons created on first use
4. **Immutable IDs**: Currency IDs never change, fees are mutable
5. **Null Safety**: Providers return defaults if storage empty
6. **Declarative UI**: ButterKnife reduces boilerplate
7. **Static Utilities**: ChangeDialog uses static factory method
8. **Context Passing**: Required for all singletons (SharedPreferences, Analytics)
