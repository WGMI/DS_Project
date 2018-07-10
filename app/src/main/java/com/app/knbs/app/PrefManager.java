package com.app.knbs.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Mobile Application Created by Rodney on 18-Oct-16.
 */
public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "knbs";

    // All Shared Preferences Keys
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_FCM_ID = "fcm_id";

    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setFCM(String fcm) {
        editor.putString(KEY_FCM_ID, fcm);
        editor.commit();
    }

    public String getFCM() {
        return pref.getString(KEY_FCM_ID, null);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
