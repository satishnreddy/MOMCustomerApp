package com.mom.momcustomerapp.customviews.adapters;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.customviews.CalendarCellView;

public class DefaultDayViewAdapter implements DayViewAdapter {
    @Override
    public void makeCellView(CalendarCellView parent) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day, parent, false);

        parent.addView(textView);
        parent.setDayOfMonthTextView(textView);
    }
}
