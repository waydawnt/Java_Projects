package com.spawner.courselistfragment.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenUtility {
    private Activity activity;
    private float dpWidth;
    private float dpHeight;

    public ScreenUtility(Activity activity) {
        this.activity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();

        display.getMetrics(metrics);

        float density = activity.getResources().getDisplayMetrics().density;

        dpHeight = metrics.heightPixels / density;
        dpWidth = metrics.widthPixels / density;
    }

    public float getDpWidth() {
        return dpWidth;
    }

    public void setDpWidth(float dpWidth) {
        this.dpWidth = dpWidth;
    }

    public float getDpHeight() {
        return dpHeight;
    }

    public void setDpHeight(float dpHeight) {
        this.dpHeight = dpHeight;
    }
}
