package com.mom.momcustomerapp.utils.crashlogs.data;

import android.provider.BaseColumns;

public class CrashTable implements BaseColumns {

  public static final String _ID = "_ID";
  public static final String TABLE_NAME = "crashes";
  public static final String PLACE = "place";
  public static final String REASON = "reason";
  public static final String STACKTRACE = "stacktrace";
  public static final String Deviceinfo = "deviceinfo";
  public static final String person_id = "person_Id";
  public static final String  username = "username";

  public static final String DATE = "date";

  public static final String CREATE_QUERY = "create table " + TABLE_NAME + " (" +
      _ID + " TEXT, " +
      PLACE + " TEXT, " +
      REASON + " TEXT, " +
      STACKTRACE + " TEXT, " +
          Deviceinfo + " TEXT, " +

          person_id + " TEXT, " +
          username + " TEXT, " +

          "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

  public static final String DROP_QUERY = "drop table " + TABLE_NAME;
  public static final String TRUNCATE_QUERY = "delete from " + TABLE_NAME;
  public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC";

  public static String selectById(int id) {
    return "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = " + id;
  }
}
