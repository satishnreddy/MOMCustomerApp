package com.mom.momcustomerapp.views.stores.delivery;

import static com.mom.momcustomerapp.data.application.Consts.EXTRA_KEY_DELIVERY_TYPE;
import static com.mom.momcustomerapp.data.application.Consts.EXTRA_KEY_TIME_SLOT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlotModel;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlotSubmissionModel;
import com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE;
import com.mom.momcustomerapp.customviews.dialogs.SingleBtnDialogFragment;
import com.mom.momcustomerapp.customviews.dialogs.TimePickerSpinnerDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.shared.models.StatusMessageModel;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.widget.SafeClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryPickupTimeActivity extends BaseActivity {

    @DELIVERY_TYPE
    private String deliveryType;
    private TimeSlotModel.TimeSlot model;
    private View daySunView, dayMonView, dayTueView, dayWedView, dayThuView, dayFriView, daySatView;
    private ImageView startTimeBtn;
    private TextView submitBtn, startTimeTv;

    private int startHrToShow = 9;
    private int startMinToShow = 0;
    private int endHrToShow = 20;
    private int endMinToShow = 0;

    public static Intent getStartIntent(Context context, TimeSlotModel.TimeSlot timeSlotModel, String deliveryType) {
        Intent intent = new Intent(context, DeliveryPickupTimeActivity.class);
        intent.putExtra(EXTRA_KEY_TIME_SLOT, timeSlotModel);
        intent.putExtra(EXTRA_KEY_DELIVERY_TYPE, deliveryType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_delivery_pickup_time);
        setupToolBar();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.title_activity_delivery_pickup_timings));
        }

        daySunView = findViewById(R.id.activity_delivery_pickup_time_v_day_sun);
        dayMonView = findViewById(R.id.activity_delivery_pickup_time_v_day_mon);
        dayTueView = findViewById(R.id.activity_delivery_pickup_time_v_day_tue);
        dayWedView = findViewById(R.id.activity_delivery_pickup_time_v_day_wed);
        dayThuView = findViewById(R.id.activity_delivery_pickup_time_v_day_thur);
        dayFriView = findViewById(R.id.activity_delivery_pickup_time_v_day_fri);
        daySatView = findViewById(R.id.activity_delivery_pickup_time_v_day_sat);

        submitBtn = findViewById(R.id.activity_delivery_pickup_time_btn_add_time_slot);

        startTimeBtn = findViewById(R.id.activity_delivery_pickup_time_iv_start_time);

        startTimeTv = findViewById(R.id.activity_delivery_pickup_time_tv_start_time);

        Intent intent = getIntent();
        deliveryType = intent.getStringExtra(EXTRA_KEY_DELIVERY_TYPE);
        model = intent.getParcelableExtra(EXTRA_KEY_TIME_SLOT);

        if (model != null) {
            String weekDaysSelected = model.getWeekDaysSelected();
            if (weekDaysSelected.length() == 7) {
                if (weekDaysSelected.charAt(0) == '1') daySunView.setSelected(true);
                if (weekDaysSelected.charAt(1) == '1') dayMonView.setSelected(true);
                if (weekDaysSelected.charAt(2) == '1') dayTueView.setSelected(true);
                if (weekDaysSelected.charAt(3) == '1') dayWedView.setSelected(true);
                if (weekDaysSelected.charAt(4) == '1') dayThuView.setSelected(true);
                if (weekDaysSelected.charAt(5) == '1') dayFriView.setSelected(true);
                if (weekDaysSelected.charAt(6) == '1') daySatView.setSelected(true);
            }

            String toSlot = model.getToSlot();
            if (!TextUtils.isEmpty(toSlot)) {
                int hrIn24Format = 0;
                int min = 0;
                if (toSlot.contains(".")) {
                    int i = toSlot.indexOf(".");
                    hrIn24Format = Integer.parseInt(toSlot.substring(0, i));
                    min = Integer.parseInt(toSlot.substring(i + 1));
                } else {
                    try {
                        hrIn24Format = Integer.parseInt(toSlot);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                endHrToShow = hrIn24Format;
                endMinToShow = min;
            }

            String fromSlot = model.getFromSlot();
            if (!TextUtils.isEmpty(fromSlot)) {
                int hrIn24Format = 0;
                int min = 0;
                if (fromSlot.contains(".")) {
                    int i = fromSlot.indexOf(".");
                    hrIn24Format = Integer.parseInt(fromSlot.substring(0, i));
                    min = Integer.parseInt(fromSlot.substring(i + 1));
                } else {
                    try {
                        hrIn24Format = Integer.parseInt(fromSlot);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                startHrToShow = hrIn24Format;
                startMinToShow = min;
            }

        }
        submitBtn.setText(isNewTimeSlot() ? getString(R.string.general_use_add_sentence_case)
                : getString(R.string.general_use_submit));


        setListeners();
    }

    private void setListeners() {
        View.OnClickListener safeClickListener = v -> v.setSelected(!v.isSelected());
        daySunView.setOnClickListener(safeClickListener);
        dayMonView.setOnClickListener(safeClickListener);
        dayTueView.setOnClickListener(safeClickListener);
        dayWedView.setOnClickListener(safeClickListener);
        dayThuView.setOnClickListener(safeClickListener);
        dayFriView.setOnClickListener(safeClickListener);
        daySatView.setOnClickListener(safeClickListener);

        submitBtn.setOnClickListener(new SafeClickListener(v -> {
            validateData();
        }));

        startTimeTv.setText(Consts.getTimeIn12hrFormat(startHrToShow, startMinToShow) + "-" + Consts.getTimeIn12hrFormat(endHrToShow, endMinToShow));

        startTimeBtn.setOnClickListener(new SafeClickListener(v -> {
            startTime();
        }));
    }

    private void startTime(){
        DialogFragment dialogFragment = TimePickerSpinnerDialogFragment.newInstance(getResources().getString(R.string.general_use_from), startHrToShow, startMinToShow,
                (view, hourOfDay, minute) -> {
                    startHrToShow = hourOfDay;
                    startMinToShow = minute;
                    endTime();
                }
        );
        dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
    }

    private void endTime(){
        DialogFragment dialogFragment = TimePickerSpinnerDialogFragment.newInstance(getResources().getString(R.string.general_use_to), endHrToShow, endMinToShow,
                (view, hourOfDay, minute) -> {
                    endHrToShow = hourOfDay;
                    endMinToShow = minute;
                    startTimeTv.setText(Consts.getTimeIn12hrFormat(startHrToShow, startMinToShow) + "-" + Consts.getTimeIn12hrFormat(endHrToShow, endMinToShow));
                }
        );
        dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
    }

    private void validateData() {
        // validate start time is less then end time

        // is atleast 1 day selected
        if (daySunView.isSelected() || dayMonView.isSelected() || dayTueView.isSelected() ||
                dayWedView.isSelected() || dayThuView.isSelected() || dayFriView.isSelected()
                || daySatView.isSelected()) {
            sendTimeSlotsToServer();
        } else {
            DialogFragment dialogFragment = SingleBtnDialogFragment.newInstance(getString(R.string.delivery_pickup_select_one_day));
            dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
        }

    }

    private boolean isNewTimeSlot() {
        return model == null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isNewTimeSlot()) return true;
        getMenuInflater().inflate(R.menu.menu_delivery_pickup_timings, menu);
        MenuItem menuCustomFields = menu.findItem(R.id.menu_delete_button);
        menuCustomFields.setOnMenuItemClickListener(item -> {
            onClickedDelete();
            return false;
        });
        return true;
    }

    private void onClickedDelete() {

        CustomerClient client = ServiceGenerator.createService(CustomerClient.class,
                MOMApplication.getInstance().getToken());
        Call<StatusMessageModel> call = client.deleteTimeSlot(model.getStoreId(), deliveryType, model.getSr_no());

        showLoadingDialog();
        call.enqueue(new Callback<StatusMessageModel>() {
            @Override
            public void onResponse(Call<StatusMessageModel> call, Response<StatusMessageModel> response) {
                if (isFinishing()) return;
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    StatusMessageModel statusMessageModel = response.body();
                    if (statusMessageModel != null && statusMessageModel.isStatusSuccess()) {
                        DialogFragment dialogFragment = SingleBtnDialogFragment.newInstance(statusMessageModel.getMessage(),
                                getString(R.string.ok), new SingleBtnDialogFragment.DialogListener() {
                                    @Override
                                    public void onDialogPositiveClick() {
                                        Intent intent = new Intent();
                                        intent.putExtra(Consts.EXTRA_ISUPDATED, true);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                });
                        dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
                    } else {
                        showErrorDialog(getString(R.string.sww));
                    }
                } else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<StatusMessageModel> call, Throwable t) {
                if (isFinishing()) return;
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }

        });
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void sendTimeSlotsToServer() {
        CustomerClient client = ServiceGenerator.createService(CustomerClient.class,
                MOMApplication.getInstance().getToken());

        TimeSlotSubmissionModel submissionModel = new TimeSlotSubmissionModel();
        submissionModel.setWeekDaysSelected(getWeekDaysSelected());
        submissionModel.setDeliveryType(deliveryType);
        submissionModel.setTimeFrom(String.valueOf(startHrToShow));
        submissionModel.setTimeTo(String.valueOf(endHrToShow));
        submissionModel.setStoreId(MOMApplication.getInstance().getStoreId());
        Call<String> call;
        if (isNewTimeSlot()) {
            call = client.addTimeSlots(submissionModel);
        } else {
            submissionModel.setSr_no(model.getSr_no());
            call = client.updateTimeSlots(submissionModel);
        }

        showLoadingDialog();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (isFinishing()) return;
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra(Consts.EXTRA_ISUPDATED, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });
    }

    private String getWeekDaysSelected() {
        return String.format("%s%s%s%s%s%s%s",
                daySunView.isSelected() ? "1" : "0",
                dayMonView.isSelected() ? "1" : "0",
                dayTueView.isSelected() ? "1" : "0",
                dayWedView.isSelected() ? "1" : "0",
                dayThuView.isSelected() ? "1" : "0",
                dayFriView.isSelected() ? "1" : "0",
                daySatView.isSelected() ? "1" : "0");
    }


}
