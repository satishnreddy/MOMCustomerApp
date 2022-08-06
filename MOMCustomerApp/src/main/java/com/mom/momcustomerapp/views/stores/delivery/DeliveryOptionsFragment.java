package com.mom.momcustomerapp.views.stores.delivery;


import static com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE.DELIVERY_TYPE_BOTH;
import static com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE.DELIVERY_TYPE_DELIVERY;
import static com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE.DELIVERY_TYPE_NA;
import static com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE.DELIVERY_TYPE_PICKUP;
import static com.mom.momcustomerapp.data.application.Consts.REQUEST_CODE_SET_TIME_SLOTS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.sales.api.OrdersClient;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlotModel;
import com.mom.momcustomerapp.controllers.stores.model.DELIVERY_TYPE;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.utils.AppUtils;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.views.stores.StoreSettingFragment;
import com.mom.momcustomerapp.widget.SafeClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryOptionsFragment extends BaseFragment {


    private ViewGroup pickupParentTimeSlotsLL, deliveryParentTimeSlotsLL;
    private ViewGroup pickupTimeSlotsLL, deliveryTimeSlotsLL;
    private View addTimeSlotDeliveryView, addTimeSlotPickupView;
    private ImageView backarrow;
    @DELIVERY_TYPE
    private String deliveryTypeOnServer = DELIVERY_TYPE_NA;
    private boolean IS_TIME_SLOT_FEATURE_ENABLED = true;

    public DeliveryOptionsFragment() {
        // Required empty public constructor
    }

    Home_Tab_Activity activity;

    private SwitchCompat switchCompat1, switchCompat2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Home_Tab_Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stores_delivery_option, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backarrow = view.findViewById(R.id.back_arrow);

        switchCompat1 = view.findViewById(R.id.fragment_delivery_option_switch_1);
        switchCompat2 = view.findViewById(R.id.fragment_delivery_option_switch_2);

        pickupParentTimeSlotsLL = view.findViewById(R.id.fragment_delivery_option_parent_pickup_time_slot);
        pickupTimeSlotsLL = view.findViewById(R.id.fragment_delivery_option_pickup_time_slot);
        addTimeSlotPickupView = view.findViewById(R.id.fragment_delivery_option_tv_add_pickup);

        deliveryParentTimeSlotsLL = view.findViewById(R.id.fragment_delivery_option_parent_delivery_time_slot);
        deliveryTimeSlotsLL = view.findViewById(R.id.fragment_delivery_option_delivery_time_slot);
        addTimeSlotDeliveryView = view.findViewById(R.id.fragment_delivery_option_tv_add_delivery);

        addTimeSlotDeliveryView.setOnClickListener(new SafeClickListener(
                v -> startActivityForResult(DeliveryPickupTimeActivity
                        .getStartIntent(activity, null, DELIVERY_TYPE_DELIVERY), REQUEST_CODE_SET_TIME_SLOTS)
        ));

        addTimeSlotPickupView.setOnClickListener(new SafeClickListener(
                v -> startActivityForResult(DeliveryPickupTimeActivity
                        .getStartIntent(activity, null, DELIVERY_TYPE_PICKUP), REQUEST_CODE_SET_TIME_SLOTS)
        ));

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.flFragment, new StoreSettingFragment()).commit();
            }
        });

        deliveryParentTimeSlotsLL.setVisibility(View.GONE);
        pickupParentTimeSlotsLL.setVisibility(View.GONE);

        switchCompat1.post(new Runnable() {
            @Override
            public void run() {
                fetchDeliveryTypeFromServer();
            }
        });
    }
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (switchCompat1.isChecked() && switchCompat2.isChecked()) {
                updateDeliveryType(DELIVERY_TYPE_BOTH);
            } else if (!switchCompat1.isChecked() && switchCompat2.isChecked()) {
                updateDeliveryType(DELIVERY_TYPE_DELIVERY);
            } else if (switchCompat1.isChecked() && !switchCompat2.isChecked()) {
                updateDeliveryType(DELIVERY_TYPE_PICKUP);
            } else {
                buttonView.setChecked(true);
            }
        }
    };

    private void fetchDeliveryTypeFromServer() {
        showLoadingDialog();
        OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());

        Call<String> call = ordersClient.getDeliveryType("");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                activity.hideLoadingDialog();
                if (!response.isSuccessful()) {
                    activity.showErrorDialog(ErrorUtils.getErrorString(response));
                    onFailedFetchDeliveryType();
                    return;
                }
                String responseString = response.body();
                responseString = responseString.replaceAll("\\n", "");
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    JSONObject data = jsonResponse.getJSONObject("data");
                    String deliveryType = data.getString("delivery_type");
                    refreshUIForDeliveryType(deliveryType);
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.showErrorDialog(getString(R.string.invalid_server_response));
                    onFailedFetchDeliveryType();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                activity.hideLoadingDialog();
                t.printStackTrace();
                activity.showErrorDialog(ErrorUtils.getFailureError(t));
                onFailedFetchDeliveryType();
            }
        });
    }

    private void onFailedFetchDeliveryType() {
        switchCompat1.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchCompat1.setOnCheckedChangeListener(listener);
                switchCompat2.setOnCheckedChangeListener(listener);
            }
        }, 700);
    }

    private void updateDeliveryType(@DELIVERY_TYPE String status) {
        activity.showLoadingDialog();
        OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());

        Call<String> call = ordersClient.updateDeliveryType(status);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                activity.hideLoadingDialog();
                if (!response.isSuccessful()) {
                    activity.showErrorDialog(ErrorUtils.getErrorString(response));
                    refreshUIForDeliveryType(deliveryTypeOnServer);
                    return;
                }
                String responseString = response.body();
                responseString = responseString.replaceAll("\\n", "");
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    String data = jsonResponse.getString("data");
                    if (data.equals("success") && isAdded()) {
                        Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        refreshUIForDeliveryType(status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.showErrorDialog(getString(R.string.invalid_server_response));
                    refreshUIForDeliveryType(deliveryTypeOnServer);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                activity.hideLoadingDialog();
                t.printStackTrace();
                activity.showErrorDialog(ErrorUtils.getFailureError(t));
                refreshUIForDeliveryType(deliveryTypeOnServer);
            }
        });
    }

    private void updateTimeSlotsUI(List<TimeSlotModel.TimeSlot> timeSlotModels, @DELIVERY_TYPE String deliveryType) {
        ViewGroup viewGroupToAddTimeSlots;
        if (deliveryType.equals(DELIVERY_TYPE_DELIVERY)) {
            viewGroupToAddTimeSlots = deliveryTimeSlotsLL;
        } else {
            viewGroupToAddTimeSlots = pickupTimeSlotsLL;
        }
        viewGroupToAddTimeSlots.removeAllViews();
        if (timeSlotModels == null || timeSlotModels.size() == 0) {
            /*Toast.makeText(activity, "No Time Slots", Toast.LENGTH_SHORT).show();*/

            if (deliveryType.equals(DELIVERY_TYPE_DELIVERY)) {
                deliveryTimeSlotsLL.setVisibility(View.GONE);
                addTimeSlotDeliveryView.setVisibility(View.VISIBLE);
            } else {

            }

        } else {
            deliveryTimeSlotsLL.setVisibility(View.VISIBLE);

            if(timeSlotModels.size() == 2){
                addTimeSlotDeliveryView.setVisibility(View.GONE);
            }
            else {
                addTimeSlotDeliveryView.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < timeSlotModels.size(); i++) {
                TimeSlotModel.TimeSlot timeSlotModel = timeSlotModels.get(i);
                View inflate = LayoutInflater.from(activity).inflate(R.layout.inflate_time_slot_data, null);
                TextView startEndTimeTv = inflate.findViewById(R.id.inflate_time_slot_tv_start_end_time);
                TextView daysTv = inflate.findViewById(R.id.inflate_time_slot_tv_days);
                startEndTimeTv.setText(getTimeFromTimeSlot(timeSlotModel));
                daysTv.setText(getDaysFromTimeSlot(timeSlotModel));
                inflate.setOnClickListener(new SafeClickListener(v -> {
                    startActivityForResult(DeliveryPickupTimeActivity.getStartIntent(activity, timeSlotModel, deliveryType)
                            , REQUEST_CODE_SET_TIME_SLOTS);
                }));
                viewGroupToAddTimeSlots.addView(inflate);
            }
        }
    }

    private String getTimeFromTimeSlot(TimeSlotModel.TimeSlot timeSlotModel) {
        return Consts.getTimeIn12hrFormat(timeSlotModel.getFromSlot())
                + " to " + Consts.getTimeIn12hrFormat(timeSlotModel.getToSlot());

    }

    private String getDaysFromTimeSlot(TimeSlotModel.TimeSlot timeSlotModel) {
        String weekDays = timeSlotModel.getWeekDaysSelected();
        return AppUtils.getStringOfDays(weekDays);
    }

    private void getTimeSlotsApi(@DELIVERY_TYPE String deliveryTypeCode) {
        if (!IS_TIME_SLOT_FEATURE_ENABLED) return;
        CustomerClient client = ServiceGenerator.createService(CustomerClient.class,
                MOMApplication.getInstance().getToken());
        Call<TimeSlotModel> call = client.getSlot(MOMApplication.getInstance().getStoreId(), deliveryTypeCode);

        activity.showLoadingDialog();
        call.enqueue(new Callback<TimeSlotModel>() {
            @Override
            public void onResponse(Call<TimeSlotModel> call, Response<TimeSlotModel> response) {
                if (activity.isFinishing()) return;
                activity.hideLoadingDialog();
                if (response.isSuccessful()) {
                    TimeSlotModel timeSlotModelArrayList = response.body();
                    updateTimeSlotsUI(timeSlotModelArrayList.getData(), deliveryTypeCode);
                } else activity.showErrorDialog(ErrorUtils.getErrorString(response));
            }

            @Override
            public void onFailure(Call<TimeSlotModel> call, Throwable t) {
                activity.hideLoadingDialog();
                activity.showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });
    }

    private void refreshUIForDeliveryType(@DELIVERY_TYPE String deliveryType) {

        switchCompat1.setOnCheckedChangeListener(null);
        switchCompat2.setOnCheckedChangeListener(null);

        deliveryTypeOnServer = deliveryType;
        switch (deliveryTypeOnServer) {

            case DELIVERY_TYPE_DELIVERY:
                switchCompat2.setChecked(true);

                if (IS_TIME_SLOT_FEATURE_ENABLED){
                    getTimeSlotsApi(DELIVERY_TYPE_DELIVERY);

                    deliveryParentTimeSlotsLL.setVisibility(View.VISIBLE);
                    pickupParentTimeSlotsLL.setVisibility(View.GONE);
                }
                break;

            case DELIVERY_TYPE_PICKUP:
                switchCompat1.setChecked(true);
                if (IS_TIME_SLOT_FEATURE_ENABLED) {
                    getTimeSlotsApi(DELIVERY_TYPE_PICKUP);

                    //deliveryParentTimeSlotsLL.setVisibility(View.GONE);
                    //pickupParentTimeSlotsLL.setVisibility(View.VISIBLE);
                }
                break;

            case DELIVERY_TYPE_BOTH:

                switchCompat1.setChecked(true);
                switchCompat2.setChecked(true);

                if (IS_TIME_SLOT_FEATURE_ENABLED)
                {
                    getTimeSlotsApi(DELIVERY_TYPE_DELIVERY);
                    //getTimeSlotsApi(DELIVERY_TYPE_PICKUP);

                    deliveryParentTimeSlotsLL.setVisibility(View.VISIBLE);
                    //pickupParentTimeSlotsLL.setVisibility(View.VISIBLE);
                }
                break;

            case DELIVERY_TYPE_NA:
                switchCompat1.setChecked(false);
                switchCompat2.setChecked(false);
        }

        switchCompat1.postDelayed(() -> {
            switchCompat1.setOnCheckedChangeListener(listener);
            switchCompat2.setOnCheckedChangeListener(listener);
        }, 700);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SET_TIME_SLOTS && resultCode == Activity.RESULT_OK){
            fetchDeliveryTypeFromServer();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }


    @Override
    public void onStop() {
        super.onStop();
       // ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
