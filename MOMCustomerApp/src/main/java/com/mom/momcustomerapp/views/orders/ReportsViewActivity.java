package com.mom.momcustomerapp.views.orders;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.customviews.dialogs.DatePickerSpinnerDialogFragment;
import com.mom.momcustomerapp.customviews.dialogs.SingleBtnDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.shared.BaseActivity;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsViewActivity extends BaseActivity {

    @BindView(R.id.download_title)
    TextView title;

    private ImageView mImgPendingOrders, mImgDeclinedOrders, mImgCompletedOrders, mImgPendingCustomerRequest;


    private String mStrReportTtitle = "";
    private String mStrReportName = "";
    private String mStrReportType = "";
    private String mStrFromDate = "";
    private String mStrToDate = "";
    private Typeface boldTypeface;



    public ReportsViewActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sales_reports);
        ButterKnife.bind(this);

        setupToolBar();

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.navigation_reports_setting));
        }
        boldTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf");
        mImgPendingOrders = findViewById(R.id.fragment_reports_pending_orders);
        mImgDeclinedOrders = findViewById(R.id.fragment_reports_declined_orders);
        mImgCompletedOrders = findViewById(R.id.fragment_reports_completed_orders);
        mImgPendingCustomerRequest = findViewById(R.id.fragment_reports_pending_customer_resquest);

        mImgPendingOrders.setOnClickListener(v -> {
            mStrReportType= "pendingReport";
            mStrReportTtitle = "Pending orders report downloaded successfully.";
            mStrReportName = "Pending_orders_report";
            fromDate();
        });

        mImgDeclinedOrders.setOnClickListener(v -> {

            mStrReportType= "returnedReport";
            mStrReportTtitle = "Decline orders report downloaded successfully.";
            mStrReportName = "Decline_orders_report";
            fromDate();
        });

        mImgCompletedOrders.setOnClickListener(v -> {

            mStrReportType= "completedReport";
            mStrReportTtitle = "Complete orders report downloaded successfully.";
            mStrReportName = "Complete_orders_report";
            fromDate();
        });

        mImgPendingCustomerRequest.setOnClickListener(v -> {

            mStrReportType= "customerPendingReport";
            mStrReportTtitle = "Pending customer request report downloaded successfully.";
            mStrReportName = "Pending_customer_request_report";
            fromDate();
        });

        setfont();;

    }

    /**
     * Set up the {@link Toolbar}.
     */
    private void setupToolBar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void setfont()
    {
        title.setTypeface(boldTypeface);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fromDate()
    {

        DatePickerSpinnerDialogFragment dialogFragment = DatePickerSpinnerDialogFragment.newInstance(getResources().getString(R.string.general_use_from), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mStrFromDate = new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-") .append(dayOfMonth).toString();
                        toDate();
                    }
                }
        );

        dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
    }

    private void toDate()
    {

        DatePickerSpinnerDialogFragment dialogFragment = DatePickerSpinnerDialogFragment.newInstance(getResources().getString(R.string.general_use_to), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mStrToDate = new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-") .append(dayOfMonth).toString();
                        getReport();
                    }
                }
        );

        dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
    }

    private void getReport()
    {

        CustomerClient client = ServiceGenerator.createService(CustomerClient.class,
                MOMApplication.getInstance().getToken());
        Call<String> call = client.getReport(mStrReportType, mStrFromDate, mStrToDate);

        showLoadingDialog();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (isFinishing()) return;
                hideLoadingDialog();

                if (response.isSuccessful()) {

                    String statusMessageModel = response.body();

                    if (statusMessageModel != null ) {

                        saveData(response.body());

                    } else {
                        showErrorDialog(getString(R.string.sww));
                    }
                } else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (isFinishing()) return;
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }

        });
    }

    public static String getCurrentDateWithFormate(String aDateFormat) {
        String formatedDate = "";

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(aDateFormat);
        formatedDate = df.format(c.getTime());

        return formatedDate.toLowerCase();

    }

    private void saveData(String data){

        String time = "";

        try {
            time = "" + getCurrentDateWithFormate("hhmmss");
        } catch (Exception ex) {
        }

        String logFile   =  mStrReportName + "_" + time + "_" + mStrFromDate + "_" + mStrToDate+ ".csv";

        File fileName = null;
        File folder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            fileName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), logFile);
        }
        else{

            folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Download");

            if (!folder.exists())
                folder.mkdir();

            fileName = new File(folder, logFile);

        }

            try{

            if (!fileName.exists())
                fileName.createNewFile();

            FileWriter fw = new FileWriter(fileName);
            fw.append(data);
            fw.close();

            showNotification(getResources().getString(R.string.home_mbasket_label_reports), mStrReportTtitle, (int) Math.random(), folder);

            DialogFragment dialogFragment = SingleBtnDialogFragment.newInstance(getResources().getString(R.string.label_report_save_success),
                    getString(R.string.ok), () -> {


                    });
            dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);

        }catch (Exception e){
            showErrorDialog(getString(R.string.label_report_save_falied));
        }
    }

    public void showNotification(String title, String subTitle, int uniqueId, File file) {
        createNotificationChannel();

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        else{

            intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);
            intent.setDataAndType(data, "resource/folder");
        }

        //Add Any key-value to pass extras to intent
        intent.putExtra(Consts.EXTRA_KEY_PUSH_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(MOMApplication.getInstance(),
                uniqueId, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(MOMApplication.getInstance(),
                MOMApplication.getInstance().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_notifications_24dp)
                .setContentTitle(title)
                .setContentText(subTitle)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(MOMApplication.getInstance(), R.color.color_258430))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MOMApplication.getInstance());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(uniqueId, builder.build());

    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            CharSequence name = MOMApplication.getInstance().getString(R.string.default_notification_channel_name);
            String description = MOMApplication.getInstance().getString(R.string.default_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(MOMApplication.getInstance().getString(R.string.default_notification_channel_id)
                    , name, importance);
            channel.enableVibration(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(defaultSoundUri, audioAttributes);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = MOMApplication.getInstance().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
