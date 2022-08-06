package com.mom.momcustomerapp.customviews.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.github.mmin18.widget.RealtimeBlurView;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.customviews.CalendarCellDecorator;
import com.mom.momcustomerapp.customviews.CalendarPickerView;
import com.mom.momcustomerapp.customviews.adapters.DefaultDayViewAdapter;
import com.mom.momcustomerapp.interfaces.CalendarDialogDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalenderDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = "CalenderDialogFragment";

    @BindView(R.id.fragment_calendar_dialog_calendar)
    CalendarPickerView mCalender;
    @BindView(R.id.blur_view)
    View mBlurView;
    @BindView(R.id.fragment_calendar_dialog_tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.fragment_calendar_dialog_tv_end_date)
    TextView mTvEndDate;
    @BindView(R.id.fragment_calendar_dialog_toolbar_done)
    ImageButton mImgBtnDone;
    @BindView(R.id.fragment_calendar_dialog_toolbar)
    Toolbar mToolbar;

    private CalendarDialogDateSetListener mCalendarDialogDateSetListener;
    private boolean isStartDateSelected = false, isEndDateSelected = false;
    private boolean isSingleDate = false;
    private String startDate, endDate;


    public CalenderDialogFragment() {
        // Required empty public constructor
    }

    public static CalenderDialogFragment newInstance(CalendarDialogDateSetListener listener) {
        CalenderDialogFragment fragment = new CalenderDialogFragment();
        fragment.mCalendarDialogDateSetListener = listener;
        return fragment;
    }

    public static CalenderDialogFragment newInstance(CalendarDialogDateSetListener listener, boolean isSingleDate) {
        CalenderDialogFragment fragment = new CalenderDialogFragment();
        fragment.mCalendarDialogDateSetListener = listener;
        fragment.isSingleDate = isSingleDate;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar_dialog, container, false);
        ButterKnife.bind(this, rootView);

        setupToolBar(rootView);
        initCalender();

        return rootView;
    }

    private void setupToolBar(View rootView) {
        mToolbar = (Toolbar) rootView.findViewById(R.id.fragment_calendar_dialog_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mToolbar.setTitle("CUSTOM RANGE");
        mToolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
    }

    private void initCalender() {
        Calendar today = Calendar.getInstance();
        mCalender.setCustomDayView(new DefaultDayViewAdapter());
        mCalender.setDecorators(Collections.<CalendarCellDecorator>emptyList());

        if (isSingleDate) {
            Calendar nextYr = Calendar.getInstance();
            nextYr.add(Calendar.YEAR, 1);

            mCalender.init(today.getTime(), nextYr.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withSelectedDate(today.getTime());

            mTvStartDate.setVisibility(View.GONE);
        } else {
            Calendar lastYr = Calendar.getInstance();
            lastYr.add(Calendar.YEAR, -1);
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DATE, 1);

            mCalender.init(lastYr.getTime(), tomorrow.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDate(today.getTime());
        }

        mCalender.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (isSingleDate) {
                    if (!isEndDateSelected) {
                        endDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);
                        //Log.d("Nish", "onDateSelected: EndDate " + endDate);
                        mTvEndDate.setText(endDate);
                        mTvEndDate.setBackgroundResource(R.drawable.bg_rounded_money_50);
                        isEndDateSelected = true;
                    }
                } else {
                    if (!isStartDateSelected) {
                        startDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);
                        //Log.d("Nish", "onDateSelected: StartDate " + startDate);
                        mTvStartDate.setText(startDate);
                        mTvStartDate.setBackgroundResource(R.drawable.bg_rounded_money_50);
                        isStartDateSelected = true;
                    } else if (!isEndDateSelected) {
                        endDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);
                        //Log.d("Nish", "onDateSelected: EndDate " + endDate);
                        mTvEndDate.setText(endDate);
                        mTvEndDate.setBackgroundResource(R.drawable.bg_rounded_money_50);
                        isEndDateSelected = true;
                    } else {
                        //Log.d("Nish", "onDateSelected: Else ");
                        isStartDateSelected = false;
                        isEndDateSelected = false;
                        mTvStartDate.setText("Pick Start Date");
                        mTvEndDate.setText("Pick End Date");
                        mTvStartDate.setBackgroundResource(R.drawable.bg_rounded_white_27);
                        mTvEndDate.setBackgroundResource(R.drawable.bg_rounded_white_27);
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                String sdate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(date);
                //Log.d("Nish", "onDateUnselected: Date " + sdate);
                if (isSingleDate) {
                    if (!TextUtils.isEmpty(sdate) && !TextUtils.isEmpty(endDate)) {
                        if (endDate.equalsIgnoreCase(sdate)) {
                            isEndDateSelected = false;
                            mTvEndDate.setText("Pick End Date");
                            mTvEndDate.setBackgroundResource(R.drawable.bg_rounded_white_27);
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(sdate) && !TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                        if (startDate.equalsIgnoreCase(sdate) || endDate.equalsIgnoreCase(sdate)) {
                            isStartDateSelected = false;
                            isEndDateSelected = false;
                            mTvStartDate.setText("Pick Start Date");
                            mTvEndDate.setText("Pick End Date");
                            mTvStartDate.setBackgroundResource(R.drawable.bg_rounded_white_27);
                            mTvEndDate.setBackgroundResource(R.drawable.bg_rounded_white_27);
                        }
                    }
                }
            }
        });
    }

    public CalendarPickerView getCalender() {
        return mCalender;
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

    @OnClick(R.id.fragment_calendar_dialog_toolbar_done)
    public void onClick() {
        if (mCalendarDialogDateSetListener != null) {
            mCalendarDialogDateSetListener.onDatesSelected(mCalender.getSelectedDates());
        }
        dismiss();
    }
}
