package com.mom.momcustomerapp.views.stores.delivery;

import static com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE.DELIVERY_TYPE_DELIVERY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlotModel;
import com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.widget.SafeClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangeDeliverySoltActivity extends BaseActivity {

    private ViewGroup deliveryDateSlotsLL, deliveryTimeSlotsLL;
    private Button mBtnSave;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChangeDeliverySoltActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_delivery_change_time);
        setupToolBar();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.title_activity_edit_delivery_slot));
        }

        deliveryDateSlotsLL = findViewById(R.id.fragment_change_delivery_option_date_slot);
        deliveryTimeSlotsLL = findViewById(R.id.fragment_change_delivery_option_time_slot);
        mBtnSave = findViewById(R.id.fragment_change_delivery_option_btn_save);
        mBtnSave.setEnabled(false);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mTimeSlot >=0)
                {

                    String from = getDateWithFormate(mTimeSlotModels.get(mTimeSlot).getFromSlot(),"HH.mm","hh:mm a");
                    String to = getDateWithFormate(mTimeSlotModels.get(mTimeSlot).getToSlot(),"HH.mm","hh:mm a");

                    Intent intent = new Intent();
                    intent.putExtra("deliveryslotid", mTimeSlotModels.get(mTimeSlot).getStoreId());
                    intent.putExtra("deliveryslot", from + " - " + to);
                    intent.putExtra("deliverydate", mStrDeliveryDate);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        getTimeSlotsApi(DELIVERY_TYPE_DELIVERY);
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


    private void getTimeSlotsApi(@DELIVERY_TYPE String deliveryTypeCode) {

        CustomerClient client = ServiceGenerator.createService(CustomerClient.class,
                MOMApplication.getInstance().getToken());
        Call<TimeSlotModel> call = client.getSlot(MOMApplication.getInstance().getStoreId(), deliveryTypeCode);

        showLoadingDialog();
        call.enqueue(new Callback<TimeSlotModel>() {
            @Override
            public void onResponse(Call<TimeSlotModel> call, Response<TimeSlotModel> response) {
                if (isFinishing()) return;
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    TimeSlotModel timeSlotModelArrayList = response.body();

                    mTimeSlotModels = timeSlotModelArrayList.getData();
                    updateDateSlotsUI();

                } else showErrorDialog(ErrorUtils.getErrorString(response));
            }

            @Override
            public void onFailure(Call<TimeSlotModel> call, Throwable t) {
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });
    }

    List<TimeSlotModel.TimeSlot> mTimeSlotModels;
    String mStrToday = "";
    String mStrDeliveryDate = "";

    LinearLayout mLinSelected;
    int mDateSlot = -1;

    private void updateDateSlotsUI() {

        LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
        SimpleDateFormat deliverydatedf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat daydf = new SimpleDateFormat("EEE");
        SimpleDateFormat dtsdf = new SimpleDateFormat("MMM dd");

        for (int i = 0; i < 7; i++)
        {

            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            String tday = daydf.format(calendar.getTime());
            String tdate = dtsdf.format(calendar.getTime());

           // TimeSlotModel.TimeSlot timeSlotModel = timeSlotModels.get(i);
            View inflate = mInflater.inflate(R.layout.inflate_select_date_slot_data, null);

            LinearLayout dayll = inflate.findViewById(R.id.inflate_date_slot_LIN_day);
            TextView day = inflate.findViewById(R.id.inflate_date_slot_tv_day);
            TextView date = inflate.findViewById(R.id.inflate_date_slot_tv_date);

            if(i == 0) {
                dayll.setBackgroundResource(R.drawable.rect_filled_01b108_corner_6dp);
                day.setTextColor(getResources().getColor(R.color.white));
                date.setTextColor(getResources().getColor(R.color.white));
                day.setText("Today");
                mStrToday = tday;
                mLinSelected = dayll;
            }
            else{
                day.setText(tday);
            }
            date.setText(tdate);

            int finalI = i;
            inflate.setOnClickListener(new SafeClickListener(v -> {
                mDateSlot = finalI;

                if(mLinSelected != null){
                    mLinSelected.setBackgroundResource(R.drawable.rect_filled_grey_corner_5dp);
                    ((TextView)mLinSelected.findViewById(R.id.inflate_date_slot_tv_day)).setTextColor(getResources().getColor(R.color.grey500));
                    ((TextView)mLinSelected.findViewById(R.id.inflate_date_slot_tv_date)).setTextColor(getResources().getColor(R.color.grey500));
                }

                dayll.setBackgroundResource(R.drawable.rect_filled_01b108_corner_6dp);
                ((TextView)dayll.findViewById(R.id.inflate_date_slot_tv_day)).setTextColor(getResources().getColor(R.color.white));
                ((TextView)dayll.findViewById(R.id.inflate_date_slot_tv_date)).setTextColor(getResources().getColor(R.color.white));

                mLinSelected = dayll;

                Calendar tcalendar = new GregorianCalendar();
                tcalendar.add(Calendar.DATE, finalI);
                mStrDeliveryDate = deliverydatedf.format(tcalendar.getTime());

                updateTimeSlotsUI(day.getText().toString());
            }));

            deliveryDateSlotsLL.addView(inflate);
        }

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 0);

        mStrDeliveryDate = deliverydatedf.format(calendar.getTime());

        updateTimeSlotsUI("Today");
    }

    ImageView mImgSelected;
    int mTimeSlot = -1;

    private void updateTimeSlotsUI(String selectedday) {

        mBtnSave.setEnabled(false);
        mImgSelected = null;
        mTimeSlot = -1;

        String tempSelectedDay = selectedday;
        if(selectedday.equalsIgnoreCase("Today"))
        {
            selectedday = mStrToday;
        }

        LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(deliveryTimeSlotsLL != null){
            deliveryTimeSlotsLL.removeAllViews();
        }

        for (int i = 0; i < mTimeSlotModels.size(); i++)
        {

            View inflate = mInflater.inflate(R.layout.inflate_select_time_slot_data, null);

            ImageView selected = inflate.findViewById(R.id.inflate_change_time_slot_img_select);
            TextView time = inflate.findViewById(R.id.inflate_change_time_slot_tv_start_end_time);

            if(tempSelectedDay.equalsIgnoreCase("Today"))
            {
                if(!isValidSlot(mTimeSlotModels.get(i).getToSlot(), "HH.mm")){

                    continue;
                }
            }

            String from = getDateWithFormate(mTimeSlotModels.get(i).getFromSlot(),"HH.mm","hh:mm a");
            String to = getDateWithFormate(mTimeSlotModels.get(i).getToSlot(),"HH.mm","hh:mm a");

            time.setText(from + " - " + to);

            int finalI = i;
            inflate.setOnClickListener(new SafeClickListener(v ->
            {
                if(mImgSelected != null){
                    mImgSelected.setBackgroundResource(R.drawable.ms_checkbox_img_inactive);
                }
                selected.setBackgroundResource(R.drawable.ms_checkbox_img_active);
                mImgSelected = selected;
                mTimeSlot = finalI;

                mBtnSave.setEnabled(true);

            }));

            if(geyDay(mTimeSlotModels.get(i).getWeekDaysSelected(), selectedday))
            {
                deliveryTimeSlotsLL.addView(inflate);
            }
        }
    }

    public boolean isValidSlot(String date1, String dateFormat){

        Date mDateOne = null;
        Date mDateTwo = null;

        try {

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String dateformatted = dateFormat1.format(date);

            SimpleDateFormat simpledateformat = new SimpleDateFormat(dateFormat);
            mDateOne = simpledateformat.parse(date1);

            mDateTwo = dateFormat1.parse(dateformatted);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long cnt = compareTo(mDateOne, mDateTwo);

        if (cnt <= 0){

            return false;
        }
        else{
            return true;
        }
    }

    public  long compareTo( Date date1, Date date2 )
    {
        return date1.getTime() - date2.getTime();
    }

    private String getDateWithFormate(String aDate, String aParseFormat, String aDisplayFormat)
    {
        String formatedDate = "";

        try {

            SimpleDateFormat displayFormat = new SimpleDateFormat(aDisplayFormat, Locale.ENGLISH);
            SimpleDateFormat parseFormat = new SimpleDateFormat(aParseFormat, Locale.ENGLISH);
            Date date = parseFormat.parse(aDate);
            formatedDate = displayFormat.format(date);

        } catch (Exception e) {
            formatedDate = aDate;

            e.printStackTrace();

        }

        return formatedDate.toLowerCase();

    }

    private boolean geyDay(String weekDays, String selectedday){


        if(selectedday.equalsIgnoreCase("Sun") && weekDays.charAt(0) == '1'){
            return true;
        }
        if(selectedday.equalsIgnoreCase("Mon") && weekDays.charAt(1) == '1'){
            return true;
        }

        if(selectedday.equalsIgnoreCase("Tue") && weekDays.charAt(2) == '1'){
            return true;
        }

        if(selectedday.equalsIgnoreCase("Wed") && weekDays.charAt(3) == '1'){
            return true;
        }

        if(selectedday.equalsIgnoreCase("Thu") && weekDays.charAt(4) == '1'){
            return true;
        }

        if(selectedday.equalsIgnoreCase("Fri") && weekDays.charAt(5) == '1'){
            return true;
        }

        if(selectedday.equalsIgnoreCase("Sat") && weekDays.charAt(6) == '1'){
            return true;
        }


        return false;
    }
}
