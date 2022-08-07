package com.mom.momcustomerapp.customviews.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
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

public class DatePickerSpinnerDialogFragment extends DialogFragment {

    private DatePicker.OnDateChangedListener mDialogListener;
    private String title;

    public DatePickerSpinnerDialogFragment() {}

    public static DatePickerSpinnerDialogFragment newInstance(String title, DatePicker.OnDateChangedListener dialogListener) {
        DatePickerSpinnerDialogFragment fragment = new DatePickerSpinnerDialogFragment();

        fragment.mDialogListener = dialogListener;
        fragment.title = title;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_dialog_date_picker_spinner, null);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        DatePicker datePicker = customLayout.findViewById(R.id.datePicker1);
        TextView titleTv = customLayout.findViewById(R.id.tv_title);
        View submitTv = customLayout.findViewById(R.id.tv_submit);

        titleTv.setText(title);

        submitTv.setOnClickListener(new SafeClickListener(v -> {
            dismiss();
            mDialogListener.onDateChanged(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        }));

        return customLayout;
    }
}
