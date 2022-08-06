package com.mom.momcustomerapp.customviews.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mom.momcustomerapp.R;

/*
 * Created by nishant on 26/10/16.
 */

public class SingleBtnDialogFragment extends DialogFragment {

    private DialogListener mDialogListener;
    private String title = "";
    private String mMessage = "";

    public SingleBtnDialogFragment() {}

    public static SingleBtnDialogFragment newInstance(String title) {
        SingleBtnDialogFragment fragment = new SingleBtnDialogFragment();
        fragment.title = title;
        fragment.mDialogListener = null;
        return fragment;
    }

    public static SingleBtnDialogFragment newInstance(String title, String btnText, DialogListener dialogListener) {
        SingleBtnDialogFragment fragment = new SingleBtnDialogFragment();
        fragment.title = title;
        fragment.mMessage = btnText;
        fragment.mDialogListener = dialogListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_dialog_ok_btn, null);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        TextView titleTv = customLayout.findViewById(R.id.inflate_dialog_title);
        TextView subTitleTv = customLayout.findViewById(R.id.inflate_dialog_sub_title);
        TextView singleBtn = customLayout.findViewById(R.id.inflate_dialog_single_btn);

        subTitleTv.setVisibility(View.GONE);
        titleTv.setText(title);

        if (mMessage == null || mMessage.isEmpty()) singleBtn.setText(getString(R.string.ok));
        else singleBtn.setText(mMessage);

        singleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) mDialogListener.onDialogPositiveClick();
                dismiss();
            }
        });

        return customLayout;
    }

    public interface DialogListener {
        void onDialogPositiveClick();
    }

}
