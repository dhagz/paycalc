package com.walng.dhagz.paypalcalc.presenters;

import com.walng.dhagz.paypalcalc.models.Currency;
import com.walng.dhagz.paypalcalc.providers.CurrencyListProvider;
import com.walng.dhagz.paypalcalc.views.PayPalCalcView;

import java.text.DecimalFormat;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class PayPalCalcPresenter {

    private static PayPalCalcPresenter instance;
    private PayPalCalcView payPalCalcView;
    private Currency mCurrency;

    public static PayPalCalcPresenter getInstance() {
        if (instance == null) {
            instance = new PayPalCalcPresenter();
        }
        return instance;
    }

    public PayPalCalcPresenter() {
        //
    }

    public void bindPayPalCalcView(PayPalCalcView view) {
        this.payPalCalcView = view;
        if (this.payPalCalcView != null) {
            this.payPalCalcView.populateCurrencyList(CurrencyListProvider.getCurrencyList());
        }
    }

    public void setCurrency(Currency currency) {
        this.mCurrency = currency;
        DecimalFormat formatter = new DecimalFormat(currency.getSymbol() + " #,###,###.00");
        String transactionAddition = formatter.format(currency.getAmountCharge());
        String transactionPercentage = String.format("%.2f%%", currency.getPercentageCharge());
        if (payPalCalcView != null) {
            payPalCalcView.setTransactionAddition(transactionAddition);
            payPalCalcView.setTransactionPercentage(transactionPercentage);
            // clear previous data
            payPalCalcView.setTransactionTotal("");
            payPalCalcView.setAmountTotal("");
        }
    }

    public void onAmountChanged(float amount) {
        String symbol = "";
        float percent = 0;
        float additional = 0;
        float totalPrice = 0;
        float addToAmount = 0;
        if (mCurrency != null) {
            symbol = mCurrency.getSymbol() + " ";
            percent = mCurrency.getPercentageCharge();
            additional = mCurrency.getAmountCharge();
        }

        totalPrice = ((amount + additional) / ((100 - percent) / 100));
        addToAmount = totalPrice - amount;

        DecimalFormat formatter = new DecimalFormat(symbol + " #,###,###.00");
        String sTotalPrice = formatter.format(totalPrice);
        String sAddToAmount = formatter.format(addToAmount);

        if (payPalCalcView != null) {
            payPalCalcView.setTransactionTotal(sAddToAmount);
            payPalCalcView.setAmountTotal(sTotalPrice);
        }
    }

}
