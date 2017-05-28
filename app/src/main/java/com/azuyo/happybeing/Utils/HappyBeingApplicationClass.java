package com.azuyo.happybeing.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.azuyo.happybeing.R;
import com.azuyo.happybeing.ui.ProfileScreen;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Calendar;

/**
 * Created by Admin on 06-01-2017.
 */

public class HappyBeingApplicationClass extends MultiDexApplication {
    private static HappyBeingApplicationClass mInstance;
    com.azuyo.happybeing.Utils.AppEnvironment appEnvironment;
    private Tracker mTracker;
    private static boolean activityVisible;
    public static UserInformation userInformation;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        Tracker tracker = analytics.newTracker(R.string.trackingId);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Foreground.init(this);
        userInformation = new UserInformation();
        //loadFromPreferences();
        FontOverride.setDefault(this, "DEFAULT","fonts/roboto-medium.ttf");
        FontOverride.setDefault(this, "MONOSPACE", "fonts/roboto-medium.ttf");
        FontOverride.setDefault(this, "SERIF", "fonts/roboto-light.ttf");
        FontOverride.setDefault(this, "SANS_SERIF", "fonts/roboto-regular.ttf");

        appEnvironment = com.azuyo.happybeing.Utils.AppEnvironment.SANDBOX;

    }

    private void loadFromPreferences() {
        MySql dbHelper = new MySql(this, "mydb", null, ProfileScreen.dbVersion);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR, 6);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.AM_PM, Calendar.AM);
        cv.put("MIND_GYM_START_TIME", calendar1.getTimeInMillis());
        calendar1.set(Calendar.HOUR, 9);
        calendar1.set(Calendar.MINUTE, 30);
        calendar1.set(Calendar.AM_PM, Calendar.PM);
        cv.put("MIND_GYM_END_TIME", calendar1.getTimeInMillis());
        calendar1.set(Calendar.HOUR, 10);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.AM_PM, Calendar.AM);
        cv.put("RELAX_START_TIME", calendar1.getTimeInMillis());
        calendar1.set(Calendar.HOUR, 8);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.AM_PM, Calendar.PM);
        cv.put("RELAX_END_TIME", calendar1.getTimeInMillis());
        db.insert("NotificationsTimings", null, cv);
    }

    public static void storeToPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("HAPPY_BEING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.NAME, userInformation.getName());
        editor.putString(AppConstants.SCREEN_NAME, userInformation.getScreenName());
        editor.putString(AppConstants.EMAIL_ID, userInformation.getEmailId());
        editor.putBoolean(AppConstants.IS_PAID, userInformation.isPaid());
        editor.putString(AppConstants.MOBILE_NUMBER, userInformation.getMobileNumber());
        editor.commit();

    }
    public static HappyBeingApplicationClass getInstance() throws NullPointerException {
        if ( mInstance == null)
            throw new NullPointerException( " Error to create Happybeing Application instance");
        return mInstance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(HappyBeingApplicationClass.this);
        super.attachBaseContext(base);
    }

}
