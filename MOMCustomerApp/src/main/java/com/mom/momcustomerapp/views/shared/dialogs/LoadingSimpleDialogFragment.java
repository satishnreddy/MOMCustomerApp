package com.mom.momcustomerapp.views.shared.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mom.momcustomerapp.R;

/*
 * Created by Nishant on 28-05-2016.
 */

public class LoadingSimpleDialogFragment extends DialogFragment
{

    public static final String FRAGMENT_TAG = "LoadingSimpleFragment";
    private String loadText = "";
    private TextView loadingTv;

    public LoadingSimpleDialogFragment() {
        // Required empty public constructor
    }

    public static LoadingSimpleDialogFragment newInstance(String title) {
        LoadingSimpleDialogFragment fragment = new LoadingSimpleDialogFragment();
        fragment.loadText = title;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loading_dialog_simple, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(true);

        loadingTv = rootView.findViewById(R.id.tv_loader_text);
        loadingTv.setText(loadText);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BorderlessDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            lp.copyFrom(dialog.getWindow().getAttributes());
        }
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.dimAmount = 0.8f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    public void setLoadText(String loadText) {
        this.loadText = loadText;
        if (loadingTv != null && isAdded()){
            loadingTv.setText(loadText);
        }
    }
}
