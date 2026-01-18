package com.walng.dhagz.paypalcalc.providers;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walng.dhagz.paypalcalc.R;
import com.walng.dhagz.paypalcalc.models.Currency;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dhagz
 * @since 2016-03-27
 */
public abstract class ListProvider<D> {

    private static SharedPreferences sharedPreferences;

    public ListProvider(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
    }

    public void set(LinkedList<D> items) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (items == null) {
            editor.clear();
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(items);
            editor.putString(getPreferenceKey(), json);
        }
        editor.commit();
    }

    @Nullable
    public LinkedList<D> get() {
        String json = sharedPreferences.getString(getPreferenceKey(), null);
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = getType();
        return gson.fromJson(json, type);
    }

    public abstract String getPreferenceKey();

    public abstract Type getType();

}
