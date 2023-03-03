package com.digwex.components.log;

public interface LogProvider {
    void println(int requestedLevel, String tag, String message);
    void printException(Throwable throwable);
}