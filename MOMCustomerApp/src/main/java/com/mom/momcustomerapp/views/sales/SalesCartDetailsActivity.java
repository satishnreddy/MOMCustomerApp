package com.mom.momcustomerapp.views.sales;

import static com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS.ORDER_STATUS_RETURNED;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.products.listners.OnItemCountChangedListener;
import com.mom.momcustomerapp.controllers.products.models.ItemDetails;
import com.mom.momcustomerapp.controllers.products.models.Items;
import com.mom.momcustomerapp.controllers.sales.SalesUpdateCustCartController;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.controllers.sales.models.SalesUpdateCartResp;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.customviews.dialogs.TwoBtnDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.interfaces.OnFragmentInteractionListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mom.momcustomerapp.widget.SafeClickListener;
import com.mswipetech.sdk.network.util.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesCartDetailsActivity extends BaseFragment implements OnFragmentInteractionListener,
		CameraButton.CameraButtonResultListener
{

	CameraButton mCamerabutton;
	private Uri mImageUri;
	private String mInvoiceUrl = "";
	private String mDeliveryStatus  = "";

	@BindView(R.id.activity_order_details_layout_container)
	LinearLayout mLayoutContainer;

	@BindView(R.id.activity_order_details_tv_grand_total)
	TextView mTvGrandTotal;
	@BindView(R.id.bottom_divider)
	View bottomDivider;

	@BindView(R.id.activity_order_details_tv_action1)
	TextView bottomAction1Tv;


	@BindView(R.id.activity_order_details_hint_to_accept)
	TextView hintToAccept;

	@BindView(R.id.header_title)
	TextView titleheader;

	private SalesUpdateCartResp mInvoiceModel;
	private Double mTotalAmount;
	private Activity mActivity;
	private int invoiceType = Consts.INVOICE_TYPE_BILL;
	private boolean retryApi = false;

	private List<Items> initialItemsList;
	private List<ItemDetails> currentItemList = new ArrayList<>();
	private double totalQty = 0;



	@Override
	//protected void onCreate(Bundle savedInstanceState)
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.activity_sales_cart_details, container, false);
		ButterKnife.bind(this, rootView);

		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_order_details);
		//ButterKnife.bind(this);
		mActivity = getActivity();

		setupToolBar();



		/*
		backarrow.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.putExtra(Consts.EXTRA_ISUPDATED, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});*/
		return rootView;
	}


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		//changeToEditMode(isEditing);
		Handler uiHandler = new Handler(Looper.getMainLooper());
		uiHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				getOrderCartDetails();
			}
		});

		bottomAction1Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
		{
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), SalesCheckoutDetailsActivity.class);
				intent.putExtra(Consts.EXTRA_ORDER_AMOUNT, mTotalAmount);
				startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);

			}

		}));
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



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == Consts.REQUEST_CODE_CHANGE_DELIVERY_SLOT)
		{
			if (resultCode == Activity.RESULT_OK)
			{

				//mDeliveryDate  = data.getStringExtra("deliverydate");
				//mDeliverySlot  = data.getStringExtra("deliveryslot");
				//deliverySlotTv.setText(mDeliverySlot);

			}
		} else if (requestCode == Consts.REQUEST_CODE_RETURN_INVOICE) {
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent();
				if (data != null && data.hasExtra(Consts.EXTRA_ISUPDATED)) {
					intent.putExtra(Consts.EXTRA_ISUPDATED, true);
				}
				//setResult(Activity.RESULT_OK, intent);
				//finish();
			}
		} else if (requestCode == Consts.REQUEST_CODE_EDIT_META) {
			if (resultCode == Activity.RESULT_OK) {
				mInvoiceModel = data.getParcelableExtra(Consts.EXTRA_INVOICE);
				updateUI();
			}
		} else if (requestCode == Consts.REQUEST_CODE_VIEW_INVOICE) {

				getOrderCartDetails();

		}else if (requestCode == CameraButton.TAKE_PICTURE_REQUEST_CODE || requestCode == CameraButton.SELECT_PICTURE_REQUEST_CODE || requestCode == CameraButton.AFTER_IMAGE_CROPPING) {
			mCamerabutton.handleActivityResults(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if (requestCode == CameraButton.REQUEST_WRITE_EXTERNAL_STORAGE || requestCode == CameraButton.REQUEST_CAMERA)
		{
			mCamerabutton.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void updateUI()
	{
		initialItemsList = null;
		mTotalAmount = 0.0;
		totalQty = 0;



		if (mInvoiceModel != null)
		{
			mLayoutContainer.removeAllViews();
			if (mInvoiceModel.getItems() != null)
			{
				initialItemsList = mInvoiceModel.getItems();
				updateItemsOnUI(initialItemsList);
			}
			else {
				if (!retryApi)
				{
					retryApi = true;
					getOrderCartDetails();
					return;
				}
			}
			retryApi = false;


		}
	}

	private List<ItemDetails> cloneAllModel(List<ItemDetails> initialItemsList)
	{
		ArrayList<ItemDetails> list = new ArrayList<>();
		for (int i = 0; i < initialItemsList.size(); i++) {
			try {
				ItemDetails clone = initialItemsList.get(i).clone();
				clone.setOriginalQuantity(clone.getQuantityInDouble());
				list.add(clone);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private void updateItemsOnUI(List<Items> newList)
	{
		mLayoutContainer.removeAllViews();


		double totalInDouble = 0;
		if(newList.size() ==0)
			bottomAction1Tv.setEnabled(false);
		else
			bottomAction1Tv.setEnabled(true);

		for (final Items itemsModel : newList)
		{
			final View view = getLayoutInflater().inflate(R.layout.layout_sales_cart_item, mLayoutContainer, false);
			View decrementItemBtn = view.findViewById(R.id.layout_invoice_item_decrement_quant);
			View removeItemBtn = view.findViewById(R.id.layout_invoice_item_remove_product);
			View incrementItemBtn = view.findViewById(R.id.layout_invoice_item_increment_quant);
			final TextView qtyValueTV = view.findViewById(R.id.layout_invoice_item_tv_qty_value);

			decrementItemBtn.setVisibility( View.VISIBLE);
			incrementItemBtn.setVisibility(View.VISIBLE);
			removeItemBtn.setVisibility( View.VISIBLE);


			decrementItemBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Logs.adb("items de increment " + itemsModel.qty);

					updateCart(itemsModel.quantity_id, 0, "1");

				}
			});

			incrementItemBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Logs.adb("items increment " + itemsModel.qty);

					updateCart(itemsModel.quantity_id, 1, "1");


				}
			});

			removeItemBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Logs.adb("items removed " + itemsModel.qty);
					updateCart(itemsModel.quantity_id, 0, itemsModel.qty);

				}
			});


			// update price according to removed items
			String totalitemPrice = "";
			String itemPrice = "";


			try
			{
				totalInDouble = totalInDouble + Double.parseDouble(itemsModel.item_total);
				totalitemPrice = "" +  Consts.getCommaFormatedStringWithDecimal(Float.parseFloat(itemsModel.item_total));
				itemPrice = "" +  Consts.getCommaFormatedStringWithDecimal(Float.parseFloat(itemsModel.selling_price));

			}
			catch (NumberFormatException e){
				e.printStackTrace();
				totalitemPrice = "";
				itemPrice = "";
				totalInDouble = 0;
			}
			((TextView) view.findViewById(R.id.layout_invoice_item_tv_name)).setText(itemsModel.name);
			((TextView) view.findViewById(R.id.layout_invoice_item_selling_price)).setText(itemPrice);
			((TextView) view.findViewById(R.id.layout_invoice_item_tv_tot_selling_price)).setText(totalitemPrice);


			((TextView) view.findViewById(R.id.layout_invoice_item_tv_qty)).setText(getString(R.string.quantityc)+" "/* + itemsModel.getQuantity()*/);
			String quantityInInt = (itemsModel.qty);
			qtyValueTV.setText(quantityInInt);

			try
			{
				totalQty = totalQty + Integer.parseInt( itemsModel.qty);

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}


			mLayoutContainer.addView(view);
		}

		mTotalAmount = totalInDouble;
		mTvGrandTotal.setText(Consts.getCommaFormatedStringWithDecimal((float) totalInDouble));


	}


	private void getOrderCartDetails()
	{

		showLoadingDialog();

		try {
			new SalesUpdateCustCartController( mActivity, new CustomerNetworkObserver())
					.getCustCart();
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
			SalesUpdateCartResp responseData = (SalesUpdateCartResp) mMOMNetworkResDataStore;
			if(responseData.status ==1)
			{


				mInvoiceModel = responseData;

				updateUI();

			}
			else {
				String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;

				showErrorDialog(errorMsg);
			}


		}
	}


	public void updateCart(final String quantity_id, final int update_cart_mode, final String qty)
	{
		try
		{
			showLoadingDialog();
			new SalesUpdateCustCartController( getActivity(), new MOMNetworkResponseListener()
			{
				@Override
				public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
				{
					hideLoadingDialog();
					SalesUpdateCartResp responseData = (SalesUpdateCartResp) mMOMNetworkResDataStore;
					if(responseData.status ==1)
					{

						mInvoiceModel = responseData;
						updateUI();


					}
					else {
						String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
						showErrorDialog(errorMsg);
					}


				}
			}).updateCustCartWithItemRes(quantity_id, update_cart_mode, qty );

		}
		catch (Exception ex ) {
			hideLoadingDialog();
			showErrorDialog(ErrorUtils.getFailureError(ex));
		}

	}

	@Override
	public void onFragmentInteraction(Object obj) {
		/*String remark = (String) obj;
		setRemark(remark);*/
	}


	private void updateUIForStatus(@ORDER_STATUS String status)
	{
		/*
		status = manipulateOrderStatus(status);
		currentOrderStatus = status;
		switch (status)
		{
			case ORDER_STATUS_PENDING:
				hintToAccept.setVisibility(View.VISIBLE);
				bottomActionBtns.setVisibility(View.VISIBLE);
				bottomAction1Tv.setText(getString(R.string.order_detail_label_cta_accept));
				bottomAction2Tv.setText(getString(R.string.order_detail_label_cta_cancel));
				bottomAction1Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
					@Override
					public void onClick(View v) {

						onTriggerAcceptOrder();
						//sendPaymentForAccept(true);
					}
				}));
				bottomAction2Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
					@Override
					public void onClick(View v) {
						TwoBtnDialogFragment.showDialog(mActivity,
								getString(R.string.general_use_are_you_sure),
								getString(R.string.dialog_order_all_removed_subtitle),
								getString(R.string.general_use_no), getString(R.string.general_use_yes),
								new TwoBtnDialogFragment.CustomDialogListener() {
									@Override
									public void onClickLeftBtn() {}

									@Override
									public void onClickRightBtn()
									{

									}
								}
						);
					}
				}));
				break;

			case ORDER_STATUS_ACCEPTED:

				//viewInvoicetBtn.setVisibility(View.VISIBLE);

				hintToAccept.setVisibility(View.VISIBLE);
				bottomActionBtns.setVisibility(View.VISIBLE);
				bottomAction1Tv.setText(getString(R.string.order_detail_label_cta_delivered));
				bottomAction2Tv.setText(getString(R.string.order_detail_label_cta_returned));
				bottomAction1Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
					@Override
					public void onClick(View v) {


						if (isPaymentTypeMswipe(mInvoiceModel))
						{
							if(isUPIPaymentDone(mInvoiceModel))
							{

								sendStatus(ORDER_STATUS_DELIVERED);
							}
							else{

								TwoBtnDialogFragment.showDialog(mActivity,
										getString(R.string.general_payment_status),
										getString(R.string.dialog_payment_not_done),
										getString(R.string.general_use_no), getString(R.string.general_use_yes),
										new TwoBtnDialogFragment.CustomDialogListener() {
											@Override
											public void onClickLeftBtn() {}

											@Override
											public void onClickRightBtn() {
												sendStatus(ORDER_STATUS_DELIVERED);

											}
										}
								);
							}
						}
						else if (isPaymentTypeUPI(mInvoiceModel))
						{

							if(isUPIPaymentDone(mInvoiceModel))
							{

								sendStatus(ORDER_STATUS_DELIVERED);
							}
							else{

								TwoBtnDialogFragment.showDialog(mActivity,
										getString(R.string.general_payment_status),
										getString(R.string.dialog_payment_not_done),
										getString(R.string.general_use_no), getString(R.string.general_use_yes),
										new TwoBtnDialogFragment.CustomDialogListener() {
											@Override
											public void onClickLeftBtn() {}

											@Override
											public void onClickRightBtn() {
												sendStatus(ORDER_STATUS_DELIVERED);

											}
										}
								);
							}
						}
						else {
							sendStatus(ORDER_STATUS_DELIVERED);
						}


					}
				}));
				bottomAction2Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
					@Override
					public void onClick(View v) {

					}
				}));

				break;

			case ORDER_STATUS_DELIVERED:
			case ORDER_STATUS_RETURNED:
			default:
				hintToAccept.setVisibility(View.GONE);
				bottomActionBtns.setVisibility(View.INVISIBLE);
				break;
		}
		*/
	}

	private void sendPaymentForAccept(final boolean isFromAccept)
	{
		/*
		showLoadingDialog();


		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MventryApp.getInstance().getToken());
		Call<String> call = ordersClient.sendPaymentLink(
				ShopxieSharedPreferences.getInstance().getMswipeUsername(),
				mInvoiceModel.getPhoneNumber(),
				mInvoiceModel.getOrderID(),
				ShopxieSharedPreferences.getInstance().getStoreName(),
				mInvoiceModel.getInvoice(),
				mAmount);

		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (isFinishing()) return;
				hideLoadingDialog();


				uploadInvoice();
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				if (isFinishing()) return;
				hideLoadingDialog();
				//showErrorDialog(ErrorUtils.getFailureError(t));
				uploadInvoice();
			}
		});
		*/

	}

	private void checkMswipePaymentStatusForAcceptDecline(final boolean isFromAccept)
	{
		/*
		showLoadingDialog();
		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MventryApp.getInstance().getToken());
		Call<String> call = ordersClient.checkOrderPaymentStatus(mInvoiceModel.salesCustOrder.txn_id,
				ShopxieSharedPreferences.getInstance().getMswipeUsername(),
				ShopxieSharedPreferences.getInstance().getVender());
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (isFinishing()) return;
				hideLoadingDialog();
				if (response.isSuccessful()) {
					String responseStr = response.body();
					try {
						JSONObject rootObj = new JSONObject(responseStr);
						JSONArray getTrasactionStatusJArr = rootObj.getJSONArray("getTrasactionStatus");
						// if there is no child object, then this will show invalid server response.
						JSONObject firstObj = getTrasactionStatusJArr.getJSONObject(0);
						String transactionStatus = firstObj.optString("transactionStatus");
						boolean isPaid = transactionStatus.equalsIgnoreCase("paid");
						onFetchedPaymentStatusForAcceptDeclineOrder(isFromAccept, isPaid);
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog(getString(R.string.invalid_server_response));
					}
				}
				else showErrorDialog(ErrorUtils.getErrorString(response));
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				if (isFinishing()) return;
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t));
			}
		});
		*/
	}

	private void checkUPIPaymentStatusForAcceptDecline(final boolean isFromAccept)
	{
		/*showLoadingDialog();
		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MventryApp.getInstance().getToken());
		Call<String> call = ordersClient.checkVPAStatus(mInvoiceModel.salesCustOrder.txn_id,
				ShopxieSharedPreferences.getInstance().getMswipeUsername());

		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (isFinishing()) return;
				hideLoadingDialog();
				if (response.isSuccessful()) {
					String responseStr = response.body();
					try {
						JSONObject rootObj = new JSONObject(responseStr);
						String transactionStatus = rootObj.optString("status");
						boolean isPaid = transactionStatus.equalsIgnoreCase("1");
						onFetchedPaymentStatusForAcceptDeclineOrder(isFromAccept, isPaid);
					}
					catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog(getString(R.string.invalid_server_response));
					}
				}
				else showErrorDialog(ErrorUtils.getErrorString(response));
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				if (isFinishing()) return;
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t));
			}
		});

		 */
	}

	private void onTriggerAcceptOrder()
	{
		/*if (currentItemList.size() == 0){

		}
		else {
			if (isQuantityChanged){
				List<ItemDetails> list = getRemovedItems(initialItemsList, currentItemList);

			}
			else {
				sendStatus(ORDER_STATUS_ACCEPTED);
			}
		}
		*/
	}

	private void sendStatus(@ORDER_STATUS final String toSendOrderStatus)
	{
		/*
		int intEquivalentOfOrderStatus = getIntEquivalentOfOrder(toSendOrderStatus);
		showLoadingDialog();
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MventryApp.getInstance().getToken());
		Call<String> call = customerClient.updateOrderStatus(mInvoiceModel.salesCustOrder.sale_id, String.valueOf(intEquivalentOfOrderStatus), mDeliveryDate, mDeliverySlot);


		call.enqueue(new Callback<String>()
		{
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				hideLoadingDialog();
				if (response.isSuccessful()){
					String responseString = response.body();
					responseString = responseString.replaceAll("\\n", "");
					try {
						if (!TextUtils.isEmpty(responseString)) {
							JSONObject jsonResponse = new JSONObject(responseString);
							if (jsonResponse.has("data") && jsonResponse.get("data").toString()
									.equalsIgnoreCase("success")) {

								if (toSendOrderStatus.equals(ORDER_STATUS_ACCEPTED)) {
									showAcceptDeclineDialog(true);
								}
								else if (toSendOrderStatus.equals(ORDER_STATUS_RETURNED)){
									if (currentOrderStatus.equals(ORDER_STATUS_PENDING)){

										// Logic changed as of 18 April 2020
										showAcceptDeclineDialog(false);
									}
									else if (currentOrderStatus.equals(ORDER_STATUS_ACCEPTED)) {
										// Logic changed as of 18 April 2020 , now just finish
										Intent intent = new Intent();
										intent.putExtra(Consts.EXTRA_ISUPDATED, true);
										setResult(Activity.RESULT_OK, intent);
										finish();
									}
								}
								else { // Delivered ,
									Intent intent = new Intent();
									intent.putExtra(Consts.EXTRA_ISUPDATED, true);
									setResult(Activity.RESULT_OK, intent);
									finish();
								}



							}
						} else {
							Log.e("ERROR", "onResponse: Something went wrong");
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

	*/
	}

	private void showAcceptDeclineDialog(final boolean isAccepted)
	{
		/*final OutputResultDialogFragment acceptDeclineDialog = OutputResultDialogFragment
				.newInstance(isAccepted, isAccepted ? getString(R.string.dialog_order_status_accepted) :
						getString(R.string.dialog_order_status_declined) ,
				null);
		acceptDeclineDialog.show(getSupportFragmentManager(),"dialog");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (acceptDeclineDialog.isVisible()) {
					acceptDeclineDialog.dismiss();
				}

				if (isAccepted){
					getOrderCartDetails();
				}
				else {
					Intent intent = new Intent();
					intent.putExtra(Consts.EXTRA_ISUPDATED, true);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		}, DIALOG_DISPLAY_TIME);
		*/
	}

	public static String getQuantityInDispFormat(String doubleInString){
		try{
			double parseDouble = Double.parseDouble(doubleInString);
			return String.valueOf((int) parseDouble);
		}
		catch (NumberFormatException e){
			e.printStackTrace();
		}
		return doubleInString;
	}




	/*
	* Due to server side issues, status in manipulated for return list only , status is
	* made returned.
	* */
	private String manipulateOrderStatus(@ORDER_STATUS String orderStatus)
	{
		if (invoiceType == Consts.INVOICE_TYPE_RETURN) return ORDER_STATUS_RETURNED;
		return orderStatus;
	}

	private OnItemCountChangedListener countChangedListener = new OnItemCountChangedListener()
	{

		@Override
		public void onItemRemoved(ItemDetails itemsModel) {
			for (int i = 0; i < currentItemList.size(); i++) {
				if (currentItemList.get(i).item_id.equals(itemsModel.item_id)){
					currentItemList.remove(currentItemList.get(i));
					return;
				}
			}
		}

		@Override
		public void onItemDecremented(ItemDetails itemsModel)
		{
			//remove from current list
			for (int i = 0; i < currentItemList.size(); i++) {
				if (currentItemList.get(i).item_id.equals(itemsModel.item_id)){
					int acurrentQuantityOfProduct = (int) currentItemList.get(i).getQuantityInDouble();
					acurrentQuantityOfProduct--;
					if (acurrentQuantityOfProduct == 0){
						currentItemList.remove(currentItemList.get(i));
					}
					else{
						currentItemList.get(i).setQuantity(String.valueOf(acurrentQuantityOfProduct));
					} return;
				}
			}
		}

		@Override
		public void onItemIncremented(ItemDetails itemsModel)
		{

			{
				for (int i = 0; i < currentItemList.size(); i++)
				{
					if (currentItemList.get(i).item_id.equals(itemsModel.item_id))
					{
					int acurrentQuantityOfProduct = (int) currentItemList.get(i).getQuantityInDouble();
					acurrentQuantityOfProduct++;


					currentItemList.get(i).setQuantity(String.valueOf(acurrentQuantityOfProduct));
					}
				}

			}
//			int acurrentQuantityOfProduct = (int) currentItemList.get(i).getQuantityInDouble();
//			int count= Integer.parseInt(String.valueOf(acurrentQuantityOfProduct));
//			count++;
			/*removedItemList.remove(itemsModel);*/
		}
	};







	private void showImage(String permitImgUrl) {
		/*FullImageDialogFragment dialogFragment = FullImageDialogFragment.newInstance(permitImgUrl);
		dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
		*/
	}


	@Override
	public void imageSaved(Uri uri) {

		Toast.makeText(mActivity, "Photo Selected : " + uri.getPath(), Toast.LENGTH_SHORT).show();
		mImageUri = uri;
		//uploadStoreProfileImage(mImageUri.getPath(), mInvoiceModel.salesCustOrder.sale_id);
	}

	private void sendLinkAnduploadInvoice()
	{
		/*
		if (isPaymentTypeMswipe(mInvoiceModel)){
			sendPaymentForAccept(true);
		}
		else if (isPaymentTypeUPI(mInvoiceModel)){
			sendPaymentForAccept(true);
		}
		else {
			uploadInvoice();
		}
		*/
	}

	private void uploadInvoice()
	{

		TwoBtnDialogFragment.showDialog(mActivity,"",
				getString(R.string.order_detail_lbl_do_you_wish_upload_invoice),
				getString(R.string.general_use_no), getString(R.string.general_use_yes),
				new TwoBtnDialogFragment.CustomDialogListener() {
					@Override
					public void onClickLeftBtn() {

					}

					@Override
					public void onClickRightBtn() {
						mCamerabutton.performClick();
					}
				}
		);
	}

	private void uploadStoreProfileImage(String imagePath, String itemId)
	{
		showLoadingDialog();
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		MultipartBody.Part body;

		File file = new File(imagePath);
		RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
		body = MultipartBody.Part.createFormData("profile_img", file.getName(), requestFile);

		RequestBody name = RequestBody.create(MediaType.parse("text/plain"), itemId);

		Call<String> call = customerClient.uploadProfileImage(name, body);
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				hideLoadingDialog();
				if (response.isSuccessful()) {
					String responseString = response.body();
					responseString = responseString.replaceAll("\\n", "");
					try {

						 //{"status":"success","message":"","url":"http:\/\/meraonlinestore.s3.amazonaws.com\/invoices\/145.jpeg"}

						if (!TextUtils.isEmpty(responseString)) {
							JSONObject jsonResponse = new JSONObject(responseString);
							if (jsonResponse.get("status").toString().equalsIgnoreCase("success")) {

								mInvoiceUrl = jsonResponse.get("url").toString();
								if(mInvoiceUrl != null && mInvoiceUrl.length() > 0) {
									//viewInvoice.setText(getResources().getString(R.string.order_detail_lbl_view_invoice));
								}
								else{
									showErrorDialog("Something went wrong. Response is empty.");
								}
							}
							else
								showErrorDialog(getString(R.string.sww));
						} else {
							showErrorDialog("Something went wrong. Response is empty.");
						}
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog(getString(R.string.invalid_server_response));
					}
				} else {
					if (response.code() == 413){
						showErrorDialog(getString(R.string.store_pic_image_size_to_large));
					}
					else {
						showErrorDialog(ErrorUtils.getErrorString(response));
					}
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
