package com.walng.dhagz.paypalcalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.walng.dhagz.paypalcalc.adapters.CurrencyListAdapter;
import com.walng.dhagz.paypalcalc.databinding.ActivityMainBinding;
import com.walng.dhagz.paypalcalc.managers.AnalyticsManager;
import com.walng.dhagz.paypalcalc.models.Currency;
import com.walng.dhagz.paypalcalc.presenters.PayPalCalcPresenter;
import com.walng.dhagz.paypalcalc.views.PayPalCalcView;

import java.util.LinkedList;

import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity implements PayPalCalcView {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private TextWatcher mAmountTextWatcher = new TextWatcher() {
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
    };

    private View.OnClickListener mTransactionPercentageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (presenter != null) {
                presenter.displayPercentageChargeChange();
            }
        }
    };

    private View.OnClickListener mTransactionAdditionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (presenter != null) {
                presenter.displayAdditionalChargeChange();
            }
        }
    };

    private View.OnClickListener mSendSomeLoveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    };

    private PayPalCalcPresenter presenter;
    private CurrencyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AnalyticsManager.getInstance(getApplication()).setScreen(TAG);

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});
        
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        
        presenter = PayPalCalcPresenter.getInstance(this);
        presenter.bindPayPalCalcView(this);

        // set the love
        binding.sendSomeLove.setText(Html.fromHtml(getString(R.string.send_some_love), Html.FROM_HTML_MODE_LEGACY));

        // add on text change on amount
        binding.amount.addTextChangedListener(mAmountTextWatcher);

        // add the click listeners
        binding.percentageContainer.setOnClickListener(mTransactionPercentageClickListener);
        binding.additionalContainer.setOnClickListener(mTransactionAdditionClickListener);
        binding.sendSomeLove.setOnClickListener(mSendSomeLoveClickListener);
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
    public void populateCurrencyList(LinkedList<Currency> currencies) {
        // Initialize the adapter sending the current context
        // Send the simple_spinner_item layout
        // And finally send the Users array (Your data)
        adapter = new CurrencyListAdapter(this, currencies);
        binding.currencySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        binding.currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Here you get the current item (a Currency object) that is selected by its position
                Currency currency = adapter.getItem(position);
                presenter.setCurrency(currency);
                updateAmount(binding.amount.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        binding.currencySpinner.setSelection(0);
    }

//    @Override
//    public Currency selectOtherCurrency() {
//        int lastPosition = adapter.getCount() - 1;
//        if (mCurrencySpinner.getSelectedItemPosition() != lastPosition) {
//            mCurrencySpinner.setSelection(lastPosition);
//        }
//        return (Currency) mCurrencySpinner.getSelectedItem();
//    }

    @Override
    public void setTransactionAddition(String transactionAddition) {
        binding.paypalTransactionChargeAdditional.setText(transactionAddition);
    }

    @Override
    public void setTransactionPercentage(String transactionPercentage) {
        binding.paypalTransactionChargePercentage.setText(transactionPercentage);
    }

    @Override
    public void setTransactionTotal(String transactionTotal) {
        binding.paypalTransactionChargeTotal.setText(transactionTotal);
    }

    @Override
    public void setAmountTotal(String amountTotal) {
        binding.totalAmount.setText(amountTotal);
    }

    @Override
    public void promptPercentageChargeChange(float currentPercentageCharge) {
        String defaultText = "";
        if (presenter != null && presenter.getCurrency() != null) {
            defaultText = presenter.getCurrency().getPercentageCharge() + "";
        }
        ChangeDialog.show(this,
                R.string.dialog_change_percentage_label,
                defaultText,
                new ChangeDialog.OnOkClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, String userInput) {
                        if (presenter != null) {
                            float percentage;
                            try {
                                percentage = Float.parseFloat(userInput);
                            } catch (Exception ex) {
                                percentage = 0;
                            }
                            if (presenter.getCurrency() != null) {
                                presenter.changeAdditionalCharge(presenter.getCurrency().getAmountCharge());
                            }
                            presenter.changePercentageCharge(percentage);
                            updateAmount(binding.amount.getText().toString());
                        }
                    }
                });
    }

    @Override
    public void promptAdditionalChargeChange(float currentAdditionalCharge) {
        String defaultText = "";
        if (presenter != null && presenter.getCurrency() != null) {
            defaultText = presenter.getCurrency().getAmountCharge() + "";
        }
        ChangeDialog.show(this,
                R.string.dialog_change_additional_label,
                defaultText,
                new ChangeDialog.OnOkClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, String userInput) {
                        if (presenter != null) {
                            float amount;
                            try {
                                amount = Float.parseFloat(userInput);
                            } catch (Exception ex) {
                                amount = 0;
                            }
                            if (presenter.getCurrency() != null) {
                                presenter.changePercentageCharge(presenter.getCurrency().getPercentageCharge());
                            }
                            presenter.changeAdditionalCharge(amount);
                            updateAmount(binding.amount.getText().toString());
                        }
                    }
                });
    }
}
