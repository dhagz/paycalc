package com.walng.dhagz.paypalcalc.providers;

import com.walng.dhagz.paypalcalc.models.Currency;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class CurrencyListProvider {

    public static List<Currency> getCurrencyList() {
        LinkedList<Currency> currencies = new LinkedList<>();

        currencies.add(new Currency("Philippine Peso", "PHP", 4.4f, 15));
        currencies.add(new Currency("US Dollar", "USD", 4.4f, 0.3f));

        return currencies;
    }

}
