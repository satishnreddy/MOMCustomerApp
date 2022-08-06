package com.mom.momcustomerapp.customviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mom.momcustomerapp.R;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 10-04-2020 19:10
 * @modified by -
 * @updated on -
 * @since -
 */
public class TwoBtnDialogFragment extends DialogFragment {


    public TwoBtnDialogFragment newInstance(String title, String message, View.OnClickListener listener){
        TwoBtnDialogFragment fragment = new TwoBtnDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_dialog_order_decline_completed, null);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        Bundle arguments = getArguments();
        if (arguments == null) return null;
        String title = arguments.getString("title");
        String message = arguments.getString("message");

        TextView titleTv = customLayout.findViewById(R.id.inflate_order_title);
        TextView subTitleTv = customLayout.findViewById(R.id.inflate_order_sub_title);
        TextView leftBtnTV = customLayout.findViewById(R.id.inflate_order_btn_left_tv);
        TextView rightBtnTV = customLayout.findViewById(R.id.inflate_order_btn_right_tv);

        titleTv.setText(title);
        subTitleTv.setText(message);
        leftBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        rightBtnTV.setOnClickListener();

        return customLayout;
    }

    public static void showDialog(Context context, String title, String message,
                                    String leftText, String rightText,
                                    final CustomDialogListener listener){
        final Dialog dialog = new Dialog(context);

        // Set transparent background and no title
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }


        dialog.setContentView(R.layout.inflate_dialog_order_decline_completed);


        TextView titleTv = dialog.findViewById(R.id.inflate_order_title);
        TextView subTitleTv = dialog.findViewById(R.id.inflate_order_sub_title);
        TextView leftBtnTV = dialog.findViewById(R.id.inflate_order_btn_left_tv);
        TextView rightBtnTV = dialog.findViewById(R.id.inflate_order_btn_right_tv);

        if (TextUtils.isEmpty(title)){
            titleTv.setVisibility(View.GONE);
        }
        else{
            titleTv.setText(title);
        }
        subTitleTv.setText(message);

        leftBtnTV.setText(leftText);
        rightBtnTV.setText(rightText);
        leftBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickLeftBtn();
                dialog.dismiss();
            }
        });

        rightBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickRightBtn();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface CustomDialogListener {
        void onClickLeftBtn();
        void onClickRightBtn();
    }



}
