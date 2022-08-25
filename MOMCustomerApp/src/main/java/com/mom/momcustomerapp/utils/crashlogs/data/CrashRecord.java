package com.mom.momcustomerapp.utils.crashlogs.data;


import java.text.SimpleDateFormat;

public class CrashRecord {
  private String place;
  private String reason;
  private String date;
  private String stackTrace;
  private String deviceinfo;
  private String person_id;
  private String username;
  private String id;


  public CrashRecord(String id, String place, String reason,String stackTrace,
                     String deviceinfo,  String date,
                     String person_id, String username) {
    this.place = place;
    this.reason = reason;
    this.date = date;
    this.stackTrace = stackTrace;
    this.person_id = person_id;
    this.deviceinfo = deviceinfo;
    this.username = username;
    this.id = id;
  }

  public String getPlace() {
    return place;
  }

  public String getReason() {
    return reason;
  }

  public String getDate() {
    return date;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public String getDeviceinfo() {
    return deviceinfo;
  }

  public String getPerson_id() {
    return person_id;
  }

  public String getUsername() {
    return username;
  }
  public String getId() {
    return id;
  }



}
