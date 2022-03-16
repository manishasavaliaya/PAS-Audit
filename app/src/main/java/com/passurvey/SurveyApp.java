package com.passurvey;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * Created by jekson on 08/04/16.
 */

public class SurveyApp extends MultiDexApplication {
    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseCrashlytics.getInstance();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

    }
}