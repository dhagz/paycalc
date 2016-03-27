package com.walng.dhagz.paypalcalc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.walng.dhagz.paypalcalc.R;
import com.walng.dhagz.paypalcalc.models.Currency;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class CurrencyListAdapter extends BaseAdapter {

    private LinkedList<Currency> values;
    private LayoutInflater inflater;

    public CurrencyListAdapter(Context context, LinkedList<Currency> values) {
        this.values = values;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Currency getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        if (position >= values.size()) {
            return -1;
        }
        return values.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listitem_currency, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView percent = (TextView) view.findViewById(R.id.percent);
        TextView symbol = (TextView) view.findViewById(R.id.symbol);
        TextView amount = (TextView) view.findViewById(R.id.amount);

        Currency currency = values.get(position);
        if (currency == null) {
            currency = new Currency(-1, "", "", 4.4f, 10);
        }

        if (name != null) {
            name.setText(currency.getName() == null ? "" : currency.getName());
        }

        if (percent != null) {
            percent.setText(String.format("%.2f", currency.getPercentageCharge()) + "%");
        }

        if (symbol != null) {
            symbol.setText(currency.getSymbol() == null ? "" : currency.getSymbol());
        }

        if (amount != null) {
            DecimalFormat formatter = new DecimalFormat("#,###,###.00");
            amount.setText(formatter.format(currency.getAmountCharge()));
        }

        return view;
    }

}
