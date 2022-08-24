package com.mom.momcustomerapp.views.login;

import static com.mom.momcustomerapp.data.application.Consts.EXTRA_KEY_PUSH_NOTIFICATION;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.mom.momcustomerapp.BuildConfig;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.updateapp.VersionModel;
import com.mom.momcustomerapp.controllers.updateapp.views.CheckUpdateDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.application.app;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.utils.crashlogs.CrashAnalyzer;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mswipetech.sdk.network.util.Logs;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplachScreenActivity extends BaseActivity
{

    private static final String TAG = "SplachScreenActivity";
    Timer timer;
    private ProfileTimerTask profileTimerTask;
    private boolean isProfileRequested = false;
    private boolean isPermissionTaken = false;
    private boolean isLoginOpened = false;
    private boolean backFromLogin = false;
    private AppCompatActivity mActivity;
    private boolean isFromPushNotification = false;

    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                      done();
                    break;
                case 3:
                    if (BuildConfig.APPLICATION_ID.contains("demo"))
                    {
                        Toast.makeText(mActivity, "Demo App", Toast.LENGTH_SHORT).show();
                    } else if (BuildConfig.APPLICATION_ID.contains("dev"))
                    {
                        Toast.makeText(mActivity, "Dev App", Toast.LENGTH_SHORT).show();
                    }
                    login();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_splach_screen);
        mActivity = this;

        setLocale();




        Intent intent = getIntent();
        if (intent != null){
            isFromPushNotification = intent.getBooleanExtra(EXTRA_KEY_PUSH_NOTIFICATION, false);
        }
    }

    public void setLocale()
    {
        /*Locale myLocale = new Locale(ShopxieSharedPreferences.getInstance().getLanguage());//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());//Update the config

         */
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isPermissionTaken = MOMApplication.getSharedPref().isPermissionTaken();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                submitCustomerLogs();
            }
        }, 100);


        if (isPermissionTaken)
        {
            if (TextUtils.isEmpty(MOMApplication.getInstance().getToken()))
            {

                if (!isLoginOpened)
                {
                    isLoginOpened = true;
                    handler.sendEmptyMessageDelayed(3, 300);
                }
            } else {

                if (!isProfileRequested)
                {
                    timer = new Timer();
                    profileTimerTask = new ProfileTimerTask();
                    timer.schedule(profileTimerTask, 300);
                }
            }

        }
        else
        {
            requestPermissions();
        }
    }

    @Override
    protected void onStop()
    {
        if (timer != null) {
            timer.cancel();
            timer = null;
            profileTimerTask = null;
        }
        super.onStop();
    }

    private void login()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("fromSplash", true);
        startActivityForResult(intent, Consts.REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Consts.REQUEST_CODE_PERMISSIONS)
        {
            isPermissionTaken = true;
            MOMApplication.getSharedPref().setPermissionTaken(isPermissionTaken);

        }
        else if (requestCode == Consts.REQUEST_CODE_LOGIN)
        {
            isLoginOpened = false;
            if (resultCode == Activity.RESULT_OK)
            {
                String token = data.getStringExtra(Consts.EXTRA_TOKEN);
                String venderId = data.getStringExtra(Consts.EXTRA_VENDER_ID);
                String storeid = data.getStringExtra(Consts.EXTRA_STORE_ID            );

                if (!TextUtils.isEmpty(token))
                {
                    MOMApplication.getInstance().setToken(token);
                    MOMApplication.getInstance().setVender(venderId);
                    MOMApplication.getInstance().setStoreId(storeid);

                    backFromLogin = true;
                }
            }
            else
            {
                this.finish();
            }
        }
        /*else if (requestCode == Consts.REQUEST_CODE_CREATE_STORE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                boolean isNotified = data.getBooleanExtra(Consts.EXTRA_ISNOTIFIED, false);
                if (isNotified) {
                    finish();
                } else {
                    //getProfile();
                }
            }
            else
            {
                finish();
            }
        }*/
    }

    private void requestPermissions()
    {
        Intent intent = new Intent(mActivity, PermissionsActivity.class);
        startActivityForResult(intent, Consts.REQUEST_CODE_PERMISSIONS);
    }

    private void done()
    {

        showHome();
        /*if ((MventryApp.getInstance().getCurrentStoreId() == null) || MventryApp.getInstance().getCurrentStoreId().isEmpty()){
            fetchStoreId();
        }
        else{
            showHome();
        }*/
    }

    private void showHome()
    {
        isProfileRequested = false;
        Intent intent = null;
        //if (isFromPushNotification){
          //  intent = Home_Tab_Activity.getStartIntent(SplachScreenActivity.this, 3);
        //}
        //else {
            intent = new Intent(mActivity, Home_Tab_Activity.class);
            //intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        //}

        startActivity(intent);


        this.finish();
    }

    public void checkUpdates()
    {
        String url;
        if (BuildConfig.APPLICATION_ID.contains("dev")) {
            url = getString(R.string.update_dev);
        } else if (BuildConfig.APPLICATION_ID.contains("demo")) {
            url = getString(R.string.update_demo);
        } else {
            url = getString(R.string.update_prod);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseString = response.body().string();
                    //Log.i("Nish", "Response : " + responseString);
                    VersionModel versionModel = new Gson().fromJson(responseString, VersionModel.class);
                    if (versionModel != null) {
                        showUpdeteDialog(versionModel);
                    }
                } else {
                    //Log.i("Nish", "Update failed");
                    //Log.i("Nish", "Code : " + response.code());
                    //Log.i("Nish", "Message : " + response.message());
                    if (response.body() != null) {
                        //Log.i("Nish", "Body : " + response.body().string());
                    }
                }
            }
        });
    }

    public void showUpdeteDialog(VersionModel versionModel) {
        try {
            int currentVersionCode;
            int installedVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

            currentVersionCode = versionModel.getCurrentVersion();
            int forceInstallCode = versionModel.getForcedVersions();
            boolean isForced = false;


            if (installedVersionCode < currentVersionCode)
            {
                if (installedVersionCode < forceInstallCode) {
                    isForced = true;
                } else if (installedVersionCode != 0 && installedVersionCode < currentVersionCode) {
                    isForced = false;
                }

                final CheckUpdateDialogFragment updateDialogFragment = new CheckUpdateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("versionModel", versionModel);
                bundle.putBoolean("forced", isForced);
                updateDialogFragment.setArguments(bundle);

                updateDialogFragment.setFileDownloadListener(new CheckUpdateDialogFragment.FileDownloadListener() {
                    @Override
                    public void onFileDownloadClicked() {
                        Toast.makeText(mActivity, "New version download started. Please install it and open app again.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(CheckUpdateDialogFragment.FRAGMENT_TAG);
                if (prev != null) {
                    ft.remove(prev);
                }
                updateDialogFragment.show(ft, CheckUpdateDialogFragment.FRAGMENT_TAG);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }




    class ProfileTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            });
        }
    }



    private void submitCustomerLogs()
    {

        String placeOfCrash = MOMApplication.getSharedPref().getplaceOfCrash();
        String reasonOfCrash = MOMApplication.getSharedPref().getreasonOfCrash();
        String stackTrace = MOMApplication.getSharedPref().getstackTrace();
        String deviceInfo = MOMApplication.getSharedPref().getdeviceInfo();
        String personid = MOMApplication.getInstance().getPersonId();
        String username = MOMApplication.getInstance().getMswipeUsername();

        if(MOMApplication.getSharedPref().getIsCrashed())
        {
            if(app.is_DEBUGGING_ON)
            Logs.adb("Splashscreen activity save crash logs");

            CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class);
            Call<String> call = customerClient.saveCrashLogs(placeOfCrash, reasonOfCrash, stackTrace,
                    deviceInfo, personid, username);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response)
                {
                    if(response.isSuccessful()) {
                        if (app.is_DEBUGGING_ON)
                            Logs.adb("Splashscreen activity save crash logs response " + response.body());
                        resetCrashLog();

                    }else {
                        if (app.is_DEBUGGING_ON)
                            Logs.adb("Splashscreen activity save crash logs error response " + response.message());
                        if (app.is_DEBUGGING_ON)
                            Logs.adb("Splashscreen activity save crash logs error response " + response.errorBody());

                        resetCrashLog();

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t)
                {

                    resetCrashLog();
                }
            });
        }
    }

    public void resetCrashLog()
    {
        MOMApplication.getSharedPref().setplaceOfCrash("");
        MOMApplication.getSharedPref().setreasonOfCrash("");
        MOMApplication.getSharedPref().setstackTrace("");
        MOMApplication.getSharedPref().setdeviceInfo("");
        MOMApplication.getSharedPref().setIsCrashed(false);



    }



}
