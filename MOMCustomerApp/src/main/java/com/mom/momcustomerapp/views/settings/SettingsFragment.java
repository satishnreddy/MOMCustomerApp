package com.mom.momcustomerapp.views.settings;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SettingsFragment extends Fragment {

    private static final int CALL_REQUEST_CODE = 0;

    @BindView(R.id.fragment_settings_tv_version)
    TextView mTvVersion;
    @BindView(R.id.fragment_settings_tv_url)
    TextView mTvUrl;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);

       // ((Home_Tab_Activity) getActivity()).setToolBarCollapsible(false);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            int version = pInfo.versionCode;
            String versionName = pInfo.versionName;
            mTvVersion.setText("Version : " + version + " : " + versionName);
        }

        mTvUrl.setText("Url : " + MOMApplication.getInstance().getServerUrl());
        mTvUrl.setVisibility(View.GONE);

        return rootView;
    }

    @OnClick({R.id.fragment_settings_layout_profile, R.id.fragment_settings_layout_password, R.id.fragment_settings_layout_contact,
            R.id.fragment_settings_layout_tnc, R.id.fragment_settings_layout_help, R.id.fragment_settings_layout_device_info,
            R.id.fragment_settings_layout_printer_setting, R.id.fragment_settings_layout_cart_setting, R.id.fragment_settings_layout_attr_setting, R.id.fragment_settings_layout_label_setting,
            R.id.fragment_settings_layout_logout})
    public void onClick(View view) {
        switch (view.getId())
        {
            /*
            case R.id.fragment_settings_layout_profile:
                Intent intentProfile = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.fragment_settings_layout_password:

                break;
            case R.id.fragment_settings_layout_tnc:
                Intent intentTnc = new Intent(getActivity(), TermsConditionsActivity.class);
                startActivity(intentTnc);
                break;
            case R.id.fragment_settings_layout_contact:
                mayRequestCallPermission();
                CallDialogFragment callDialogFragment = CallDialogFragment.newInstance();
                callDialogFragment.show(getFragmentManager().beginTransaction(), "DIALOG_FRAGMENT_CALL");
                break;
            case R.id.fragment_settings_layout_help:
                Intent intentHelp = new Intent(getActivity(), HelpActivity.class);
                intentHelp.putExtra(Consts.EXTRA_HELP_INDEX, Consts.EXTRA_HELP_HELP);
                startActivity(intentHelp);
                break;
            case R.id.fragment_settings_layout_device_info:
                Intent intentDeviceInfo = new Intent(getActivity(), DeviceInfoActivity.class);
                startActivity(intentDeviceInfo);
                break;
            case R.id.fragment_settings_layout_printer_setting:
                Intent intentPrinterSetting = new Intent(getActivity(), PrinterSettingsActivity.class);
                startActivity(intentPrinterSetting);
                break;
            case R.id.fragment_settings_layout_cart_setting:
                Intent intentCartSetting = new Intent(getActivity(), CartSettingActivity.class);
                startActivity(intentCartSetting);
                break;
            case R.id.fragment_settings_layout_attr_setting:
                Intent intentAttrSetting = new Intent(getActivity(), CustomAttributesActivity.class);
                startActivity(intentAttrSetting);
                break;
            case R.id.fragment_settings_layout_label_setting:
                Intent intentLabelSetting = new Intent(getActivity(), LabelTemplateActivity.class);
                startActivity(intentLabelSetting);
                break;
            case R.id.fragment_settings_layout_logout:
                ((HomeActivity) getActivity()).logOut();
                break;

             */
        }
    }

    private boolean mayRequestCallPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        int permissionCall = ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE);
        int permissionPhoneState = ContextCompat.checkSelfPermission(getActivity(), READ_PHONE_STATE);

        if (permissionCall == PackageManager.PERMISSION_GRANTED
                && permissionPhoneState == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
            if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                Snackbar.make(mTvVersion, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                requestPermissions(new String[]{CALL_PHONE, READ_PHONE_STATE}, CALL_REQUEST_CODE);
                            }
                        });
            }
        } else {
            requestPermissions(new String[]{CALL_PHONE, READ_PHONE_STATE}, CALL_REQUEST_CODE);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.REQUEST_CODE_CHANGE_PASSWORD) {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(getActivity(), "Your password has been changed successfully. Pls sign in with the changed password", Toast.LENGTH_LONG).show();
                //((HomeActivity) getActivity()).logOut();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_REQUEST_CODE) {
            //Log.d("Nish", "permissionsGranted : ALL");
        }
    }
}
