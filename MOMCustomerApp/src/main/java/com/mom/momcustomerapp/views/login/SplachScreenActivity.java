package com.mom.momcustomerapp.views.login;

import static com.mom.momcustomerapp.data.application.Consts.EXTRA_KEY_PUSH_NOTIFICATION;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
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
import com.mom.momcustomerapp.utils.crashlogs.CrashReportingIntentService;
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
    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_splach_screen);
        mActivity = this;
        startService(new Intent( this, CrashReportingIntentService.class ) );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        boolean isPermissionTaken = MOMApplication.getSharedPref().isPermissionTaken();

        if (isPermissionTaken)
        {
            if (TextUtils.isEmpty(MOMApplication.getInstance().getToken()))
            {

                handler.sendEmptyMessageDelayed(3, 600);


            } else {

                handler.sendEmptyMessageDelayed(1, 600);


            }

        }
        else
        {
            Intent intent = new Intent(mActivity, PermissionsActivity.class);
            startActivityForResult(intent, Consts.REQUEST_CODE_PERMISSIONS);

        }
    }

    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    startActivity(new Intent(mActivity, Home_Tab_Activity.class));
                    finish();
                    break;
                case 3:
                    if (BuildConfig.APPLICATION_ID.contains("demo"))
                    {
                        Toast.makeText(mActivity, "Demo App", Toast.LENGTH_SHORT).show();
                    } else if (BuildConfig.APPLICATION_ID.contains("dev"))
                    {
                        Toast.makeText(mActivity, "Dev App", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    break;
            }
        }
    };













}
