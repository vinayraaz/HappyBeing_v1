package com.azuyo.happybeing.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Admin on 10-01-2017.
 */

public class Foreground implements Application.ActivityLifecycleCallbacks {
    private static Foreground mInstance;
    private boolean foreground = true, paused = true;

    public static Foreground init(Application application) {
        if (mInstance == null) {
            mInstance = new Foreground();
            application.registerActivityLifecycleCallbacks(mInstance);
        }
        return mInstance;
    }

    public static Foreground get(Application application) {
        if (mInstance == null) {
            init(application);
        }
        return mInstance;
    }

    public static Foreground get() {
        return mInstance;
    }

    public boolean isForeground() {
        return foreground;
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i("ActivityLifeCycle", "In on Create");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i("ActivityLifeCycle", "In on start");

    }

    @Override
    public void onActivityResumed(Activity activity) {
        foreground = true;
        Log.i("ActivityLifeCycle", "In on resume"+foreground);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i("ActivityLifeCycle", "In on pause");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                if (foreground) {
                    foreground = false;

                } else {
                    Log.i("NotificationTrigger", "still foreground");
                }
            }
        }, 500);

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i("ActivityLifeCycle", "In on stop");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
