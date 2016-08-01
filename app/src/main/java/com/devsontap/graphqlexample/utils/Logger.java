package com.devsontap.graphqlexample.utils;

import android.util.Log;

/**
 * Util class for sending messages to Logcat
 */

public class Logger {

    private static final String TAG = "GraphQL";

    public static void debug(String message) {
        Log.d(TAG, message);
    }

    public static void error(String message, Throwable error) {
        Log.e(TAG, message, error);
    }
}
