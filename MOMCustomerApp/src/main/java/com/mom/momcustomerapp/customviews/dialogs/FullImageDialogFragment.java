package com.mom.momcustomerapp.customviews.dialogs;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.utils.GlideLoader;


/**
 * A simple {@link DialogFragment} subclass.
 */
public class FullImageDialogFragment extends DialogFragment {

    private ImageView permitImage;
    private View backButton;

    public FullImageDialogFragment() {}

    private String imageUrl = "";

    public static FullImageDialogFragment newInstance(String imageUrl) {
        FullImageDialogFragment fragment = new FullImageDialogFragment();
        fragment.imageUrl = imageUrl;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_image_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.fragment_dialog_iv_back_btn);
        permitImage = view.findViewById(R.id.fragment_dialog_iv_permit_image);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        GlideLoader.url(view.getContext()).load(imageUrl).placeholder(R.color.grey500).into(permitImage);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

}
