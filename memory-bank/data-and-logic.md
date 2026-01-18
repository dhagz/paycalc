# Data Models and Business Logic - PayPal Calc

## Core Data Model

### Currency Class

**Location**: `app/src/main/java/com/walng/dhagz/paypalcalc/models/Currency.java`

**Purpose**: Represents a currency with its PayPal fee structure

#### Properties
```java
private int id;                    // Unique identifier (1-25, 0=Custom)
private String name;               // Display name: "Philippine Peso"
private String symbol;             // Currency code: "PHP"
private float percentageCharge;    // PayPal percentage: 4.4
private float amountCharge;        // PayPal fixed fee: 15.0
```

#### Immutability Design
- **ID, name, symbol**: Set in constructor, no setters (immutable)
- **percentageCharge, amountCharge**: Have setters (user can customize)

#### Constructor
```java
public Currency(int id, String name, String symbol, 
                float percentageCharge, float amountCharge)
```

**Example**:
```java
new Currency(15, "Philippine Peso", "PHP", 4.4f, 15f)
new Currency(25, "US Dollar", "USD", 4.4f, 0.3f)
new Currency(10, "Japanese Yen", "JPY", 3.9f, 40f)  // Different fees!
```

#### Parcelable Implementation

**Why**: Efficient passing between Android components

**Methods**:
- `Currency(Parcel in)`: Constructor from parcel
- `writeToParcel(Parcel parcel, int flags)`: Serialize to parcel
- `describeContents()`: Returns 0 (no file descriptors)
- `CREATOR`: Static factory for parcel unpacking

**Parcel Order**:
1. id (int)
2. name (String)
3. symbol (String)
4. percentageCharge (float)
5. amountCharge (float)

**Note**: Must read in same order as written

#### Object Methods

**equals()**: Compares all 5 fields
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Currency)) return false;
    
    Currency currency = (Currency) o;
    return id == currency.id
        && Float.compare(currency.percentageCharge, percentageCharge) == 0
        && Float.compare(currency.amountCharge, amountCharge) == 0
        && Objects.equals(name, currency.name)
        && Objects.equals(symbol, currency.symbol);
}
```

**hashCode()**: Uses all 5 fields
```java
@Override
public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
    result = 31 * result + (percentageCharge != +0.0f ? Float.floatToIntBits(percentageCharge) : 0);
    result = 31 * result + (amountCharge != +0.0f ? Float.floatToIntBits(amountCharge) : 0);
    return result;
}
```

**toString()**: User-friendly display format
```java
@Override
public String toString() {
    return name + " (" + symbol + ")";
}
// Result: "Philippine Peso (PHP)"
```

**Usage**: Default display in Spinner dropdown

## Business Logic: Fee Calculation

### The PayPal Formula

**Location**: `PayPalCalcPresenter.onAmountChanged()` (Lines 74-91)

#### Variables
- `amount`: User's desired receive amount
- `additional`: Fixed fee (e.g., 15 PHP)
- `percent`: Percentage fee (e.g., 4.4%)
- `totalPrice`: Amount to charge customer
- `addToAmount`: Extra amount covering fees

#### Formula
```java
totalPrice = ((amount + additional) / ((100 - percent) / 100));
addToAmount = totalPrice - amount;
```

#### Mathematical Derivation

**Goal**: Calculate total charge `T` such that after fees, you receive `A`

**PayPal Fee Formula**:
```
Fee = (T × percent/100) + additional
```

**Receive Amount**:
```
A = T - Fee
A = T - (T × percent/100 + additional)
A = T(1 - percent/100) - additional
```

**Solve for T**:
```
A + additional = T(1 - percent/100)
T = (A + additional) / (1 - percent/100)
T = (A + additional) / ((100 - percent)/100)
```

**This matches the code!**

#### Worked Example 1: Philippine Peso

**Input**:
- Desired amount: 100 PHP
- Percentage: 4.4%
- Fixed fee: 15 PHP

**Calculation**:
```java
totalPrice = (100 + 15) / ((100 - 4.4) / 100)
           = 115 / 0.956
           = 120.29

addToAmount = 120.29 - 100
            = 20.29
```

**Result**:
- Charge customer: **120.29 PHP**
- PayPal takes: **20.29 PHP** (4.4% of 120.29 = 5.29 + 15 = 20.29)
- You receive: **100.00 PHP** ✓

#### Worked Example 2: US Dollar

**Input**:
- Desired amount: 100 USD
- Percentage: 4.4%
- Fixed fee: 0.30 USD

**Calculation**:
```java
totalPrice = (100 + 0.30) / ((100 - 4.4) / 100)
           = 100.30 / 0.956
           = 104.92

addToAmount = 104.92 - 100
            = 4.92
```

**Result**:
- Charge customer: **104.92 USD**
- PayPal takes: **4.92 USD** (4.4% of 104.92 = 4.62 + 0.30 = 4.92)
- You receive: **100.00 USD** ✓

#### Worked Example 3: Japanese Yen (Different Fees)

**Input**:
- Desired amount: 10000 JPY
- Percentage: 3.9% (lower!)
- Fixed fee: 40 JPY (higher!)

**Calculation**:
```java
totalPrice = (10000 + 40) / ((100 - 3.9) / 100)
           = 10040 / 0.961
           = 10447.45

addToAmount = 10447.45 - 10000
            = 447.45
```

**Result**:
- Charge customer: **10,447.45 JPY**
- PayPal takes: **447.45 JPY** (3.9% of 10447.45 = 407.45 + 40 = 447.45)
- You receive: **10,000.00 JPY** ✓

### Edge Cases

#### Zero Amount
```java
amount = 0;
totalPrice = (0 + 15) / 0.956 = 15.69
addToAmount = 15.69 - 0 = 15.69
```
**Result**: Still need to charge for fixed fee

#### Empty Input
```java
try {
    amount = Float.parseFloat("");  // Throws NumberFormatException
} catch (Exception ex) {
    amount = 0;  // Default to 0
}
```
**Result**: Treats as zero amount

#### Very Large Amount
```java
amount = 1000000;
totalPrice = (1000000 + 15) / 0.956 = 1045636.31
```
**No Validation**: Accepts any float value

#### Negative Amount (Prevented)
```xml
<EditText android:digits="1234567890." />
```
**UI Prevention**: Digits filter doesn't allow minus sign

## Currency Data Structure

### Default Currencies (26 Total)

**Location**: `CurrencyListProvider.getDefaultCurrencyList()`

#### Currency List
```java
ID  Name                      Symbol  Percent  Fixed Fee
==  =======================   ======  =======  =========
15  Philippine Peso           PHP     4.4      15.00     [Default/First]
1   Australian Dollar         AUD     4.4      0.30
2   Brazilian Real            BRL     4.4      0.60
3   Canadian Dollar           CAD     4.4      0.30
4   Czech Koruna              CZK     4.4      10.00
5   Danish Kroner             DKK     4.4      2.60
6   Euro                      EUR     4.4      0.35
7   Hong Kong Dollar          HKD     4.4      2.35
8   Hungarian Forint          HUF     4.4      90.00
9   Israeli New Shekel        ILS     4.4      1.20
10  Japanese Yen              JPY     3.9      40.00     [Different %!]
11  Malaysian Ringgit         MYR     4.4      2.00
12  Mexican Peso              MXN     4.4      4.00
13  New Zealand Dollar        NZD     4.4      0.45
14  Norwegian Krone           NOK     4.4      2.80
16  Polish Zloty              PLN     4.4      1.35
17  Russian Ruble             RUB     4.4      10.00
18  Singapore Dollar          SGD     4.4      0.50
19  Swedish Krona             SEK     4.4      3.25
20  Swiss Franc               CHF     4.4      0.55
21  New Taiwan Dollar         TWD     4.4      10.00
22  Thai Baht                 THB     4.4      11.00
23  Turkish Lira              TRY     4.4      0.45
24  U.K. Pounds Sterling      GBP     4.4      0.20
25  US Dollar                 USD     4.4      0.30
0   Custom                    (empty) 4.4      0.30      [Customizable]
```

#### Notable Patterns

**Standard Structure** (24 currencies):
- Percentage: 4.4%
- Fixed fee: Varies by currency strength

**Exception - Japanese Yen**:
- Percentage: 3.9% (lower)
- Fixed fee: 40 JPY (higher)
- **Reason**: Different PayPal pricing for JPY region

**Custom Currency**:
- ID: 0 (special case)
- Name: "Custom"
- Symbol: Empty string
- Default fees: 4.4% + 0.30
- **Purpose**: User can set any fees

#### Fee Observations

**Low Fixed Fees** (Western currencies):
- USD: $0.30
- GBP: £0.20
- EUR: €0.35
- AUD, CAD: $0.30

**Medium Fixed Fees** (Asian currencies):
- PHP: ₱15.00
- HKD: HK$2.35
- SGD: S$0.50

**High Fixed Fees** (Weaker currencies):
- HUF: Ft90.00
- CZK: Kč10.00
- JPY: ¥40.00
- RUB: ₽10.00

**Pattern**: Fee roughly equivalent to $0.20-0.40 USD when converted

### Data Persistence

#### Storage Format
```json
[
  {
    "id": 15,
    "name": "Philippine Peso",
    "symbol": "PHP",
    "percentageCharge": 4.4,
    "amountCharge": 15.0
  },
  // ... 25 more currencies
]
```

**Storage Location**: SharedPreferences
**Key**: "CurrencyListProvider"
**Serialization**: Gson (automatic)

#### Load Priority
1. **Saved Data**: Load from SharedPreferences if exists
2. **Defaults**: Use `getDefaultCurrencyList()` if null
3. **Never Null**: Always returns valid list

#### Save Trigger
**Immediate**: Every currency change saved instantly
```java
public void setCurrency(Currency currency) {
    // Update list
    mCurrencies.set(index, currency);
    // Save immediately
    CurrencyListProvider.getInstance(context).set(mCurrencies);
}
```

**No Batching**: Each change is a separate write

## State Management

### Presenter State

**Singleton Instance**: Lives for app lifetime
```java
private static PayPalCalcPresenter instance;
```

**Held State**:
```java
private Context context;                        // App context
private PayPalCalcView payPalCalcView;         // View reference
private LinkedList<Currency> mCurrencies;      // All currencies
private Currency mCurrency;                     // Currently selected
```

**Lifecycle**:
1. Created on first `getInstance()` call
2. Survives configuration changes (rotation)
3. View reference updated on each `bindPayPalCalcView()`
4. Never destroyed (until app process killed)

### View State (NOT Saved)

**Lost on Rotation**:
- EditText amount value
- Scroll position
- Focus state

**Survives**:
- Currency list (in Presenter)
- Selected currency (in Presenter)
- Custom fees (in SharedPreferences)

**Missing**: No `onSaveInstanceState()` implementation

## Formatting Logic

### Currency Display

**DecimalFormat Pattern**:
```java
DecimalFormat formatter = new DecimalFormat(symbol + " #,###,###.00");
```

**Examples**:
- `"PHP 1,234.56"`
- `"USD 123.45"`
- `" 999.99"` (if symbol empty)

**Features**:
- Thousands separator: comma
- Always 2 decimal places
- Symbol prefix with space

### Percentage Display

**String.format Pattern**:
```java
String.format("%.2f%%", percentageCharge);
```

**Examples**:
- `"4.40%"`
- `"3.90%"`
- `"0.00%"`

**Features**:
- Always 2 decimal places
- Percent symbol suffix
- No thousands separator

## Validation

### Amount Input Validation

**XML Restrictions**:
```xml
android:inputType="numberDecimal"
android:digits="1234567890."
```

**Allowed**: Digits 0-9 and one decimal point
**Blocked**: Letters, symbols (except .), negative sign

**Java Parsing**:
```java
try {
    amount = Float.parseFloat(amountString);
} catch (Exception ex) {
    amount = 0;  // Silent default
}
```

**No Validation For**:
- Maximum value
- Multiple decimal points (prevented by digits filter)
- Scientific notation

### Fee Customization Validation

**None**: User can enter any value in ChangeDialog
```java
try {
    percentage = Float.parseFloat(userInput);
} catch (Exception ex) {
    percentage = 0;
}
```

**Possible Issues**:
- Negative fees: Accepted
- Zero fees: Accepted
- Very large fees: Accepted
- Percentage > 100: Accepted (mathematical nonsense)

**No UI Feedback**: Silent defaults to 0 on parse error

## Calculation Performance

### Complexity: O(1)
- Two divisions
- Two subtractions
- One addition
- No loops, no recursion

### Precision: float
```java
float amount;
float totalPrice;
```

**Precision Issues**: Possible rounding errors at high precision
- Float: ~7 decimal digits accuracy
- Could use `double` for better precision
- Or `BigDecimal` for exact currency calculations

**Practical Impact**: Minimal for typical amounts (< $10,000)

### Formatting Performance

**DecimalFormat**: Relatively expensive
- Created on every calculation
- Pattern parsing on each instantiation

**Optimization Opportunity**:
```java
// Current: Creates new formatter each time
DecimalFormat formatter = new DecimalFormat(symbol + " #,###,###.00");

// Better: Cache formatters
private Map<String, DecimalFormat> formatters = new HashMap<>();
```

**Not Implemented**: Premature optimization for this use case

## Business Rules

### Rule 1: All Fees Are Positive
**Assumption**: PayPal charges, never credits
**Not Enforced**: User could set negative fees

### Rule 2: Percentage < 100
**Assumption**: PayPal takes less than 100% of transaction
**Not Enforced**: User could set percentage >= 100

### Rule 3: Symbol Matches ID
**Assumption**: Each ID maps to specific currency
**Enforced By**: Hardcoded in `getDefaultCurrencyList()`

### Rule 4: Currency IDs Are Unique
**Assumption**: No two currencies share same ID
**Enforced By**: Manual assignment in code

### Rule 5: First Currency Is Default
**Assumption**: PHP (ID 15) is default selection
**Enforced By**: Spinner auto-selects position 0

### Rule 6: Custom Currency Is Last
**Assumption**: ID 0 currency always appears last
**Enforced By**: Added last in `getDefaultCurrencyList()`

## Integration Points

### Google Analytics Events

**Could Track** (but doesn't):
- Currency selection
- Fee customization
- Calculation events
- Error events

**Currently Tracks**:
- Screen views only (MainActivity)

**Opportunity**: Add event tracking in Presenter
```java
public void onAmountChanged(float amount) {
    // Calculation logic...
    
    // Could add:
    AnalyticsManager.getInstance(context)
        .sendEvent("Calculation", "Amount: " + amount);
}
```

### AdMob Integration

**No Business Logic**: Ads don't affect calculations
**Separate Concern**: UI-only integration

## Testing Considerations

### Unit Test Targets (Not Implemented)

**Testable Methods**:
```java
// PayPalCalcPresenter
@Test
public void testCalculation_standardFees() {
    Currency currency = new Currency(1, "USD", "USD", 4.4f, 0.3f);
    presenter.setCurrency(currency);
    presenter.onAmountChanged(100f);
    
    verify(view).setTransactionTotal("USD 4.92");
    verify(view).setAmountTotal("USD 104.92");
}

// Currency
@Test
public void testEquals_sameCurrency() {
    Currency c1 = new Currency(1, "USD", "USD", 4.4f, 0.3f);
    Currency c2 = new Currency(1, "USD", "USD", 4.4f, 0.3f);
    assertEquals(c1, c2);
}
```

**MVP Advantage**: Presenter has no Android dependencies, easily testable

### Integration Test Targets

**Not Implemented**: Could test end-to-end flow
```java
@Test
public void testUserFlow_calculateFees() {
    // Select currency
    onView(withId(R.id.currency_spinner)).perform(click());
    onData(allOf(is(instanceOf(Currency.class)), 
                 hasProperty("symbol", is("USD"))))
        .perform(click());
    
    // Enter amount
    onView(withId(R.id.amount)).perform(typeText("100"));
    
    // Verify result
    onView(withId(R.id.total_amount))
        .check(matches(withText("USD 104.92")));
}
```
