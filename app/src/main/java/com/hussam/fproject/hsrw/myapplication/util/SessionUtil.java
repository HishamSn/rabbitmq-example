package com.hussam.fproject.hsrw.myapplication.util;

import com.hussam.fproject.hsrw.myapplication.prefs.PrefsUtils;

public class SessionUtil {

    private static SessionUtil instance;
    private String user;

    private SessionUtil() {

    }

    public static synchronized SessionUtil getInstance() {
        if (instance == null) {
            instance = new SessionUtil();
        }
        return instance;
    }

    public void login(String user) {
        PrefsUtils.getInstance().setUserName(user);
        PrefsUtils.getInstance().setLogin(true);
    }

    public void logout() {
        PrefsUtils.getInstance().setUserName("");
        PrefsUtils.getInstance().setLogin(false);
    }

}
