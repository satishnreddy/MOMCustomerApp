package com.mom.momcustomerapp.views.shared;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private Activity mActivity;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            if (context instanceof BaseActivity) {
                mActivity = (BaseActivity) context;
            } else {
                mActivity = (Activity) context;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mActivity == null) {
            mActivity = activity;
        }
    }

    public void showLoadingDialog() {
        if (mActivity != null && mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showLoadingDialog();
        }
    }

    public void hideLoadingDialog() {
        if (mActivity != null && mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).hideLoadingDialog();
        }
    }

    public void showErrorDialog(String title, String message) {
        if (mActivity != null && mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showErrorDialog(title, message);
        }
    }

    public void parseError(Response response, boolean fromBody) {
        if (mActivity != null && mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).parseError(response, fromBody);
        }
    }

    public void showErrorDialog(String message) {
        if (mActivity != null && mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showErrorDialog(message);
        }
    }
}
