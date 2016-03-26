package com.walng.dhagz.paypalcalc.views;

import com.walng.dhagz.paypalcalc.models.Currency;

import java.util.List;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public interface PayPalCalcView {
    void populateCurrencyList(List<Currency> currencies);

    void setTransactionAddition(String transactionAddition);

    void setTransactionPercentage(String transactionPercentage);

    void setTransactionTotal(String transactionTotal);

    void setAmountTotal(String amountTotal);
}
