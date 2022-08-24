package com.mom.momcustomerapp.utils.crashlogs;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

public class CrashAnalyzer {

  private final Throwable throwable;
  public String placeOfCrash;
  public String reasonOfCrash;
  public String stackTrace;
  public String date;
  public static final String DATE_FORMAT = "EEE MMM dd kk:mm:ss z yyyy";


  public CrashAnalyzer(Throwable throwable) {
    this.throwable = throwable;
  }

  public void getAnalysis() {
    StringBuilder factsBuilder = new StringBuilder();

    factsBuilder.append(throwable.getLocalizedMessage());
    factsBuilder.append("\n");
    factsBuilder.append(stackTrace(throwable.getStackTrace()));
    factsBuilder.append("\n");
    if (throwable.getCause() != null) {
      factsBuilder.append("Caused By: ");
      StackTraceElement[] stackTrace = throwable.getCause().getStackTrace();
      placeOfCrash = getCrashOriginatingClass(stackTrace);
      factsBuilder.append(stackTrace(stackTrace));
    } else {
      placeOfCrash = getCrashOriginatingClass(throwable.getStackTrace());
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    date = dateFormat.format( new Date());
    reasonOfCrash = throwable.getLocalizedMessage();
    stackTrace = factsBuilder.toString();


  }

  private String getCrashOriginatingClass(StackTraceElement[] stackTraceElements) {
    if (stackTraceElements.length > 0) {
      StackTraceElement stackTraceElement = stackTraceElements[0];
      return format("%s:%d", stackTraceElement.getClassName(), stackTraceElement.getLineNumber());
    }
    return "";
  }

  private static String stackTrace(StackTraceElement[] stackTrace) {
    List<StackTraceElement> stackTraceElements = Arrays.asList(stackTrace);
    StringBuilder builder = new StringBuilder();
    for (StackTraceElement stackTraceElement : stackTraceElements)
    {
      builder.append("at ");
      builder.append(stackTraceElement.toString());
      builder.append("\n");
    }

    return builder.toString();
  }
}
