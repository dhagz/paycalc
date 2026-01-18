package com.walng.dhagz.paypalcalc.managers;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * @author Dhagz
 * @since 2016-03-26
 */
public class AnalyticsManager {

    private static AnalyticsManager instance;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static AnalyticsManager getInstance(Application application) {
        if (instance == null) {
            instance = new AnalyticsManager(application);
        }
        return instance;
    }

    public AnalyticsManager(Application application) {
        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(application);
    }

    public void setScreen(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    public void sendEvent(String category, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("action", action);
        mFirebaseAnalytics.logEvent("custom_event", bundle);
    }

}
