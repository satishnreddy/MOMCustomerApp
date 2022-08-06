package com.mom.momcustomerapp.views.sales.dialogs;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlots;
import com.mom.momcustomerapp.widget.SafeClickListener;
import com.mswipetech.sdk.network.util.Logs;

import java.util.List;

import butterknife.BindView;

/*
 * Created by nishant on 26/10/16.
 */

public class DeliverySlotstDialogFragment extends DialogFragment
{

    TextView mTxtTitle;
    View catLeftShift;
    View catRightShift;
    ViewGroup categoriesVG;
    int mSelectedCategoryIndex = 0;
    int mCustPosCategoryIndex = 0;
    Typeface boldTypeface;
    RadioGroup mRadioGrpTimeSlot;
    int miRadGrpTimeSlotIndex;
    String mStDeliverySlot = "00.00 - 00.00";
    int mDeliveryType;

    private DialogListener mDialogListener;
    private boolean isAccepted = false;
    private String mMessage = "";
    private List<TimeSlots> mLstTimeSlots;
    private String mCurDate = "";
    Date mDtCurdate;



    public DeliverySlotstDialogFragment() {}

    public DeliverySlotstDialogFragment( List<TimeSlots> mLstTimeSlots, DialogListener mDialogListener,
                                                String currentDate, int deliveryType)
    {
        this.mLstTimeSlots = mLstTimeSlots;

        this.mDialogListener = mDialogListener;
        mCurDate = currentDate;
        mDeliveryType = deliveryType;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            mDtCurdate = format.parse(mCurDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static DeliverySlotstDialogFragment newInstance(boolean isAccepted, String message, DialogListener dialogListener)
    {
        DeliverySlotstDialogFragment fragment = new DeliverySlotstDialogFragment();
        fragment.isAccepted = isAccepted;
        fragment.mMessage = message;
        fragment.mDialogListener = dialogListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_dialog_sales_delivery_slots, null);
        boldTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null)
        {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        mTxtTitle = customLayout.findViewById(R.id.header_title);

        if(mDeliveryType ==0)
        {
            mTxtTitle.setText("Home Delivery");
        } else {
            mTxtTitle.setText("Pickup from Store");
        }

        TextView bottomAction1Tv = customLayout.findViewById(R.id.dialog_timeslots_action1);
        bottomAction1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(mDialogListener != null)
                {
                    if (isVisible())
                    {
                        dismiss();
                    }
                    mDialogListener.onDialogPositiveClick(mStDeliverySlot,  mSelectedCategoryIndex,  mDtCurdate);
                }
            }
        });

        TextView bottomAction2Tv = customLayout.findViewById(R.id.dialog_timeslots_action2);

        bottomAction2Tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isVisible()) {
                    dismiss();
                }
                mDialogListener.onDialogPositiveClick(mStDeliverySlot, mSelectedCategoryIndex, mDtCurdate );
            }
        });

        catLeftShift =  customLayout.findViewById(R.id.cat_left);
        catRightShift = customLayout.findViewById(R.id.cat_right);;
        categoriesVG = customLayout.findViewById(R.id.fragment_products_in_stock_to_be_selected_cat_ll);

        catLeftShift.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
        {
            @Override
            public void onClick(View v)
            {
                ViewParent parent = categoriesVG.getParent();
                if (parent instanceof HorizontalScrollView)
                {
                    if(mCustPosCategoryIndex != 0)
                    {
                        mCustPosCategoryIndex = mCustPosCategoryIndex -1;
                        int left = categoriesVG.getChildAt(mCustPosCategoryIndex).getLeft();
                        ((HorizontalScrollView) parent).smoothScrollTo(left, 0);
                    }
                }

            }
        }));

        catRightShift.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
        {
            @Override
            public void onClick(View v)
            {
                ViewParent parent = categoriesVG.getParent();
                if(mCustPosCategoryIndex != 6)
                {
                    mCustPosCategoryIndex = mCustPosCategoryIndex  + 1;
                    int left = categoriesVG.getChildAt(mCustPosCategoryIndex).getLeft();
                    ((HorizontalScrollView) parent).smoothScrollTo(left, 0);
                }

            }
        }));

        updateCategoriesUI();

        mRadioGrpTimeSlot = customLayout.findViewById(R.id.checkout_radioGroup_timeslots);
        mRadioGrpTimeSlot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                miRadGrpTimeSlotIndex = checkedId;
                mStDeliverySlot = ((RadioButton)group.findViewById(checkedId)).getText().toString();

            }
        });
        Date tempDate = mDtCurdate;
        Calendar c = Calendar.getInstance();
        c.setTime(tempDate);
        final int dayofweek = c.get(Calendar.DAY_OF_WEEK);


        updateTimeSlotsUI(dayofweek);



        return customLayout;
    }

    private void updateCategoriesUI()
    {
        categoriesVG.removeAllViews();

        Date tempDate = mDtCurdate;
        Calendar c = Calendar.getInstance();
        c.setTime(tempDate);


        for (int i = 0; i < 7; i++)
        {
            TextView tv;
            if (i == mSelectedCategoryIndex)
            {
                tv = (TextView) View.inflate(categoriesVG.getContext(),
                        R.layout.selected_simple_list_item, null);
                tv.setAlpha(1f);
                tv.setTypeface(boldTypeface);

            } else {
                tv = (TextView) View.inflate(categoriesVG.getContext(),
                        android.R.layout.simple_list_item_1, null);
                tv.setAlpha(0.8f);
            }


            String dayToday = android.text.format.DateFormat.format("EEE", c).toString();
            String month = android.text.format.DateFormat.format("MMM", c).toString();
            final int dayofweek = c.get(Calendar.DAY_OF_WEEK);

            tv.setText( dayToday + " " + month + " " + c.get(Calendar.DAY_OF_MONTH));

            final int finalI = i;

            tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    setSelectionCategory(finalI, dayofweek);

                }
            });
            c.add(Calendar.DATE, 1);

            categoriesVG.addView(tv);
        }
        categoriesVG.setVisibility(View.VISIBLE);

        final int finalSelectedViewIndex = mSelectedCategoryIndex;

        categoriesVG.post(new Runnable()
        {
            @Override
            public void run() {
                ViewParent parent = categoriesVG.getParent();
                if (parent instanceof HorizontalScrollView)
                {
                    int left = categoriesVG.getChildAt(finalSelectedViewIndex).getLeft();
                    ((HorizontalScrollView) parent).smoothScrollTo(left, 0);
                }
            }
        });


    }


    private void updateTimeSlotsUI(int dayofWeek)
    {

        mRadioGrpTimeSlot.removeAllViews();
        int index = 0;
        for(int i = 0; i <mLstTimeSlots.size(); i ++)
        {
            TimeSlots timeSlot = mLstTimeSlots.get(i);
            char c = timeSlot.weekDaysSelected.charAt(dayofWeek -1);

            if( c == '1')
            {

                RadioButton radioBtn = new RadioButton(getActivity());
                radioBtn.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioBtn.setTextAppearance(getContext(), R.style.font_medium_14_black);
                radioBtn.setText(timeSlot.fromSlot + " - " + timeSlot.toSlot);
                radioBtn.setId(index);
                index ++;

                mRadioGrpTimeSlot.addView(radioBtn);
            }


        }



    }

    public void setSelectionCategory(int index, int dayofWeek )
    {
        mSelectedCategoryIndex = index;
        mCustPosCategoryIndex = index;


        updateCategoriesUI();

        updateTimeSlotsUI(dayofWeek);


    }


    public interface DialogListener
    {
        void onDialogPositiveClick(String stDeliverySlot, int mSelectedCategoryIndex, Date mDtCurdate);
    }

    public void  Dismiss(){

        dismiss();
    }
}
