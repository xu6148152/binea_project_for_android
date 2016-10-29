package com.android.tools.fd.common;

import java.util.logging.Level;

public class Log {
    public static Logging logging = null;

    public interface Logging {
        boolean isLoggable(Level paramLevel);

        void log(Level paramLevel, String paramString);

        void log(Level paramLevel, String paramString, Throwable paramThrowable);
    }
}