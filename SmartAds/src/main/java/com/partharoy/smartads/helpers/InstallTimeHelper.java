package com.partharoy.smartads.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;

public class InstallTimeHelper {
    private static final String PREFS_NAME = "SmartAdsPrefs";
    private static final String KEY_FIRST_LAUNCH_TIME = "first_launch_time";
    private static SharedPreferences sharedPreferences;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long firstLaunchTime = sharedPreferences.getLong(KEY_FIRST_LAUNCH_TIME, 0);
        if (firstLaunchTime == 0) {
            sharedPreferences.edit().putLong(KEY_FIRST_LAUNCH_TIME, System.currentTimeMillis()).apply();
        }
    }

    public static boolean isAdGatingPeriodOver(int daysToWait) {
        if (daysToWait <= 0) {
            return true;
        }
        long firstLaunchTime = sharedPreferences.getLong(KEY_FIRST_LAUNCH_TIME, 0);
        if (firstLaunchTime == 0) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        long daysSinceInstall = TimeUnit.MILLISECONDS.toDays(currentTime - firstLaunchTime);
        return daysSinceInstall >= daysToWait;
    }
}
