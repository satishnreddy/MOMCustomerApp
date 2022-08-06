package com.mom.momcustomerapp.views.shared.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;

/*
 * Created by nishant on 26/10/16.
 */

public class ErrorDialogFragment extends DialogFragment {

    private ErrorDialogFragment.ErrorDialogListener mErrorDialogListener;
    private String mTitle = "";
    private String mMessage = "";

    public ErrorDialogFragment() {

    }

    public static ErrorDialogFragment newInstance(String title, String msg) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = msg;
        return fragment;
    }

    public static ErrorDialogFragment newInstance(String title, String msg, ErrorDialogListener errorDialogListener) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = msg;
        fragment.mErrorDialogListener = errorDialogListener;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            if (mErrorDialogListener == null) {
                // Instantiate the NoticeDialogListener so we can send events to the host
                mErrorDialogListener = (ErrorDialogFragment.ErrorDialogListener) activity;
            }
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Activity must implement ErrorDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mErrorDialogListener.onDialogPositiveClick();
                    }
                });

        // Create the AlertDialog object and return it
        if (!TextUtils.isEmpty(mMessage)) {
            builder.setMessage(mMessage);
        } else {
            builder.setMessage("Empty result");
        }

        return builder.create();
    }

    public interface ErrorDialogListener {
        void onDialogPositiveClick();
    }

}
