package com.mom.momcustomerapp.views.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mom.momcustomerapp.controllers.products.api.ItemsClient;
import com.mom.momcustomerapp.controllers.sales.SalesCustomerController;
import com.mom.momcustomerapp.controllers.sales.models.SalesCustStatsResp;
import com.mom.momcustomerapp.controllers.stores.model.ProfileModel;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ParseUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.orders.BillingFragment;
import com.mom.momcustomerapp.views.orders.ReportsViewActivity;
import com.mom.momcustomerapp.widget.SafeClickListener;
import com.mom.momcustomerapp.R;
import com.mswipetech.sdk.network.util.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends  Fragment {

    @BindView(R.id.frag_home_mbasket_tv_shop_name)
    TextView shopNameInCardTv;
    @BindView(R.id.frag_home_mbasket_tv_revenue_amt)
    TextView revenueAmt;
    @BindView(R.id.frag_home_mbasket_tv_pending_count)
    TextView pendingCountTv;
    @BindView(R.id.frag_home_mbasket_tv_return_count)
    TextView returnCountTv;
    @BindView(R.id.frag_home_mbasket_tv_completed_count)
    TextView completedCountTv;
    @BindView(R.id.tv_label_revenue_home_mbasket)
    TextView revenuelableTv;
    @BindView(R.id.your_store)
    TextView yourstoreTv;
    @BindView(R.id.share_friends_lay)
    LinearLayout sharelink;
    @BindView(R.id.modify_store_lay)
    LinearLayout modifystore;

    @BindView(R.id.view_storeroprts_lay)
    LinearLayout storereports;

    @BindView(R.id.view_store_lay)
    LinearLayout viewstore;

  @BindView(R.id.pending_item)
  LinearLayout pendingitem;
    @BindView(R.id.decliend_item)
    LinearLayout declineditem;

    @BindView(R.id.completed_item)
    LinearLayout compliteditem;


    @BindView(R.id.title_order)
    TextView ordertitle;

    String data;
    private Home_Tab_Activity activity;


    private Typeface boldTypeface,boldTypeface1;


    public HomeFragment(){
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        modifystore.setVisibility(View.GONE);

        boldTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        boldTypeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        //((Home_Tab_Activity) getActivity()).setToolBarCollapsible(false);

        //setShopName();
        shopNameInCardTv.setText( MOMApplication.getSharedPref().getName() +"!");

        setfont();



       return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCustomerOrdersStockCount();


    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //getShareUrl();
        pendingitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","pending"); // Put anything what you want

                BillingFragment fragment2 = new BillingFragment();
                fragment2.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, fragment2)
                        .commit();

            }
        });

        declineditem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("key","declined"); // Put anything what you want

                BillingFragment fragment2 = new BillingFragment();
                fragment2.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, fragment2)
                        .commit();

            }
        });

        compliteditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","completed"); // Put anything what you want

                BillingFragment fragment2 = new BillingFragment();
                fragment2.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, fragment2)
                        .commit();



            }
        });

        modifystore.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v)
            {
                //Intent intent = new Intent(getActivity(), ModifyStoreActivity.class);
                //startActivity(intent);
                //getFragmentManager().beginTransaction().replace(R.id.flFragment, new ModifyStoreFragement()).commit();

            }
        }));

        sharelink.setOnClickListener( new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {

                mShareType = 4;
                sendSimpleMsgs(getString(R.string.mbasket_order_online_with_url, "http://mom.com"));

            }
        }));

        storereports.setOnClickListener( new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v)
            {
                Intent intentReports = new Intent(getContext(), ReportsViewActivity.class);
                startActivity(intentReports);
            }
        }));

        viewstore.setOnClickListener( new SafeClickListener(new SafeClickListener.Callback()
        {
            @Override
            public void onClick(View v) {
                String data = (getString(R.string.mbasket_order_online_with_url, "http://mom.com"));;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(data));
                startActivity(browserIntent);

            }
        }));
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (Home_Tab_Activity) context;
    }

    private void setfont()
    {
        revenueAmt.setTypeface(boldTypeface);
        ordertitle.setTypeface(boldTypeface);
        shopNameInCardTv.setTypeface(boldTypeface);
        yourstoreTv.setTypeface(boldTypeface);
        revenuelableTv.setTypeface(boldTypeface1);

    }

    public int mShareType = 0;
    public void sendSimpleMsgs(String shareMsg)
    {
        if(mShareType == 1){

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);

            final PackageManager pm = getActivity().getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;

            for(final ResolveInfo info : matches)
            {
                if (info.activityInfo.packageName.toLowerCase().contains("com.whatsapp"))
                {
                    best = info;
                }
            }

            if (best != null) {
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
            }
            else{
                Toast.makeText(MOMApplication.getInstance(), getString(R.string.unable_to_share_the_link), Toast.LENGTH_LONG).show();
            }

        }
        else if(mShareType == 2){

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);

            final PackageManager pm = getActivity().getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;

            for(final ResolveInfo info : matches)
            {
                if (info.activityInfo.packageName.toLowerCase().contains("com.facebook.katana"))
                {
                    best = info;
                }
            }

            if (best != null)
            {
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

                final ActivityInfo activity = best.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                emailIntent.setComponent(name);

                startActivity(emailIntent);
            }
            else{
                Toast.makeText(MOMApplication.getInstance(), getString(R.string.unable_to_share_the_link), Toast.LENGTH_LONG).show();
            }
        }
        else  if(mShareType == 3){

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", shareMsg);
            startActivity(sendIntent);

        }
        else if(mShareType == 4){

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }


    public void getShareUrl()
    {
        /*
        activity.showLoadingDialog();
        CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MventryApp.getInstance().getToken());

        Call<String> call = customerClient.getSmsLink(
                MventryApp.getInstance().getCurrentStoreId(),
                ShopxieSharedPreferences.getInstance().getMswipeUsername(),
                ShopxieSharedPreferences.getInstance().getVender()
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;
                activity.hideLoadingDialog();
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    responseString = responseString.replaceAll("\\n", "");
                    try {
                        if (!TextUtils.isEmpty(responseString)) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                                data = jsonResponse.getString("data");
                                sendSimpleMsgs(getString(R.string.mbasket_order_online_with_url, data));
                            }
                        } else {
                            activity.showErrorDialog(getString(R.string.sww));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.showErrorDialog(getString(R.string.invalid_server_response));
                    }
                } else {
                    activity.showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!isAdded()) return;
                activity.hideLoadingDialog();
                activity.showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });

        */
    }



    private void fetchCustomerOrdersStockCount()
    {
        Calendar calendar = Calendar.getInstance();

        try {
            new SalesCustomerController(activity, new CustomerNetworkObserver())
                    .getSalesCustomer(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
        }
        catch (Exception ex )
        {
            activity.showErrorDialog(ErrorUtils.getFailureError(ex));
        }
    }

    class CustomerNetworkObserver implements MOMNetworkResponseListener
    {

        @Override
        public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
        {

            SalesCustStatsResp responseData = (SalesCustStatsResp) mMOMNetworkResDataStore;
            if(responseData.status ==1)
            {



                returnCountTv.setText(responseData.returnCount);
                pendingCountTv.setText(responseData.pendingCount);
                completedCountTv.setText(responseData.completedCount);
                //revenueAmt.setText(responseData.order_total);
                revenueAmt.setText( "₹ "+ Consts.getCommaFormatedStringWithDecimal(Float.parseFloat(responseData.order_total)));


            }
            else {

                String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
                activity.showErrorDialog((errorMsg));
                returnCountTv.setText("-");
                pendingCountTv.setText("-");
                completedCountTv.setText("-");
                revenueAmt.setText("-");
            }


        }
    }


}
