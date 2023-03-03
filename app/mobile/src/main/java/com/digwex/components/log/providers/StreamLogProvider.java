package com.digwex.components.log.providers;


import com.digwex.components.log.Log;
import com.digwex.components.log.LogProvider;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class StreamLogProvider implements LogProvider {

  private static final String LOG_EXT = ".log";

  private final String mDir;
  private static int mDay = -1;
  private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
  private DateTimeFormatter dateFormatName = DateTimeFormat.forPattern("yyyy-MM-dd");

  private PrintStream mStream;

  StreamLogProvider(String dir) {
    mDir = dir + '/';
  }

  @Override
  public void println(int requestedLevel, String tag, String message) {
    DateTime date = DateTime.now();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date.toDate());
    int day = cal.get(Calendar.DAY_OF_MONTH);

    if (mDay != day) {
      String logFile = mDir + dateFormatName.print(date) + LOG_EXT;
      try {
        mStream = new PrintStream(new FileOutputStream(logFile, true),
          true,
          StandardCharsets.UTF_8.name());
        mDay = day;
      } catch (Exception ignored) {
        return;
      }
    }

//    if (MainApplication.instance.isDebuggable())
//      System.out.println(message);

    mStream.append(dateFormat.print(DateTime.now()))
      .append(' ')
      .append(Log.LEVELS[requestedLevel])
      .append(tag).append(" - ")
      .append(message).append('\n');
  }

  @Override
  public void printException(Throwable throwable) {
    try {
      mStream.append(throwable.getMessage()).append('\n');
    } catch (Exception ignore) {
    }
  }

  public static void reset() {
    mDay = -1;
  }
}