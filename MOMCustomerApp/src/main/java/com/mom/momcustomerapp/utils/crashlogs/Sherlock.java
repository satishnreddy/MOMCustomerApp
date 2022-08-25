package com.mom.momcustomerapp.utils.crashlogs;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.customers.models.CustomerModel;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.application.app;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mswipetech.sdk.network.util.Logs;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sherlock {
  private static final String TAG = Sherlock.class.getSimpleName();
  private static Sherlock instance;
  static Context context = null;
  private Sherlock(Context context)
  {
    this.context = context;
  }

  public static void init(final Context context) {
     instance = new Sherlock(context);

    final Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread thread, Throwable throwable) {
        analyzeAndReportCrash(throwable, thread);
        handler.uncaughtException(thread, throwable);
      }
    });
  }

  public static boolean isInitialized() {
    return instance != null;
  }

  public static Sherlock getInstance() {
    if (!isInitialized()) {
      throw new SherlockNotInitializedException();
    }
    return instance;
  }


  private static void analyzeAndReportCrash(Throwable throwable, Thread thread)
  {

    CrashAnalyzer crashAnalyzer = new CrashAnalyzer(throwable);
    crashAnalyzer.getAnalysis();

    String manufacturer = "";
    String model = "";
    String brand = "";
    String sdkversion = "";
    String appVersion = "";
    String deviceInfo = "";
    String threadName = "";
    try {
      threadName = thread.getName();
      manufacturer = Build.MANUFACTURER;
      model = Build.MODEL;
      brand = (Build.BRAND);
      sdkversion = String.valueOf(Build.VERSION.SDK_INT);
      appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
      deviceInfo = "thread=>" + threadName +":Manuf=>" + manufacturer + ":model=>" + model + ":sdkversion=>" + sdkversion
              + ":appversion=>" + appVersion+ ":brand=>" + brand ;

    }catch (Exception ex ) {}


    MOMApplication.getSharedPref().setplaceOfCrash(crashAnalyzer.placeOfCrash);
    MOMApplication.getSharedPref().setreasonOfCrash(crashAnalyzer.reasonOfCrash);
    MOMApplication.getSharedPref().setstackTrace(crashAnalyzer.stackTrace);
    MOMApplication.getSharedPref().setdeviceInfo(deviceInfo);
    MOMApplication.getSharedPref().setIsCrashed(true);






  }





}
