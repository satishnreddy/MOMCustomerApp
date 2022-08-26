package com.mom.momcustomerapp.views.customers;

import static com.mom.momcustomerapp.data.application.Consts.DIALOG_DISPLAY_TIME;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.github.mmin18.widget.RealtimeBlurView;

import com.mom.momcustomerapp.controllers.customers.api.EmployeeClient;
import com.mom.momcustomerapp.controllers.customers.models.CustomerModel;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.customviews.dialogs.OutputResultDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.utils.GlideLoader;
import com.mom.momcustomerapp.utils.UIUtils;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.views.shared.dialogs.ErrorDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerActivity extends BaseActivity implements
		CameraButton.CameraButtonResultListener
{

	@BindView(R.id.activity_add_customer_btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.activity_add_customer_et_mobile)
	EditText mEtMobile;
	@BindView(R.id.activity_add_customer_et_name)
	EditText mEtName;
	@BindView(R.id.activity_add_customer_et_email)
	EditText mEtEmail;
	@BindView(R.id.activity_add_customer_et_password)
	EditText mEtPassowrd;


	@BindView(R.id.activity_add_customer_et_gstin)
	EditText mEtGstin;
	@BindView(R.id.activity_add_customer_et_address)
	EditText mEtAddress;
	@BindView(R.id.activity_add_customer_et_address_line2)
	EditText mEtAddressLine2;
	@BindView(R.id.activity_add_customer_et_pincode)
	EditText mEtPincode;
	@BindView(R.id.activity_add_customer_et_city)
	EditText mEtCity;
	@BindView(R.id.activity_add_customer_et_state)
	EditText mEtState;
	@BindView(R.id.activity_add_customer_et_country)
	EditText mEtCountry;
	@BindView(R.id.blur_view)
	View mBlurView;
	@BindView(R.id.activity_add_customer_iv_image)
	ImageView mIvImage;
	@BindView(R.id.activity_add_customer_fab_photo)
	FloatingActionButton mFabPhoto;
	@BindView(R.id.activity_add_customer_et_address_landmark)
	EditText landmarkTv;
	/*@BindView(R.id.activity_add_customer_et_zip)
	EditText mEtZip;*/

	private CustomerModel mCustomerModel;
	private int activityMode = Consts.ACTIVITY_MODE_CREATE;
	private CameraButton mCamerabutton;
	private Uri mImageUri;
	private boolean isImageChanged = false;
	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customers_add_customer);
		ButterKnife.bind(this);
		mActivity = this;

		setupToolBar();
		loadIntentData();

		mEtMobile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((AppBarLayout) findViewById(R.id.appbar)).setExpanded(false);
			}
		});

		mEtPincode.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s)
			{
				String pincodeInStr = s.toString().trim();
				if (pincodeInStr.length() == 6)
				{
					//updateCityStateCountry(pincodeInStr);
				}
			}
		});
	}

	/**
	 * Set up the {@link Toolbar}.
	 */
	private void setupToolBar()
	{
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case android.R.id.home:
				UIUtils.closeKeyboard(AddCustomerActivity.this);
				finish();
				break;
		}
		return true;
	}

	private void loadIntentData()
	{
		Intent intent = getIntent();
		if (intent.hasExtra(Consts.EXTRA_ACTIVITY_MODE))
		{
			activityMode = intent.getIntExtra(Consts.EXTRA_ACTIVITY_MODE, Consts.ACTIVITY_MODE_CREATE);
			if (activityMode == Consts.ACTIVITY_MODE_EDIT)
			{
				setTitle(getString(R.string.title_activity_edit_customer));

				new Handler().post(new Runnable()
				{
					@Override
					public void run()
					{
						getCustomer();
					}
				});

			}
		}


	}

	@SuppressLint("RestrictedApi")
	private void loadModelIfRequired()
	{
		mCamerabutton = new CameraButton(this);
		mCamerabutton.setCameraButtonResultListener(this);

		if (activityMode == Consts.ACTIVITY_MODE_EDIT && mCustomerModel != null)
		{
			mEtName.setText(mCustomerModel.getName());
			mEtMobile.setText(mCustomerModel.getPhone_number());
			mEtEmail.setText(mCustomerModel.getEmail());
			mEtGstin.setText(mCustomerModel.getGstin());
			mEtAddress.setText(mCustomerModel.getAddress_1());
			mEtAddressLine2.setText(mCustomerModel.getAddress_2());
			mEtPincode.setText(mCustomerModel.getZip());
			mEtCity.setText(mCustomerModel.getCity());
			mEtState.setText(mCustomerModel.getState());
			mEtCountry.setText(mCustomerModel.getCountry());
			landmarkTv.setText(mCustomerModel.getLand_mark());

			if (mCustomerModel.getImage() != null && !mCustomerModel.getImage().isEmpty()
					&& !TextUtils.isEmpty(mCustomerModel.getImage().get(0).getImage_path()))
			{
				GlideLoader.url(this).load(mCustomerModel.getImage().get(0).getImage_path()).into(mIvImage);
				//mBlurView.setOverlayColor(Color.parseColor("#CCffffff"));
			}
		} else if (activityMode == Consts.ACTIVITY_MODE_QUICK_ADD)
		{
			mEtAddress.setVisibility(View.GONE);
			mEtPincode.setVisibility(View.GONE);
			mEtCity.setVisibility(View.GONE);
			mEtState.setVisibility(View.GONE);
			mEtCountry.setVisibility(View.GONE);

			mIvImage.setVisibility(View.GONE);
			mBlurView.setVisibility(View.GONE);
			mFabPhoto.setVisibility(View.GONE);

			((AppBarLayout) findViewById(R.id.appbar)).setExpanded(false);
		}
	}

	@OnClick({R.id.activity_add_customer_fab_photo, R.id.activity_add_customer_btn_submit})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.activity_add_customer_fab_photo:
				mCamerabutton.performClick();
				break;
			case R.id.activity_add_customer_btn_submit:
				View view = this.getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

				validateData();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CameraButton.TAKE_PICTURE_REQUEST_CODE || requestCode == CameraButton.SELECT_PICTURE_REQUEST_CODE || requestCode == CameraButton.AFTER_IMAGE_CROPPING) {
			mCamerabutton.handleActivityResults(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == CameraButton.REQUEST_WRITE_EXTERNAL_STORAGE || requestCode == CameraButton.REQUEST_CAMERA) {
			mCamerabutton.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void imageSaved(Uri uri) {
		//Log.d("Nish", "Photo Selected : " + uri.getPath());
		mImageUri = uri;
		isImageChanged = true;
		mIvImage.setImageURI(uri);
		//mBlurView.setOverlayColor(Color.parseColor("#CCffffff"));
	}

	/*private void uploadImage() {
		if (mImageUri != null) {
			if (!TextUtils.isEmpty(mCustomerModel.getPerson_id())) {
				String stringUri = mImageUri.getPath();
				if (stringUri.contains("root/")) {
					stringUri = stringUri.replace("root/", "");
					stringUri = Environment.getExternalStorageDirectory().getAbsolutePath() + stringUri;
				}
				uploadProfileImage(stringUri, mCustomerModel.getPerson_id());
			} else {
				Intent intent = new Intent();
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	}*/


	private void validateData()
	{
		String phoneNo = "", name = "", email = "", gstin = "", address1 = "", pincode = "",address2 = "", landMarkStr = "";
		String city = "", state = "", country = "";
		boolean cancel = false;
		View focusView = null;

		if (!TextUtils.isEmpty(mEtMobile.getText()))
		{
			phoneNo = mEtMobile.getText().toString().trim();

			if (isPhoneValid(phoneNo))
			{
				if (!TextUtils.isEmpty(mEtName.getText())) {
					name = mEtName.getText().toString().trim();
				} else {
					mEtName.setError(getString(R.string.error_field_required));
					focusView = mEtName;
					cancel = true;
				}
			} else {
				mEtMobile.setError(getString(R.string.error_invalid_phone_no));
				focusView = mEtMobile;
				cancel = true;
			}



			address1 = mEtAddress.getText().toString().trim();
			address2 = mEtAddressLine2.getText().toString().trim();
			if (address1.isEmpty()){
				mEtAddress.setError(getString(R.string.error_invalid_empty_address_line1));
				focusView = mEtAddress;
				cancel = true;
			}

			if (address2.isEmpty()){
				mEtAddressLine2.setError(getString(R.string.error_invalid_empty_address_line2));
				focusView = mEtAddressLine2;
				cancel = true;
			}
		} else {
			mEtMobile.setError(getString(R.string.error_field_required));
			focusView = mEtMobile;
			cancel = true;
		}

		String pinCodeTrimmed = mEtPincode.getText().toString().trim();
		if (pinCodeTrimmed.length() != 6)
		{
			mEtPincode.setError(getString(R.string.error_field_required_pincode));
			focusView = mEtPincode;
			cancel = true;
		}

		city = mEtCity.getText().toString().trim();
		if (city.length() ==0)
		{
			mEtCity.setError(getString(R.string.error_field_required_city));
			focusView = mEtCity;
			cancel = true;
		}

		state = mEtState.getText().toString().trim();
		if (state.length() ==0)
		{
			mEtState.setError(getString(R.string.error_field_required_state));
			focusView = mEtState;
			cancel = true;
		}

		country = mEtCountry.getText().toString().trim();
		if (country.length() ==0)
		{
			mEtCountry.setError(getString(R.string.error_field_required_country));
			focusView = mEtCountry;
			cancel = true;
		}


		String paswword = mEtPassowrd.getText().toString().trim();

		if (paswword.length() != 6)
		{
			mEtPassowrd.setError(getString(R.string.error_field_required_password));
			focusView = mEtPassowrd;
			cancel = true;
		}

		if (!cancel)
		{
			if (!TextUtils.isEmpty(mEtEmail.getText())) {
				email = mEtEmail.getText().toString().trim();

                if (!isEmailValid(email)) {
                    mEtEmail.setError(getString(R.string.error_invalid_email));
                    focusView = mEtEmail;
                   	cancel = true;
                }
			}

			if (!TextUtils.isEmpty(mEtGstin.getText())) {
				gstin = mEtGstin.getText().toString().trim();
			}

			if (!TextUtils.isEmpty(mEtAddress.getText())) {
				address1 = mEtAddress.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(mEtAddressLine2.getText())) {
				address2 = mEtAddressLine2.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(mEtPincode.getText())) {
				pincode = pinCodeTrimmed;
			}
			if (!TextUtils.isEmpty(mEtCity.getText())) {
				city = mEtCity.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(mEtState.getText())) {
				state = mEtState.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(mEtCountry.getText())) {
				country = mEtCountry.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(landmarkTv.getText())){
				landMarkStr = landmarkTv.getText().toString().trim();
			}
		}

		if (cancel)
		{
			focusView.requestFocus();
		} else {
			if (mCustomerModel == null) {
				mCustomerModel = new CustomerModel();
			}

			mCustomerModel.setPhone_number(phoneNo);
			mCustomerModel.setName(name);
			mCustomerModel.setEmail(email);
			mCustomerModel.setGstin(gstin);
			mCustomerModel.setAddress_1(address1);
			mCustomerModel.setAddress_2(address2);
			mCustomerModel.setZip(pincode);
			mCustomerModel.setCity(city);
			mCustomerModel.setState(state);
			mCustomerModel.setCountry(country);
			mCustomerModel.setLand_mark(landMarkStr);
			mCustomerModel.setPassword(paswword);

			if (activityMode == Consts.ACTIVITY_MODE_CREATE || activityMode == Consts.ACTIVITY_MODE_QUICK_ADD) {
				submitCustomer();
			} else {
				updateCustomer();
			}
		}
	}

	private boolean isPhoneValid(String phone) {
		CharSequence inputStr = phone.replaceAll("\\+", "");

		String expression = "[0-9]*$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

	private boolean isEmailValid(String emailAddress) {
		if (emailAddress.contains("@")) {
			String emailLocalPart = emailAddress.substring(emailAddress.indexOf("@"));
			if (emailLocalPart.length() > 64) {
				return false;
			}
		}

		if (emailAddress.length() > 254) {
			return false;
		}

		String expression = "[A-Za-z0-9][\\.\\w]*+@([\\w]+\\.)+[A-Za-z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	}

	private void submitCustomer()
	{
		showLoadingDialog();
		//CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class);

		Call<CustomerModel> call = customerClient.signupCustomer(
				mCustomerModel.getName(), mCustomerModel.getEmail(), mCustomerModel.getGstin(),
				mCustomerModel.getPhone_number(), mCustomerModel.getAddress_1(), mCustomerModel.getAddress_2(),mCustomerModel.getLand_mark(), mCustomerModel.getZip(),
				mCustomerModel.getCity(), mCustomerModel.getState(), mCustomerModel.getCountry(),
				mCustomerModel.getPassword());

		call.enqueue(new Callback<CustomerModel>() {
			@Override
			public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
				hideLoadingDialog();
				if (response.isSuccessful()) {
					CustomerModel customerModel = response.body();
					if (customerModel != null && !TextUtils.isEmpty(customerModel.getPerson_id())) {
						/*if (isImageChanged) {
							mCustomerModel = customerModel;
							*//*uploadImage();*//*
						} else {*/
						showProductAddedDialog();



						//}
					}
					else {
						showErrorDialog(getString(R.string.invalid_server_response));
					}
				} else {
					showErrorDialog(ErrorUtils.getErrorString(response));
				}
			}

			@Override
			public void onFailure(Call<CustomerModel> call, Throwable t) {
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t));
				if (!(t instanceof IOException)) {
				}
			}
		});
	}


	private void showProductAddedDialog()
	{
		final OutputResultDialogFragment acceptDeclineDialog = OutputResultDialogFragment
				.newInstance(true,
						getString(activityMode == Consts.ACTIVITY_MODE_EDIT ? R.string.dialog_message_new_customer_updated :  R.string.dialog_message_new_customer_created),
						null);
		try
		{
			acceptDeclineDialog.show(this.getSupportFragmentManager(), "dialog");
		}
		catch (IllegalStateException e){
			e.printStackTrace();
			//If view is already destroyed , then ignore
		}

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run() {
				if (!isFinishing()) {
					try{
						if (acceptDeclineDialog.isVisible()) acceptDeclineDialog.dismiss();
					}
					catch (IllegalStateException e){
						e.printStackTrace();
						//If view is already destroyed , then ignore
					}

					finish();
				}
			}
		}, DIALOG_DISPLAY_TIME);
	}

	private void updateCustomer()
	{
		showLoadingDialog();
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		Call<CustomerModel> call = customerClient.updateCustomerDetails(
				MOMApplication.getInstance().getPersonId(),MOMApplication.getInstance().getVender(),
				MOMApplication.getInstance().getStoreId(), MOMApplication.getInstance().getMswipeUsername(),
				mCustomerModel.getName(), mCustomerModel.getEmail(), mCustomerModel.getGstin(),
				mCustomerModel.getPhone_number(), mCustomerModel.getAddress_1(),  mCustomerModel.getAddress_2(), mCustomerModel.getLand_mark(), mCustomerModel.getZip(),
				mCustomerModel.getCity(), mCustomerModel.getState(), mCustomerModel.getCountry(),
				mCustomerModel.getPassword());

		call.enqueue(new Callback<CustomerModel>() {
			@Override
			public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
				hideLoadingDialog();
				if (response.isSuccessful()) {
					CustomerModel customerModel = response.body();
					if (customerModel != null && !TextUtils.isEmpty(customerModel.getPerson_id())) {

							showProductAddedDialog();

					}
					else {
						showErrorDialog(getString(R.string.invalid_server_response), new ErrorDialogFragment.ErrorDialogListener() {
							@Override
							public void onDialogPositiveClick() {
								finish();
							}
						});
					}
				} else {
					showErrorDialog(ErrorUtils.getErrorString(response), new ErrorDialogFragment.ErrorDialogListener() {
						@Override
						public void onDialogPositiveClick() {
							finish();
						}
					});
				}
			}

			@Override
			public void onFailure(Call<CustomerModel> call, Throwable t) {
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t), new ErrorDialogFragment.ErrorDialogListener() {
					@Override
					public void onDialogPositiveClick() {
						finish();
					}
				});
			}

		});
	}


	public void getCustomer()
	{
		showLoadingDialog();
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());

		Call<CustomerModel> call = customerClient.getCustomerDetails(
				MOMApplication.getInstance().getPersonId(),MOMApplication.getInstance().getVender(),
				MOMApplication.getInstance().getStoreId(), MOMApplication.getInstance().getMswipeUsername()
				);
		call.enqueue(new Callback<CustomerModel>() {
			@Override
			public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response)
			{
				hideLoadingDialog();
				if (response.isSuccessful()) {
					CustomerModel customerModel = response.body();
					if (customerModel != null) {
						mCustomerModel = customerModel;
						loadModelIfRequired();



					} else {
						showErrorDialog(getString(R.string.invalid_server_response), new ErrorDialogFragment.ErrorDialogListener() {
							@Override
							public void onDialogPositiveClick() {
								finish();;
							}
						});
					}
				} else {
					showErrorDialog(ErrorUtils.getErrorString(response), new ErrorDialogFragment.ErrorDialogListener() {
						@Override
						public void onDialogPositiveClick() {
							finish();
						}
					});
				}
			}

			@Override
			public void onFailure(Call<CustomerModel> call, Throwable t) {
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t), new ErrorDialogFragment.ErrorDialogListener() {
					@Override
					public void onDialogPositiveClick() {
						finish();
					}
				});

			}
		});
	}

	private void updateCityStateCountry(String pincodeInStr)
	{
		showLoadingDialog();

		EmployeeClient employeeClient = ServiceGenerator.createService(EmployeeClient.class, MOMApplication.getInstance().getToken());
		Call<String> call = employeeClient.getLocationDetails(pincodeInStr);

		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				hideLoadingDialog();
				if (response.isSuccessful()){
					String body = response.body();
					try {
						JSONObject jsonObject = new JSONObject(body);
						JSONArray responseMessage = jsonObject.getJSONArray("pincodeResponseDetails");
						if (responseMessage.length() > 0){
							JSONObject firstResult = responseMessage.getJSONObject(0);
							String cityName = firstResult.getString("cityName");
							String stateName = firstResult.getString("stateName");
							String countryName = firstResult.getString("countryName");
							mEtCity.setText(cityName);
							mEtState.setText(stateName);
							mEtCountry.setText(countryName);
						}
						else {
							showErrorDialog(getString(R.string.city_state_not_found_from_pincode));
							mEtCity.setText("");
							mEtState.setText("");
							mEtCountry.setText("");
						}
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog(getString(R.string.invalid_server_response));
					}
				}
				else {
					showErrorDialog(ErrorUtils.getErrorString(response));
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t));
			}
		});

	}
}
