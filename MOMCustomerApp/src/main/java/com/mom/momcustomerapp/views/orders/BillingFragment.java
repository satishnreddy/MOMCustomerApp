package com.mom.momcustomerapp.views.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.customviews.adapters.GenericFragmentPagerAdapter;

import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mswipetech.sdk.network.util.Logs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingFragment extends BaseFragment
{


    @BindView(R.id.fragment_billing_tabs)
    TabLayout mTabLayout;

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
    private int iTabFirstSelectedIgnore = 0;

    private Home_Tab_Activity context;


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


        setupBillingFramgment();
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            if (bundle.get("key") == "pending") {
                //TabLayout.Tab tab = mTabLayout.getTabAt(0);
                //tab.select();
            }

            if (bundle.get("key") == "declined") {
                TabLayout.Tab tab = mTabLayout.getTabAt(1);
                tab.select();
            }
            if (bundle.get("key") == "completed") {
                TabLayout.Tab tab = mTabLayout.getTabAt(2);
                tab.select();
            }
        }

        return rootView;
    }

    private void setupBillingFramgment()
    {

        mPendingOrdersFragment = PendingOrdersFragment.newInstance();
        mReturnsFragment = ReturnsFragment.newInstance();
        mCompletedFragment = CompletedFragment.newInstance();

        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.orders_title_tab_pending)),true);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.orders_title_tab_returned)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.orders_title_tab_completed)));


        getChildFragmentManager().beginTransaction().replace(R.id.main_container, mPendingOrdersFragment).commit();



        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {


                switch (mTabLayout.getSelectedTabPosition())
                {
                    case 0:
                        //mFab.setVisibility(View.GONE);
                        getChildFragmentManager().beginTransaction().replace(R.id.main_container, mPendingOrdersFragment).commit();
                        break;
                    case 1:
                        //mFab.setVisibility(View.GONE);
                        getChildFragmentManager().beginTransaction().replace(R.id.main_container, mReturnsFragment).commit();
                        break;
                    case 2:
                        //mFab.setVisibility(View.GONE);
                        getChildFragmentManager().beginTransaction().replace(R.id.main_container, mCompletedFragment).commit();
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });




        mCurrentFragment = mPendingOrdersFragment;

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Consts.REQUEST_CODE_CART) {
            if (resultCode == Activity.RESULT_OK) {
                //mAdapter.getItem(mViewpager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }



}
