package com.digwex.components.log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static android.util.Log.ASSERT;
import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

public class Log {
    public static final String[] LEVELS = new String[]{"UNKNOWN", "UNKNOWN", "VERBOSE", "DEBUG  ",
            "INFO   ", "WARN   ", "ERROR  ", "ASSERT ", "DISABLE"};
    public static final int DISABLE = ASSERT + 1;
    private static int mLevel = DISABLE;
    private static ArrayList<LogProvider> providers = new ArrayList<>();

    public static void reset() {
        providers.clear();
    }

    public static void addProvider(LogProvider provider) {
        providers.add(provider);
    }

    public static void setLevel(int level) {
        mLevel = level;
    }

    public static boolean isVerbose() {
        return mLevel <= VERBOSE;
    }

    public static boolean isDebug() {
        return mLevel <= DEBUG;
    }

    public static boolean isInfo() {
        return mLevel <= INFO;
    }

    public static boolean isWarn() {
        return mLevel <= WARN;
    }

    public static boolean isError() {
        return mLevel <= ERROR;
    }

    public static boolean isRed() {
        return mLevel <= ERROR;
    }

    public static boolean isGreen() {
        return mLevel <= DEBUG;
    }

    public static synchronized void println(int requestedLevel, Class<?> clazz, String message) {

        if (mLevel > requestedLevel) {
            return;
        }

        String tag = clazz.getSimpleName();

        if (requestedLevel == DISABLE) {
            printConsole(message);
            return;
        }

        for (LogProvider provider : providers) {
            provider.println(requestedLevel, tag, message);
        }
    }

    private static void printConsole(String message) {
        System.out.println(message);
    }

    public static synchronized void println(int requestedLevel, Class<?> clazz, String format,
                                            Object... args) {
        println(requestedLevel, clazz, String.format(format, args));
    }

    public static void printException(@Nullable Throwable throwable) {
        if (throwable == null)
            throwable = new Exception("No message!");
        for (LogProvider provider : providers) {
            provider.printException(throwable);
        }
    }
}