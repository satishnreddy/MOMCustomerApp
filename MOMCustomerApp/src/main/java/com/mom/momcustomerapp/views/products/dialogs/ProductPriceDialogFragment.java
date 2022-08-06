package com.mom.momcustomerapp.views.products.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.utils.AppUtils;
import com.mom.momcustomerapp.utils.UIUtils;
import com.mom.momcustomerapp.widget.SafeClickListener;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 10-04-2020 19:10
 * @modified by -
 * @updated on -
 * @since -
 */
public class ProductPriceDialogFragment extends DialogFragment {


    public ProductPriceDialogFragment newInstance(String title, String message, View.OnClickListener listener){
        ProductPriceDialogFragment fragment = new ProductPriceDialogFragment();
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

    public static void showDialog(final Context context, String existingProductPrice,
                                  final AddedPriceListener listener){
        final Dialog dialog = new Dialog(context);

        // Set transparent background and no title
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        dialog.setContentView(R.layout.inflate_dialog_enter_price);
        final EditText enterPriceEdt = dialog.findViewById(R.id.inflate_enter_price_edt);
        final View submitTv = dialog.findViewById(R.id.inflate_enter_price_submit_tv);
        if (!TextUtils.isEmpty(existingProductPrice)){
            enterPriceEdt.setText(existingProductPrice);
        }
        enterPriceEdt.requestFocus();

        submitTv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {
                String trimmedText = enterPriceEdt.getText().toString().trim();
                try{
                    Double doubleText = Double.parseDouble(trimmedText);
                    boolean isValid = AppUtils.isValidMinPrice(doubleText);
                    if (isValid){
                        UIUtils.closeKeyboard((Activity) context);
                        if (listener != null) listener.onPriceAdded(trimmedText);
                        dialog.dismiss();
                        return;
                    }
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }
                Toast.makeText(submitTv.getContext(),"Price not valid" ,
                        Toast.LENGTH_SHORT)
                        .show();

            }
        }));

        dialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (context instanceof Activity){
                            UIUtils.closeKeyboard((Activity) context);
                        }
                    }
                }
        );
        dialog.show();
    }

    public interface AddedPriceListener {
        void onPriceAdded(String s);
    }



}
