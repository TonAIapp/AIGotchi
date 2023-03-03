package com.digwex.components.log.providers;

import android.util.Log;

import com.digwex.components.log.LogProvider;


public class LogCatProvider implements LogProvider {

    @Override
    public void println(int requestedLevel, String tag, String message) {
        Log.println(requestedLevel, tag, message);
    }

    @Override
    public void printException(Throwable throwable) {
        throwable.printStackTrace();
    }
}