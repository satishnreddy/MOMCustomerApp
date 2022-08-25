package com.mom.momcustomerapp.utils.crashlogs.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.NonNull;

import com.mom.momcustomerapp.data.application.app;
import com.mswipetech.sdk.network.util.Logs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SherlockDatabaseHelper extends SQLiteOpenHelper
{
  private static final int VERSION = 2;
  private static final String DB_NAME = "Sherlock";

  public SherlockDatabaseHelper(Context context) {
    super(context, DB_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database)
  {
    database.execSQL(CrashTable.CREATE_QUERY);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
  {
    sqLiteDatabase.execSQL(CrashTable.DROP_QUERY);
    sqLiteDatabase.execSQL(CrashTable.CREATE_QUERY);
  }

  public void insertCrash(String place, String reason, String stackTrace, String deviceinfo,
                          String person_id, String username)
  {
    if(app.is_DEBUGGING_ON)
      Logs.adb("sherlockdatabae database insert");
    ContentValues values = new ContentValues();
    values.put(CrashTable._ID, UUID.randomUUID().toString());
    values.put(CrashTable.PLACE, place);
    values.put(CrashTable.REASON, reason);
    values.put(CrashTable.STACKTRACE, stackTrace);
    values.put(CrashTable.Deviceinfo, deviceinfo);
    values.put(CrashTable.person_id, person_id);
    values.put(CrashTable.username, username);
    SQLiteDatabase database = getWritableDatabase();
    long id = database.insert(CrashTable.TABLE_NAME, null, values);
    if(app.is_DEBUGGING_ON)
      Logs.adb("sherlockdatabae database after insert " + id);

    database.close();
  }

  public void flushCrash()
  {
    if(app.is_DEBUGGING_ON)
      Logs.adb("flushCrash from databse ");

    SQLiteDatabase database = getWritableDatabase();
    String sql = "delete from " + CrashTable.TABLE_NAME +
          " where "+ CrashTable._ID +" not in (select "+ CrashTable._ID +" from "+ CrashTable.TABLE_NAME
            + " order by date desc limit 5);";

    if(app.is_DEBUGGING_ON)
      Logs.adb("flushCrash from databse query" + sql);


    database.execSQL(sql);
    database.close();
  }


  public List<CrashRecord> getCrashes()
  {
    if(app.is_DEBUGGING_ON)
      Logs.adb("get Crashes from databse ");

    SQLiteDatabase readableDatabase = getReadableDatabase();
    ArrayList<CrashRecord> crashes = new ArrayList<>();
    Cursor cursor = readableDatabase.rawQuery(CrashTable.SELECT_ALL, null);

    if (isCursorPopulated(cursor))
    {
      do {
        if(app.is_DEBUGGING_ON)
          Logs.adb("get Crashes from databse and adding it to crash object");

        crashes.add(toCrash(cursor));
        if(app.is_DEBUGGING_ON)
          Logs.adb("get Crashes from databse and after adding it to crash object");


      } while (cursor.moveToNext());
    }

    cursor.close();
    readableDatabase.close();

    return crashes;
  }

  @NonNull
  private CrashRecord toCrash(Cursor cursor)
  {
    if(app.is_DEBUGGING_ON)
      Logs.adb("get Crashes from databse toCrash");


    String id = cursor.getString(cursor.getColumnIndex(CrashTable._ID));
    if(app.is_DEBUGGING_ON)
      Logs.adb("get Crashes from databse toCrash " + id);

    String placeOfCrash = cursor.getString(cursor.getColumnIndex(CrashTable.PLACE));
    String reasonOfCrash = cursor.getString(cursor.getColumnIndex(CrashTable.REASON));
    String stacktrace = cursor.getString(cursor.getColumnIndex(CrashTable.STACKTRACE));
    String deviceinfo = cursor.getString(cursor.getColumnIndex(CrashTable.Deviceinfo));

    if(app.is_DEBUGGING_ON)
      Logs.adb("get Crashes from databse toCrash " + deviceinfo);

    String person_id = cursor.getString(cursor.getColumnIndex(CrashTable.person_id));
    String username = cursor.getString(cursor.getColumnIndex(CrashTable.username));
    String date = cursor.getString(cursor.getColumnIndex(CrashTable.DATE));
    if(app.is_DEBUGGING_ON)
      Logs.adb("get Crashes from databse toCrash " + date);







    return new CrashRecord( id, placeOfCrash, reasonOfCrash, stacktrace, deviceinfo, date, person_id, username);
  }

  private boolean isCursorPopulated(Cursor cursor)
  {
    return cursor != null && cursor.moveToFirst();
  }
}
