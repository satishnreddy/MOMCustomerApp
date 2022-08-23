package com.mom.momcustomerapp.views.sales;

import static com.mom.momcustomerapp.data.application.Consts.DIALOG_DISPLAY_TIME;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.login.LoginCustomerController;
import com.mom.momcustomerapp.controllers.login.model.LoginCustomerResp;
import com.mom.momcustomerapp.controllers.sales.SalesCompleteController;
import com.mom.momcustomerapp.controllers.sales.SalesGetDeliveryTimeSlotsController;
import com.mom.momcustomerapp.controllers.sales.models.SalesCompleteResp;
import com.mom.momcustomerapp.controllers.sales.models.SalesDeliveryTimeSlotsResp;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlots;
import com.mom.momcustomerapp.customviews.dialogs.OutputResultDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.sales.dialogs.DeliverySlotstDialogFragment;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.views.shared.dialogs.ErrorDialogFragment;
import com.mom.momcustomerapp.widget.SafeClickListener;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SalesCheckoutDetailsActivity extends BaseActivity
{

	@BindView(R.id.checkout_checkbox_wallet_balance)
	CheckBox mChkWallet;

	@BindView(R.id.checkout_radioGroup_payment)
	RadioGroup mRadGrp_payment;
	@BindView(R.id.checkout_radio_cash)
	RadioButton mRadio_cash;


	int mRadGrp_payment_checkedId = 0;

	@BindView(R.id.checkout_radioGroup_delivery)
	RadioGroup mRadGrp_delivery;
	@BindView(R.id.checkout_radio_pickupstore)
	RadioButton mRadio_pickup;
	@BindView(R.id.checkout_radio_homedelivery)
	RadioButton mRadio_home;

	int mRadGrp_delivery_checkedId = 0;
	int mDelivery_type = 0;


	@BindView(R.id.activity_order_details_tv_grand_total)
	TextView mTvGrandTotal;

	@BindView(R.id.activity_order_details_tv_voucher_amount)
	TextView mTxtWalletAmount;

	@BindView(R.id.activity_order_details_tv_total_amount)
	TextView mTxtTotalAmount;

	@BindView(R.id.checkout_lnr_voucher)
	LinearLayout mLnr_voucher;


	@BindView(R.id.activity_order_details_tv_action1)
	TextView bottomAction1Tv;
	@BindView(R.id.activity_order_details_tv_action2)
	TextView bottomAction2Tv;

	@BindView(R.id.checkout_txt_address1)
	TextView mTxtAddress1;
	@BindView(R.id.checkout_txt_address2)
	TextView mTxtAddress2;
	@BindView(R.id.checkout_txt_city)
	TextView mTxtCity;
	@BindView(R.id.checkout_txt_state)
	TextView mTxtState;
	@BindView(R.id.checkout_txt_zip)
	TextView mTxtZip;
	@BindView(R.id.checkout_txt_country)
	TextView mTxtCountry;
	@BindView(R.id.checkout_txt_landmark)
	TextView mTxtLandmark;
	@BindView(R.id.activity_order_details_tv_delivery_slot)
	TextView mTxtDeliverySlot;
	@BindView(R.id.activity_order_details_iv_delivery_slot)
	ImageView mIVDeliverySlot;





	private List<TimeSlots> mLstTimeSlots;
	private String mCurrentDate = "";

	private double mTotalAmount = 0.0;
	private double mWalletAmount = 0.0;
	private Activity mActivity;
	private boolean retryApi = false;
	private String mStDeliveryDate = "";
	private String mMobile = "";
	private String mEmail  ="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales_checkout_details);
		ButterKnife.bind(this);
		mActivity = this;

		setupToolBar();

		loadIntentData();

		if(mDelivery_type == 0)
		{
			mRadio_home.setVisibility(View.GONE);
			mRadio_pickup.setChecked(true);
			mRadio_pickup.setEnabled(false);

		}
		else if(mDelivery_type == 1)
		{
			mRadio_pickup.setChecked(true);
			mRadio_pickup.setEnabled(false);

			mRadio_home.setVisibility(View.GONE);
		}

		mRadGrp_payment.setEnabled(false);
		mRadio_cash.setChecked(true);
		mRadio_cash.setEnabled(false);


		Handler uiHandler = new Handler(Looper.getMainLooper());
		uiHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				getCustWalletDetails();
			}
		});

		//customerPhone, email, address_1, address_2, city, state, zip, country, land_mark
		String address = MOMApplication.getSharedPref().getCustAddress();
		String add[] = address.split("\\|\\|");

		mMobile = add[0];
		mEmail  = add[1];

		mTxtAddress1.setText(add[2]);
		mTxtAddress2.setText(add[3]);
		mTxtCity.setText(add[4]);
		mTxtState.setText(add[5] );
		mTxtZip.setText(add[6]);
		mTxtCountry.setText(add[7]);
		mTxtLandmark.setText(add[8]);
		mTxtDeliverySlot.setText("00.00 - 00.00");


		bottomAction1Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
		{
			@Override
			public void onClick(View v)
			{
				//hard coding since delivery slot time have been disabled for this applicaion
				Calendar c = Calendar.getInstance();
				mStDeliveryDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
				mStDeliveryDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);

				if (mRadGrp_payment.getCheckedRadioButtonId() == -1)
				{
					showErrorDialog("Select payment type");
				}
				else if (mRadGrp_delivery.getCheckedRadioButtonId() == -1){
					showErrorDialog("Select Delivery type");
				}
				else if (mRadGrp_delivery.getCheckedRadioButtonId() == mRadio_home.getId()) {
					if (mTxtAddress1.getText().length() == 0)
					{
						showErrorDialog("Invalid address 1");
						return;
					} else if (mTxtCity.getText().length() == 0) {
						showErrorDialog("Invalid city");
						return;
					} else if (mTxtZip.getText().length() == 0) {
						showErrorDialog("Invalid Zip");
						return;
					} else if (mTxtState.getText().length() == 0) {
						showErrorDialog("Invalid State");
						return;
					} else if (mTxtCountry.getText().length() == 0) {
						showErrorDialog("Invalid Country");
						return;

					}
				}

				String paymentType = "Cash";
				if(mRadGrp_payment.getCheckedRadioButtonId() == mRadio_home.getId()){
					paymentType = "Cash";
				}
				String wallet_amount = "0";
				if(mChkWallet.isSelected() && mWalletAmount > 0) {
					wallet_amount = mWalletAmount + "";
				}

				String name = MOMApplication.getSharedPref().getName();
				String storename = MOMApplication.getSharedPref().getStoreName();
				try
				{

				String request = "delivery_type=" + mRadGrp_delivery_checkedId + "&address_1=" +
						 URLEncoder.encode(mTxtAddress1.getText().toString(), "utf-8") +
						"&payment_type=" + paymentType + "&wallet_amount=" + wallet_amount +
						"&mobile_no=" + mMobile + "&email_id=" + URLEncoder.encode(mEmail , "utf-8")+
						"&address_2=" + URLEncoder.encode(mTxtAddress2.getText().toString(), "utf-8")  +
						"&zip=" + URLEncoder.encode(mTxtZip.getText().toString(), "utf-8") +
						"&land_mark=" + URLEncoder.encode(mTxtLandmark.getText().toString(), "utf-8") +
						 "&city=" + URLEncoder.encode(mTxtCity.getText().toString(), "utf-8") +
						 "&state=" + URLEncoder.encode(mTxtState.getText().toString(), "utf-8") +
						 "&country=" + URLEncoder.encode(mTxtCountry.getText().toString(), "utf-8") +
						"&customer_name=" + URLEncoder.encode(name,  "utf-8") +
						"&store_name=" + URLEncoder.encode(storename, "utf-8") +
						"&trx_id=0" + "&mqrid=0" + "&delivery_date=" + mStDeliveryDate +
						"&delivery_slot=" + URLEncoder.encode(mTxtDeliverySlot.getText().toString(), "utf-8") +
						"&permit_image=";




					showLoadingDialog();
					new SalesCompleteController( mActivity, new MOMNetworkResponseListener()
					{
						@Override
						public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
						{
							hideLoadingDialog();

							SalesCompleteResp responseData = (SalesCompleteResp) mMOMNetworkResDataStore;
							if(responseData.status ==1)
							{


								showProductAddedDialog();
							}
							else {
								String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
								showErrorDialog(errorMsg, new ErrorDialogFragment.ErrorDialogListener() {
									@Override
									public void onDialogPositiveClick() {
										finish();
									}
								});
							}


						}
					}).saveOrder(request);

				}
				catch (Exception ex ) {
					hideLoadingDialog();
					showErrorDialog(ErrorUtils.getFailureError(ex), new ErrorDialogFragment.ErrorDialogListener() {
						@Override
						public void onDialogPositiveClick() {
							finish();
						}
					});
				}

		}

		}));

		bottomAction2Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
		{
			@Override
			public void onClick(View v)
			{

				finish();
			}

		}));

		mRadGrp_delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if(mRadio_home.getId() == checkedId) {
					mRadGrp_delivery_checkedId = 0;


					//getDeliveryTimeSlots(0 + "");

				}
				else if(mRadio_pickup.getId() == checkedId) {
					mRadGrp_delivery_checkedId = 1;
					//getDeliveryTimeSlots(1 + "");

				}

			}
		});

		mIVDeliverySlot.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				getDeliveryTimeSlots(0 + "");
			}
		});

		mRadGrp_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				mRadGrp_payment_checkedId = checkedId;
			}
		});

		mChkWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				getCustWalletDetails();
			}
		});


	}

	private void loadIntentData()
	{
		if (getIntent().hasExtra(Consts.EXTRA_ORDER_AMOUNT))
		{
			mTotalAmount = getIntent().getDoubleExtra(Consts.EXTRA_ORDER_AMOUNT, 0);
		}

		mDelivery_type = MOMApplication.getSharedPref().getDeliveryType();
	}

	/**
	 * Set up the {@link Toolbar}.
	 */
	private void setupToolBar()
	{
		/*
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null)
		{
			setSupportActionBar(toolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		/*switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent();
				intent.putExtra(Consts.EXTRA_ISUPDATED, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
		}
		return true;
		*/
		return true;

	}

	private void updateItemsOnUI()
	{


		if(mWalletAmount <= 0)
		{
			mLnr_voucher.setVisibility(View.GONE);
			mChkWallet.setEnabled(false);
			mChkWallet.setChecked(false);
		}
		else {
			mLnr_voucher.setVisibility(View.VISIBLE);
			mChkWallet.setEnabled(true);
		}
		if(mChkWallet.isSelected()) {
			mTxtTotalAmount.setText(Consts.getCommaFormatedStringWithDecimal(Float.parseFloat(mTotalAmount + "")));
			mTxtWalletAmount.setText(Consts.getCommaFormatedStringWithDecimal(Float.parseFloat(mWalletAmount + "")));
			mTvGrandTotal.setText(Consts.getCommaFormatedStringWithDecimal(Float.parseFloat((mTotalAmount - mWalletAmount) + "")));
		} else {
			mTvGrandTotal.setText(Consts.getCommaFormatedStringWithDecimal(Float.parseFloat((mTotalAmount) + "")));

		}

		if(mDelivery_type == 0)
		{
			mRadio_pickup.setVisibility(View.GONE);
			mRadio_home.setVisibility(View.VISIBLE);
			mRadio_home.setChecked(true);
			mRadio_home.setEnabled(false);

		}
		else if(mDelivery_type == 1)
		{
			mRadio_pickup.setVisibility(View.VISIBLE);
			mRadio_home.setVisibility(View.GONE);
			mRadio_pickup.setVisibility(View.VISIBLE);
			mRadio_pickup.setChecked(true);
			mRadio_pickup.setEnabled(false);

		}else {
			mRadio_home.setVisibility(View.VISIBLE);
			mRadio_pickup.setVisibility(View.VISIBLE);
		}
	}

	public void getCustWalletDetails()
	{
		try
		{
			showLoadingDialog();
			new LoginCustomerController( mActivity, new MOMNetworkResponseListener()
			{
				@Override
				public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
				{
					hideLoadingDialog();
					LoginCustomerResp responseData = (LoginCustomerResp) mMOMNetworkResDataStore;
					if(responseData.status ==1)
					{

						mWalletAmount = ((LoginCustomerResp) mMOMNetworkResDataStore).waller_amt;
						mDelivery_type = ((LoginCustomerResp) mMOMNetworkResDataStore).delivery_type;
						updateItemsOnUI();


					}
					else {
						String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
						showErrorDialog(getString(R.string.title_checkout), errorMsg,
								new ErrorDialogFragment.ErrorDialogListener()
								{
									@Override
									public void onDialogPositiveClick()
									{
										finish();
									}
								});

					}


				}
			}).getCustWallet();

		}
		catch (Exception ex )
		{
			hideLoadingDialog();
			showErrorDialog(getString(R.string.title_checkout), ErrorUtils.getFailureError(ex),

			new ErrorDialogFragment.ErrorDialogListener()
			{
				@Override
				public void onDialogPositiveClick()
				{
					finish();
				}
			});

		}

	}

	public void getDeliveryTimeSlots(final String delivery_type)
	{
		try
		{
			showLoadingDialog();
			new SalesGetDeliveryTimeSlotsController( mActivity, new MOMNetworkResponseListener()
			{
				@Override
				public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
				{
					hideLoadingDialog();
					SalesDeliveryTimeSlotsResp responseData = (SalesDeliveryTimeSlotsResp) mMOMNetworkResDataStore;
					if(responseData.status ==1)
					{

						mLstTimeSlots = responseData.getTimeSlots();
						mCurrentDate = responseData.current_date;


						showDeliverySlotsDialog(true);

					}
					else {
						String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
						showErrorDialog(errorMsg);
					}


				}
			}).getDeliveryTimeSlots(delivery_type);

		}
		catch (Exception ex ) {
			hideLoadingDialog();
			showErrorDialog(ErrorUtils.getFailureError(ex));
		}

	}

	private void showDeliverySlotsDialog(final boolean isAccepted)
	{
		final DeliverySlotstDialogFragment acceptDeclineDialog = new DeliverySlotstDialogFragment(mLstTimeSlots,
		new DeliverySlotstDialogFragment.DialogListener()
		{

			@Override
			public void onDialogPositiveClick(String stDeliverySlot, int mSelectedCategoryIndex, Date mDtCurdate)
			{

				mTxtDeliverySlot.setText(stDeliverySlot);
				if(!mTxtDeliverySlot.equals("00.00 - 00.00"))
				{
					Date tempDate = mDtCurdate;
					Calendar c = Calendar.getInstance();
					c.setTime(tempDate);
					if(mSelectedCategoryIndex !=0)
						c.add(Calendar.DATE, mSelectedCategoryIndex);

					mStDeliveryDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
				}

			}
		}, mCurrentDate, mDelivery_type);
		acceptDeclineDialog.show(getSupportFragmentManager(),"dialog");


	}

	public static String getQuantityInDispFormat(String doubleInString)
	{
		try
		{
			double parseDouble = Double.parseDouble(doubleInString);
			return String.valueOf((int) parseDouble);
		}
		catch (NumberFormatException e){
			e.printStackTrace();
		}
		return doubleInString;
	}

	private void showProductAddedDialog()
	{
		final OutputResultDialogFragment acceptDeclineDialog = OutputResultDialogFragment
				.newInstance(true,
						getString(R.string.dialog_message_new_order_accepted),
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

                    /*Intent intent = new Intent(AddProductDetailsActivity.this, HomeActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    */

					finish();
				}
			}
		}, DIALOG_DISPLAY_TIME);
	}
}
