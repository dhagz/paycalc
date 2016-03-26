package com.walng.dhagz.paypalcalc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.walng.dhagz.paypalcalc.R;
import com.walng.dhagz.paypalcalc.models.Currency;

import java.util.List;

/**
 * @author Dhagz
 * @since 2016-03-25
 */
public class CurrencyListAdapter extends BaseAdapter {

    private List<Currency> values;
    private LayoutInflater inflater;

    public CurrencyListAdapter(Context context, List<Currency> values) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listitem_currency, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView symbol = (TextView) view.findViewById(R.id.symbol);

        Currency currency = values.get(position);
        name.setText(currency.getName());
        symbol.setText(currency.getSymbol());

        return view;
    }

}
