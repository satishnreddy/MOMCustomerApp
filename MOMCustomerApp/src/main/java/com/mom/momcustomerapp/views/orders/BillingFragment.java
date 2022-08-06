package com.mom.momcustomerapp.views.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.ItemsClient;
import com.mom.momcustomerapp.controllers.sales.api.OrdersClient;
import com.mom.momcustomerapp.controllers.stores.model.ProfileModel;
import com.mom.momcustomerapp.customviews.DashboardPeriodChooserDialogFragment;
import com.mom.momcustomerapp.customviews.adapters.GenericFragmentPagerAdapter;
import com.mom.momcustomerapp.customviews.dialogs.CalenderDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.interfaces.CalendarDialogDateSetListener;
import com.mom.momcustomerapp.interfaces.DashboardPeriodChooserListener;
import com.mom.momcustomerapp.networkservices.ParseUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mswipetech.sdk.network.util.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingFragment extends BaseFragment implements CalendarDialogDateSetListener,
        DashboardPeriodChooserListener
{


    @BindView(R.id.fragment_billing_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.fragment_billing_viewpager)
    ViewPager mViewpager;
    @BindView(R.id.fragment_billing_fab_bg)
    View mFabBg;

    private GenericFragmentPagerAdapter mAdapter;
    private boolean isFabPressed = false;
    private String mRange, mEmail;

    private PendingOrdersFragment mPendingOrdersFragment;
    private ReturnsFragment mReturnsFragment;
    private CompletedFragment mCompletedFragment;
    private Fragment mCurrentFragment;
    private FloatingActionButton mFab/*, mFabScanner, mFabAddCart*/;
    private Unbinder unbinder;
    private boolean isFabOpen = false;
    private boolean isTabSelectedOnce = false;

    public BillingFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment BillingFragment.
     */
    public static BillingFragment newInstance(boolean isFabPressed)
    {
        BillingFragment fragment = new BillingFragment();
        fragment.isFabPressed = isFabPressed;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //  ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        //fetchOrdersStockCount();

        if (isFabPressed) {
            isFabPressed = false;
        }
    }


    @Override
    public void onStop()
    {
        super.onStop();
        //  ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sales_billing, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        //((Home_Tab_Activity) getActivity()).setToolBarCollapsible(false);
        /*setUpFab();*/

        mViewpager = rootView.findViewById(R.id.fragment_billing_viewpager);
        if (mViewpager != null)
        {
            setupViewPager();

//            Bundle bundle = this.getArguments();
//
//            if(bundle != null){
//                if(bundle.get("key")=="pending")
//                {
//                    mViewpager.getCurrentItem();
//                }
//            }
        }

        mTabLayout.setupWithViewPager(mViewpager);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            if(bundle.get("key")=="pending")
            {
                TabLayout.Tab tab = mTabLayout.getTabAt(0);
                tab.select();
            }

            if(bundle.get("key")=="declined")
            {
                TabLayout.Tab tab = mTabLayout.getTabAt(1);
                tab.select();
            }
            if(bundle.get("key")=="completed")
            {
                TabLayout.Tab tab = mTabLayout.getTabAt(2);
                tab.select();
            }
        }

        return rootView;
    }

    private void setupViewPager()
    {
        mAdapter = new GenericFragmentPagerAdapter(getActivity(), getChildFragmentManager());
        mPendingOrdersFragment = PendingOrdersFragment.newInstance();
        mReturnsFragment = ReturnsFragment.newInstance();
        mCompletedFragment = CompletedFragment.newInstance();
        mAdapter.addFragment(mPendingOrdersFragment, getString(R.string.orders_title_tab_pending));
        mAdapter.addFragment(mReturnsFragment, getString(R.string.orders_title_tab_returned));
        mAdapter.addFragment(mCompletedFragment, getString(R.string.orders_title_tab_completed));

        mViewpager.setAdapter(mAdapter);
//        mViewpager.getCurrentItem();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if (!isTabSelectedOnce) {
                    isTabSelectedOnce = true;
                }
                else {
                    //fetchOrdersStockCount();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (mPendingOrdersFragment.isLoaded)
                        {

                            mPendingOrdersFragment.loadBills();
                        }
                        break;
                    case 1:
                        if (mReturnsFragment.isLoaded)
                        {

                            mReturnsFragment.loadReturns();
                        }
                        break;
                    case 2:
                        if (mCompletedFragment.isLoaded)
                        {

                            mCompletedFragment.loadBills();
                        }
                        break;
                }
            }
        });
        mCurrentFragment = mPendingOrdersFragment;
    }

    private void setUpFab()
    {
        /*mFab = getActivity().findViewById(R.id.fragment_home_fab);
        mFabAddCart = getActivity().findViewById(R.id.fragment_home_fab_add_to_cart);
        mFabScanner = getActivity().findViewById(R.id.fragment_home_fab_scanner);
        mFab.show();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFabOpen)
                    showFabMenu();
                else
                    closeFabMenu();
            }
        });
        mFab.setVisibility(View.VISIBLE);


        mFabAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
                Intent intent = new Intent(getActivity(), AddProductToCartActivity.class);
                startActivityForResult(intent, Consts.REQUEST_CODE_CART);
            }
        });

        mFabScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
                Intent intent = new Intent(getActivity(), OrderScanProductActivity.class);
                startActivityForResult(intent, Consts.REQUEST_CODE_CART);
            }
        });*/
    }

    private void showFabMenu()
    {
        isFabOpen = true;
        /*if (mFabScanner != null) mFabScanner.setVisibility(View.VISIBLE);
        if (mFabAddCart != null) mFabAddCart.setVisibility(View.VISIBLE);*/
        if (mFabBg != null) mFabBg.setVisibility(View.VISIBLE);

        mFabBg.animate().alpha(1f);
        mFab.animate().rotation(135f);
        /*mFabScanner.animate()
                .translationY(-(getResources().getDimension(R.dimen.fab_translate_max)))
                .rotation(0f);
        mFabAddCart.animate()
                .translationY(-(getResources().getDimension(R.dimen.fab_translate_min)))
                .rotation(0f);*/
    }

    private void closeFabMenu()
    {
        isFabOpen = false;
        mFab.animate().rotation(0f);
        mFabBg.animate().alpha(0f);
        /*mFabScanner.animate()
                .translationY(0f)
                .rotation(720f);
        mFabAddCart.animate()
                .translationY(0f)
                .rotation(720f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isFabOpen) {
                    if (mFabBg != null) {
                        mFabBg.setVisibility(View.GONE);
                        mFabScanner.setVisibility(View.GONE);
                        mFabAddCart.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/
    }



    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_invoice, menu);
        MenuItem menuExport = menu.findItem(R.id.menu_invoice_export);
        MenuItem menuSettings = menu.findItem(R.id.menu_invoice_setting);
        MenuItem menuHelp = menu.findItem(R.id.menu_invoice_help);

        if (MventryApp.getInstance().getUserType().equalsIgnoreCase("Expert")) {
            menuExport.setVisible(true);
            menuSettings.setVisible(true);

            menuExport.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    exportClicked();
                    return false;
                }
            });

            menuSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    settingsClicked();
                    return false;
                }
            });
        } else {
            menuExport.setVisible(false);
            menuSettings.setVisible(false);
        }

        menuHelp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                helpClicked();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    private void exportClicked()
    {
        String profile = MOMApplication.getSharedPref().getProfile();
        ProfileModel profileModel = new Gson().fromJson(profile, ProfileModel.class);

        if (!TextUtils.isEmpty(profileModel.getUserProfile().getEmail())) {
            mEmail = profileModel.getUserProfile().getEmail();
            showPeriodChooserDialog();
        } else {
            showErrorDialog("Warning", "No email address found in your Profile.");
        }
    }

    private void settingsClicked()
    {
        /*
        Intent intent = new Intent(getActivity(), InvoiceSettingsActivity.class);
        startActivity(intent);*/
    }

    private void helpClicked()
    {
        /*Intent intentHelp = new Intent(getActivity(), HelpActivity.class);
        intentHelp.putExtra(Consts.EXTRA_HELP_INDEX, Consts.EXTRA_HELP_BILLING);
        startActivity(intentHelp);*/
    }

    private void exportInvoicereport()
    {
        showLoadingDialog();
        OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());
        Call<String> call = ordersClient.generateInvoiceReport(mRange, mEmail);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    responseString = responseString.replaceAll("\\n", "");
                    try {
                        if (!TextUtils.isEmpty(responseString)) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            if (jsonResponse.has("status")) {
                                if (jsonResponse.get("status").toString().equalsIgnoreCase("success")) {
                                    showErrorDialog("Success", "Report is sent to your email address.");
                                }
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog("Error", "Something went wrong : Invalid Response");
                    }
                } else {
                    try {
                        Response<String> response1 = Response.success(response.errorBody().string().replace("\n", ""));
                        parseError(response1, true);
                    } catch (Exception e) {
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
                if (getActivity() != null) {
                    hideLoadingDialog();
                    if (t instanceof SocketTimeoutException || t instanceof IOException) {
                        showErrorDialog("Network Error", "No Network. Please check connection");
                    } else {
                        showErrorDialog("Error", "Something went wrong : " + t.getMessage());
                             Log.e("ERROR", "onFailure: Something went wrong", t);
                    }
                }
            }
        });
    }

    private void exportInvoicereportRange(String startDate, String endDate)
    {
        showLoadingDialog();
        OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());
        Call<String> call = ordersClient.generateInvoiceReportRange(startDate, endDate, mEmail);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    responseString = responseString.replaceAll("\\n", "");
                    try {
                        if (!TextUtils.isEmpty(responseString)) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            if (jsonResponse.has("status")) {
                                if (jsonResponse.get("status").toString().equalsIgnoreCase("success")) {
                                    showErrorDialog("Success", "Report is sent to your email address.");
                                }
                            }
                        } else {
                            if (response.message() != null) {
                                showErrorDialog("Error", "Something went wrong: " + response.message());
                            } else {
                                showErrorDialog("Error", "Something went wrong: " + response.body());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog("Error", "Something went wrong : Invalid Response");
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
                if (getActivity() != null) {
                    hideLoadingDialog();
                    if (t instanceof SocketTimeoutException || t instanceof IOException) {
                        showErrorDialog("Network Error", "No Network. Please check connection");
                    } else {
                        showErrorDialog("Error", "Something went wrong : " + t.getMessage());
                        Log.e("ERROR", "onFailure: Something went wrong", t);
                    }
                }
            }
        });
    }


    public void showPeriodChooserDialog()
    {
        DashboardPeriodChooserDialogFragment fragment = (DashboardPeriodChooserDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(DashboardPeriodChooserDialogFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = DashboardPeriodChooserDialogFragment.newInstance(this, mRange);
            fragment.setCancelable(true);
            getActivity().getSupportFragmentManager().beginTransaction().add(fragment, DashboardPeriodChooserDialogFragment.FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }

    public void showCalendarDialog()
    {
        CalenderDialogFragment fragment = (CalenderDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(CalenderDialogFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = CalenderDialogFragment.newInstance(this);
            fragment.setCancelable(true);
            getActivity().getSupportFragmentManager().beginTransaction().add(fragment, CalenderDialogFragment.FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }

    @Override
    public void onPeriodSelection(int selectedPeriod)
    {
        mRange = "today";
        switch (selectedPeriod) {
            case 1:
                mRange = "today";
                break;
            case 2:
                mRange = "yesterday";
                break;
            case 3:
                mRange = "last 7days";
                break;
            case 4:
                mRange = "last 30days";
                break;
            case 5:
                mRange = "this Month";
                break;
            case 6:
                mRange = "last Month";
                break;
            case 7:
                mRange = "custom";
                showCalendarDialog();
                break;
            default:
                mRange = "today";
                break;
        }

        if (selectedPeriod != 7) {
            exportInvoicereport();
        }
    }

    @Override
    public void onDatesSelected(List<Date> selectedDates)
    {
        if (selectedDates != null && !selectedDates.isEmpty()) {
            Date sDate = selectedDates.get(0);
            Date eDate = selectedDates.get(selectedDates.size() - 1);

            String startDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(sDate);
            String endDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(eDate);
            //Log.d("Nish", "Dates : " + startDate + " : " + endDate);
            exportInvoicereportRange(startDate, endDate);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Consts.REQUEST_CODE_CART) {
            if (resultCode == Activity.RESULT_OK) {
                mAdapter.getItem(mViewpager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView()
    {
        if (isFabOpen) {
            closeFabMenu();
        }
        super.onDestroyView();
        unbinder.unbind();
    }


    public void myUpdatePageTitle(int pagePosition, String textToSet)
    {
        if (textToSet == null || textToSet.isEmpty()) return;
        TabLayout.Tab tab = mTabLayout.getTabAt(pagePosition);
        if (tab != null) {
            boolean isCountZero = textToSet.equals("0");
            String preText = "";
            switch (pagePosition){
                case 0:
                    preText = isCountZero ? getString(R.string.orders_title_tab_pending):
                            getString(R.string.orders_title_tab_pending_with_count, textToSet);
                    break;
                case 1:
                    preText = isCountZero ? getString(R.string.orders_title_tab_returned) :
                            getString(R.string.orders_title_tab_returned_with_count, textToSet);
                    break;
                case 2:
                    preText = isCountZero ? getString(R.string.orders_title_tab_completed) :
                            getString(R.string.orders_title_tab_completed_with_count, textToSet);
                    break;
            }
            tab.setText(preText);
        }
    }


    private void fetchOrdersStockCount()
    {
        ItemsClient itemsClient = ServiceGenerator.createService(ItemsClient.class, MOMApplication.getInstance().getToken());
        Call<String> call = itemsClient.getStatsCount(MOMApplication.getInstance().getStoreId(),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime()));


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()){
                    String responseString = response.body();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseString);
                        JSONArray data = jsonResponse.getJSONArray("data");
                        if (data.length() != 0){
                            String pendingCount = data.getJSONObject(0).optString("pendingCount");
                            String acceptedCount = data.getJSONObject(0).optString("acceptedCount");
                            if (ParseUtils.isValidString(pendingCount) && ParseUtils.isValidString(acceptedCount)){
                                try {
                                    int pendingInInt = Integer.parseInt(pendingCount);
                                    int acceptedInInt = Integer.parseInt(acceptedCount);
                                    myUpdatePageTitle(0,  String.valueOf(pendingInInt + acceptedInInt));
                                }
                                catch (NumberFormatException e){
                                    e.printStackTrace();
                                }
                            }

                            String returnCount = data.getJSONObject(0).optString("returnCount","");
                            if (ParseUtils.isValidString(returnCount)){
                                myUpdatePageTitle(1, returnCount);
                            }
                            String completedCount = data.getJSONObject(0).optString("completedCount","");
                            if (ParseUtils.isValidString(completedCount)){
                                myUpdatePageTitle(2, completedCount);

                                try{
                                    if(completedCount != null && completedCount.length() > 0){
                                        int cnt = Integer.parseInt(completedCount);
                                        if(cnt >= 2) {
                                            MOMApplication.getSharedPref().setSecondOrderCompleted();
                                        }
                                    }
                                }catch (Exception e){}
                            }
                        }
                    }
                    catch (JSONException je){
                        je.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }






}
