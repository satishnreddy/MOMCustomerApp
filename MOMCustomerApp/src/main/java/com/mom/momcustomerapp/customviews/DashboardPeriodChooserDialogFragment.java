package com.mom.momcustomerapp.customviews;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.interfaces.DashboardPeriodChooserListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by nishant on 25/10/16.
 */

public class DashboardPeriodChooserDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = "DashboardPeriodChooserDialogFragment";

    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_today)
    TextView mTvToday;
    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_yesterday)
    TextView mTvYesterday;
    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_last_7days)
    TextView mTvLast7days;
    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_last_30days)
    TextView mTvLast30days;
    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_this_month)
    TextView mTvThisMonth;
    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_last_month)
    TextView mTvLastMonth;
    @BindView(R.id.fragment_dashboard_period_chooser_dialog_tv_custom)
    TextView mTvCustom;

    private DashboardPeriodChooserListener mDashboardPeriodChooserListener;
    private String mSelecetedPeriod;


    public DashboardPeriodChooserDialogFragment() {
        // Required empty public constructor
    }

    public static DashboardPeriodChooserDialogFragment newInstance(DashboardPeriodChooserListener listener, String selectedPeriod) {
        DashboardPeriodChooserDialogFragment fragment = new DashboardPeriodChooserDialogFragment();
        fragment.mDashboardPeriodChooserListener = listener;
        fragment.mSelecetedPeriod = selectedPeriod;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard_period_chooser_dialog, container, false);
        ButterKnife.bind(this, rootView);

        setUpSeletedPeriod();
        return rootView;
    }

    private void setUpSeletedPeriod() {
        if (!TextUtils.isEmpty(mSelecetedPeriod)) {
            if (mSelecetedPeriod.contains(" - ")) {
                mSelecetedPeriod = "Custom";
            }
            switch (mSelecetedPeriod) {
                case "Today":
                    mTvToday.setSelected(true);
                    mTvYesterday.setSelected(false);
                    mTvLast7days.setSelected(false);
                    mTvLast30days.setSelected(false);
                    mTvThisMonth.setSelected(false);
                    mTvLastMonth.setSelected(false);
                    mTvCustom.setSelected(false);
                    break;
                case "Yesterday":
                    mTvToday.setSelected(false);
                    mTvYesterday.setSelected(true);
                    mTvLast7days.setSelected(false);
                    mTvLast30days.setSelected(false);
                    mTvThisMonth.setSelected(false);
                    mTvLastMonth.setSelected(false);
                    mTvCustom.setSelected(false);
                    break;
                case "Last 7 Days":
                    mTvToday.setSelected(false);
                    mTvYesterday.setSelected(false);
                    mTvLast7days.setSelected(true);
                    mTvLast30days.setSelected(false);
                    mTvThisMonth.setSelected(false);
                    mTvLastMonth.setSelected(false);
                    mTvCustom.setSelected(false);
                    break;
                case "Last 30 Days":
                    mTvToday.setSelected(false);
                    mTvYesterday.setSelected(false);
                    mTvLast7days.setSelected(false);
                    mTvLast30days.setSelected(true);
                    mTvThisMonth.setSelected(false);
                    mTvLastMonth.setSelected(false);
                    mTvCustom.setSelected(false);
                    break;
                case "This Month":
                    mTvToday.setSelected(false);
                    mTvYesterday.setSelected(false);
                    mTvLast7days.setSelected(false);
                    mTvLast30days.setSelected(false);
                    mTvThisMonth.setSelected(true);
                    mTvLastMonth.setSelected(false);
                    mTvCustom.setSelected(false);
                    break;
                case "Last Month":
                    mTvToday.setSelected(false);
                    mTvYesterday.setSelected(false);
                    mTvLast7days.setSelected(false);
                    mTvLast30days.setSelected(false);
                    mTvThisMonth.setSelected(false);
                    mTvLastMonth.setSelected(true);
                    mTvCustom.setSelected(false);
                    break;
                case "Custom":
                    mTvToday.setSelected(false);
                    mTvYesterday.setSelected(false);
                    mTvLast7days.setSelected(false);
                    mTvLast30days.setSelected(false);
                    mTvThisMonth.setSelected(false);
                    mTvLastMonth.setSelected(false);
                    mTvCustom.setSelected(true);
                    break;
            }
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CalendarDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    @OnClick({R.id.fragment_dashboard_period_chooser_dialog_imgbtn_close, R.id.fragment_dashboard_period_chooser_dialog_tv_today, R.id.fragment_dashboard_period_chooser_dialog_tv_yesterday, R.id.fragment_dashboard_period_chooser_dialog_tv_last_7days, R.id.fragment_dashboard_period_chooser_dialog_tv_last_30days, R.id.fragment_dashboard_period_chooser_dialog_tv_this_month, R.id.fragment_dashboard_period_chooser_dialog_tv_last_month, R.id.fragment_dashboard_period_chooser_dialog_tv_custom})
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_dashboard_period_chooser_dialog_imgbtn_close) {
            dismiss();
        } else {
            if (mDashboardPeriodChooserListener != null) {
                switch (view.getId()) {
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_today:
                        mDashboardPeriodChooserListener.onPeriodSelection(1);
                        break;
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_yesterday:
                        mDashboardPeriodChooserListener.onPeriodSelection(2);
                        break;
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_last_7days:
                        mDashboardPeriodChooserListener.onPeriodSelection(3);
                        break;
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_last_30days:
                        mDashboardPeriodChooserListener.onPeriodSelection(4);
                        break;
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_this_month:
                        mDashboardPeriodChooserListener.onPeriodSelection(5);
                        break;
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_last_month:
                        mDashboardPeriodChooserListener.onPeriodSelection(6);
                        break;
                    case R.id.fragment_dashboard_period_chooser_dialog_tv_custom:
                        mDashboardPeriodChooserListener.onPeriodSelection(7);
                        break;
                }
                dismiss();
            }
        }
    }
}
