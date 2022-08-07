package com.mom.momcustomerapp.views.home;


import static com.mom.momcustomerapp.data.application.Consts.EXTRA_CATEGORY_ID;
import static com.mom.momcustomerapp.data.application.Consts.EXTRA_KEY_OPEN_ON_HOME;
import static com.mom.momcustomerapp.data.application.Consts.EXTRA_SUB_CATEGORY_ID;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_CHANGE_LANGUAGE;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_DELIVERY;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_MBASKET;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_PROFILE;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_REPORT_SETTINGS;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_STORE_DETAILS;
import static com.mom.momcustomerapp.data.application.Consts.FRAGMENT_STORE_SETTING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import com.mom.momcustomerapp.controllers.stores.model.ProfileModel;
import com.mom.momcustomerapp.controllers.updateapp.VersionModel;
import com.mom.momcustomerapp.controllers.updateapp.views.CheckUpdateDialogFragment;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.ErrorModel;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.login.SplachScreenActivity;
import com.mom.momcustomerapp.views.orders.BillingFragment;
import com.mom.momcustomerapp.views.products.ProductsInStockFragment;
import com.mom.momcustomerapp.views.sales.SalesCartDetailsActivity;
import com.mom.momcustomerapp.views.settings.SettingsFragmentNew;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.R;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Tab_Activity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener
{

    BottomNavigationView bottomNavigationView;
    private static final int PROFILE_SETTING = 100000;

    @BindView(R.id.main_container)
    FrameLayout mMainContainer;
    @BindView(R.id.fragment_home_fab)
    FloatingActionButton mFab;

    /*@BindView(R.id.fragment_home_fab_scanner)
    FloatingActionButton mFabScanner;
    @BindView(R.id.fragment_home_fab_add_to_cart)
    FloatingActionButton mFabAddToCart;
     */

    private BroadcastReceiver cartCountReceiver;
    private Activity mActivity;
    private static final int HOME_DRAWER_ID = 14;
    private int initialFragmentToOpen = HOME_DRAWER_ID;
    private String titleToSet = "Select Product";



    HomeFragment home = new HomeFragment();
    SalesCartDetailsActivity cart = new SalesCartDetailsActivity();
    BillingFragment orders = new BillingFragment();
    SettingsFragmentNew settings  = new SettingsFragmentNew();
    ProductsInStockFragment product = new ProductsInStockFragment();


    public static Intent getStartIntent(Context context, int codeOfViewToOpen)
    {
        /*Intent intent = new Intent(context, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_KEY_OPEN_ON_HOME, codeOfViewToOpen);
        intent.putExtras(bundle);
        return intent;*/
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);
        ButterKnife.bind(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        mActivity = this;
        mMainContainer = (FrameLayout) findViewById(R.id.main_container);
        Intent intent = getIntent();
        if (intent != null)
        {
            int intExtra = intent.getIntExtra(EXTRA_KEY_OPEN_ON_HOME, -1);
            if (intExtra >= 0)
            {
                initialFragmentToOpen = intExtra;
            }
        }



    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {

            case R.id.home:

                mFab.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, home).commit();
                return true;

            case R.id.orders:
                mFab.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, orders).commit();
                return true;

            case R.id.customer:
                mFab.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, cart).commit();
                return true;


            case R.id.products:

                mFab.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, product).commit();

                return true;


            case R.id.settings:
                mFab.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, settings).commit();
                return true;



        }
        return false;
    }


    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        //add the values which need to be saved from the drawer to the bundle

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartCountReceiver);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        LocalBroadcastManager.getInstance(this).registerReceiver(cartCountReceiver, new IntentFilter(Consts.BROADCAST_UPDATE_CART));
        super.onResume();
    }

    public void logOut()
    {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Home_Tab_Activity.super.onBackPressed();
                        MOMApplication.getSharedPref().setLoggedOut();
                        Intent intent = new Intent(getApplicationContext(), SplachScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

}

