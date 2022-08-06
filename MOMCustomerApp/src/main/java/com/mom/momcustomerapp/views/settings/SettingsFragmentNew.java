package com.mom.momcustomerapp.views.settings;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.mom.momcustomerapp.interfaces.OnBackPressed;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.views.stores.StoreSettingFragment;
import com.mom.momcustomerapp.widget.SafeClickListener;


import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragmentNew extends BaseFragment implements OnBackPressed {

    @BindView(R.id.store_settings_lay)
    LinearLayout storesettings;

//    @BindView(R.id.store_reports_lay)
//    LinearLayout storereports;

    @BindView(R.id.logout_lay)
    LinearLayout storrlogout;

    @BindView(R.id.my_profile_lay)
    LinearLayout myprofile;

    private Fragment mCurrentFragment;
    boolean replace = false;

    public SettingsFragmentNew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static SettingsFragmentNew newInstance() {
        SettingsFragmentNew fragment = new SettingsFragmentNew();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Home_Tab_Activity activity;
    StoreSettingFragment store = new StoreSettingFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settingsnew, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
      //  ((Home_Tab_Activity) getActivity()).setToolBarCollapsible(false);
        mCurrentFragment = SettingsFragmentNew.newInstance();
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storesettings.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {
                //getFragmentManager().beginTransaction().replace(R.id.flFragment, new StoreSettingFragment()).commit();

            }
        }));


        myprofile.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(getContext(), EditProfileActivity.class);
                //startActivityForResult(intent, REQUEST_CODE_PROFILE);
            }
        }));

        /*
        storereports.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {
                Intent intentReports = new Intent(getContext(), ReportsViewActivity.class);
                startActivity(intentReports);

            }
        }));
        */

        storrlogout.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
        {
            @Override
            public void onClick(View v) {
                ((Home_Tab_Activity) getActivity()).logOut();

            }
        }));


    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (Home_Tab_Activity) context;

    }

    @Override
    public void onResume() {
        super.onResume();
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }


    @Override
    public void onStop() {
        super.onStop();
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    @Override
    public void onBackPressed() {

        getActivity().getSupportFragmentManager().popBackStack();
    }
}
