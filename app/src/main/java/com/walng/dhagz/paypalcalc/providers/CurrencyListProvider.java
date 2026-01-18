package com.walng.dhagz.paypalcalc.providers;

import android.content.Context;
import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.walng.dhagz.paypalcalc.models.Currency;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class CurrencyListProvider extends ListProvider<Currency> {

    private static final String TAG = "CurrencyListProvider";
    private static CurrencyListProvider instance = null;

    public static CurrencyListProvider getInstance(Context context) {
        if (instance == null) {
            instance = new CurrencyListProvider(context);
        }
        return instance;
    }

    public CurrencyListProvider(Context context) {
        super(context);
    }

    private LinkedList<Currency> getDefaultCurrencyList() {
        LinkedList<Currency> currencies = new LinkedList<>();

        currencies.add(new Currency(15, "Philippine Peso", "PHP", 4.4f, 15));
        currencies.add(new Currency(1, "Australian Dollar", "AUD", 4.4f, 0.3f));
        currencies.add(new Currency(2, "Brazilian Real", "BRL", 4.4f, 0.6f));
        currencies.add(new Currency(3, "Canadian Dollar", "CAD", 4.4f, 0.3f));
        currencies.add(new Currency(4, "Czech Koruna", "CZK", 4.4f, 10));
        currencies.add(new Currency(5, "Danish Kroner", "DKK", 4.4f, 2.6f));
        currencies.add(new Currency(6, "Euro", "EUR", 4.4f, 0.35f));
        currencies.add(new Currency(7, "Hong Kong Dollar", "HKD", 4.4f, 2.35f));
        currencies.add(new Currency(8, "Hungarian Forint", "HUF", 4.4f, 90f));
        currencies.add(new Currency(9, "Israeli New Shekel", "ILS", 4.4f, 1.2f));
        currencies.add(new Currency(10, "Japanese Yen", "JPY", 3.9f, 40f));
        currencies.add(new Currency(11, "Malaysian Ringgit", "MYR", 4.4f, 2f));
        currencies.add(new Currency(12, "Mexican Peso", "MXN", 4.4f, 4f));
        currencies.add(new Currency(13, "New Zealand Dollar", "NZD", 4.4f, 0.45f));
        currencies.add(new Currency(14, "Norwegian Krone", "NOK", 4.4f, 2.8f));
        currencies.add(new Currency(16, "Polish Zloty", "PLN", 4.4f, 1.35f));
        currencies.add(new Currency(17, "Russian Ruble", "RUB", 4.4f, 10f));
        currencies.add(new Currency(18, "Singapore Dollar", "SGD", 4.4f, 0.5f));
        currencies.add(new Currency(19, "Swedish Krona", "SEK", 4.4f, 3.25f));
        currencies.add(new Currency(20, "Swiss Franc", "CHF", 4.4f, 0.55f));
        currencies.add(new Currency(21, "New Taiwan Dollar", "TWD", 4.4f, 10f));
        currencies.add(new Currency(22, "Thai Baht", "THB", 4.4f, 11f));
        currencies.add(new Currency(23, "Turkish Lira", "TRY", 4.4f, 0.45f));
        currencies.add(new Currency(24, "U.K. Pounds Sterling", "GBP", 4.4f, 0.2f));
        currencies.add(new Currency(25, "US Dollar", "USD", 4.4f, 0.3f));
        currencies.add(getOtherCurrency());

        return currencies;
    }

    @Override
    public LinkedList<Currency> get() {
        LinkedList<Currency> list = super.get();
        if (list == null) {
            return getDefaultCurrencyList();
        }
        return list;
    }

    @Override
    public void set(LinkedList<Currency> items) {
        if (items == null) {
            items = getDefaultCurrencyList();
        }
        super.set(items);
    }

    public void setDefaults() {
        set(null);
    }

    @Override
    public String getPreferenceKey() {
        return TAG;
    }

    @Override
    public Type getType() {
        return new TypeToken<LinkedList<Currency>>(){}.getType();
    }

    public static Currency getCurrencyById(List<Currency> currencies, int id) {
        for (Currency currency : currencies) {
            if (currency.getId() == id) {
                return currency;
            }
        }
        return getOtherCurrency();
    }

    public static Currency getOtherCurrency() {
        return new Currency(0, "Custom", "", 4.4f, 0.3f);
    }
}
