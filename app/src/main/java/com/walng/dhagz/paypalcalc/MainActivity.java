package com.walng.dhagz.paypalcalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.walng.dhagz.paypalcalc.adapters.CurrencyListAdapter;
import com.walng.dhagz.paypalcalc.managers.AnalyticsManager;
import com.walng.dhagz.paypalcalc.models.Currency;
import com.walng.dhagz.paypalcalc.presenters.PayPalCalcPresenter;
import com.walng.dhagz.paypalcalc.views.PayPalCalcView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PayPalCalcView {

    private static final String TAG = "MainActivity";

    @Bind(R.id.currency_spinner)
    Spinner mCurrencySpinner;

    @Bind(R.id.amount)
    EditText mAmount;

    @Bind(R.id.paypal_transaction_charge_percentage)
    TextView mTransactionPercentage;

    @Bind(R.id.paypal_transaction_charge_additional)
    TextView mTransactionAddition;

    @Bind(R.id.paypal_transaction_charge_total)
    TextView mTransactionTotal;

    @Bind(R.id.total_amount)
    TextView mAmountTotal;

    @Bind(R.id.ad_view)
    AdView mAdView;

    private PayPalCalcPresenter presenter;
    private CurrencyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AnalyticsManager.getInstance(getApplication()).setScreen(TAG);

        presenter = PayPalCalcPresenter.getInstance();
        presenter.bindPayPalCalcView(this);

        //
        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    private void updateAmount(String amountString) {
        float amount;
        try {
            amount = Float.parseFloat(amountString);
        } catch (Exception ex) {
            amount = 0;
        }
        if (presenter != null) {
            presenter.onAmountChanged(amount);
        }
    }

    @Override
    public void populateCurrencyList(List<Currency> currencies) {
        // Initialize the adapter sending the current context
        // Send the simple_spinner_item layout
        // And finally send the Users array (Your data)
        adapter = new CurrencyListAdapter(this, currencies);
        mCurrencySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Here you get the current item (a Currency object) that is selected by its position
                Currency currency = adapter.getItem(position);
                presenter.setCurrency(currency);
                updateAmount(mAmount.getText().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
        mCurrencySpinner.setSelection(0);
    }

    @Override
    public void setTransactionAddition(String transactionAddition) {
        this.mTransactionAddition.setText(transactionAddition);
    }

    @Override
    public void setTransactionPercentage(String transactionPercentage) {
        this.mTransactionPercentage.setText(transactionPercentage);
    }

    @Override
    public void setTransactionTotal(String transactionTotal) {
        this.mTransactionTotal.setText(transactionTotal);
    }

    @Override
    public void setAmountTotal(String amountTotal) {
        this.mAmountTotal.setText(amountTotal);
    }
}
