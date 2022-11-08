package com.mom.momcustomerapp.views.login;

import com.mom.momcustomerapp.BuildConfig;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//import com.facebook.FacebookSdk;
//import com.facebook.LoggingBehavior;
import com.google.gson.Gson;
import com.mom.momcustomerapp.BuildConfig;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.login.LoginCustomerController;
import com.mom.momcustomerapp.controllers.login.model.LoginCustomerResp;
import com.mom.momcustomerapp.controllers.updateapp.VersionModel;
import com.mom.momcustomerapp.controllers.updateapp.views.CheckUpdateDialogFragment;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.utils.crashlogs.view.CrashActivity;
import com.mom.momcustomerapp.views.customers.AddCustomerActivity;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.widget.SafeClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mom.momcustomerapp.data.application.Consts.REQUEST_CODE_CHANGE_LANGUAGE;
import static com.mom.momcustomerapp.data.application.app.INTERNAL_MBASKET_VERSION_CODE;

/**
 * A login screen that offers login via email/mPassword.
 */
public class LoginActivity extends BaseActivity
{

	private static final String TAG = "LoginActivity";

	@BindView(R.id.activity_login_et_phone)
	EditText mEtLoginMobile;
	@BindView(R.id.activity_login_et_password)
	EditText mEtPassword;
	@BindView(R.id.activity_login_imgbtn_show_password)
	ImageButton mImgbtnShowPassword;
	@BindView(R.id.activity_login_tv_tnc)
	TextView mTvTnC;
	@BindView(R.id.activity_login_tv_tnc_text)
	TextView mTvTnCText;
	@BindView(R.id.activity_login_tv_privacy)
	TextView mTvPrivacy;
	@BindView(R.id.activity_login_tv_version)
	TextView mTvVersion;
	@BindView(R.id.activity_login_tv_signup_btn)
	View signUpBtn;
	@BindView(R.id.activity_login_btn_login)
	View signInBtn;
	@BindView(R.id.activity_login_tv_version_name)
	TextView versionNameTv;
	@BindView(R.id.activity_login_tv_forgot_pwd)
	View forgotPwdView;

	private Activity mActivity;
	private boolean isBackPressed = false;
	private String mPhone, mPassword;
	private String mToken, mVenderId, mStoreId;

	//The mswipe controller instance used for calling up the api's
	private boolean isMswipeLoginSuccessful = true;
	private boolean isLoginSuccessful = false;
	private int versionClickedCount = 0;
	private VersionModel mVersionModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_mbasket);
		ButterKnife.bind(this);
		mActivity = this;

		/*mEtPassword.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));*/
		mEtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
			{
				if ( id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_DONE) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mEtLoginMobile.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
					mEtPassword.requestFocus();
					return true;
				}
				return false;
			}
		});

		//mEtLoginMobile.setText("9833195559");
		//mEtPassword.setText("305347");

		mEtLoginMobile.setText("9885135489");
		mEtPassword.setText("123456");

		forgotPwdView.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
		{
			@Override
			public void onClick(View v)
			{
				//startActivity(new Intent(v.getContext(), ForgotPasswordActivity.class));
			}
		}));

		signUpBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
		{
			@Override
			public void onClick(View v)
			{

				startActivity(new Intent(v.getContext(), AddCustomerActivity.class));
			}
		}));

		signInBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
			@Override
			public void onClick(View v)
			{
				attemptLogin();
			}
		}));

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (pInfo != null) {
			/*int version = pInfo.versionCode;*/
			String versionName = pInfo.versionName;
			versionNameTv.setText("Version " + versionName);
		}


	}

	@Override
	public void onBackPressed()
	{
		if (isBackPressed) {
			Toast.makeText(this, "Are you sure you want to exit the app?", Toast.LENGTH_SHORT).show();
			isBackPressed = true;
		} else {


			mActivity.finish();
		}
	}

	@OnClick({R.id.activity_login_imgbtn_show_password,
			R.id.activity_login_tv_tnc, R.id.activity_login_tv_privacy, R.id.activity_login_tv_version})
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.activity_login_imgbtn_show_password:
				togglePassword();
				break;
		}
	}


	private void togglePassword()
	{
		int selectionStart = mEtPassword.getSelectionStart();
		int selectionEnd = mEtPassword.getSelectionEnd();

		if (mImgbtnShowPassword.isSelected()) {
			mImgbtnShowPassword.setSelected(false);
			mEtPassword.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
		} else {
			mImgbtnShowPassword.setSelected(true);
			mEtPassword.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_NORMAL);
		}

		mEtPassword.setSelection(selectionStart, selectionEnd);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid mPhone, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLogin()
	{
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		mEtLoginMobile.setError(null);
		mEtPassword.setError(null);


		mPhone = mEtLoginMobile.getText().toString().trim();
		mPassword = mEtPassword.getText().toString().trim();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mPassword)) {
			mEtPassword.setError(getString(R.string.error_field_required));
			focusView = mEtPassword;
			cancel = true;
		} else if (!isPasswordValid(mPassword)) {
			mEtPassword.setError(getString(R.string.error_invalid_password));
			focusView = mEtPassword;
			cancel = true;
		}

		if (TextUtils.isEmpty(mPhone)) {
			mEtLoginMobile.setError(getString(R.string.error_field_required));
			focusView = mEtLoginMobile;
			cancel = true;
		} else if (!isPhoneValid(mPhone)) {
			mEtLoginMobile.setError(getString(R.string.error_invalid_phone_no));
			focusView = mEtLoginMobile;
			cancel = true;
		}

		if (cancel)
		{
			focusView.requestFocus();
		} else {
			showLoadingDialog();

			loginCustomer();

		}
	}

	private boolean isPhoneValid(String phone)
	{
		String str = phone.replaceAll("\\+", "");
		String expression = "[0-9]*$";
		CharSequence inputStr = str;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

	private boolean isPasswordValid(String password)
	{
		return password.length() > 5;
	}

	private void loginCustomer()
	{
		try
		{
			new LoginCustomerController(this, new CustomerNetworkObserver())
					.loginCustomer(mPhone, mPassword, String.valueOf(INTERNAL_MBASKET_VERSION_CODE));
		}
		catch (Exception ex ) {
			hideLoadingDialog();
			showErrorDialog(ErrorUtils.getFailureError(ex));
		}
	}

	class CustomerNetworkObserver implements MOMNetworkResponseListener
	{

		@Override
		public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
		{
			hideLoadingDialog();


			LoginCustomerResp responseData = (LoginCustomerResp) mMOMNetworkResDataStore;
			if(responseData.status ==1)
			{
				if (TextUtils.isEmpty(responseData.token))
				{
					isLoginSuccessful = false;
					Toast.makeText(mActivity, "Something went wrong. No Token", Toast.LENGTH_SHORT).show();
				}
				else {

					mToken = responseData.token;
					mVenderId = responseData.vendor_id;
					mStoreId = responseData.store_id;

					MOMApplication.getInstance().setToken(mToken);
					MOMApplication.getInstance().setVender(mVenderId);
					MOMApplication.getInstance().setStoreId(mStoreId);


					MOMApplication.getSharedPref().setName(responseData.cust_name);
					MOMApplication.getInstance().setMswipeUsername(mPhone);
					MOMApplication.getInstance().setPersonId(responseData.person_id);
					MOMApplication.getSharedPref().setDeliveryType(responseData.delivery_type);

					MOMApplication.getSharedPref().setCustAddress( responseData.customerPhone
						+ "||" + responseData.email + "||" + responseData.address_1 + "||"
						+ responseData.address_2 + "||" + responseData.city + "||" + responseData.state
						+ "||" + responseData.zip+ "||" + responseData.country+ "||" + responseData.land_mark);


					isLoginSuccessful = true;
					checkLoginStatus();
				}
			}
			else {
				String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
				Toast.makeText(mActivity, errorMsg, Toast.LENGTH_LONG).show();
			}


		}
	}

	private void checkLoginStatus()
	{
		//if (isLoginSuccessful && isMswipeLoginSuccessful)
		//this will go back to splash screen
		if (isLoginSuccessful)
		{
			hideLoadingDialog();
			mActivity.finish();
		}

	}

	private int count = 0;
	private long startMillis=0;
	//detect any touch event in the screen (instead of an specific view)
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		int eventaction = event.getAction();
		if (eventaction == MotionEvent.ACTION_UP)
		{

			//get system current milliseconds
			long time= System.currentTimeMillis();

			//if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
			if (startMillis==0 || (time-startMillis> 3000) ) {
				startMillis=time;
				count=1;
			}
			//it is not the first, and it has been  less than 3 seconds since the first
			else{ //  time-startMillis< 3000
				count++;
			}

			if (count==10)
			{
				Intent intent = new Intent(this, CrashActivity.class);
				startActivity(intent);


			}
			return true;
		}
		return false;
	}


}

