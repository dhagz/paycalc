package com.walng.dhagz.paypalcalc;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * @author Dhagz
 * @since 2016-03-26
 */
public class PayPalCalcApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    /**
     * Gets the Firebase Analytics instance for this {@link Application}.
     *
     * @return FirebaseAnalytics instance
     */
    public FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }
}
