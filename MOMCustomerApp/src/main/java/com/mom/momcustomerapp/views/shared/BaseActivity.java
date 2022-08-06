package com.mom.momcustomerapp.views.shared;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.mom.momcustomerapp.R;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.mom.momcustomerapp.data.shared.ErrorModel;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.views.shared.dialogs.ErrorDialogFragment;
import com.mom.momcustomerapp.views.shared.dialogs.LoadingSimpleDialogFragment;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Response;

/*
 * Created by nishant on 23/09/16.
 */

public class BaseActivity extends AppCompatActivity implements ErrorDialogFragment.ErrorDialogListener {

    private ErrorDialogFragment mErrorDialogFragment;
    private boolean showdialog = false;
    private ErrorDialogFragment.ErrorDialogListener mErrorDialogListener = null;
    public static final String DIALOG_TAG = "dialog";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Fabric.with(this, new Crashlytics());*/
        mErrorDialogListener = null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        showdialog = false;
        super.onStop();
    }

    @Override
    protected void onResumeFragments() {
        showdialog = true;
        super.onResumeFragments();
    }


    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.processing));
    }

    public void showLoadingDialog(String loadingMessage)
    {
        try
        {
            LoadingSimpleDialogFragment fragment = (LoadingSimpleDialogFragment) getSupportFragmentManager().findFragmentByTag(LoadingSimpleDialogFragment.FRAGMENT_TAG);
            if (fragment == null)
            {
                fragment = LoadingSimpleDialogFragment.newInstance(loadingMessage);
                fragment.setCancelable(true);

                if (!isFinishing())
                {
                    getSupportFragmentManager().beginTransaction().add(fragment, LoadingSimpleDialogFragment.FRAGMENT_TAG).commitNowAllowingStateLoss();
                }
            }
            else {


                fragment.setLoadText(loadingMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoadingDialog() {

        LoadingSimpleDialogFragment fragment = (LoadingSimpleDialogFragment) getSupportFragmentManager().findFragmentByTag(LoadingSimpleDialogFragment.FRAGMENT_TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
        } else {
            Log.d("Nish", "hideLoadingDialog : Dialog not found");
        }
    }

    public void showErrorDialog(String title, String message, ErrorDialogFragment.ErrorDialogListener errorDialogListener) {
        try {
            if (showdialog) {
                if (errorDialogListener != null) {
                    mErrorDialogListener = errorDialogListener;
                    mErrorDialogFragment = ErrorDialogFragment.newInstance(title, message, mErrorDialogListener);
                } else {
                    mErrorDialogFragment = ErrorDialogFragment.newInstance(title, message);
                }
                if (!isFinishing()) {
                    mErrorDialogFragment.show(getSupportFragmentManager(), "ErrorDialogFragment");
                }
            } else {
                Log.d("Nish", "showErrorDialog : showdialog is false");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method will take Default error title
     * @param message   message to show on UI.
     */
    public void showErrorDialog(String message)
    {
        showErrorDialog(getString(R.string.error_title), message, null);
    }

    public void showErrorDialog(String title, String message)
    {
        showErrorDialog(title, message, null);
    }

    @Override
    public void onDialogPositiveClick() {
        if (mErrorDialogFragment != null && mErrorDialogFragment.isVisible()) {
            if (mErrorDialogListener != null) {
                mErrorDialogListener.onDialogPositiveClick();
            }
            mErrorDialogFragment.dismiss();
            mErrorDialogFragment = null;
        }
    }

    public void parseError(Response response, boolean fromBody)
    {
        ErrorModel error;
        if (fromBody) {
            error = ErrorUtils.parseErrorFromBody(response);
        } else {
            error = ErrorUtils.parseError(response);
        }

        Log.e("ERROR", "Error : Code : " + error.getErrorcode() + " Message : " + error.getMessage());
        if (!TextUtils.isEmpty(error.getStatus()) && error.getStatus().equalsIgnoreCase("error")) {
            if (error.getMessage() != null) {
                showErrorDialog("Error", "Something went wrong: " + error.getMessage());
            } else {
                showErrorDialog("Error", "Something went wrong: " + error.getStatus());
            }
        }
    }
}
