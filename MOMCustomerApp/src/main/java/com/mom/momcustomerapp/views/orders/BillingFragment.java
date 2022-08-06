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
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseFragment;

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

        //((Home_Tab_Activity) getActivity()).setToolBarCollapsible(false);
        /*setUpFab();*/

        mViewpager = rootView.findViewById(R.id.fragment_billing_viewpager);
        if (mViewpager != null)
        {
            setupViewPager();


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
        super.onDestroyView();
        unbinder.unbind();
    }



}
