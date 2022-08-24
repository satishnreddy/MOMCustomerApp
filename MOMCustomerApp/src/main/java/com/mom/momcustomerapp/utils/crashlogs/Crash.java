package com.mom.momcustomerapp.utils.crashlogs;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Crash {
  private int id;
  private String place;
  private String reason;
  private String stackTrace;
  private Date date;
  public static final String DATE_FORMAT = "EEE MMM dd kk:mm:ss z yyyy";

  public Crash(String place, String reason, String stackTrace) {
    this.place = place;
    this.stackTrace = stackTrace;
    this.reason = reason;
    this.date = new Date();

  }

  public Crash(int id, String placeOfCrash, String reasonOfCrash, String stacktrace, String date) {
    this(placeOfCrash, reasonOfCrash, stacktrace);
    this.id = id;
    DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    try {
      this.date = df.parse(date);
    } catch (ParseException e) {
      this.date = new Date();
    }
  }


  public Class<Crash> getType() {
    return Crash.class;
  }

  public String getReason() {
    return reason;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public String getPlace() {
    return place;
  }

  public int getId() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  public void setId(int id) {
    this.id = id;
  }
}
