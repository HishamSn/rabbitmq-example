package com.hussam.fproject.hsrw.myapplication.prefs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hussam.fproject.hsrw.myapplication.common.MyApplication;

public class PrefsUtils {


    private static final String LOGIN = "login";
    private static final String USER_NAME = " user_name";

    private static PrefsUtils instance;
    private SharedPreferences prefs;

    private PrefsUtils() {
        prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    public static synchronized PrefsUtils getInstance() {
        if (instance == null) {
            instance = new PrefsUtils();
        }
        return instance;
    }

    public boolean isLogin() {
        return prefs.getBoolean(LOGIN, false);
    }

    public void setLogin(boolean login) {
        prefs.edit().putBoolean(LOGIN, login).apply();
    }

    public String getUserName() {
        return prefs.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        prefs.edit().putString(USER_NAME, userName).apply();
    }


}
