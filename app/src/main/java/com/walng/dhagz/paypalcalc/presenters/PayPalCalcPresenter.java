package com.walng.dhagz.paypalcalc.presenters;

import android.content.Context;

import com.walng.dhagz.paypalcalc.models.Currency;
import com.walng.dhagz.paypalcalc.providers.CurrencyListProvider;
import com.walng.dhagz.paypalcalc.views.PayPalCalcView;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class PayPalCalcPresenter {

    private static PayPalCalcPresenter instance;

    private Context context;
    private PayPalCalcView payPalCalcView;
    private LinkedList<Currency> mCurrencies;
    private Currency mCurrency;

    public static PayPalCalcPresenter getInstance(Context context) {
        if (instance == null) {
            instance = new PayPalCalcPresenter(context);
        }
        return instance;
    }

    public PayPalCalcPresenter(Context context) {
        this.context = context;
    }

    public void bindPayPalCalcView(PayPalCalcView view) {
        this.payPalCalcView = view;
        this.mCurrencies = CurrencyListProvider.getInstance(context).get();
        if (this.payPalCalcView != null) {
            this.payPalCalcView.populateCurrencyList(this.mCurrencies);
        }
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        for (int i = 0; i < mCurrencies.size(); i++) {
            if (mCurrencies.get(i).getId() == currency.getId()) {
                mCurrencies.set(i, currency);
                mCurrency = mCurrencies.get(i);
                break;
            }
        }
        // seve to preferences
        CurrencyListProvider.getInstance(context).set(mCurrencies);
        // update the view
        DecimalFormat formatter = new DecimalFormat(mCurrency.getSymbol() + " #,###,###.00");
        String transactionAddition = formatter.format(mCurrency.getAmountCharge());
        String transactionPercentage = String.format("%.2f%%", mCurrency.getPercentageCharge());
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
        float totalPrice;
        float addToAmount;
        if (mCurrency != null) {
            symbol = mCurrency.getSymbol().isEmpty() ? "" : mCurrency.getSymbol() + " ";
            percent = mCurrency.getPercentageCharge();
            additional = mCurrency.getAmountCharge();
        }

        totalPrice = ((amount + additional) / ((100 - percent) / 100));
        addToAmount = totalPrice - amount;

        DecimalFormat formatter = new DecimalFormat(symbol + "#,###,###.00");
        String sTotalPrice = formatter.format(totalPrice);
        String sAddToAmount = formatter.format(addToAmount);

        if (payPalCalcView != null) {
            payPalCalcView.setTransactionTotal(sAddToAmount);
            payPalCalcView.setAmountTotal(sTotalPrice);
        }
    }

    public void displayPercentageChargeChange() {
        if (payPalCalcView != null) {
            payPalCalcView.promptPercentageChargeChange(mCurrency.getPercentageCharge());
        }
    }

    public void displayAdditionalChargeChange() {
        if (payPalCalcView != null) {
            payPalCalcView.promptAdditionalChargeChange(mCurrency.getAmountCharge());
        }
    }

    public void changePercentageCharge(float percentageCharge) {
        if (payPalCalcView != null) {
            Currency currency = getCurrency();
            currency.setPercentageCharge(percentageCharge);
            setCurrency(currency);
        }
    }

    public void changeAdditionalCharge(float additionalCharge) {
        if (payPalCalcView != null) {
            Currency currency = getCurrency();
            currency.setAmountCharge(additionalCharge);
            setCurrency(currency);
        }
    }

}
