package com.app.court.helpers;

import android.os.SystemClock;

/**
 * Created on 9/26/2017.
 */

public class Utils {
    private static long lastClickTime;
    public static String getTAG(Object o) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        int position = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getFileName().contains(o.getClass().getSimpleName())
                    && !elements[i+1].getFileName().contains(o.getClass().getSimpleName())){
                position = i;
                break;
            }
        }

        StackTraceElement element = elements[position];
        String className = element.getFileName().replace(".java", "");
        return "[" + className + "](" + element.getMethodName() + ":" + element.getLineNumber() + ")";
    }

    public static boolean doubleClickCheck() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }    public static boolean double2ClickCheck() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 3000) {
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
}
