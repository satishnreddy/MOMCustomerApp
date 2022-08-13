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

		//mEtLoginMobile.setText("9885135489");
		//mEtPassword.setText("988513");

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
			public void onClick(View v) {
				startActivityForResult(new Intent(LoginActivity.this, AddCustomerActivity.class), REQUEST_CODE_CHANGE_LANGUAGE);
			}
		}));

		signInBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
			@Override
			public void onClick(View v) {
				/*showCategorySelection();*/
				attemptLogin();
			}
		}));


		//setTermsCondition();

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

		boolean fromSplash = getIntent().getBooleanExtra("fromSplash", false);

		if(fromSplash){
			/*
			mMSWisepadController = MSWisepadController.
					getSharedMSWisepadController(getApplicationContext(),
							ShopxieSharedPreferences.getInstance().getMswipeGatewayEnvironmentName(),
						//ShopxieSharedPreferences.getInstance().getMswipeNetworkSourceName(mActivity),
							MSWisepadController.NETWORK_SOURCE.WIFI,
							LoginActivity.this);

			*/

			//checkUpdates();

		}
		else{
			startActivity(new Intent(LoginActivity.this, SplachScreenActivity.class));
			finish();
		}

		//getLinkData();
	}


	private String mReferralID = "";

	private void getLinkData()
	{

		//Log.d(TAG, "getLinkData");
		/*
		FirebaseDynamicLinks.getInstance()
				.getDynamicLink(getIntent())
				.addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
					@Override
					public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
						// Get deep link from result (may be null if no link is found)

						//Log.d(TAG, "onSuccess");
						//Log.d(TAG, "pendingDynamicLinkData: " + pendingDynamicLinkData);

						Uri deepLink = null;

						if (pendingDynamicLinkData != null) {
							deepLink = pendingDynamicLinkData.getLink();
						}

						if(deepLink != null) {

							mReferralID = deepLink.getQueryParameter("referrerid");

							if(mReferralID != null && mReferralID.length() > 0) {
								ShopxieSharedPreferences.getInstance().setReferralID(mReferralID);
							}

							if (BuildConfig.APPLICATION_ID.contains("dev")) {

								Log.d(TAG, "mReferralID: " + mReferralID);
								//Toast.makeText(LoginActivity.this, "LID: " + mReferralID, Toast.LENGTH_SHORT).show();
							}
						}
					}
				})
				.addOnFailureListener(this, new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {

						Log.d(TAG, "onFailure");

					}
				});


		*/
	}

	private void showCategorySelection()
	{


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.menu_registration_help:
				helpClicked();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void helpClicked()
	{
		/*Intent intentHelp = new Intent(mActivity, HelpActivity.class);
		intentHelp.putExtra(Consts.EXTRA_HELP_INDEX, Consts.EXTRA_HELP_REGISTRATION);
		startActivity(intentHelp);*/
	}

	@Override
	public void onBackPressed()
	{
		if (isBackPressed) {
			Toast.makeText(this, "Are you sure you want to exit the app?", Toast.LENGTH_SHORT).show();
			isBackPressed = true;
		} else {
			super.onBackPressed();
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
			/*case R.id.activity_login_tv_tnc:
				Intent intentTnc = new Intent(mActivity, TermsConditionsActivity.class);
				intentTnc.putExtra(Consts.EXTRA_TITLE, "tnc");
				startActivity(intentTnc);
				break;*/
			/*case R.id.activity_login_tv_privacy:
				Intent intentPrivacy = new Intent(mActivity, TermsConditionsActivity.class);
				intentPrivacy.putExtra(Consts.EXTRA_TITLE, "privacy");
				startActivity(intentPrivacy);
				break;*/
			/*case R.id.activity_login_tv_version:
				versionClickedCount++;
				if (versionClickedCount >= 5) {
					if (BuildConfig.APPLICATION_ID.contains("dev") || BuildConfig.APPLICATION_ID.contains("demo")) {
						openUrlEditorDialog();
					}
					versionClickedCount = 0;
				}
				break;*/
		}
	}

	private void openUrlEditorDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("Server Url");
		builder.setMessage("Please Set Server Url");
		final EditText etUrl = new EditText(mActivity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		etUrl.setLayoutParams(lp);
		etUrl.setText(MOMApplication.getInstance().getServerUrl());
		builder.setView(etUrl);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!TextUtils.isEmpty(etUrl.getText())) {
					//MOMApplication.getInstance().setServerUrl(etUrl.getText().toString().trim());
					dialog.dismiss();
				} else {
					etUrl.setError("Please enter url");
					etUrl.requestFocus();
				}
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setCancelable(false);
		if (!mActivity.isFinishing()) {
			builder.show();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_CHANGE_LANGUAGE) {
			if (resultCode == Activity.RESULT_OK)
			{
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		}
	}

	private void loginMswipe()
	{

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
			Intent intent = new Intent();
			intent.putExtra(Consts.EXTRA_TOKEN, mToken);
			intent.putExtra(Consts.EXTRA_VENDER_ID, mVenderId);
			intent.putExtra(Consts.EXTRA_STORE_ID, mStoreId);

			mActivity.setResult(Activity.RESULT_OK, intent);
			mActivity.finish();
		}

	}

	public void checkUpdates()
	{
		String url;
		if (BuildConfig.APPLICATION_ID.contains("dev")) {
			url = getString(R.string.update_dev);
		} else if (BuildConfig.APPLICATION_ID.contains("demo")) {
			url = getString(R.string.update_demo);
		} else {
			url = getString(R.string.update_prod);
		}

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(url)
				.get()
				.build();

		client.newCall(request).enqueue(new okhttp3.Callback() {
			@Override
			public void onFailure(okhttp3.Call call, IOException e) {
				call.cancel();
			}

			@Override
			public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
				if (response.isSuccessful()) {
					final String responseString = response.body().string();
					//Log.i("Nish", "Response : " + responseString);
					mVersionModel = new Gson().fromJson(responseString, VersionModel.class);
					if (mVersionModel != null) {
						showUpdeteDialog();
					}
				} else {
					//Log.i("Nish", "Update failed");
					//Log.i("Nish", "Code : " + response.code());
					//Log.i("Nish", "Message : " + response.message());
					if (response.body() != null) {
						//Log.i("Nish", "Body : " + response.body().string());
					}
				}
			}
		});
	}

	public void showUpdeteDialog()
	{
		if (!mayRequestStorage()) {
			return;
		}
		try {
			int currentVersionCode;
			int installedVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

			currentVersionCode = mVersionModel.getCurrentVersion();
			int forceInstallCode = mVersionModel.getForcedVersions();
			boolean isForced = false;

			//Log.i("Nish", "installedVersionCode : " + installedVersionCode);
			//Log.i("Nish", "currentVersionCode : " + currentVersionCode);
			//Log.i("Nish", "forceInstallCode : " + forceInstallCode);

			if (installedVersionCode < currentVersionCode) {
				if (installedVersionCode < forceInstallCode) {
					isForced = true;
				} else if (installedVersionCode != 0 && installedVersionCode < currentVersionCode) {
					isForced = false;
				}

				final CheckUpdateDialogFragment updateDialogFragment = new CheckUpdateDialogFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelable("versionModel", mVersionModel);
				bundle.putBoolean("forced", isForced);
				updateDialogFragment.setArguments(bundle);

				updateDialogFragment.setFileDownloadListener(new CheckUpdateDialogFragment.FileDownloadListener() {
					@Override
					public void onFileDownloadClicked() {
						Toast.makeText(mActivity, "New version download started. Please install it and open app again.", Toast.LENGTH_LONG).show();
						finish();
					}
				});

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				Fragment prev = getSupportFragmentManager().findFragmentByTag(CheckUpdateDialogFragment.FRAGMENT_TAG);
				if (prev != null) {
					ft.remove(prev);
				}
				updateDialogFragment.show(ft, CheckUpdateDialogFragment.FRAGMENT_TAG);
			}

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private boolean mayRequestStorage()
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		int permissionWrite = mActivity.checkSelfPermission(WRITE_EXTERNAL_STORAGE);
		if (permissionWrite == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		List<String> listPermissionsNeeded = new ArrayList<>();
		if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
			//listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			mActivity.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CameraButton.REQUEST_ID_MULTIPLE_PERMISSIONS);
			return false;
		}
		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == CameraButton.REQUEST_WRITE_EXTERNAL_STORAGE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				showUpdeteDialog();
			}
		}

		if (requestCode == CameraButton.REQUEST_ID_MULTIPLE_PERMISSIONS) {
			showUpdeteDialog();
		}
	}




	@Override
	protected void onResume() {
		super.onResume();
		if (BuildConfig.DEBUG){
			//FacebookSdk.setIsDebugEnabled(true);
			//FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
		}
	}
}

