package com.lamnd.zerotohero.config;

public class AppConstants {
    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    public static final int MINAGE = 18;
    protected static final String[] AUTH_URLS = {"/auth/**"};
    protected static final String[] USER_URLS = {"/users"};
}
