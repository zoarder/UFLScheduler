package me.muktadir.uflscheduler.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import me.muktadir.uflscheduler.R;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class SharedPreferencesManager {

    private static SharedPreferencesManager sSharedPreferencesManager = null;
    private Context mContext = null;
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mSharedPreferencesEditor = null;


    private SharedPreferencesManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (sSharedPreferencesManager == null) {
            sSharedPreferencesManager = new SharedPreferencesManager(context);
        }
        return sSharedPreferencesManager;
    }

    public void setStringValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    public String getStringValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void setIntegerValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
    }

    public int getIntegerValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void setBooleanValue(String key, Boolean value) {
        mSharedPreferencesEditor.putBoolean(key, value);
        mSharedPreferencesEditor.commit();
    }

    public Boolean getBooleanValue(String key, Boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void removeValue(String key) {
        mSharedPreferencesEditor.remove(key);
        mSharedPreferencesEditor.commit();
    }
}
