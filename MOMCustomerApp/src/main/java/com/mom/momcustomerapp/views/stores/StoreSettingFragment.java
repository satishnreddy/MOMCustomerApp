package com.mom.momcustomerapp.views.stores;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.ItemsClient;
import com.mom.momcustomerapp.customviews.dialogs.TwoBtnDialogFragment;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.settings.SettingsFragmentNew;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mom.momcustomerapp.views.stores.delivery.DeliveryOptionsFragment;
import com.mom.momcustomerapp.widget.SafeClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link BaseFragment} subclass.
 */
public class StoreSettingFragment extends BaseFragment
{

    private SwitchCompat switchLockCustomersList;;
    private View llDeliveryOptions;
    private View llChangeLanguage;
    private View llStoreDetails;
    private Home_Tab_Activity activity;
    private boolean lastKnownLockCustValue = false;

    private SwitchCompat switchLockStoreImages;
    private SwitchCompat switchLockCatImages;
    private SwitchCompat switchLockItemImages;
    private ImageView backarrow;
    private RelativeLayout relCatImages;
    private RelativeLayout relItemImages;

    private String strStoreImages = "0";
    private String strCatImages = "0";
    private String strItemImages = "0";

    private Boolean switchStateStoreImages ;
    private Boolean switchStateCatImages ;
    private Boolean switchStateItemImages ;


    public StoreSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Home_Tab_Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_setting, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        backarrow = view.findViewById(R.id.back_arrow);
        switchLockCustomersList = view.findViewById(R.id.fragment_store_setting_switch_loc);

        switchLockStoreImages = view.findViewById(R.id.fragment_store_setting_switch_store_images);
        switchLockCatImages = view.findViewById(R.id.fragment_store_setting_switch_cat_images);
        switchLockItemImages = view.findViewById(R.id.fragment_store_setting_switch_item_images);

        relCatImages = view.findViewById(R.id.fragment_store_setting_rel_cat_images);
        relItemImages = view.findViewById(R.id.fragment_store_setting_rel_item_images);

        strStoreImages = MOMApplication.getSharedPref().getStoreImageSettings();
        strCatImages = MOMApplication.getSharedPref().getCatImageSettings();
        strItemImages = MOMApplication.getSharedPref().getItemImageSettings();

        switchStateStoreImages = switchLockStoreImages.isChecked();
        switchStateCatImages = switchLockCatImages.isChecked();
        switchStateItemImages = switchLockItemImages.isChecked();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().beginTransaction().replace(R.id.flFragment, new SettingsFragmentNew()).commit();
            }
        });
        if(switchStateStoreImages.equals(true))
        {
            strStoreImages = "1";
        }
        if(switchStateCatImages.equals(true))
        {
            strCatImages = "1";
        }
        if(switchStateItemImages.equals(true))
        {
            strItemImages = "1";
        }

       /*if(switchState.equals(true))
        {
            switchLockStoreImages.setChecked(true);
            relCatImages.setVisibility(View.VISIBLE);
            relItemImages.setVisibility(View.VISIBLE);
            }*/


        if(strStoreImages.equalsIgnoreCase("1"))
        {
            switchLockStoreImages.setChecked(true);

            relCatImages.setVisibility(View.VISIBLE);
            relItemImages.setVisibility(View.VISIBLE);

            if(strCatImages.equalsIgnoreCase("1"))
            {

                switchLockCatImages.setChecked(true);
            }

            if(strItemImages.equalsIgnoreCase("1"))
            {
                switchLockItemImages.setChecked(true);
            }
        }

        switchLockStoreImages.setOnCheckedChangeListener(storeCheckedChangeListener);
        switchLockCatImages.setOnCheckedChangeListener(catCheckedChangeListener);
        switchLockItemImages.setOnCheckedChangeListener(itemCheckedChangeListener);

        llDeliveryOptions = view.findViewById(R.id._frag_store_setting_ll_delivery_options);
        llChangeLanguage = view.findViewById(R.id._frag_store_setting_ll_change_language);
        llStoreDetails = view.findViewById(R.id._frag_store_setting_ll_store_details);

        llDeliveryOptions.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v)
            {
              //activity.selectDrawerItemByTag(FRAGMENT_DELIVERY);
              getFragmentManager().beginTransaction().replace(R.id.flFragment, new DeliveryOptionsFragment()).commit();

            }
        }));

        llChangeLanguage.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v)
            {
                //activity.selectDrawerItemByTag(FRAGMENT_CHANGE_LANGUAGE);
                //getFragmentManager().beginTransaction().replace(R.id.flFragment, new ChangeLanguageFragment()).commit();

            }
        }));

        llStoreDetails.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {
                //activity.selectDrawerItemByTag(FRAGMENT_STORE_DETAILS);
                //getFragmentManager().beginTransaction().replace(R.id.flFragment, new StoreDetailsFragment()).commit();
            }
        }));


        new Handler().postDelayed(() -> {
            fetchLockCustomerStatus();
        }, 300);
    }

    private void showConfirmationDialog(boolean isChecked)
    {
        String subTitle = isChecked ? getString(R.string.want_to_lock_list)
        : getString(R.string.want_to_unlock_list);

        TwoBtnDialogFragment.showDialog(activity,
                "",subTitle,
                getString(R.string.general_use_no), getString(R.string.general_use_yes),
                new TwoBtnDialogFragment.CustomDialogListener() {
                    @Override
                    public void onClickLeftBtn() {
                        setLastValue();
                    }

                    @Override
                    public void onClickRightBtn() {
                        updateDeliveryType(isChecked);
                    }
                }
        );
    }


    private void fetchLockCustomerStatus()
    {
        /*
        showLoadingDialog();
        OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());

        Call<String> call = ordersClient.getLockCustomer();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;
                activity.hideLoadingDialog();

                if (!response.isSuccessful()) {
                    activity.showErrorDialog(ErrorUtils.getErrorString(response));
                    setLastValue();
                    return;
                }
                String responseString = response.body();
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    JSONObject data = jsonResponse.getJSONObject("data");
                    String deliveryType = data.getString("lock_customer");
                    boolean status = false;
                    switch (deliveryType.toLowerCase()){
                        case "0":
                            status = false;
                            break;
                        case "1":
                            status = true;
                            break;
                        default:
                            showErrorDialog(getString(R.string.sww));
                            break;
                    }
                    lastKnownLockCustValue = status;
                    switchLockCustomersList.setChecked(status);

                    switchLockCustomersList.setOnCheckedChangeListener(checkedChangeListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.showErrorDialog(getString(R.string.invalid_server_response));
                    setLastValue();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!isAdded()) return;
                activity.hideLoadingDialog();
                t.printStackTrace();
                activity.showErrorDialog(ErrorUtils.getFailureError(t));
                setLastValue();
            }
        });
        */
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            showConfirmationDialog(isChecked);
        }
    };

    private CompoundButton.OnCheckedChangeListener storeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if(isChecked){
                strStoreImages = "1";
                relCatImages.setVisibility(View.VISIBLE);
                relItemImages.setVisibility(View.VISIBLE);
            }
            else{
                strStoreImages = "0";
                relCatImages.setVisibility(View.GONE);
                relItemImages.setVisibility(View.GONE);
                switchLockCatImages.setChecked(false);
                switchLockItemImages.setChecked(false);

                updateStoreImageSettings();
            }

        }
    };

    private CompoundButton.OnCheckedChangeListener catCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked){
                strCatImages = "1";
            }
            else{
                strCatImages = "0";
            }

            if(strStoreImages.equalsIgnoreCase("1")) {
                updateStoreImageSettings();
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener itemCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                strItemImages = "1";
            }
            else{
                strItemImages = "0";
            }

            if(strStoreImages.equalsIgnoreCase("1")) {
                updateStoreImageSettings();
            }
        }
    };



    private void updateDeliveryType(boolean status)
    {
        /*
        showLoadingDialog();
        OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());
        Call<String> call = ordersClient.updateLockCustomer(status ? 1 : 0);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;
                hideLoadingDialog();
                if (!response.isSuccessful()) {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                    setLastValue();
                    return;
                }
                String responseString = response.body();
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    String data = jsonResponse.getString("data");
                    if (data.equals("success")) {
                        lastKnownLockCustValue = status;
                        if (isAdded())
                            Toast.makeText(getContext(), getString(R.string.update_successfully), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        showErrorDialog(getString(R.string.sww));
                        setLastValue();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorDialog(getString(R.string.invalid_server_response));
                    setLastValue();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!isAdded()) return;
                hideLoadingDialog();
                t.printStackTrace();
                activity.showErrorDialog(ErrorUtils.getFailureError(t));
                setLastValue();
            }
        });

         */
    }

    private void setLastValue() {
        switchLockCustomersList.setOnCheckedChangeListener(null);
        switchLockCustomersList.setChecked(lastKnownLockCustValue);
        switchLockCustomersList.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchLockCustomersList.setOnCheckedChangeListener(checkedChangeListener);
            }
        },500);

    }


    private void updateStoreImageSettings()
    {
        showLoadingDialog();

        ItemsClient itemsClient = ServiceGenerator.createService(ItemsClient.class, MOMApplication.getInstance().getToken());

        Call<String> call = itemsClient.updateStoreImageSettings(strStoreImages+strCatImages+strItemImages);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoadingDialog();
                if (response.isSuccessful()) {

                    String responseString = response.body();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String status = jsonResponse.getString("data");
                        boolean success = status.equals("success");

                        if (success)
                        {
                            MOMApplication.getSharedPref().setStoreImageSettings(strStoreImages);
                            MOMApplication.getSharedPref().setCatImageSettings(strCatImages);
                            MOMApplication.getSharedPref().setItemImageSettings(strItemImages);

                            Toast.makeText(getContext(), getString(R.string.update_successfully), Toast.LENGTH_SHORT).show();

                        }
                        else {
                            showErrorDialog(getString(R.string.sww));
                        }
                    }
                    catch (JSONException je){
                        je.printStackTrace();
                        showErrorDialog(getString(R.string.invalid_server_response));
                    }
                } else {
                    try {
                        if (response.code() == 401) {
                            showErrorDialog("Error", response.message());
                        } else {
                            Response<String> response1 = Response.success(response.errorBody().string().replace("\n", ""));
                            parseError(response1, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (response.body() != null) {
                            showErrorDialog("Error", "Something went wrong: " + response.body());
                        } else {
                            showErrorDialog("Error", "Something went wrong: " + response.message());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoadingDialog();
                if (t instanceof SocketTimeoutException || t instanceof IOException) {
                    showErrorDialog("Network Error", "No Network. Please check connection");
                } else {
                    showErrorDialog("Error", "Something went wrong : " + t.getMessage());
                    Log.e("ERROR", "onFailure: Something went wrong", t);
                }
            }
        });
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
