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
import com.mom.momcustomerapp.views.products.ProductsInStockFragment;
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
        CameraButton.CameraButtonResultListener ,BottomNavigationView.OnNavigationItemSelectedListener
{

    BottomNavigationView bottomNavigationView;
    private static final int PROFILE_SETTING = 100000;

    @BindView(R.id.main_container)
    FrameLayout mMainContainer;
    @BindView(R.id.fragment_home_fab)
    FloatingActionButton mFab;

    @BindView(R.id.fragment_home_fab_scanner)
    FloatingActionButton mFabScanner;
    @BindView(R.id.fragment_home_fab_add_to_cart)
    FloatingActionButton mFabAddToCart;

    private Drawer mDrawer = null;
    private AccountHeader mHeaderResult = null;
    private CameraButton mCameraButton;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private Fragment mCurrentFragment;

    private boolean isBackPressed = false;
    private boolean fabBillingPressed = false;
    String title = "";
    private BroadcastReceiver cartCountReceiver;
    private ProfileModel mProfileModel;
    private Activity mActivity;
    private int menuTitlecolor;
    private Typeface boldTypeface;
    private static final int HOME_DRAWER_ID = 14;
    private View appLogo;
    private int initialFragmentToOpen = HOME_DRAWER_ID;
    private String titleToSet = "Select Product";



    HomeFragment home = new HomeFragment();
    //CustomersFragment  customer = new CustomersFragment();
    //BillingFragment orders = new BillingFragment();
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

        //new MventryCart();


        //setupToolBar();
        init();
        checkPreConditions(savedInstanceState);
        checkUpdates();

    }


    public void storedetails()
    {
        /*boolean replace = false;
        FRAGMENT_STORE_DETAILS:// Store Details
        if (!(mCurrentFragment instanceof StoreDetailsFragment))
        {
            mCurrentFragment = new StoreDetailsFragment();
            replace = true;

        }*/

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

            /*case R.id.orders:
                mFab.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, orders).commit();
                return true;
                */
            /*case R.id.customer:
                mFab.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, customer).commit();
                return true;
                */


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
        if (mDrawer != null) {
            outState = mDrawer.saveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Set up the {@link Toolbar}.
     */
    private void setupToolBar()
    {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_home);
        mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        /*storesNameTitle = findViewById(R.id.toolbar_home_tv_stores_name);*/


        //mCartCount =  mToolbar.findViewById(R.id.toolbar_tv_cartcount);
        //mNotifCount = mToolbar.findViewById(R.id.toolbar_tv_notifcount);
//        appLogo = mToolbar.findViewById(R.id.ll_mbasket_logo);

        cartCountReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Log.d("Nish", "Broadcast Received : Update Count");
                updateCartCount();
            }
        };

        /*View viewById = mToolbar.findViewById(R.id.ll_stores_icon);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ViewInStoreActivity.class));
            }
        });*/
        /*getSupportActionBar().setCustomView(R.layout.activity_receive_stock);*/
        getSupportActionBar().setDisplayShowTitleEnabled(true);


    }

    public ActionBar getActionToolBar() {
        return mActionBar;
    }

    private void init() {
        /*
        String profile = ShopxieSharedPreferences.getInstance().getProfile();
        if (!TextUtils.isEmpty(profile)) {
            mProfileModel = new Gson().fromJson(profile, ProfileModel.class);
        }

         */

        /*storesNameTitle.setText(getString(R.string.app_name_meraonlinestore));
        try {
            ProfileModel.StoreProfileModel storeProfileModel = mProfileModel.getStoreFromId(MventryApp.getInstance().getCurrentStoreId());
            if (storeProfileModel != null && !TextUtils.isEmpty(storeProfileModel.getStoreName())) {
                String storeName = storeProfileModel.getStoreName();
                storesNameTitle.setText(storeName);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

        /*
        if (mProfileModel != null && mProfileModel.getStores()!= null && mProfileModel.getStores().size() > 0) {
            if (mProfileModel.getStores().get(0).isOwner())
            {
                MventryApp.getInstance().setUserType("Expert");
            } else {
                if (TextUtils.isEmpty(mProfileModel.getStores().get(0).getAccessType())) {
                    MventryApp.getInstance().setUserType("Basic");
                } else {
                    MventryApp.getInstance().setUserType(mProfileModel.getStores().get(0).getAccessType());
                }
            }



        }
        */
    }

    private void checkPreConditions(Bundle savedInstanceState)
    {
        setUpDrawer(savedInstanceState);
        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            mDrawer.setSelection(initialFragmentToOpen, false);
        }
        //updateCartCount();
    }

    private void setUpDrawer(Bundle savedInstanceState)
    {
        menuTitlecolor = getResources().getColor(R.color.color_258430);
        boldTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        mCameraButton = new CameraButton(mActivity);

        if (mProfileModel == null) {
            String profile = MOMApplication.getSharedPref().getProfile();
            if (!TextUtils.isEmpty(profile)) {
                mProfileModel = new Gson().fromJson(profile, ProfileModel.class);
            }
        } else {
            if (mProfileModel.getUserProfile() == null) {
                Log.e("Nish", "User Profile is null");
            }
            if (TextUtils.isEmpty(mProfileModel.getUserProfile().getName())) {
                Log.e("Nish", "User Profile Name is empty");
            }
            mCameraButton.setImageName(mProfileModel.getUserProfile().getName().replace(" ", "_") + ".jpeg");
        }

        if (mProfileModel != null) {
            ProfileModel.StoreProfileModel storeProfileModel = mProfileModel.getStoreFromId(MOMApplication.getInstance().getStoreId());

            //initialize and create the image loader logic
            DrawerImageLoader.init(new AbstractDrawerImageLoader()
            {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder)
                {
                    Glide.with(imageView.getContext())
                            .load(uri)
                            .placeholder(placeholder)
                            .listener(new RequestListener<Uri, GlideDrawable>()
                            {
                                @Override
                                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    e.printStackTrace();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    //Log.d("Nish", "onResourceReady: " + model.toString());
                                    return false;
                                }
                            })
                            .into(imageView);
                }

                @Override
                public void cancel(ImageView imageView) {
                    Glide.clear(imageView);
                }

                @Override
                public Drawable placeholder(Context ctx, String tag) {
                    if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                        return DrawerUIUtils.getPlaceHolder(ctx);
                    } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                        return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.colorAccent).sizeDp(56);
                    } else if ("customUrlItem".equals(tag)) {
                        return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.colorAccent).sizeDp(56);
                    }
                    return super.placeholder(ctx, tag);
                }
            });

            AccountHeaderBuilder accountHeaderBuilder = new AccountHeaderBuilder();
            accountHeaderBuilder.withActivity(this);
            accountHeaderBuilder.withCompactStyle(true);
            accountHeaderBuilder.withHeaderBackground(new ColorDrawable(getResources().getColor(R.color.white)));
            accountHeaderBuilder.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(View view, IProfile profile, boolean current)
                {

                    if (profile instanceof IDrawerItem)
                    {

                        if (!mDrawer.switchedDrawerContent()) {
                            mDrawer.setSelection(mDrawer.getDrawerItem(HOME_DRAWER_ID));
                        }
                        //MventryCart.loadCartContents();
                    }
                    //false if you have not consumed the event and it should close the drawer
                    return false;
                }
            });

            accountHeaderBuilder.withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                @Override
                public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                    if (current) {

                    }
                    //false if you have not consumed the event and it should close the drawer
                    return false;
                }

                @Override
                public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                    return false;
                }
            });

            for (ProfileModel.StoreProfileModel storeModel : mProfileModel.getStores())
            {
                String imageUrl = "";
                if (storeModel.getImage() != null && storeModel.getImage().size() > 0)
                {
                    ProfileModel.ImageModel imageModel = storeModel.getImage().get(0);
                    if (imageModel != null) {
                        if (!TextUtils.isEmpty(imageModel.getImagePath())) {
                            imageUrl = imageModel.getImagePath();
                        }
                    }
                }
                if (TextUtils.isEmpty(imageUrl)) {
                    accountHeaderBuilder.addProfiles(new ProfileDrawerItem()
                            .withName(storeModel.getStoreName())
                            .withEmail(storeModel.getStoreCity())
                            .withIcon(DrawerUIUtils.getPlaceHolder(mActivity))
                            .withNameShown(true)
                            .withIdentifier(Long.parseLong(storeModel.getStoreId()))
                    );
                } else {
                    accountHeaderBuilder.addProfiles(new ProfileDrawerItem()
                            .withName(storeModel.getStoreName())
                            .withEmail(storeModel.getStoreCity())
                            .withIcon(imageUrl)
                            .withNameShown(true)
                            .withIdentifier(Long.parseLong(storeModel.getStoreId()))
                    );
                }
            }

            boolean isOwner = false;
            for (ProfileModel.StoreProfileModel storeModel : mProfileModel.getStores()) {
                if (storeModel.isOwner()) {
                    isOwner = true;
                }
            }

//            if (isOwner) {
//                accountHeaderBuilder.addProfiles(new ProfileSettingDrawerItem()
//                        .withName("Add Store").withDescription("Add New Store")
//                        .withIcon(R.drawable.ic_add_36dp)
//                        .withIdentifier(PROFILE_SETTING));
//            }

            mHeaderResult = accountHeaderBuilder.build();

            mDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withDrawerWidthDp(0)
                    /*.withToolbar(mToolbar)*/
                    .withHasStableIds(true)
                    .withDrawerItems(generateDrawerItems())
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                           // selectDrawerItem(drawerItem);
                            return false;
                        }
                    })
                    .withHeader(R.layout.material_drawer_new_header/*mHeaderResult.getView()*/)
                    /*.withFooter(R.layout.material_drawer_footer)*/
                    .withSavedInstance(savedInstanceState)
                    .withShowDrawerOnFirstLaunch(false)
                    .withFullscreen(false)
                    .build();

            mHeaderResult.setDrawer(mDrawer);
            /*mHeaderResult.setActiveProfile(Long.parseLong(MventryApp.getInstance().getCurrentStoreId()));*/

            ((ImageView) mHeaderResult.getView().findViewById(R.id.material_drawer_account_header_text_switcher)).setImageResource(R.drawable.ic_dropdown_24dp);


        } else {

            mDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(mToolbar)
                    .withHasStableIds(true)
                    .withDrawerItems(generateDrawerItems())
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                           // selectDrawerItem(drawerItem);
                            return false;
                        }
                    })
                    .withSavedInstance(savedInstanceState)
                    .withShowDrawerOnFirstLaunch(true)
                    .withFullscreen(false)
                    .build();

            View header = getLayoutInflater().inflate(R.layout.navigationview_header, mDrawer.getDrawerLayout(), false);
            //header.findViewById(R.id.navigationview_header_iv_avatar).setVisibility(View.GONE);
            //header.findViewById(R.id.navigationview_header_tv_avatar).setVisibility(View.VISIBLE);
            //((TextView)header.findViewById(R.id.navigationview_header_tv_avatar)).setText();
            mDrawer.setHeader(header);

        }

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);

        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            mDrawer.setSelection(initialFragmentToOpen);
        }

        /*mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);*/
        /*mDrawer.getActionBarDrawerToggle().setHomeAsUpIndicator(R.color.transparent);*/
    }

    /*public void updateDrawer() {
        mDrawer.removeAllItems();
        long currentItem = mDrawer.getCurrentSelection();
        boolean isSelectionPresent = false;
        mDrawer.setItems(generateDrawerItems());
        for (IDrawerItem drawerItem : mDrawer.getDrawerItems()) {
            if (!isSelectionPresent) {
                if (currentItem == drawerItem.getIdentifier()) {
                    isSelectionPresent = true;
                }
            }
        }

        if (isSelectionPresent) {
            mDrawer.setSelection(currentItem);
        } else {
            mDrawer.setSelection(HOME_DRAWER_ID);
        }
    }*/

    private void updateDrawerHeader()
    {

        String storeProfileImage = "";
        ProfileModel.StoreProfileModel storeModel = mProfileModel.getStoreFromId(MOMApplication.getInstance().getStoreId());

        if (storeModel.getImage() != null && storeModel.getImage().size() > 0) {
            ProfileModel.ImageModel imageModel = storeModel.getImage().get(0);
            if (imageModel != null) {
                if (!TextUtils.isEmpty(imageModel.getImagePath())) {
                    storeProfileImage = imageModel.getImagePath();
                }
            }
        }

        long profileIdentifier = mHeaderResult.getActiveProfile().getIdentifier();
        IProfile profile = new ProfileDrawerItem()
                .withName(storeModel.getStoreName())
                .withEmail(storeModel.getStoreCity())
                .withIcon(storeProfileImage)
                .withNameShown(true)
                .withIdentifier(profileIdentifier);

        mHeaderResult.updateProfile(profile);
        mHeaderResult.setActiveProfile(profileIdentifier);

        if (!TextUtils.isEmpty(storeProfileImage)) {
//            Glide.with(mActivity).load(storeProfileImage).fitCenter().into(mHeaderResult.getHeaderBackgroundView());
//            mHeaderResult.getView().findViewById(R.id.material_drawer_account_header_blur_view).setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<IDrawerItem> generateDrawerItems()
    {

        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();

        /*

        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_MBASKET).withTag(FRAGMENT_MBASKET).withSelectable(true));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_MBASKET).withTag(FRAGMENT_MBASKET).withSelectable(true));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_CUSTOMERS).withTag(FRAGMENT_CUSTOMERS).withSelectable(true));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_ORDERS).withTag(FRAGMENT_ORDERS).withSelectable(true));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_PROFILE).withTag(FRAGMENT_PROFILE).withSelectable(true));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_VOUCHER).withTag(FRAGMENT_VOUCHER).withSelectable(true));
        */
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_DELIVERY).withTag(FRAGMENT_DELIVERY).withSelectable(false));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_STORE_SETTING).withTag(FRAGMENT_STORE_SETTING).withSelectable(false));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_STORE_DETAILS).withTag(FRAGMENT_STORE_DETAILS).withSelectable(false));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_REPORT_SETTINGS).withTag(FRAGMENT_REPORT_SETTINGS).withSelectable(false));
        drawerItems.add(getNewDrawerIcon().withName("").withIdentifier(FRAGMENT_CHANGE_LANGUAGE).withTag(FRAGMENT_CHANGE_LANGUAGE).withSelectable(false));

        boolean showOldMenus = false;
        if (showOldMenus){// Add debug point here to make it true.
            drawerItems.add(new PrimaryDrawerItem().withName("").withTag("").withSelectable(false));
            drawerItems.add(new PrimaryDrawerItem().withName(R.string.navigation_dashboard).withIdentifier(1).withTag(getString(R.string.navigation_dashboard)).withSelectable(true));
            drawerItems.add(new PrimaryDrawerItem().withName(R.string.navigation_settings).withIdentifier(11).withTag(getString(R.string.navigation_settings)).withSelectable(true));
        }


        return drawerItems;
    }

    private PrimaryDrawerItem getNewDrawerIcon(){
        return new PrimaryDrawerItem().withTextColor(menuTitlecolor)
                .withTypeface(boldTypeface)
                .withSelectedTextColor(menuTitlecolor);
        /*.withName(R.string.navigation_home_mbasket).withIdentifier(14).withTag(getString(R.string.navigation_home_mbasket))*//*.withIcon(R.drawable.ic_settings_24dp)*//*.withSelectable(true)*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.d("Nish", "onRestart: updateCartCount");
        updateCartCount();
    }

    /*public void onFabProductClicked(View view) {
//        Intent intent = new Intent();
//        intent.setClass(this, AddProductSelectCategoryActivity.class);
//        intent.putExtra(Consts.EXTRA_FROM_ACTIVITY, "Dashboard");
//        startActivity(intent);
    }

    public void onFabOrderClicked(View view) {
        fabBillingPressed = true;
        selectDrawerItemByTag(getString(R.string.navigation_orders));

    }

    public void onFabCustomerClicked(View view) {
        Intent intent = new Intent();
        intent.setClass(this, AddCustomerActivity.class);
        startActivity(intent);
    }

    public void onCartClick(View view) {
        if (MventryCart.getCartSize() > 0) {
            Intent intent = new Intent(this, PlaceOrderActivity.class);
            startActivityForResult(intent, Consts.REQUEST_CODE_CART);
        } else {
            MventryCart.loadCartContents();
        }
    }

    public void onNotifClicked(View view) {

    }*/

    private void updateCartCount()
    {

    }

    public void selectDrawerItemByTag(int tag) {
        mDrawer.setSelection(mDrawer.getDrawerItem(tag));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CameraButton.TAKE_PICTURE_REQUEST_CODE || requestCode == CameraButton.SELECT_PICTURE_REQUEST_CODE ||
                requestCode == CameraButton.AFTER_IMAGE_CROPPING) {
            mCameraButton.handleActivityResults(requestCode, resultCode, data);
        } else if (requestCode == Consts.REQUEST_CODE_CREATE_STORE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean isNotified = data.getBooleanExtra(Consts.EXTRA_ISNOTIFIED, false);
                if (isNotified) {
                    finish();
                } else {
                    checkPreConditions(null);
                }
            } else {
                finish();
            }
        } else if (requestCode == Consts.REQUEST_CODE_CART) {
            //MventryCart.loadCartContents();
        /*} else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }

         */
        } else if (requestCode == Consts.REQUEST_CODE_PROFILE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean isUpdated = data.getBooleanExtra(Consts.EXTRA_ISUPDATED, false);
                if (isUpdated) {
                    mProfileModel = data.getParcelableExtra(Consts.EXTRA_PROFILE);
                    updateDrawerHeader();
                }
            }
        } else if (requestCode == Consts.REQUEST_CODE_CREATE_MULTI_STORE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean isStoreAdded = data.getBooleanExtra(Consts.EXTRA_MULTI_STORE_ADDED, false);
                if (isStoreAdded) {
                    //getProfile();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackPressed()
    {
        /*if (mDrawer != null && !mDrawer.isDrawerOpen()) {
            mDrawer.openDrawer();
            return;
        }*/
        /*
        if (mCurrentFragment instanceof HomeMbasketFragment) finish();
        else selectDrawerItemByTag(FRAGMENT_MBASKET);
        */
        /*super.onBackPressed();*/
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

    @Override
    public void imageSaved(Uri uri) {
        MOMApplication.getSharedPref().setProfileImage(uri.getPath());
        mHeaderResult.getActiveProfile().withIcon(uri.getPath());
        mHeaderResult.updateProfile(mHeaderResult.getActiveProfile());
    }


    private void checkAddedProfile(ProfileModel profileModel)
    {
        ProfileModel.StoreProfileModel recentlyAddedStore = null;
        if (mProfileModel == null) {
            String profile = MOMApplication.getSharedPref().getProfile();
            if (!TextUtils.isEmpty(profile)) {
                mProfileModel = new Gson().fromJson(profile, ProfileModel.class);
            }
        }

        ArrayList<ProfileModel.StoreProfileModel> existingStores = new ArrayList<>(mProfileModel.getStores());
        ArrayList<ProfileModel.StoreProfileModel> newStores = new ArrayList<>(profileModel.getStores());

        for (ProfileModel.StoreProfileModel store : newStores) {
            String storeId = store.getStoreId();
            for (ProfileModel.StoreProfileModel oldstore : existingStores) {
                if (!oldstore.getStoreId().equalsIgnoreCase(storeId)) {
                    recentlyAddedStore = store;
                    break;
                }
            }
        }

        if (recentlyAddedStore != null) {
            IProfile newProfile = new ProfileDrawerItem()
                    .withName(recentlyAddedStore.getStoreName())
                    .withEmail(recentlyAddedStore.getStoreCity())
                    .withIcon(DrawerUIUtils.getPlaceHolder(mActivity))
                    .withNameShown(true)
                    .withIdentifier(Long.parseLong(recentlyAddedStore.getStoreId()));

            if (mHeaderResult.getProfiles() != null) {
                //we know that there is 1 add store elements. set the new profile above it ;)
                mHeaderResult.addProfile(newProfile, mHeaderResult.getProfiles().size() - 1);
            } else {
                mHeaderResult.addProfiles(newProfile);
            }

            mHeaderResult.toggleSelectionList(mActivity);
            ServiceGenerator.resetServices();
            String storeId = newProfile.getIdentifier() + "";
            /*MventryApp.getInstance().setCurrentStoreId(storeId);*/
            mHeaderResult.setActiveProfile(newProfile);
            mDrawer.setSelection(mDrawer.getDrawerItem(HOME_DRAWER_ID));

            mProfileModel = profileModel;
        }


        String profile = new Gson().toJson(profileModel, ProfileModel.class);
        MOMApplication.getSharedPref().setProfile(profile);
        MOMApplication.getSharedPref().setProfileSetup(true);
    }

//    public void setToolBarCollapsible(boolean isCollapsible) {
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
//        if (isCollapsible) {
//            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
//        } else {
//            params.setScrollFlags(0);
//        }
//        mAppBarLayout.requestLayout();
//    }

    public void checkUpdates() {
        String url;
        if (!MOMApplication.IS_PROD) {
            url = getString(R.string.update_dev);

        } else {
            url = getString(R.string.update_prod);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseString = response.body().string();
                    //Log.i("Nish", "Response : " + responseString);
                    VersionModel versionModel = new Gson().fromJson(responseString, VersionModel.class);
                    if (versionModel != null) {
                        showUpdeteDialog(versionModel);
                    }
                } else {
                    //Log.i("Nish", "Update failed");
                    //Log.i("Nish", "Code : " + response.code());
                    //Log.i("Nish", "Message : " + response.message());
                    if (response.body() != null) {
                        //Log.i("Nish", "Body : " + response.body().string());
                    }
                }
            }
        });
    }

    public void showUpdeteDialog(VersionModel versionModel) {
        try {
            int currentVersionCode;
            int installedVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

            currentVersionCode = versionModel.getCurrentVersion();
            int forceInstallCode = versionModel.getForcedVersions();
            boolean isForced = false;

            //Log.i("Nish", "installedVersionCode : " + installedVersionCode);
            //Log.i("Nish", "currentVersionCode : " + currentVersionCode);
            //Log.i("Nish", "forceInstallCode : " + forceInstallCode);

            if (installedVersionCode < currentVersionCode) {
                if (installedVersionCode < forceInstallCode) {
                    isForced = true;
                } else if (installedVersionCode != 0 && installedVersionCode < currentVersionCode) {
                    isForced = false;
                }

                final CheckUpdateDialogFragment updateDialogFragment = new CheckUpdateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("versionModel", versionModel);
                bundle.putBoolean("forced", isForced);
                updateDialogFragment.setArguments(bundle);

                updateDialogFragment.setFileDownloadListener(new CheckUpdateDialogFragment.FileDownloadListener() {
                    @Override
                    public void onFileDownloadClicked() {
                        Toast.makeText(mActivity, "New version download started. Please install it and open app again.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(CheckUpdateDialogFragment.FRAGMENT_TAG);
                if (prev != null) {
                    ft.remove(prev);
                }
                updateDialogFragment.show(ft, CheckUpdateDialogFragment.FRAGMENT_TAG);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    public void selectDrawerItem(IDrawerItem drawerItem)
    {
        if (drawerItem != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            boolean replace = false;
            /*if (drawerItem.getTag() != null) {
                title = drawerItem.getTag().toString();
                if (getSupportActionBar()!= null){
                    getSupportActionBar().setTitle(title);
                }
            }*/

            mFab.setVisibility(View.VISIBLE);
            mFabAddToCart.setVisibility(View.GONE);
            mFabScanner.setVisibility(View.GONE);
            /*
            switch ((int) drawerItem.getTag())
            {
                case 1: //Dashboard
                    mCurrentFragment = DashboardFragment.newInstance();
                    replace = true;
                    mFab.setVisibility(View.GONE);
                    break;

                case FRAGMENT_PRODUCTS: //Products
                    if (!(mCurrentFragment instanceof ProductsFragment)) {
                        title = getString(R.string.navigation_products);
                        mCurrentFragment = ProductsFragment.newInstance();
                        replace = true;
                    }
                    mFab.show();
                    break;

                case FRAGMENT_ORDERS: //Orders
                    mFab.setVisibility(View.GONE);
                    if (!(mCurrentFragment instanceof BillingFragment)) {
                        title = getString(R.string.navigation_orders);
                        mCurrentFragment = BillingFragment.newInstance(fabBillingPressed);
                        if (fabBillingPressed) {
                            fabBillingPressed = false;
                        }
                        replace = true;
                    }
                    break;

                case FRAGMENT_CUSTOMERS: //Customers
                    if (!(mCurrentFragment instanceof CustomersFragment)) {
                        title = getString(R.string.navigation_customers);
                        mCurrentFragment = CustomersFragment.newInstance();
                        replace = true;
                    }
                    break;

                case 5: //Brands
                    if (!(mCurrentFragment instanceof BrandsFragment)) {
                        mCurrentFragment = BrandsFragment.newInstance();
                        replace = true;
                    }
                    break;

                case 6: //Suppliers
                    if (!(mCurrentFragment instanceof SuppliersFragment)) {
                        mCurrentFragment = SuppliersFragment.newInstance();
                        replace = true;
                    }
                    break;

                case 7: //Employees
                    if (!(mCurrentFragment instanceof EmployeesFragment)) {
                        mCurrentFragment = EmployeesFragment.newInstance();
                        replace = true;
                    }
                    break;

                case FRAGMENT_VOUCHER: //Vouchers
                    if (!(mCurrentFragment instanceof VouchersFragment)) {
                        title = getString(R.string.navigation_vouchers);
                        mCurrentFragment = VouchersFragment.newInstance();
                        replace = true;
                    }
                    break;

                case 9: //Stock Transfer
                    if (!(mCurrentFragment instanceof StockTransferFragment)) {
                        mCurrentFragment = StockTransferFragment.newInstance();
                        replace = true;
                    }
                    break;

                case 10: //Settings
                    if (!(mCurrentFragment instanceof StockTakingFragment)) {
                        mCurrentFragment = StockTakingFragment.newInstance();
                        replace = true;
                    }
                    break;

                case 11: //Settings
                    if (!(mCurrentFragment instanceof SettingsFragment)) {
                        mCurrentFragment = SettingsFragment.newInstance();
                        replace = true;
                    }
                    mFab.setVisibility(View.GONE);
                    break;

                case 13: //Logout
                    logOut();
                    break;

                case FRAGMENT_MBASKET: // New Dashboard
                    if (!(mCurrentFragment instanceof HomeMbasketFragment)) {
                        title = getString(R.string.navigation_home_mbasket);
                        mCurrentFragment = new HomeMbasketFragment();
                        replace = true;
                    }
                    mFab.setVisibility(View.GONE);
                    break;

                case FRAGMENT_PROFILE: // New Profile
                    title = getString(R.string.navigation_profile);
                    Intent intentProfile = new Intent(Home_Tab_Activity.this, UserProfileActivity.class);
                    startActivity(intentProfile);
                    mFab.setVisibility(View.GONE);
                    break;

                case FRAGMENT_DELIVERY:// Delivery Option
                    if (!(mCurrentFragment instanceof DeliveryOptionsFragment)){
                        title = getString(R.string.navigation_delivery);
                        mCurrentFragment = new DeliveryOptionsFragment();
                        replace = true;
                    }
                    mFab.setVisibility(View.GONE);
                    break;

                case FRAGMENT_STORE_SETTING:// Store Setting
                    if (!(mCurrentFragment instanceof StoreSettingFragment)){
                        title = getString(R.string.navigation_store_setting);
                        mCurrentFragment = new StoreSettingFragment();
                        replace = true;
                    }
                    mFab.setVisibility(View.GONE);
                    break;

                case FRAGMENT_STORE_DETAILS:// Store Details
                    if (!(mCurrentFragment instanceof StoreDetailsFragment)){
                        title = getString(R.string.navigation_store_details);
                        mCurrentFragment = new StoreDetailsFragment();
                        replace = true;
                    }
                    mFab.setVisibility(View.GONE);
                    break;

                case FRAGMENT_REPORT_SETTINGS:// Reports Details
                    title = getString(R.string.navigation_reports_setting);
                    Intent intentReports = new Intent(Home_Tab_Activity.this, ReportsViewActivity.class);
                    startActivity(intentReports);
                    mFab.setVisibility(View.GONE);
                    break;


                case FRAGMENT_CHANGE_LANGUAGE:// Delivery Option
                    if (!(mCurrentFragment instanceof ChangeLanguageFragment)){
                        title = getString(R.string.navigation_change_language);
                        mCurrentFragment = new ChangeLanguageFragment();
                        replace = true;
                    }
                    mFab.setVisibility(View.GONE);
                    break;

                default:
                    mCurrentFragment = new Fragment();
                    replace = true;
                    break;
            }

            if (getSupportActionBar()!= null){
                getSupportActionBar().setTitle(title);
            }

            if (mCurrentFragment != null && replace) {
                showHideBackButton(!(mCurrentFragment instanceof HomeMbasketFragment));


                fragmentTransaction.replace(R.id.main_container, mCurrentFragment);
                fragmentTransaction.commit();

                if (!TextUtils.isEmpty(title)) {
                    if (!(mCurrentFragment instanceof  HomeMbasketFragment)){
                        mActionBar.setTitle(title);
                    }

                }
            }
            */
        }
    }

    private void moveToProfile()
    {
        //Intent intentProfile = new Intent(Home_Tab_Activity.this, UserProfileActivity.class);
        //startActivity(intentProfile);

    }

    public void showHideBackButton(boolean show)
    {
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setHomeButtonEnabled(show);
            appLogo.setVisibility(show ? View.GONE: View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        /*
        if (mCurrentFragment instanceof CustomersFragment)
        {
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.main_container);
            if (fragmentById instanceof CustomersFragment)
            {
                CustomersFragment customersFragment = (CustomersFragment) fragmentById;
                if (customersFragment.isEditing){
                    customersFragment.changeToEditMode(false);
                    return true;
                }
            }
        }
        */
        selectDrawerItemByTag(FRAGMENT_MBASKET);
       return true;
    }

}

