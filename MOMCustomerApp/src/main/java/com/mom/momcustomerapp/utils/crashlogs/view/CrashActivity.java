package com.mom.momcustomerapp.utils.crashlogs.view;

import static com.mom.momcustomerapp.data.application.Consts.REQUEST_CODE_CHANGE_LANGUAGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mom.momcustomerapp.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.application.app;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.utils.UIUtils;
import com.mom.momcustomerapp.utils.crashlogs.data.CrashRecord;
import com.mom.momcustomerapp.utils.crashlogs.data.SherlockDatabaseHelper;
import com.mom.momcustomerapp.views.customers.AddCustomerActivity;
import com.mom.momcustomerapp.views.login.LoginActivity;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.widget.SafeClickListener;
import com.mswipetech.sdk.network.util.Logs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrashActivity extends BaseActivity
{
  private SherlockDatabaseHelper database;
  List<CrashRecord> crashRecord = null;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    database = new SherlockDatabaseHelper(this);
    setContentView(R.layout.crash_activity);
    crashRecord = database.getCrashes();
      setupToolBar();

        render();

  }

    private void setupToolBar()
    {
        final androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_crashlogs_options, menu);


        MenuItem email = menu.findItem(R.id.menu_email);
        MenuItem upload = menu.findItem(R.id.menu_upload);

        email.setOnMenuItemClickListener(item ->
        {



            return false;

        });

        upload.setOnMenuItemClickListener(item ->
        {


            if(crashRecord != null && crashRecord.size() > 0)
            {
                showLoadingDialog();
                submitCustomerLogs();
            }
            else {
                showErrorDialog("No crash logs");
            }
            return false;

        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                 finish();
                break;
        }
        return true;
    }

  int i = 0;
  int increment = 1;
  public void render()
  {
      TextView crashLocation = (TextView) findViewById(R.id.crash_location);
      TextView crashReason = (TextView) findViewById(R.id.crash_reason);
      TextView stackTrace = (TextView) findViewById(R.id.stacktrace);

      TextView username = (TextView) findViewById(R.id.username);
      TextView deviceinfo = (TextView) findViewById(R.id.deviceinfo);
      TextView date = (TextView) findViewById(R.id.date);

      TextView cancel = (TextView) findViewById(R.id.activity_login_btn_previous);
      cancel.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
      {
          @Override
          public void onClick(View v)
          {
              if(crashRecord != null &&  i - 1 > -1 )
              {
                  i = i - 1;
                  CrashRecord rec = crashRecord.get(i);

                  date.setText(rec.getDate());
                  username.setText(rec.getUsername());
                  deviceinfo.setText(rec.getDeviceinfo());

                  crashLocation.setText(rec.getPlace());
                  crashReason.setText(rec.getReason());
                  stackTrace.setText(rec.getStackTrace());

              } else {


                  Toast.makeText(CrashActivity.this, "no more crash logs", Toast.LENGTH_SHORT).show();

              }
          }
      }));

      TextView next  = (TextView) findViewById(R.id.activity_login_btn_next);
      next.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
      {
          @Override
          public void onClick(View v)
          {


              if(crashRecord != null && i + 1 < crashRecord.size() )
              {
                  i = i + 1;
                  CrashRecord rec = crashRecord.get(i);

                  date.setText(rec.getDate());
                  username.setText(rec.getUsername());
                  deviceinfo.setText(rec.getDeviceinfo());

                  crashLocation.setText(rec.getPlace());
                  crashReason.setText(rec.getReason());
                  stackTrace.setText(rec.getStackTrace());

              } else {


                  Toast.makeText(CrashActivity.this, "no more crash logs", Toast.LENGTH_SHORT).show();

              }

          }
      }));



      if(crashRecord != null && crashRecord.size() > 0)
      {
          CrashRecord rec = crashRecord.get(i);

          date.setText(rec.getDate());
          username.setText(rec.getUsername());
          deviceinfo.setText(rec.getDeviceinfo());

            crashLocation.setText(rec.getPlace());
            crashReason.setText(rec.getReason());
            stackTrace.setText(rec.getStackTrace());

      }
      else {

          Toast.makeText(CrashActivity.this, "no crash logs found", Toast.LENGTH_SHORT).show();

      }

  }


    private void submitCustomerLogs() {

        CrashRecord rec = crashRecord.get(i);

        String placeOfCrash = rec.getPlace();
        String reasonOfCrash = rec.getReason();
        String stackTrace = rec.getStackTrace();
        String deviceInfo = rec.getDeviceinfo();
        String personid = rec.getPerson_id();
        String username = rec.getUsername();

        if (app.is_DEBUGGING_ON)
            Logs.adb("Splashscreen activity save crash logs");

        CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class);
        Call<String> call = customerClient.saveCrashLogs(placeOfCrash, reasonOfCrash, stackTrace,
                deviceInfo, personid, username);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoadingDialog();
                showErrorDialog(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                    hideLoadingDialog();
                 showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });
    }



}
