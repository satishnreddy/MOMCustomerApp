package com.mom.momcustomerapp.views.orders;

import static com.mom.momcustomerapp.controllers.orders.api.ServerSideOrderUtils.getIntEquivalentOfOrder;
import static com.mom.momcustomerapp.controllers.orders.api.ServerSideOrderUtils.getRemovedItems;
import static com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS.ORDER_STATUS_ACCEPTED;
import static com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS.ORDER_STATUS_DELIVERED;
import static com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS.ORDER_STATUS_PENDING;
import static com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS.ORDER_STATUS_RETURNED;
import static com.mom.momcustomerapp.data.application.Consts.DIALOG_DISPLAY_TIME;
import static com.mom.momcustomerapp.data.application.Consts.REQUEST_CODE_CHANGE_DELIVERY_SLOT;
import static com.mom.momcustomerapp.utils.DateTimeUtils.convertDtTimeInAppFormat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.SalesDetailsController;
import com.mom.momcustomerapp.controllers.orders.models.SalesDetailsResp;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.products.listners.OnItemCountChangedListener;
import com.mom.momcustomerapp.controllers.products.models.ItemDetails;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.customviews.CameraButton;
import com.mom.momcustomerapp.customviews.CustomImageView;
import com.mom.momcustomerapp.customviews.dialogs.FullImageDialogFragment;
import com.mom.momcustomerapp.customviews.dialogs.OutputResultDialogFragment;
import com.mom.momcustomerapp.customviews.dialogs.TwoBtnDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.interfaces.OnFragmentInteractionListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.utils.AppUtils;
import com.mom.momcustomerapp.utils.IntentUtils;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.views.stores.delivery.ChangeDeliverySoltActivity;
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


public class OrderDetailsActivity extends BaseActivity implements OnFragmentInteractionListener,
		CameraButton.CameraButtonResultListener {

	CameraButton mCamerabutton;
	private Uri mImageUri;
	private String mInvoiceUrl = "";
	private String mDeliveryStatus  = "";

	@BindView(R.id.activity_order_details_btn_send_bill)
	Button mBtnSendBill;
	/*@BindView(R.id.activity_order_details_tv_title)
	TextView mTvTitle;*/
	@BindView(R.id.activity_order_details_tv_date)
	TextView mTvDate;
	@BindView(R.id.activity_order_details_tv_invoice_id)
	TextView mTvInvoiceId;
	@BindView(R.id.activity_order_details_circleiv_shop_avatar)
	CustomImageView mIvShopAvatar;
	/*@BindView(R.id.activity_order_details_tv_shop_name)
	TextView mTvShopName;
	@BindView(R.id.activity_order_details_tv_shop_address)
	TextView mTvShopAddress;*/
	@BindView(R.id.activity_order_details_tv_customer_name)
	TextView mTvCustomerName;
	@BindView(R.id.activity_order_details_tv_customer_phone)
	TextView mTvCustomerPhone;
	/*@BindView(R.id.activity_order_details_tv_customer_email)
	TextView mTvCustomerEmail;
	@BindView(R.id.activity_order_details_tv_customer_gstin)
	TextView mTvCustomerGstin;*/
	@BindView(R.id.activity_order_details_layout_customer_gstin)
	LinearLayout mLayoutCustomerGstin;
	@BindView(R.id.activity_order_details_iv_payment_type)
	ImageView mIvPaymentType;
	@BindView(R.id.activity_order_details_layout_container)
	LinearLayout mLayoutContainer;
	@BindView(R.id.activity_order_details_tv_discount_amount)
	TextView mTvDiscountAmount;
	@BindView(R.id.activity_order_details_layout_discount)
	LinearLayout mLayoutDiscount;
	/*@BindView(R.id.activity_order_details_tv_vat_amount)
	TextView mTvVatAmount;
	@BindView(R.id.activity_order_details_layout_vat)
	LinearLayout mLayoutVat;
	@BindView(R.id.activity_order_details_tv_cgst_amount)
	TextView mTvCgstAmount;
	@BindView(R.id.activity_order_details_layout_cgst)
	LinearLayout mLayoutCgst;
	@BindView(R.id.activity_order_details_tv_sgst_amount)
	TextView mTvSgstAmount;
	@BindView(R.id.activity_order_details_layout_sgst)
	LinearLayout mLayoutSgst;
	@BindView(R.id.activity_order_details_layout_global_gst)
	LinearLayout mLayoutTotalGST;
	@BindView(R.id.activity_order_details_tv_igst_amount)
	TextView mTvIgstAmount;
	@BindView(R.id.activity_order_details_tv_gst_amount)
	TextView mTvGSTAmount;
	@BindView(R.id.activity_order_details_layout_igst)
	LinearLayout mLayoutIgst;
	@BindView(R.id.activity_order_details_tv_cst_value)
	TextView mTvCstValue;
	@BindView(R.id.activity_order_details_layout_cst)
	LinearLayout mLayoutCst;
	@BindView(R.id.activity_order_details_tv_shipping_amount)
	TextView mTvShippingAmount;
	@BindView(R.id.activity_order_details_layout_shipping)
	LinearLayout mLayoutShipping;*/
	@BindView(R.id.activity_order_details_tv_grand_total)
	TextView mTvGrandTotal;
	/*@BindView(R.id.activity_order_details_tv_remark)
	TextView mTvRemark;*/
	@BindView(R.id.activity_order_details_layout_payment_container)
	LinearLayout mLayoutPaymentContainer;
	@BindView(R.id.bottom_divider)
	View bottomDivider;
	/*@BindView(R.id.activity_order_details_tv_amount)
	TextView mTvAmount;*/
	@BindView(R.id.activity_order_details_tv_payment_type)
	TextView mTvPaymentType;
	/*@BindView(R.id.item_row_invoice_payment_type_tv_label_payment_type)
	TextView mTvLabelPaymentType;*/
	@BindView(R.id.activity_order_details_tv_tnc)
	TextView mTvTnc;
	@BindView(R.id.activity_order_details_layout_tnc)
	LinearLayout mLayoutTnc;
	@BindView(R.id.activity_order_details_tv_vatno)
	TextView mTvVatno;
	@BindView(R.id.activity_order_details_tv_cstno)
	TextView mTvCstno;
	@BindView(R.id.activity_order_details_layout_vat_cst)
	LinearLayout mLayoutVatCst;
	@BindView(R.id.activity_order_details_tv_pan)
	TextView mTvPan;
	@BindView(R.id.activity_order_details_tv_tan)
	TextView mTvTan;
	@BindView(R.id.activity_order_details_layout_pan_tan)
	LinearLayout mLayoutPanTan;
	@BindView(R.id.activity_order_details_tv_gstin)
	TextView mTvGstin;
	@BindView(R.id.activity_order_details_layout_gstin)
	LinearLayout mLayoutGstin;
	@BindView(R.id.activity_order_details_tv_total_qty)
	TextView mTvTotalQty;
	@BindView(R.id.activity_invoice_layout_global_discount)
	LinearLayout mLayoutGlobalDiscount;
	@BindView(R.id.activity_invoice_tv_discount_global_discount_amount)
	TextView mTvGlobalDiscount;
	@BindView(R.id.activity_order_details_tv_tot_amt)
	TextView mTvTotalAmount;
	@BindView(R.id.activity_order_details_ll_action_btns)
	View bottomActionBtns;
	@BindView(R.id.activity_order_details_tv_action1)
	TextView bottomAction1Tv;
	@BindView(R.id.activity_order_details_tv_action2)
	TextView bottomAction2Tv;
	@BindView(R.id.activity_order_details_hint_to_accept)
	TextView hintToAccept;
	@BindView(R.id.activity_order_details_btn_whatsapp)
	View whatsappBtn;

	@BindView(R.id.activity_order_details_tr_payment_status)
	View paymentStatusTR;
	@BindView(R.id.activity_order_details_tv_payment_status)
	TextView paymentStatusTv;
	@BindView(R.id.activity_order_details_iv_payment_status_refresh)
	ImageView refreshPaymentStatusBtn;

	@BindView(R.id.activity_order_details_tr_delivery_slot)
	View deliverySlotTR;
	@BindView(R.id.activity_order_details_tv_delivery_slot)
	TextView deliverySlotTv;
	@BindView(R.id.activity_order_details_iv_delivery_slot)
	ImageView deliverySlotEditBtn;

	@BindView(R.id.activity_order_details_tv_address)
	TextView customerAddressTv;
	@BindView(R.id.activity_order_details_tr_address)
	View customerAddresTR;
	@BindView(R.id.activity_order_details_tr_address_landmark)
	View landMarkTR;
	@BindView(R.id.activity_order_details_tv_address_landmark)
	TextView landMarkTv;

	@BindView(R.id.activity_order_details_ll_view_permit)
	View viewPermitBtn;

	@BindView(R.id.activity_order_details_ll_view_invoice)
	View viewInvoicetBtn;

	@BindView(R.id.activity_order_details_TXT_invoice)
	TextView viewInvoice;

	@BindView(R.id.back_arrow)
	ImageView backarrow;

	@BindView(R.id.header_title)
	TextView titleheader;

	private SalesDetailsResp mInvoiceModel;
	private String mOrderId;
	private String mAmount;
	private String mInvoie;
	private Activity mActivity;
	private int invoiceType = Consts.INVOICE_TYPE_BILL;
	private boolean retryApi = false;
	private int taxPriceVisibility = View.GONE;
	private boolean showMetaData;
	private String currentOrderStatus, serverSideOrderStatusCode;

	private List<ItemDetails> initialItemsList;
	/*private List<InvoiceModel.InvoiceItemsModel> removedItemList = new ArrayList<>();*/
	private List<ItemDetails> currentItemList = new ArrayList<>();
	private double totalQty = 0;
	/*private OutputResultDialogFragment acceptDeclineDialog;*/

	private boolean isQuantityChanged = false;
	private boolean isAmountNotPaid = false;

	private String mDeliveryDate  = "";
	private String mDeliverySlot  = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales_order_details);
		ButterKnife.bind(this);
		mActivity = this;

		setupToolBar();
		loadIntentData();
		getOrderDetails();

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
		});
	}

	/**
	 * Set up the {@link Toolbar}.
	 */
	private void setupToolBar()
	{
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null)
		{
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
		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent();
				intent.putExtra(Consts.EXTRA_ISUPDATED, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
		}
		return true;
	}

	private void loadIntentData()
	{
		if (getIntent().hasExtra(Consts.EXTRA_ORDER_ID))
		{
			mOrderId = getIntent().getStringExtra(Consts.EXTRA_ORDER_ID);

			mAmount = getIntent().getStringExtra(Consts.EXTRA_ORDER_AMOUNT);
			mInvoie = getIntent().getStringExtra(Consts.EXTRA_ORDER_INVOCIE);
		}
		else {
			showErrorDialog("Error", "Something went wrong : Please go back and try again.");
		}

		if (getIntent().hasExtra(Consts.EXTRA_INVOICE_RETURN))
		{
			invoiceType = getIntent().getIntExtra(Consts.EXTRA_INVOICE_RETURN, Consts.INVOICE_TYPE_BILL);
		}

		if (getIntent().hasExtra(Consts.EXTRA_CURRENT_STATUS_CODE)) {
			currentOrderStatus = getIntent().getStringExtra(Consts.EXTRA_CURRENT_STATUS_CODE);
			serverSideOrderStatusCode = getIntent().getStringExtra(Consts.EXTRA_CURRENT_STATUS_CODE_INT_STRING);

			updateUIForStatus(currentOrderStatus);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (invoiceType == Consts.INVOICE_TYPE_BILL) {
			getMenuInflater().inflate(R.menu.menu_invoice_details, menu);

			MenuItem menuReturn = menu.findItem(R.id.menu_return_product);
			menuReturn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					returnClicked();
					return false;
				}
			});

			MenuItem menuCustomFields = menu.findItem(R.id.menu_cart_invoice_custom_fields);
			menuCustomFields.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					editCustomFieldsClicked();
					return false;
				}
			});
		}
		return true;
	}*/



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE_CHANGE_DELIVERY_SLOT) {
			if (resultCode == Activity.RESULT_OK) {

				mDeliveryDate  = data.getStringExtra("deliverydate");
				mDeliverySlot  = data.getStringExtra("deliveryslot");

				deliverySlotTv.setText(mDeliverySlot);

			}
		} else if (requestCode == Consts.REQUEST_CODE_RETURN_INVOICE) {
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent();
				if (data != null && data.hasExtra(Consts.EXTRA_ISUPDATED)) {
					intent.putExtra(Consts.EXTRA_ISUPDATED, true);
				}
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		} else if (requestCode == Consts.REQUEST_CODE_EDIT_META) {
			if (resultCode == Activity.RESULT_OK) {
				mInvoiceModel = data.getParcelableExtra(Consts.EXTRA_INVOICE);
				updateUI();
			}
		} else if (requestCode == Consts.REQUEST_CODE_VIEW_INVOICE) {
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent();
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}else if (requestCode == CameraButton.TAKE_PICTURE_REQUEST_CODE || requestCode == CameraButton.SELECT_PICTURE_REQUEST_CODE || requestCode == CameraButton.AFTER_IMAGE_CROPPING) {
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

	private void updateUI()
	{
		if (mInvoiceModel != null)
		{
			/*mTvShopName.setText(mInvoiceModel.getStore().getStore_name());
			mTvShopAddress.setText(mInvoiceModel.getStore().getStore_address() + " ," + mInvoiceModel.getStore().getStore_city());*/

			mTvCustomerName.setText(mInvoiceModel.salesCustOrder.customerName);
			titleheader.setText(mInvoiceModel.salesCustOrder.customerName);
			final String phone_number = mInvoiceModel.salesCustOrder.customerPhone;
			mTvCustomerPhone.setText(phone_number);

			if (!TextUtils.isEmpty(phone_number) && phone_number.length() == 10)
			{
				whatsappBtn.setVisibility(View.VISIBLE);
				whatsappBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						IntentUtils.sendWhatsAppMessageToNumber(v.getContext(),
								"91"+phone_number,"");
					}
				});
			}

			/*if (TextUtils.isEmpty(mInvoiceModel.getCustomer().getEmail())) {
				mTvCustomerEmail.setVisibility(View.GONE);
			} else {
				mTvCustomerEmail.setText(mInvoiceModel.getCustomer().getEmail());
			}*/

			/*if (TextUtils.isEmpty(mInvoiceModel.getCustomer().getCustomerGstin())) {
				mLayoutCustomerGstin.setVisibility(View.GONE);
			} else {
				mLayoutCustomerGstin.setVisibility(View.VISIBLE);
				mTvCustomerGstin.setText(mInvoiceModel.getCustomer().getCustomerGstin());
			}*/

			mTvDate.setText(convertDtTimeInAppFormat(mInvoiceModel.salesCustOrder.sale_time));
			mTvInvoiceId.setText(mInvoiceModel.salesCustOrder.invoice_number);
			//mTvRemark.setText(mInvoiceModel.getRemark());

			try
			{

				double grandTotal = Double.parseDouble(mInvoiceModel.salesCustOrder.total_price.replaceAll(",", ""));

				mTvGrandTotal.setText(Consts.getCommaFormatedStringWithDecimal((float) grandTotal));
				mTvTotalAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) grandTotal));
				//mTvAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) grandTotal));
			} catch (Exception e) {
				mTvGrandTotal.setText(mInvoiceModel.salesCustOrder.total_price);
				mTvTotalAmount.setText(mInvoiceModel.salesCustOrder.total_price);
				//mTvAmount.setText(mInvoiceModel.getOrder_total());
				e.printStackTrace();
			}

			/*if (TextUtils.isEmpty(mInvoiceModel.getFree())) {
				mLayoutTnc.setVisibility(View.GONE);
			} else {
				mLayoutTnc.setVisibility(View.VISIBLE);
				mTvTnc.setText(mInvoiceModel.getFree());
			}*/

			/*if (TextUtils.isEmpty(mInvoiceModel.getGstin()) || mInvoiceModel.getGstin().equalsIgnoreCase("0")) {
				mLayoutGstin.setVisibility(View.GONE);
			} else {
				mLayoutGstin.setVisibility(View.VISIBLE);
				mTvGstin.setText(mInvoiceModel.getGstin());
			}

			if (TextUtils.isEmpty(mInvoiceModel.getVat_number()) || mInvoiceModel.getVat_number().equalsIgnoreCase("0")) {
				mLayoutVatCst.setVisibility(View.GONE);
			} else {
				mLayoutVatCst.setVisibility(View.GONE);
				mTvVatno.setText(mInvoiceModel.getVat_number());
				mTvCstno.setText(mInvoiceModel.getCst_number());
			}

			if (mInvoiceModel.getTaxInclusive() == 1) {
				mLayoutVatCst.setVisibility(View.GONE);
				mLayoutGstin.setVisibility(View.GONE);
			} else if (mInvoiceModel.getTaxInclusive() == 2) {
				mLayoutVatCst.setVisibility(View.GONE);
				mLayoutGstin.setVisibility(View.VISIBLE);
			} else {
				mLayoutVatCst.setVisibility(View.GONE);
				mLayoutGstin.setVisibility(View.VISIBLE);
			}*/

			/*if (TextUtils.isEmpty(mInvoiceModel.getPan()) || mInvoiceModel.getPan().equalsIgnoreCase("0")) {
				mLayoutPanTan.setVisibility(View.GONE);
			} else {
				mLayoutPanTan.setVisibility(View.VISIBLE);
				mTvPan.setText(mInvoiceModel.getPan());
				mTvTan.setText(mInvoiceModel.getTan());
			}*/

			switch (mInvoiceModel.salesCustOrder.payment_type)
			{
				case "Cash":
					mIvPaymentType.setImageResource(R.drawable.ic_cash_24dp);
					mTvPaymentType.setText("Cash");
					break;
				case "Credit Card":
					mIvPaymentType.setImageResource(R.drawable.ic_credit_card_24dp);
					mTvPaymentType.setText("Credit Card");
					break;
				case "Debit Card":
					mIvPaymentType.setImageResource(R.drawable.ic_debit_card_24dp);
					mTvPaymentType.setText("Debit Card");
					break;
				case "Net Banking":
					mIvPaymentType.setImageResource(R.drawable.ic_net_banking_24dp);
					mTvPaymentType.setText("Net Banking");
					break;
				case "Wallet":
					mIvPaymentType.setImageResource(R.drawable.ic_wallet_24dp);
					mTvPaymentType.setText("Wallet");
					break;
				case "Voucher":
					mIvPaymentType.setImageResource(R.drawable.ic_voucher_24dp);
					mTvPaymentType.setText("Voucher");
					break;
				case "Mix":
					mIvPaymentType.setImageResource(R.drawable.ic_mix_payment_24dp);
					mTvPaymentType.setText("Mix");
					break;
				case "mswipe":
					mIvPaymentType.setImageResource(R.drawable.ic_mix_payment_24dp);
					mTvPaymentType.setText("Pay By Link");
					break;

				case AppUtils.PAYMENT_TYPE_UPI:
					mIvPaymentType.setImageResource(R.drawable.ic_mix_payment_24dp);
					mTvPaymentType.setText(getString(R.string.payment_type_upi_display_text));
					break;

				default:
					mIvPaymentType.setImageResource(R.drawable.ic_cash_24dp);
					mTvPaymentType.setText("Cash");
			}


			double totalVat = 0;
			double totalGst = 0;

			double totalDiscount = 0;
			mLayoutContainer.removeAllViews();
			if (mInvoiceModel.getItems() != null)
			{

				initialItemsList = mInvoiceModel.getItems();
				for (int i = 0; i < initialItemsList.size(); i++)
				{
					ItemDetails invoiceItemsModel = initialItemsList.get(i);

					invoiceItemsModel.setOriginalQuantity(invoiceItemsModel.getQuantityInDouble());
				}
				currentItemList = cloneAllModel(initialItemsList);
				updateItemsOnUI(currentItemList);
			}
			else {
				if (!retryApi)
				{
					retryApi = true;
					getOrderDetails();
					return;
				}
			}

			mTvTotalQty.setText(totalQty + "");

			/*if (mInvoiceModel.getTaxInclusive() == 1) {
//                mTvVatAmount.setText(Consts.getCommaFormatedString((float) totalVat));
				if (!TextUtils.isEmpty(mInvoiceModel.getVat())) {
					mTvVatAmount.setText(mInvoiceModel.getVat());
				} else {
					mTvVatAmount.setText("0");
				}
				mLayoutVat.setVisibility(View.GONE);
				mLayoutCgst.setVisibility(View.GONE);
				mLayoutSgst.setVisibility(View.GONE);
				mLayoutIgst.setVisibility(View.GONE);
			} else if (mInvoiceModel.getTaxInclusive() == 2) {
				if (mInvoiceModel.getInvoice_version() != null && mInvoiceModel.getInvoice_version().equalsIgnoreCase("0")) {
					mLayoutTotalGST.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(mInvoiceModel.getGst())) {
						mTvGSTAmount.setText(mInvoiceModel.getGst());
					} else {
						mTvGSTAmount.setText("0.0");
					}

					if (!TextUtils.isEmpty(mInvoiceModel.getCgstValue())) {
						mTvCgstAmount.setText(mInvoiceModel.getCgstValue());
					} else {
						mLayoutCgst.setVisibility(View.GONE);
					}

					if (!TextUtils.isEmpty(mInvoiceModel.getSgstValue())) {
						mTvSgstAmount.setText(mInvoiceModel.getSgstValue());
					} else {
						mLayoutSgst.setVisibility(View.GONE);
					}

					if(!TextUtils.isEmpty(mInvoiceModel.getIgstValue())){
						mTvIgstAmount.setText(mInvoiceModel.getIgstValue());
					}else{
						mLayoutIgst.setVisibility(View.GONE);
					}

				} else {
					mLayoutVat.setVisibility(View.GONE);
					mLayoutCgst.setVisibility(View.VISIBLE);
					mLayoutSgst.setVisibility(View.VISIBLE);
					mLayoutIgst.setVisibility(View.GONE);

					if (totalGst != 0) {
						if (!TextUtils.isEmpty(mInvoiceModel.getCgstValue())) {
							mLayoutCgst.setVisibility(View.VISIBLE);
							try {
								double cgst = totalGst / 2;
								mTvCgstAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) cgst));
							} catch (Exception e) {
								mTvCgstAmount.setText(mInvoiceModel.getCgstValue());
								e.printStackTrace();
							}
						} else {
							mLayoutCgst.setVisibility(View.GONE);
						}


						if (!TextUtils.isEmpty(mInvoiceModel.getSgstValue())) {
							mLayoutSgst.setVisibility(View.VISIBLE);
							try {
								double sgst = totalGst / 2;
								mTvSgstAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) sgst));
							} catch (Exception e) {
								mTvSgstAmount.setText(mInvoiceModel.getSgstValue());
								e.printStackTrace();
							}
						} else {
							mLayoutSgst.setVisibility(View.GONE);
						}

						if (!TextUtils.isEmpty(mInvoiceModel.getIgstValue())) {
							mLayoutIgst.setVisibility(View.VISIBLE);
							try {
								double igst = Double.parseDouble(mInvoiceModel.getIgstValue().replaceAll(",", ""));
								mTvIgstAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) igst));
							} catch (Exception e) {
								mTvIgstAmount.setText(mInvoiceModel.getIgstValue());
								e.printStackTrace();
							}
						} else {
							mLayoutIgst.setVisibility(View.GONE);
						}
					} else {
						mTvCgstAmount.setText("0");
						mTvSgstAmount.setText("0");
					}
				}
			} else {
				if (mInvoiceModel.getInvoice_version() != null && mInvoiceModel.getInvoice_version().equalsIgnoreCase("0")) {
					mLayoutTotalGST.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(mInvoiceModel.getGst())) {
						mTvGSTAmount.setText(mInvoiceModel.getGst());
					} else {
						mTvGSTAmount.setText("0.0");
					}

					if (!TextUtils.isEmpty(mInvoiceModel.getCgstValue())) {
						mTvCgstAmount.setText(mInvoiceModel.getCgstValue());
					} else {
						mLayoutCgst.setVisibility(View.GONE);
					}

					if (!TextUtils.isEmpty(mInvoiceModel.getSgstValue())) {
						mTvSgstAmount.setText(mInvoiceModel.getSgstValue());
					} else {
						mTvSgstAmount.setVisibility(View.GONE);
					}

				} else {
					if (!TextUtils.isEmpty(mInvoiceModel.getVat())) {
						double vatPrice;
						try {
							vatPrice = Double.parseDouble(mInvoiceModel.getVat().replaceAll(",", ""));
							if (vatPrice > 0) {
								mLayoutVat.setVisibility(View.GONE);
								try {
									mTvVatAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) vatPrice));
								} catch (Exception e) {
									mTvVatAmount.setText(mInvoiceModel.getVat());
									e.printStackTrace();
								}
							} else {
								mLayoutVat.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							mLayoutVat.setVisibility(View.GONE);
							e.printStackTrace();
						}
					} else {
						mLayoutVat.setVisibility(View.GONE);
					}

					if (!TextUtils.isEmpty(mInvoiceModel.getCgstValue())) {
						if (totalGst != 0) {
							double cgstPrice;
							try {
								cgstPrice = totalGst / 2;
								if (cgstPrice > 0) {
									mLayoutCgst.setVisibility(View.VISIBLE);
									mTvCgstAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) cgstPrice));
								} else {
									mLayoutCgst.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								mLayoutCgst.setVisibility(View.GONE);
								e.printStackTrace();
							}
						} else {
							mLayoutCgst.setVisibility(View.GONE);
						}
					} else {
						mLayoutCgst.setVisibility(View.GONE);
					}

					if (!TextUtils.isEmpty(mInvoiceModel.getSgstValue())) {
						if (totalGst != 0) {
							double sgstPrice;
							try {
								sgstPrice = totalGst / 2;
								if (sgstPrice > 0) {
									mLayoutSgst.setVisibility(View.VISIBLE);
									mTvSgstAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) sgstPrice));
								} else {
									mLayoutSgst.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								mLayoutSgst.setVisibility(View.GONE);
								e.printStackTrace();
							}
						} else {
							mLayoutSgst.setVisibility(View.GONE);
						}
					} else {
						mLayoutSgst.setVisibility(View.GONE);
					}

					if (mInvoiceModel.getInvoice_version() != null && mInvoiceModel.getInvoice_version().equalsIgnoreCase("0")) {
						mLayoutTotalGST.setVisibility(View.VISIBLE);
						mTvGSTAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) totalGst));
					}


					if (!TextUtils.isEmpty(mInvoiceModel.getIgstValue())) {
						double igstPrice;
						try {
							igstPrice = Double.parseDouble(mInvoiceModel.getIgstValue().replaceAll(",", ""));
							if (igstPrice > 0) {
								mLayoutIgst.setVisibility(View.VISIBLE);
								mTvIgstAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) igstPrice));
							} else {
								mLayoutIgst.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							mLayoutIgst.setVisibility(View.GONE);
							e.printStackTrace();
						}
					} else {
						mLayoutIgst.setVisibility(View.GONE);
					}

//                if (!TextUtils.isEmpty(mInvoiceModel.getCst())) {
//                    mTvCstValue.setText(mInvoiceModel.getCst());
//                } else {
//                    mTvCstValue.setText("0");
//                }
				}
			}

			try {
				if (TextUtils.isEmpty(mInvoiceModel.getTotal_discount_amount())) {
					mTvGlobalDiscount.setText("0.0");
					mTvDiscountAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) totalDiscount));
				} else {
					totalDiscount = totalDiscount + Double.parseDouble(mInvoiceModel.getTotal_discount_amount().replaceAll(",", ""));
					mTvGlobalDiscount.setText(mInvoiceModel.getTotal_discount_amount());
					mTvDiscountAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) totalDiscount));
				}
			} catch (Exception e) {
				if (TextUtils.isEmpty(mInvoiceModel.getTotal_discount_amount()))
					mTvGlobalDiscount.setText("0.0");
				else
					mTvGlobalDiscount.setText(mInvoiceModel.getTotal_discount_amount());
				if (TextUtils.isEmpty(mInvoiceModel.getDiscount()))
					mTvDiscountAmount.setText("0.0");
				else
					mTvDiscountAmount.setText(mInvoiceModel.getDiscount());
				e.printStackTrace();
			}

			if (!TextUtils.isEmpty(mInvoiceModel.getShipping())) {
				mTvShippingAmount.setText(mInvoiceModel.getShipping());
			} else {
				mTvShippingAmount.setText("0");
			}
			*/
			/*if (mInvoiceModel.getPaymentType().equalsIgnoreCase("Mix")) {
				if (mInvoiceModel.getMix_payment() != null) {
					ArrayList<InvoiceModel.MixPaymentModel> mixPaymentModelArrayList = new ArrayList<>(mInvoiceModel.getMix_payment());
					if (mixPaymentModelArrayList.size() > 1) {
						mLayoutPaymentContainer.removeAllViews();
						for (int i = 0; i < mixPaymentModelArrayList.size(); i++) {
							LinearLayout paymentRow = (LinearLayout) getLayoutInflater().inflate(R.layout.item_row_invoice_payment_type, mLayoutPaymentContainer, false);
							((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_payment_type_order_detail)).setText(mixPaymentModelArrayList.get(i).getPayment_type());
							try {
								double payment = Double.parseDouble(mixPaymentModelArrayList.get(i).getPayment_amount().replaceAll(",", ""));
								((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_amount_order_detail)).setText(Consts.getCommaFormatedStringWithDecimal((float) payment));
							} catch (Exception e) {
								((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_amount_order_detail)).setText(mixPaymentModelArrayList.get(i).getPayment_amount());
								e.printStackTrace();
							}

							mLayoutPaymentContainer.addView(paymentRow);
						}
					}
				}
			} else if (mInvoiceModel.getPaymentType().equalsIgnoreCase("Partial")) {
				if (mInvoiceModel.getMix_payment() != null && mInvoiceModel.getMix_payment().size() > 0) {
					ArrayList<InvoiceModel.MixPaymentModel> mixPaymentModelArrayList = new ArrayList<>(mInvoiceModel.getMix_payment());
					if (mixPaymentModelArrayList.size() > 0) {
						mLayoutPaymentContainer.removeAllViews();
						for (int i = 0; i < mixPaymentModelArrayList.size(); i++) {
							LinearLayout paymentRow = (LinearLayout) getLayoutInflater().inflate(R.layout.item_row_invoice_payment_type, mLayoutPaymentContainer, false);
							((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_payment_type_order_detail)).setText(mixPaymentModelArrayList.get(i).getPayment_type());
							try {
								double payment = Double.parseDouble(mixPaymentModelArrayList.get(i).getPayment_amount().replaceAll(",", ""));
								((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_amount_order_detail)).setText(Consts.getCommaFormatedStringWithDecimal((float) payment));
							} catch (Exception e) {
								((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_amount_order_detail)).setText(mixPaymentModelArrayList.get(i).getPayment_amount());
								e.printStackTrace();
							}

							mLayoutPaymentContainer.addView(paymentRow);
						}
					}
				} else if (mInvoiceModel.getPartial_payment() != null && mInvoiceModel.getPartial_payment().size() > 0) {
					ArrayList<InvoiceModel.PartialPaymentModel> partialPaymentModelArrayList = new ArrayList<>(mInvoiceModel.getPartial_payment());
					if (partialPaymentModelArrayList.size() > 0) {
						mLayoutPaymentContainer.removeAllViews();
						for (int i = 0; i < partialPaymentModelArrayList.size(); i++) {
							LinearLayout paymentRow = (LinearLayout) getLayoutInflater().inflate(R.layout.item_row_invoice_payment_type, mLayoutPaymentContainer, false);
							((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_payment_type_order_detail)).setText(partialPaymentModelArrayList.get(i).getPayment_type());
							try {
								double payment = Double.parseDouble(partialPaymentModelArrayList.get(i).getPayment_amount().replaceAll(",", ""));
								((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_amount_order_detail)).setText(Consts.getCommaFormatedStringWithDecimal((float) payment));
							} catch (Exception e) {
								((TextView) paymentRow.findViewById(R.id.item_row_invoice_payment_type_tv_amount_order_detail)).setText(partialPaymentModelArrayList.get(i).getPayment_amount());
								e.printStackTrace();
							}

							mLayoutPaymentContainer.addView(paymentRow);
						}
					}
				}
			}*/

			if (invoiceType == Consts.INVOICE_TYPE_RETURN) {
				/*mTvTitle.setText("RETURN INVOICE");*/
				/*mTvLabelPaymentType.setText("REFUND");*/
			}

			String deliveryAddress = mInvoiceModel.getDelivery_address();
			if (currentOrderStatus.equalsIgnoreCase(ORDER_STATUS_PENDING)  && deliveryAddress != null && !deliveryAddress.isEmpty())
			{
				customerAddresTR.setVisibility(View.VISIBLE);
				customerAddressTv.setText(deliveryAddress);
				String land_mark = mInvoiceModel.salesCustOrder.land_mark;
				if (land_mark != null && !land_mark.isEmpty()){
					landMarkTv.setText(land_mark);
					landMarkTR.setVisibility(View.VISIBLE);
				}
			}
			else {
				customerAddresTR.setVisibility(View.GONE);
			}

			if (currentOrderStatus.equalsIgnoreCase(ORDER_STATUS_PENDING))
			{
				deliverySlotTR.setVisibility(View.VISIBLE);
				String deliverySlot = mInvoiceModel.salesCustOrder.delivery_slot;

				if (deliverySlot != null && !deliverySlot.isEmpty()){

					mDeliveryDate  = mInvoiceModel.salesCustOrder.delivery_date;
					mDeliverySlot  = deliverySlot;

					deliverySlotTv.setText(deliverySlot);
				}

				deliverySlotEditBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
				{
					@Override
					public void onClick(View v)
					{
						startActivityForResult(ChangeDeliverySoltActivity.getStartIntent(OrderDetailsActivity.this), REQUEST_CODE_CHANGE_DELIVERY_SLOT);
					}
				}));
			}
			else {
				deliverySlotTR.setVisibility(View.GONE);
			}


			// View Permit button
			String permitImgUrl = mInvoiceModel.salesCustOrder.permit_image;
			if (TextUtils.isEmpty(permitImgUrl)){
				viewPermitBtn.setVisibility(View.GONE);
			}
			else{
				viewPermitBtn.setVisibility(View.VISIBLE);
				viewPermitBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
				{
					@Override
					public void onClick(View v) {
						showImage(permitImgUrl);
					}
				}));
			}

			if (mDeliveryStatus.equalsIgnoreCase("Pending"))
			{
				if(mInvoiceModel.salesCustOrder.delivery_status.equalsIgnoreCase("Accepted")) {
					sendLinkAnduploadInvoice();
				}
			}

			mDeliveryStatus = mInvoiceModel.salesCustOrder.delivery_status;
			mInvoiceUrl = mInvoiceModel.salesCustOrder.invoice_filename;

			if (mDeliveryStatus.equalsIgnoreCase("Accepted")){

				if (TextUtils.isEmpty(mInvoiceUrl)){
					viewInvoice.setText(getResources().getString(R.string.order_detail_lbl_upload_invoice));
				}

				viewInvoicetBtn.setVisibility(View.VISIBLE);
				viewInvoicetBtn.setOnClickListener(new SafeClickListener(v -> {

					if (TextUtils.isEmpty(mInvoiceUrl)){
						mCamerabutton.performClick();
					}
					else {
						showImage(mInvoiceUrl);
					}
				}));
			}
			else{
				viewInvoicetBtn.setVisibility(View.GONE);
			}

			retryApi = false;

			if (AppUtils.isPaymentTypeMswipe(mInvoiceModel)){
				updateUIWithPaymentStatus(AppUtils.isUPIPaymentDone(mInvoiceModel));
			}
			else if (AppUtils.isPaymentTypeUPI(mInvoiceModel)){
				updateUIWithPaymentStatus(AppUtils.isUPIPaymentDone(mInvoiceModel));
			}
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

	private void updateItemsOnUI(List<ItemDetails> newList)
	{
		mLayoutContainer.removeAllViews();
		boolean isLastQuantityLeft = false;
		if (newList.size() == 1  && newList.get(0).getQuantityInDouble() == 1)
		{
			isLastQuantityLeft = true;
		}

		double totalInDouble = 0;
		for (final ItemDetails itemsModel : newList)
		{
			final View view = getLayoutInflater().inflate(R.layout.layout_sales_invoice_item, mLayoutContainer, false);
			View decrementItemBtn = view.findViewById(R.id.layout_invoice_item_decrement_quant);
			View removeItemBtn = view.findViewById(R.id.layout_invoice_item_remove_product);
			View addItemBtn = view.findViewById(R.id.layout_invoice_item_increment_quant);
			final TextView qtyValueTV = view.findViewById(R.id.layout_invoice_item_tv_qty_value);

			String orderStatus = mInvoiceModel.salesCustOrder.delivery_status;
			orderStatus = manipulateOrderStatus(orderStatus);
			boolean canRemoveItems = orderStatus.equals(ORDER_STATUS_PENDING) &&  itemsModel.getQuantityInDouble() != 0;
			decrementItemBtn.setVisibility((canRemoveItems && !isLastQuantityLeft)? View.VISIBLE: View.GONE);
			removeItemBtn.setVisibility((canRemoveItems && !isLastQuantityLeft) ? View.VISIBLE: View.GONE);

			decrementItemBtn.setVisibility( View.GONE);
			removeItemBtn.setVisibility( View.GONE);


			//			addItemBtn.setVisibility((canRemoveItems && !isLastQuantityLeft)? View.VISIBLE: View.GONE);

			removeItemBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					isQuantityChanged = true;
					countChangedListener.onItemRemoved(itemsModel);
					/*mLayoutContainer.removeView(view);*/
					//updateItemsOnUI(currentItemList);
				}
			});

			decrementItemBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (itemsModel.getQuantityInDouble() == 0) return;

					isQuantityChanged = true;
					countChangedListener.onItemDecremented(itemsModel);
					/*quant--;
					if (quant == 0){
						mLayoutContainer.removeView(view);
					}
					else {
						String quantityInInt = getQuantityInDispFormat(String.valueOf(quant));
						qtyValueTV.setText(quantityInInt);
					}*/
					updateItemsOnUI(currentItemList);
				}
			});

//			addItemBtn.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					isQuantityChanged = true;
//					countChangedListener.onItemIncremented(itemsModel);
//					updateItemsOnUI(currentItemList);
//
//				}
//			});


			((TextView) view.findViewById(R.id.layout_invoice_item_tv_name)).setText(itemsModel.name);

			// update price according to removed items
			String itemPrice = itemsModel.price;

			try
			{
				double priceInDouble = itemsModel.getPriceInDouble();
				if (itemsModel.getQuantityInDouble() != itemsModel.getOriginalQuantity()){
					double d = ((priceInDouble / itemsModel.getOriginalQuantity())*itemsModel.getQuantityInDouble());
					totalInDouble = totalInDouble + d;
					itemPrice = "" +  Consts.getCommaFormatedStringWithDecimal((float) d);
				}
				else {
					totalInDouble = totalInDouble + itemsModel.getPriceInDouble();
				}

			}
			catch (NumberFormatException e){
				e.printStackTrace();
				itemPrice = "";
			}
			((TextView) view.findViewById(R.id.layout_invoice_item_tv_selling_price)).setText(itemPrice);



			/*if (itemsModel.getBarcode().size() > 0)
				((TextView) view.findViewById(R.id.layout_invoice_item_tv_barcode)).setText(itemsModel.getBarcode().get(0));
			else
				((TextView) view.findViewById(R.id.layout_invoice_item_tv_barcode)).setText("");*/

			((TextView) view.findViewById(R.id.layout_invoice_item_tv_qty)).setText(getString(R.string.quantityc)+" "/* + itemsModel.getQuantity()*/);
			String quantityInInt = getQuantityInDispFormat(itemsModel.quantity);
			qtyValueTV.setText(quantityInInt);
			/*((TextView) view.findViewById(R.id.layout_invoice_item_tv_size)).setText("Size : " + itemsModel.getSize());*/
			LinearLayout metaLayout = view.findViewById(R.id.layout_invoice_item_layout_bottom);

			try
			{
				totalQty = totalQty + (int) itemsModel.getQuantityInDouble();

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

					/*if (mInvoiceModel.getTaxInclusive() == 1) {
						double vatPrice = 0;
						try {
							vatPrice = Double.parseDouble(itemsModel.getVatValue().replaceAll(",", ""));
						} catch (Exception e) {
							e.printStackTrace();
						}

						view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
						String taxType = "VAT : (" + itemsModel.getVat() + "%)";
						((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
						((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) vatPrice));
					} else if (mInvoiceModel.getTaxInclusive() == 2) {
						if (mInvoiceModel.getInvoice_version() != null && mInvoiceModel.getInvoice_version().equalsIgnoreCase("0")) {
							view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
							view.findViewById(R.id.layout_invoice_item_layout_qty_discount).setVisibility(View.GONE);
//							double gstPrice = 0;
//							try {
//								gstPrice = calculateInclusiveGST(itemsModel);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//							totalGst = totalGst + gstPrice;
//							view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(View.VISIBLE);
//							if (!TextUtils.isEmpty(itemsModel.getItem_gst_per())) {
//								String taxType = "GST : (" + itemsModel.getItem_gst_per() + "%)";
//								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
//							} else {
//								String taxType = "GST : (0%)";
//								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
//							}
//							((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) gstPrice));
							String taxType = "GST : (" + itemsModel.getItem_gst_per() + "%)";
							((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
							((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(itemsModel.getItem_gst_amount());

							if (!TextUtils.isEmpty(itemsModel.getItem_discount_per())) {
								totalDiscount = totalDiscount + Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
								String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
							} else {
								totalDiscount = totalDiscount + 0;
								String discount_percent = "Discount : (0%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText("0");
							}

						} else {
							double gstPrice = 0;
							try {
								gstPrice = calculateInclusiveGST(itemsModel);
							} catch (Exception e) {
								e.printStackTrace();
							}

							totalGst = totalGst + gstPrice;
							view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
							if (!TextUtils.isEmpty(itemsModel.getItem_gst_per())) {
								String taxType = "GST : (" + itemsModel.getItem_gst_per() + "%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
							} else {
								String taxType = "GST : (0%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
							}
							((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) gstPrice));

							if (!TextUtils.isEmpty(itemsModel.getItem_discount_per())) {
								totalDiscount = totalDiscount + Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
								String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
							} else {
								totalDiscount = totalDiscount + 0;
								String discount_percent = "Discount : (0%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText("0");
							}
//						String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
//						((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
//						((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
						}
					}
					else {
						//				view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(View.GONE);
						if (!TextUtils.isEmpty(itemsModel.getVatValue())) {
							double vatPrice;
							try {
								vatPrice = Double.parseDouble(itemsModel.getVatValue().replaceAll(",", ""));
								if (vatPrice > 0) {
									view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
									String taxType = "GST : (" + itemsModel.getVat() + "%)";
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) vatPrice));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (!TextUtils.isEmpty(itemsModel.getItem_gst_per())) {
							if (mInvoiceModel.getInvoice_version() != null && mInvoiceModel.getInvoice_version().equalsIgnoreCase("0")) {
								view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
								view.findViewById(R.id.layout_invoice_item_layout_qty_discount).setVisibility(View.GONE);
								try {
//									double gstPrice = 0;
//									gstPrice = calculateExlusiveGST(itemsModel);
//
//									totalGst = totalGst + gstPrice;
//									view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(View.VISIBLE);
//									if (!TextUtils.isEmpty(itemsModel.getItem_gst_per())) {
//										String taxType = "GST : (" + itemsModel.getItem_gst_per() + "%)";
//										((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
//									} else {
//										String taxType = "GST : (0%)";
//										((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
//									}
//									((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) gstPrice));
									String taxType = "GST : (" + itemsModel.getItem_gst_per() + "%)";
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(itemsModel.getItem_gst_amount());

									if (!TextUtils.isEmpty(itemsModel.getItem_discount_per())) {
										totalDiscount = totalDiscount + Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
										String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
									} else {
										totalDiscount = totalDiscount + 0;
										String discount_percent = "Discount : (0%)";
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText("0");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							} else {
								double gstPrice = 0;
								try {
									gstPrice = calculateExlusiveGST(itemsModel);

									totalGst = totalGst + gstPrice;
									view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
									if (!TextUtils.isEmpty(itemsModel.getItem_gst_per())) {
										String taxType = "GST : (" + itemsModel.getItem_gst_per() + "%)";
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
									} else {
										String taxType = "GST : (0%)";
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
									}
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) gstPrice));

									if (!TextUtils.isEmpty(itemsModel.getItem_discount_per())) {
										totalDiscount = totalDiscount + Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
										String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
									} else {
										totalDiscount = totalDiscount + 0;
										String discount_percent = "Discount : (0%)";
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
										((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText("0");
									}


								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							if (mInvoiceModel.getInvoice_version() != null && mInvoiceModel.getInvoice_version().equalsIgnoreCase("0")) {
								view.findViewById(R.id.layout_invoice_item_layout_tax_price).setVisibility(taxPriceVisibility);
								view.findViewById(R.id.layout_invoice_item_layout_qty_discount).setVisibility(View.GONE);
								String taxType = "GST : (0%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText("0");

								if (!TextUtils.isEmpty(itemsModel.getItem_discount_per())) {
									totalDiscount = totalDiscount + Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
									String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
								} else {
									totalDiscount = totalDiscount + 0;
									String discount_percent = "Discount : (0%)";
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText("0");
								}

							} else {
								String taxType = "GST : (0%)";
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_type)).setText(taxType);
								((TextView) view.findViewById(R.id.layout_invoice_item_tv_tax_price)).setText("0");
								if (!TextUtils.isEmpty(itemsModel.getItem_discount_per())) {
									totalDiscount = totalDiscount + Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
									String discount_percent = "Discount : (" + itemsModel.getItem_discount_per() + "%)";
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText(Consts.getCommaFormatedStringWithDecimal((float) Double.parseDouble(itemsModel.getItem_discount_amount())));
								} else {
									totalDiscount = totalDiscount + 0;
									String discount_percent = "Discount : (0%)";
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount)).setText(discount_percent);
									((TextView) view.findViewById(R.id.layout_invoice_item_tv_discount_price)).setText("0");
								}
							}

						}
					}

			boolean isMetaAdded = false;
			int padding = ShopxieUtils.dpToPx(mActivity, 4);
			if (showMetaData*//*itemsModel.getSalesMeta() != null*//*) {
				List<InvoiceModel.InvoiceSalesMetaModel> invoiceSalesMetaModelList = itemsModel.getSalesMeta();
				if (invoiceSalesMetaModelList != null && !invoiceSalesMetaModelList.isEmpty()) {
					for (InvoiceModel.InvoiceSalesMetaModel saleMetaModel : invoiceSalesMetaModelList) {
						List<SalesStoreMetaModel> salesStoreMetaModelList = saleMetaModel.getStoreMeta();
						for (final SalesStoreMetaModel storeMetaModel : salesStoreMetaModelList) {
							if (!TextUtils.isEmpty(storeMetaModel.getStoreMetaFieldValue())) {
								TextView textView = new TextView(mActivity);
								textView.setPadding(0, padding, 0, padding);
								String text = storeMetaModel.getStoreMetaFieldName() + " : " + storeMetaModel.getStoreMetaFieldValue();
								textView.setTextAppearance(mActivity, R.style.font_regular_14_graphite);
								textView.setText(text);
								metaLayout.addView(textView);
								isMetaAdded = true;
							}
						}
					}
				}
				if (isMetaAdded) {
					metaLayout.setVisibility(View.VISIBLE);
				} else {
					metaLayout.setVisibility(View.GONE);
				}
			} else {
				metaLayout.setVisibility(View.GONE);
			}*/
			mLayoutContainer.addView(view);
		}

		if (currentOrderStatus.equals(ORDER_STATUS_PENDING))
		{
			mTvTotalQty.setText(Consts.getCommaFormatedStringWithDecimal((float) totalInDouble));
			mTvGrandTotal.setText(Consts.getCommaFormatedStringWithDecimal((float) totalInDouble));
			mTvTotalAmount.setText(Consts.getCommaFormatedStringWithDecimal((float) totalInDouble));
		}
	}

	/*@OnClick({R.id.activity_order_details_iv_edit_remark, R.id.activity_order_details_btn_send_bill, R.id.activity_order_details_btn_print_bill})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.activity_order_details_iv_edit_remark:
				editRemarkClicked();
				break;
			case R.id.activity_order_details_btn_send_bill:
				sendInvoice();
				break;
			case R.id.activity_order_details_btn_print_bill:
				printInvoice();
				break;
		}
	}

private void returnClicked() {
		Intent intent = new Intent(mActivity, ReturnActivity.class);
		intent.putExtra(Consts.EXTRA_ORDER_ID, mInvoiceModel.getOrderID());
		intent.putExtra(Consts.EXTRA_INVOICE, mInvoiceModel);
		startActivityForResult(intent, Consts.REQUEST_CODE_RETURN_INVOICE);
	}

	private void editCustomFieldsClicked() {
		Intent intent = new Intent(mActivity, EditCustomFieldsActivity.class);
		intent.putExtra(Consts.EXTRA_INVOICE, mInvoiceModel);
		startActivityForResult(intent, Consts.REQUEST_CODE_EDIT_META);
	}

	private void sendInvoice() {
		showLoadingDialog();
		CartClient cartClient = ServiceGenerator.createService(CartClient.class, MventryApp.getInstance().getToken());
		//Log.d("Nish", "Token :" + MventryApp.getInstance().getToken());

		Call<String> call = cartClient.sendInvoice(mInvoiceModel.getOrderID() + "");

		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				hideLoadingDialog();
				if (response.isSuccessful()) {
					String responseString = response.body();
					responseString = responseString.replaceAll("\\n", "");
					try {
						if (!TextUtils.isEmpty(responseString)) {
							JSONObject jsonResponse = new JSONObject(responseString);
							if (jsonResponse.has("status")) {
								if (jsonResponse.get("status").toString().equalsIgnoreCase("success")) {
									Toast.makeText(OrderDetailsActivity.this, "Bill Sent Successfully", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent();
									setResult(Activity.RESULT_OK, intent);
									finish();
								}
							}
						} else {
							showErrorDialog("Error", "Something went wrong : Response is empty");
						}
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog("Error", "Something went wrong : Invalid Response");
					}
				} else {
					try {
						if (response.code() == 401) {
							showErrorDialog("Error", response.message());
						} else {
							Response<String> response1 = Response.success(response.errorBody().string().replace("\n", ""));
							parseError(response1, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
						if (response.body() != null) {
							showErrorDialog("Error", "Something went wrong: " + response.body());
						} else {
							showErrorDialog("Error", "Something went wrong: " + response.message());
						}
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

	private void printInvoice() {
		Intent intent;
		if (ShopxieSharedPreferences.getInstance().getReceiptPrinter().equalsIgnoreCase(Consts.PRINTER_TYPE_ZEBRA)) {
			intent = new Intent(mActivity, PrintReceiptActivity.class);
		} else if (ShopxieSharedPreferences.getInstance().getReceiptPrinter().equalsIgnoreCase(Consts.PRINTER_TYPE_XPRINTER)) {
			if (ShopxieSharedPreferences.getInstance().getXprinterPrinterConnection().equalsIgnoreCase(Consts.PRINTER_TYPE_XPRINTER_BLUETOOTH)) {
				intent = new Intent(mActivity, XprinterReceiptActivity.class);
			} else if (ShopxieSharedPreferences.getInstance().getXprinterPrinterConnection().equalsIgnoreCase(Consts.PRINTER_TYPE_XPRINTER_USB)) {
				intent = new Intent(mActivity, XprinterReceiptUsbActivity.class);
			} else {
				intent = new Intent(mActivity, XprinterReceiptActivity.class);
			}
		} else if (ShopxieSharedPreferences.getInstance().getReceiptPrinter().equalsIgnoreCase(Consts.PRINTER_TYPE_SIMPLY)) {
			intent = new Intent(mActivity, SimplyReceiptActivity.class);
		} else {
			intent = new Intent(mActivity, PrintReceiptActivity.class);
		}
		intent.putExtra(Consts.EXTRA_INVOICE, mInvoiceModel);
		startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);
	}

	private void editRemarkClicked() {
		String remark = "";
		if (!TextUtils.isEmpty(mTvRemark.getText())) {
			remark = mTvRemark.getText().toString().trim();
		}

		RemarkDialogFragment remarkDialogFragment = (RemarkDialogFragment) getSupportFragmentManager().findFragmentByTag(RemarkDialogFragment.FRAGMENT_TAG);
		if (remarkDialogFragment == null) {
			remarkDialogFragment = RemarkDialogFragment.newInstance(remark, OrderDetailsActivity.this);
			remarkDialogFragment.setCancelable(true);
			getSupportFragmentManager().beginTransaction().add(remarkDialogFragment, RemarkDialogFragment.FRAGMENT_TAG).commitAllowingStateLoss();
		}
	}*/


	private void getOrderDetails()
	{

		mCamerabutton = new CameraButton(this);
		mCamerabutton.setCameraButtonResultListener(this);

		if (!TextUtils.isEmpty(mOrderId))
		{
			showLoadingDialog();

			String deliveryType = "0";

			switch (currentOrderStatus)
			{
				case ORDER_STATUS_PENDING:
				case ORDER_STATUS_ACCEPTED: {
					deliveryType = "0";
					break;
				}
				case ORDER_STATUS_RETURNED: {
					deliveryType = "1";
					break;
				}
				case ORDER_STATUS_DELIVERED: {
					deliveryType = "2";
					break;
				}
			}


			try {
				new SalesDetailsController( mActivity, new CustomerNetworkObserver())
						.getSalesDetails(mOrderId, deliveryType);
			}
			catch (Exception ex ) {

				showErrorDialog(ErrorUtils.getFailureError(ex));
			}




		}
	}


	class CustomerNetworkObserver implements MOMNetworkResponseListener
	{

		@Override
		public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
		{
			hideLoadingDialog();
			SalesDetailsResp responseData = (SalesDetailsResp) mMOMNetworkResDataStore;
			if(responseData.status ==1)
			{


				mInvoiceModel = responseData;

				updateUI();
				updateUIForStatus(mInvoiceModel.salesCustOrder.delivery_status);

			}
			else {
				String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;

				showErrorDialog(errorMsg);
			}


		}
	}


	private void getOrderDetails_old()
	{
/*
		mCamerabutton = new CameraButton(this);
		mCamerabutton.setCameraButtonResultListener(this);

		if (!TextUtils.isEmpty(mOrderId))
		{
			showLoadingDialog();

			OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MventryApp.getInstance().getToken());
			String storeId = MventryApp.getInstance().getCurrentStoreId();
			String deliveryType = "0";

			switch (currentOrderStatus)
			{
				case ORDER_STATUS_PENDING:
				case ORDER_STATUS_ACCEPTED: {
					deliveryType = "0";
					break;
				}
				case ORDER_STATUS_RETURNED: {
					deliveryType = "1";
					break;
				}
				case ORDER_STATUS_DELIVERED: {
					deliveryType = "2";
					break;
				}
			}
			Call<OrderDetailOuter> call = ordersClient.getInvoiceDetailsFromOrderId(mOrderId, storeId, deliveryType);
			call.enqueue(new Callback<OrderDetailOuter>() {
				@Override
				public void onResponse(Call<OrderDetailOuter> call, Response<OrderDetailOuter> response) {
					hideLoadingDialog();
					if (response.isSuccessful()) {
						OrderDetailOuter outer = response.body();
						if (outer == null){
							showErrorDialog(getString(R.string.sww));
							return;
						}

						mInvoiceModel = outer.getData();

						updateUI();
						updateUIForStatus(mInvoiceModel.getOrderStatus());

					}
					else
						showErrorDialog(ErrorUtils.getErrorString(response));
				}

				@Override
				public void onFailure(Call<OrderDetailOuter> call, Throwable t) {
					hideLoadingDialog();
					showErrorDialog(ErrorUtils.getFailureError(t));
				}
			});
		}
		*/
	}

	/*private void setRemark(final String remark) {
		showLoadingDialog();
		CartClient cartClient = ServiceGenerator.createService(CartClient.class, MventryApp.getInstance().getToken());
		Call<String> call = cartClient.updateRemark(mInvoiceModel.getOrderID(), remark);
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				hideLoadingDialog();
				if (response.isSuccessful()) {
					String responseString = response.body();
					responseString = responseString.replaceAll("\\n", "");
					try {
						if (!TextUtils.isEmpty(responseString)) {
							JSONObject jsonResponse = new JSONObject(responseString);
							if (jsonResponse.has("status")) {
								if (jsonResponse.get("status").toString().equalsIgnoreCase("success")) {
									mTvRemark.setText(remark);
									showErrorDialog("Success", "Remark is updated");
								}
							}
						} else {
							try {
								if (response.code() == 401) {
									showErrorDialog("Error", response.message());
								} else {
									Response<String> response1 = Response.success(response.errorBody().string().replace("\n", ""));
									parseError(response1, true);
								}
							} catch (Exception e) {
								e.printStackTrace();
								if (response.body() != null) {
									showErrorDialog("Error", "Something went wrong: " + response.body());
								} else {
									showErrorDialog("Error", "Something went wrong: " + response.message());
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog("Error", "Something went wrong : Invalid Response");
					}
				} else {
					if (response.message() != null) {
						showErrorDialog("Error", "Something went wrong: " + response.message());
					} else {
						showErrorDialog("Error", "Something went wrong: " + response.body());
					}
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				hideLoadingDialog();
				showErrorDialog(ErrorUtils.getFailureError(t));
			}
		});
	}*/

	@Override
	public void onFragmentInteraction(Object obj) {
		/*String remark = (String) obj;
		setRemark(remark);*/
	}

	/*private double calculateInclusiveGST(InvoiceModel.InvoiceItemsModel itemsModel) {
		double gst_value = 0;
		double price = 0;
		try {
			double gst_percent = Double.parseDouble(itemsModel.getItem_gst_per());
			if (itemsModel.getItem_discount_amount() != null)
				price = Double.parseDouble(itemsModel.getMrp().replaceAll(",", "")) - Double.parseDouble(itemsModel.getItem_discount_amount().replaceAll(",", ""));
			else
				price = Double.parseDouble(itemsModel.getMrp().replaceAll(",", ""));
			gst_value = price - ((price / (100 + gst_percent) * 100));
		} catch (Exception e) {
			e.printStackTrace();
			gst_value = 0;
		}
		return gst_value;
	}

	private double calculateExlusiveGST(InvoiceModel.InvoiceItemsModel itemsModel) {
		double gst_value = 0;
		try {
			double gst_percent = Double.parseDouble(itemsModel.getItem_gst_per());
			double price = Double.parseDouble(itemsModel.getMrp().replaceAll(",", ""));
			if (!TextUtils.isEmpty(itemsModel.getItem_discount_amount())) {
				price = price - Double.parseDouble(itemsModel.getItem_discount_amount());
			}
			gst_value = (price * gst_percent) / 100;
		} catch (Exception e) {
			e.printStackTrace();
			gst_value = 0;
		}
		return gst_value;
	}*/

	private void updateUIForStatus(@ORDER_STATUS String status)
	{
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
						/*if (isPaymentTypeMswipe(mInvoiceModel)){
							sendPaymentForAccept(true);
						}
						else if (isPaymentTypeUPI(mInvoiceModel)){
							sendPaymentForAccept(true);
						}
						else {
							onTriggerAcceptOrder();
						}*/

						onTriggerAcceptOrder();
						//sendPaymentForAccept(true);
					}
				}));
				bottomAction2Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
					@Override
					public void onClick(View v) {
						TwoBtnDialogFragment.showDialog(OrderDetailsActivity.this,
								getString(R.string.general_use_are_you_sure),
								getString(R.string.dialog_order_all_removed_subtitle),
								getString(R.string.general_use_no), getString(R.string.general_use_yes),
								new TwoBtnDialogFragment.CustomDialogListener() {
									@Override
									public void onClickLeftBtn() {}

									@Override
									public void onClickRightBtn() {
										/*if (isPaymentTypeMswipe(mInvoiceModel)){
											checkMswipePaymentStatusForAcceptDecline(false);
										}
										else if (isPaymentTypeUPI(mInvoiceModel)){
											checkUPIPaymentStatusForAcceptDecline(false);
										}
										else {
											callReturnApi(initialItemsList, ORDER_STATUS_RETURNED, false);
										}*/

										callReturnApi(initialItemsList, ORDER_STATUS_RETURNED, false);

									}
								}
						);
					}
				}));
				break;

			case ORDER_STATUS_ACCEPTED:
				viewInvoicetBtn.setVisibility(View.VISIBLE);
				hintToAccept.setVisibility(View.VISIBLE);
				bottomActionBtns.setVisibility(View.VISIBLE);
				bottomAction1Tv.setText(getString(R.string.order_detail_label_cta_delivered));
				bottomAction2Tv.setText(getString(R.string.order_detail_label_cta_returned));
				bottomAction1Tv.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
					@Override
					public void onClick(View v) {

						/*if (isPaymentTypeMswipe(mInvoiceModel)){
							checkMswipePaymentStatusForAcceptDecline(true);
						}
						else if (isPaymentTypeUPI(mInvoiceModel)){
							checkUPIPaymentStatusForAcceptDecline(true);
						}
						else {
							sendStatus(ORDER_STATUS_DELIVERED);
						}*/

						if (AppUtils.isPaymentTypeMswipe(mInvoiceModel))
						{
							if(AppUtils.isUPIPaymentDone(mInvoiceModel))
							{

								sendStatus(ORDER_STATUS_DELIVERED);
							}
							else{

								TwoBtnDialogFragment.showDialog(OrderDetailsActivity.this,
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
						else if (AppUtils.isPaymentTypeUPI(mInvoiceModel))
						{

							if(AppUtils.isUPIPaymentDone(mInvoiceModel))
							{

								sendStatus(ORDER_STATUS_DELIVERED);
							}
							else{

								TwoBtnDialogFragment.showDialog(OrderDetailsActivity.this,
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
						callReturnApi(currentItemList, ORDER_STATUS_RETURNED, true);
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
		showLoadingDialog();
		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		Call<String> call = ordersClient.checkOrderPaymentStatus(mInvoiceModel.salesCustOrder.txn_id,
				MOMApplication.getInstance().getMswipeUsername(),
				MOMApplication.getInstance().getVender());
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
	}

	private void checkUPIPaymentStatusForAcceptDecline(final boolean isFromAccept)
	{
		showLoadingDialog();
		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		Call<String> call = ordersClient.checkVPAStatus(mInvoiceModel.salesCustOrder.txn_id,
				MOMApplication.getInstance().getMswipeUsername());

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
	}

	private void onTriggerAcceptOrder()
	{
		if (currentItemList.size() == 0){
			callReturnApi(initialItemsList, ORDER_STATUS_RETURNED, false);
		}
		else {
			if (isQuantityChanged){
				List<ItemDetails> list = getRemovedItems(initialItemsList, currentItemList);
				callReturnApi(list, ORDER_STATUS_ACCEPTED, false);
			}
			else {
				sendStatus(ORDER_STATUS_ACCEPTED);
			}
		}
	}

	private void sendStatus(@ORDER_STATUS final String toSendOrderStatus)
	{
		int intEquivalentOfOrderStatus = getIntEquivalentOfOrder(toSendOrderStatus);
		showLoadingDialog();
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
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
								else if (toSendOrderStatus.equals(ORDER_STATUS_RETURNED)/* || toSendOrderStatus.equals(ORDER_STATUS_PARTIAL_COMPLETED)*/){
									if (currentOrderStatus.equals(ORDER_STATUS_PENDING)){
										// do nothing , let the user click accept to accept the remaining order.
										// update the UI
										/*if (isQuantityChanged){
											getOrderDetails();
										}
										else {
											//show all removed dialog
											finish();
										}*/
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


	}

	private void showAcceptDeclineDialog(final boolean isAccepted) {
		final OutputResultDialogFragment acceptDeclineDialog = OutputResultDialogFragment
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
					getOrderDetails();
				}
				else {
					Intent intent = new Intent();
					intent.putExtra(Consts.EXTRA_ISUPDATED, true);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		}, DIALOG_DISPLAY_TIME);

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


	/**
	 * call return api if onclick of decline
	 *
	 */
	private void callReturnApi(List<ItemDetails> listToSend,
							   final  @ORDER_STATUS String statusCodeToSend, boolean isAlreadyAcceptedOrder)
	{
		/*
		showLoadingDialog();
		CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class,
				MventryApp.getInstance().getToken());
		LogUtils.logd("orde","return product : st"+statusCodeToSend);
		//List<InvoiceModel.InvoiceItemsModel> list = getRemovedItems(initialItemsList, currentItemList);


		String paymentTypeToSend = mInvoiceModel.salesCustOrder.payment_type;
		if (isAmountNotPaid){
			paymentTypeToSend = "Cash";
		}

		JSONObject jsonObject = ServerSideOrderUtils.generateReturnJsonObject(listToSend, mInvoiceModel,
				paymentTypeToSend, isAmountNotPaid);

		Call<String> call = isAlreadyAcceptedOrder ? customerClient.returnAccepted(jsonObject.toString().trim())
				:  customerClient.returnProductsV2(jsonObject.toString().trim());

		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				hideLoadingDialog();
				if (response.isSuccessful()){
					String responseString = response.body();
					responseString = responseString.replaceAll("\\n", "");
					try {
						if (!TextUtils.isEmpty(responseString)) {
							JSONObject jsonResponse = new JSONObject(responseString);
							if (jsonResponse.has("status") && jsonResponse.get("status").toString().equalsIgnoreCase("success")) {

								if (jsonResponse.has("upi_remaining_amout"))
								{
									if (isQuantityChanged) {

										String upi_remaining_amout = jsonResponse.getString("upi_remaining_amout");

										if(upi_remaining_amout != null && upi_remaining_amout.length() > 0)
										{
											double grandTotal = Double.parseDouble(upi_remaining_amout.replaceAll(",", ""));
											mAmount  = ""  + grandTotal;
										}
									}
								}

								showAcceptDeclineDialog(statusCodeToSend.equals(ORDER_STATUS_ACCEPTED));
							}
							else {
								showErrorDialog(OrderDetailsActivity.this.getString(R.string.invalid_server_response));
							}
						} else {
							showErrorDialog(OrderDetailsActivity.this.getString(R.string.invalid_server_response));
						}
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog(OrderDetailsActivity.this.getString(R.string.invalid_server_response));
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


	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent();
		intent.putExtra(Consts.EXTRA_ISUPDATED, true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void fetchMswipePaymentStatus()
	{
        String txn_id = mInvoiceModel.salesCustOrder.txn_id;
        if (txn_id == null || txn_id.isEmpty()) return;
        showLoadingDialog();
		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		Call<String> call = ordersClient.checkOrderPaymentStatus(txn_id,
				MOMApplication.getInstance().getMswipeUsername(),
				MOMApplication.getInstance().getVender());

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
						if (getTrasactionStatusJArr.length() > 0){
							JSONObject firstObj = getTrasactionStatusJArr.getJSONObject(0);
							String transactionStatus = firstObj.optString("transactionStatus");
							boolean isPaid = transactionStatus.equalsIgnoreCase("paid");
                            updateUIWithPaymentStatus(isPaid);
						}
						else {
                            showErrorDialog(getString(R.string.sww));
                        }
					} catch (JSONException e) {
						e.printStackTrace();
                        showErrorDialog(getString(R.string.invalid_server_response));
					}
				}
				else {
					//refreshPaymentStatusBtn.setVisibility(View.VISIBLE);
					paymentStatusTv.setText("-");
					showErrorDialog(ErrorUtils.getErrorString(response));
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				if (isFinishing()) return;
				hideLoadingDialog();
				//refreshPaymentStatusBtn.setVisibility(View.VISIBLE);
				paymentStatusTv.setText("-");
				showErrorDialog(ErrorUtils.getFailureError(t));
			}
		});

	}

	private void fetchUPIPaymentStatus()
	{
        String txn_id = mInvoiceModel.salesCustOrder.txn_id;
        if (txn_id == null || txn_id.isEmpty()) return;
        showLoadingDialog();
		CustomerClient ordersClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());
		Call<String> call = ordersClient.checkVPAStatus(txn_id,
				MOMApplication.getSharedPref().getMswipeUsername());

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
						updateUIWithPaymentStatus(isPaid);
					} catch (JSONException e) {
						e.printStackTrace();
						showErrorDialog(getString(R.string.invalid_server_response));
					}
				}
				else {
					//refreshPaymentStatusBtn.setVisibility(View.VISIBLE);
					paymentStatusTv.setText("-");
					showErrorDialog(ErrorUtils.getErrorString(response));
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t)
			{
				if (isFinishing()) return;
				hideLoadingDialog();
				//refreshPaymentStatusBtn.setVisibility(View.VISIBLE);
				paymentStatusTv.setText("-");
				showErrorDialog(ErrorUtils.getFailureError(t));
			}
		});

	}

	private void updateUIWithPaymentStatus(boolean isPaid)
	{
        paymentStatusTR.setVisibility(View.VISIBLE);
        paymentStatusTv.setVisibility(View.VISIBLE);
        paymentStatusTv.setText(isPaid ? getString(R.string.payment_status_paid) :getString(R.string.payment_status_pending));
        /*if (!isPaid){
            refreshPaymentStatusBtn.setVisibility(View.VISIBLE);
            refreshPaymentStatusBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
                @Override
                public void onClick(View v) {
                    paymentStatusTv.setVisibility(View.INVISIBLE);
                    refreshPaymentStatusBtn.setVisibility(View.INVISIBLE);
                   *//* if (isPaymentTypeMswipe(mInvoiceModel)){
                        fetchMswipePaymentStatus();
                    }
                    else if (isPaymentTypeUPI(mInvoiceModel)){
                        fetchUPIPaymentStatus();
                    }*//*
                }
            }));
        }*/
    }

    private void onFetchedPaymentStatusForAcceptDeclineOrder(boolean isFromAccept, boolean isPaid)
	{
		isAmountNotPaid = !isPaid;
		if (isFromAccept){
			if (isPaid){
				//onTriggerAcceptOrder();
				sendStatus(ORDER_STATUS_DELIVERED);
			}
			else {
				// don't allow to accept the order.
				TwoBtnDialogFragment.showDialog(OrderDetailsActivity.this,
						getString(R.string.general_use_are_you_sure),
						getString(R.string.dialog_payment_status_not_paid_amount),
						getString(R.string.ok), getString(R.string.cancel),
						new TwoBtnDialogFragment.CustomDialogListener() {
							@Override
							public void onClickLeftBtn() {
								callReturnApi(initialItemsList, ORDER_STATUS_RETURNED, false);
							}

							@Override
							public void onClickRightBtn() {}
						}
				);
			}
		}
		else {
			callReturnApi(initialItemsList, ORDER_STATUS_RETURNED, false);
		}
	}

	private void showImage(String permitImgUrl) {
		FullImageDialogFragment dialogFragment = FullImageDialogFragment.newInstance(permitImgUrl);
		dialogFragment.show(getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
	}

	@Override
	public void imageSaved(Uri uri) {

		Toast.makeText(this, "Photo Selected : " + uri.getPath(), Toast.LENGTH_SHORT).show();
		mImageUri = uri;
		uploadStoreProfileImage(mImageUri.getPath(), mInvoiceModel.salesCustOrder.sale_id);
	}

	private void sendLinkAnduploadInvoice()
	{

		if (AppUtils.isPaymentTypeMswipe(mInvoiceModel)){
			sendPaymentForAccept(true);
		}
		else if (AppUtils.isPaymentTypeUPI(mInvoiceModel)){
			sendPaymentForAccept(true);
		}
		else {
			uploadInvoice();
		}
	}

	private void uploadInvoice()
	{

		TwoBtnDialogFragment.showDialog(OrderDetailsActivity.this,"",
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
									viewInvoice.setText(getResources().getString(R.string.order_detail_lbl_view_invoice));
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
