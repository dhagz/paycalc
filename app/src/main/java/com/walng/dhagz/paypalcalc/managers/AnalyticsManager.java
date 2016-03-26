package com.walng.dhagz.paypalcalc.managers;

import android.app.Application;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.walng.dhagz.paypalcalc.PayPalCalcApplication;

/**
 * @author Dhagz
 * @since 2016-03-26
 */
public class AnalyticsManager {

    private static AnalyticsManager instance;
    private Tracker mTracker;

    public static AnalyticsManager getInstance(Application application) {
        if (instance == null) {
            instance = new AnalyticsManager(application);
        }
        return instance;
    }

    public AnalyticsManager(Application application) {
        if (application instanceof PayPalCalcApplication) {
            // Obtain the shared Tracker instance.
            PayPalCalcApplication app = (PayPalCalcApplication) application;
            mTracker = app.getDefaultTracker();
        }
    }

    public void setScreen(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendEvent(String category, String action) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

}
