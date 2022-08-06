package com.mom.momcustomerapp.customviews.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.widget.SafeClickListener;

/*
 * Created by nishant on 26/10/16.
 */

public class TimePickerSpinnerDialogFragment extends DialogFragment {

    private TimePicker.OnTimeChangedListener mDialogListener;
    private int currentHour;
    private int currentMin;
    private String title;

    public TimePickerSpinnerDialogFragment() {}

    public static TimePickerSpinnerDialogFragment newInstance(String title, int hour, int minute, TimePicker.OnTimeChangedListener dialogListener) {
        TimePickerSpinnerDialogFragment fragment = new TimePickerSpinnerDialogFragment();

        fragment.mDialogListener = dialogListener;
        fragment.title = title;
        fragment.currentHour = hour;
        fragment.currentMin = minute;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_dialog_time_picker_spinner, null);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        TimePicker timePicker = customLayout.findViewById(R.id.datePicker1);
        TextView titleTv = customLayout.findViewById(R.id.tv_title);
        View submitTv = customLayout.findViewById(R.id.tv_submit);
        timePicker.setIs24HourView(false);
        timePicker.setCurrentHour(currentHour);
        timePicker.setCurrentMinute(currentMin);

        titleTv.setText(title);
        timePicker.getCurrentHour();

        submitTv.setOnClickListener(new SafeClickListener(v -> {
            dismiss();
            mDialogListener.onTimeChanged(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        }));

        return customLayout;
    }
}
