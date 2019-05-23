package com.example.mrz.logger;

public interface Logger {

    public String getTag();

    public boolean isVerboseEnabled();

    public boolean isDebugEnabled();

    public boolean isInfoEnabled();

    public boolean isWarnEnabled();

    public boolean isErrorEnabled();

    public void verbose(String msg);

    public void verbose(String format, Object... params);

    public void verbose(Throwable throwable);

    public void verbose(String msg, Throwable throwable);

    public void debug(String msg);

    public void debug(String format, Object... params);

    public void debug(Throwable throwable);

    public void debug(String msg, Throwable throwable);

    public void info(String msg);

    public void info(String format, Object... params);

    public void info(Throwable throwable);

    public void info(String msg, Throwable throwable);

    public void warn(String msg);

    public void warn(String format, Object... params);

    public void warn(Throwable throwable);

    public void warn(String msg, Throwable throwable);

    public void error(String msg);

    public void error(String format, Object... params);

    public void error(Throwable throwable);

    public void error(String msg, Throwable throwable);
}
