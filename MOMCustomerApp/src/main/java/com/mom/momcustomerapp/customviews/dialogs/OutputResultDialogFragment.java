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
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.momcustomerapp.R;

/*
 * Created by nishant on 26/10/16.
 */

public class OutputResultDialogFragment extends DialogFragment {

    private DialogListener mDialogListener;
    private boolean isAccepted = false;
    private String mMessage = "";

    public OutputResultDialogFragment() {}

    public static OutputResultDialogFragment newInstance(boolean isAccepted, String message, DialogListener dialogListener) {
        OutputResultDialogFragment fragment = new OutputResultDialogFragment();
        fragment.isAccepted = isAccepted;
        fragment.mMessage = message;
        fragment.mDialogListener = dialogListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_dialog_output_result, null);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        TextView messageTv = customLayout.findViewById(R.id.inflate_order_status_msg);
        ImageView icon = customLayout.findViewById(R.id.inflate_order_status_icon);

        icon.setImageResource(isAccepted ? R.drawable.ic_check_green_24dp : R.drawable.ic_close_24dp);

        messageTv.setText(mMessage
                /*isAccepted ? getString(R.string.dialog_order_status_accepted) : getString(R.string.dialog_order_status_declined)*/
        );

        return customLayout;
    }

    public interface DialogListener {
        void onDialogPositiveClick();
    }

}
