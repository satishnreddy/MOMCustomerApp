package com.mom.momcustomerapp.data.application;


import com.mom.momcustomerapp.networkservices.ParseUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

/*
 * Created by Nishant on 28-04-2016.
 */
public class Consts {

    public static final String SHARED_PREF = "shared_pref";
    public static final String PREF_TOKEN = "token_pref";
    public static final String PREF_VENDER = "vender_pref";
    public static final String PREF_STORE_ID = "store_pref";
    public static final String PREF_STORE_NAME = "store_name_pref";
    public static final String PREF_PROFILE = "profile_pref";
    public static final String PREF_SERVER_URL = "pref_server_url";
    public static final String PREF_PROFILE_SETUP = "profile_setup_pref";
    public static final String PREF_PROFILE_SETUP_NOTIFIED_OWNER = "profile_setup_notified_owner_pref";
    public static final String PREF_PROFILE_IMAGE = "profile_image_pref";
    public static final String PREF_USER_TYPE = "user_type_pref";
    public static final String PREF_IS_DATA_CLEARED = "pref_is_data_cleared";
    public static final String PREF_IS_PERMISSION_TAKEN = "pref_is_permission_taken";
    public static final String PREF_PRINTER_RECEIPT = "pref_printer_receipt";
    public static final String PREF_PRINTER_LABEL = "pref_printer_label";
    public static final String PREF_PRINTER_RECEIPT_MAC = "pref_printer_receipt_mac";
    public static final String PREF_PRINTER_LABEL_MAC = "pref_printer_label_mac";
    public static final String PREF_XPRINTER_CONN = "pref_xprinter_conn";
    public static final String PREF_USB_PRINTER_NAME = "pref_usb_printer_name";
    public static final String PREF_DEF_LABEL_TEMPLATE = "pref_def_label_template";
    public static final String PREF_STOCK_TAKING_END = "pref_stock_taking_end";
    public static final String PREF_GLOBAL_DISCOUNT_TOTAL = "profile_global_discount_total";
    public static final String PREF_PERSON_ID = "298322048230";
    public static final String PREF_SHOULD_LOG_FIRST_PRODUCT = "38723949034";
    public static final String PREF_CUST_ADDRESS = "cust_address";
    public static final String PREF_DELIVERY_TYPE = "delivery_type";



    public static final int FRAGMENT_PRODUCTS           = 2;
    public static final int FRAGMENT_ORDERS             = 3;
    public static final int FRAGMENT_CUSTOMERS          = 4;
    public static final int FRAGMENT_VOUCHER            = 8;
    public static final int FRAGMENT_MBASKET            = 14;
    public static final int FRAGMENT_PROFILE            = 15;
    public static final int FRAGMENT_DELIVERY           = 16;
    public static final int FRAGMENT_STORE_SETTING      = 17;
    public static final int FRAGMENT_STORE_DETAILS      = 18;
    public static final int FRAGMENT_REPORT_SETTINGS    = 19;
    public static final int FRAGMENT_CHANGE_LANGUAGE    = 20;

    public static final String PREF_SHOW_RATE_APP_TIME      = "4400000001";
    public static final String PREF_SECOND_ORDER_RECEIVED   = "4400000002";
    public static final String PREF_STORE_IMAGE_SETTING     = "4400000003";
    public static final String PREF_CATEGORY_IMAGE_SETTING  = "4400000004";
    public static final String PREF_ITEM_IMAGE_SETTING      = "4400000005";
    public static final String PREF_NAME                    = "4400000006";
    public static final String PREF_REFERRAL_ID             = "4400000007";
    public static final String PREF_REFERRAL_SENT           = "4400000008";
    public static final String PREF_SIGNUPP_NUM             = "4400000009";
    public static final String PREF_LANGUAGE                = "4400000010";

    public static final String MSWIPE_USERNAME = "mswipe_username";
    public static final String MSWIPE_PASSWORD = "mswipe_password";
    public static final String MSWIPE_NETWORK_SOURCE_NAME = "mswipe_network_source_name";
    public static final String MSWIPE_GATEWAY_ENVIRONMENT_NAME = "mswipe_gateway_environment_name";
    public static final String MSWIPE_SESSION_TOKEN = "mswipe_session_token";
    public static final String MSWIPE_REFERENCE_ID = "mswipe_reference_id";
    public static final String MSWIPE_TIP_ENABLED = "mswipe_tip_enabled";
    public static final String MSWIPE_CURRENY_CODE = "mswipe_curreny_code";
    public static final String MSWIPE_CONVENIENCE_PERCENTAGE = "mswipe_convenience_percentage";
    public static final String MSWIPE_SERVICE_PERCENTAGE_ON_CONVENIENCE = "mswipe_service_percentage_on_convenience";
    public static final String MSWIPE_LAST_TRX_AMOUNT = "mswipe_last_trx_amount";
    public static final String MSWIPE_LAST_TRX_LAST_FOUR_DIGITS = "mswipe_last_trx_last_four_digits";
    public static final String MSWIPE_LAST_TRX_KSN = "mswipe_last_trx_ksn";
    public static final String MSWIPE_PIN_BYPASS = "mswipe_pin_bypass";
    public static final String MSWIPE_RECEIPT_ENABLED = "mswipe_receipt_enabled";
    public static final String MSWIPE_PRINTER_SUPPORT = "mswipe_printer_support";
    public static final String MSWIPE_PRINT_SIGNATURE = "mswipe_print_signature";
    public static final String MSWIPE_PRINTER_ADDRESS = "mswipe_printer_address";


    public static final String BROADCAST_VERIFICATION_CODE = "broadcast_verification_code";
    public static final String BROADCAST_UPDATE_CART = "broadcast_update_cart";
    public static final String BROADCAST_UPDATE_IMAGE = "broadcast_update_image";

    public static final String AUTHORITY = "com.mswipe.mventry.provider";
    public static final String STORAGE_FOLDER = "/ShopXie";
    public static final String STORAGE_CACHE_FOLDER = "/ShopXie/Cache";
    public static final String GALLERY_FALLBACK_PREFERENCE = "GALLERY_FALLBACK_PREFERENCE";
    public static final String GALLERY_USE_OLD = "GALLERY_USE_OLD";
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    public static final int REQUEST_CHECK_SETTINGS = 1002;
    public static final String DIALOG_ERROR_TAG = "dialog_error";
    public static final int DIALOG_ERROR = 1002;

    public static final int ACTIVITY_MODE_CREATE = 0;
    public static final int ACTIVITY_MODE_EDIT = 1;
    public static final int ACTIVITY_MODE_CAPTURE = 2;
    public static final int ACTIVITY_MODE_QUICK_ADD = 3;
    public static final int ACTIVITY_MODE_MULTI_STORE = 4;

    public static final int INVOICE_TYPE_BILL = 1;
    public static final int INVOICE_TYPE_RETURN = 2;
    public static final int INVOICE_TYPE_PARTIAL = 3;


    public static final String PRINTER_TYPE_ZEBRA = "zebra";
    public static final String PRINTER_TYPE_XPRINTER = "xprinter";
    public static final String PRINTER_TYPE_XPRINTER_BLUETOOTH = "xprinter_bluetooth";
    public static final String PRINTER_TYPE_XPRINTER_USB = "xprinter_usb";
    public static final String PRINTER_TYPE_SIMPLY = "simply";


    public static final int GEO_SUCCESS_RESULT = 0;
    public static final int GEO_FAILURE_RESULT = 1;
    public static final String GEO_PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String GEO_RECEIVER = GEO_PACKAGE_NAME + ".RECEIVER";
    public static final String GEO_RESULT_DATA_KEY = GEO_PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String GEO_RESULT_DATA_ADDRESS = GEO_PACKAGE_NAME + ".RESULT_DATA_ADDRESS";
    public static final String GEO_LOCATION_DATA_EXTRA = GEO_PACKAGE_NAME + ".LOCATION_DATA_EXTRA";


    public static final int REQUEST_CODE_PERMISSIONS = 101;
    public static final int REQUEST_CODE_LOGIN = 1000;
    public static final int REQUEST_CODE_ADD_SUPPLIER = 1001;
    public static final int REQUEST_CODE_VIEW_SUPPLIER = 1002;
    public static final int REQUEST_CODE_DELETE_SUPPLIER = 1003;
    public static final int REQUEST_CODE_ADD_EMPLOYEE = 2001;
    public static final int REQUEST_CODE_VIEW_EMPLOYEE = 2002;
    public static final int REQUEST_CODE_ADD_CUSTOMER = 3001;
    public static final int REQUEST_CODE_VIEW_CUSTOMER = 3002;
    public static final int REQUEST_CODE_DELETE_CUSTOMER = 3003;
    public static final int REQUEST_CODE_ADD_PRODUCT = 4001;
    public static final int REQUEST_CODE_VIEW_PRODUCT = 4002;
    public static final int REQUEST_CODE_CHANGE_CATEGORY = 4003;
    public static final int REQUEST_CODE_ADD_PRODUCT_PRINT = 4004;
    public static final int REQUEST_CODE_EDIT_PRODUCT = 4005;
    public static final int REQUEST_CODE_ADD_BRAND = 5001;
    public static final int REQUEST_CODE_VIEW_BRAND = 5002;
    public static final int REQUEST_CODE_DELETE_BRAND = 5003;
    public static final int REQUEST_CODE_ADD_ITEM = 6001;
    public static final int REQUEST_CODE_VIEW_ITEM = 6002;
    public static final int REQUEST_CODE_CREATE_STORE = 7001;
    public static final int REQUEST_CODE_CREATE_MULTI_STORE = 7002;
    public static final int REQUEST_CODE_CART = 8001;
    public static final int REQUEST_CODE_CART_PRICE = 8002;
    public static final int REQUEST_CODE_CART_MSWIPE = 8003;
    public static final int REQUEST_CODE_EDIT_META = 8004;
    public static final int REQUEST_CODE_EDIT_STORE = 9001;
    public static final int REQUEST_CODE_TOUR = 10001;
    public static final int REQUEST_CODE_PROFILE = 11001;
    public static final int REQUEST_CODE_VIEW_VOUCHER = 12001;
    public static final int REQUEST_CODE_ADD_VOUCHER = 12002;
    public static final int REQUEST_CODE_RETURN_INVOICE = 13001;
    public static final int REQUEST_CODE_VIEW_INVOICE = 13002;
    public static final int REQUEST_CODE_PARTIAL_PAYMENT = 13003;
    public static final int REQUEST_CODE_CHANGE_PASSWORD = 14001;
    public static final int REQUEST_CODE_STOCK_SCAN = 15001;
    public static final int REQUEST_CODE_STOCK_TRANSFER = 15002;
    public static final int REQUEST_CODE_STOCK_TAKING = 16001;
    public static final int REQUEST_CODE_PRINT = 17001;
    public static final int REQUEST_CODE_MSWIPE_LOGIN = 18001;
    public static final int REQUEST_CODE_SET_TIME_SLOTS = 19001;
    public static final int REQUEST_CODE_STORE_DETAILS = 20001;
    public static final int REQUEST_CODE_CHANGE_DELIVERY_SLOT = 20002;
    public static final int REQUEST_CODE_CHANGE_LANGUAGE = 20003;


    public static final int REQUEST_CODE_CATEGORY_OPERATIONS = 10002;


    public static final String EXTRA_TOKEN = "extra_token";
    public static final String EXTRA_VENDER_ID = "extra_vender_id";
    public static final String EXTRA_STORE_ID = "extra_store_id";

    public static final String EXTRA_OTP = "extra_otp";
    public static final String EXTRA_MOBILE = "extra_mobile";
    public static final String EXTRA_FORGOT = "extra_forgot";
    public static final String EXTRA_ACTIVITY_MODE = "extra_activity_mode";
    public static final String EXTRA_ISUPDATED = "extra_isupdated";
    public static final String EXTRA_ITEM = "extra_item";
    public static final String EXTRA_ITEM_ID = "extra_item_id";
    public static final String EXTRA_BRAND = "extra_brand";
    public static final String EXTRA_BRAND_ID = "extra_brand_id";
    public static final String EXTRA_SUPPLIER = "extra_supplier";
    public static final String EXTRA_SUPPLIER_PHONE = "extra_supplier_phone";
    public static final String EXTRA_EMPLOYEE = "extra_employee";
    public static final String EXTRA_EMPLOYEE_ACCESS = "extra_employee_access";
    public static final String EXTRA_CUSTOMER = "extra_customer";
    public static final String EXTRA_CUSTOMER_NUMBER = "extra_customer_number";
    public static final String EXTRA_PRODUCT_ITEM = "extra_product_item";
    public static final String EXTRA_ISNOTIFIED = "extra_isnotified";
    public static final String EXTRA_CART = "extra_cart";
    public static final String EXTRA_CART_LIST = "extra_cart_list";
    public static final String EXTRA_CART_ITEM = "extra_cart_item";
    public static final String EXTRA_IMAGE_URLS_ARRAY = "extra_image_urls_array";
    public static final String EXTRA_IMAGE_NAME = "extra_image_name";
    public static final String EXTRA_INVOICE = "extra_invoice";
    public static final String EXTRA_INVOICE_RETURN = "extra_invoice_return";
    public static final String EXTRA_ORDER_ID = "extra_order_id";
    public static final String EXTRA_ORDER_AMOUNT = "extra_order_amount";
    public static final String EXTRA_ORDER_INVOCIE = "extra_order_invoice";
    public static final String EXTRA_ISUPLOADING_IMAGE = "extra_isuploading_image";
    public static final String EXTRA_PROFILE = "extra_profile";
    public static final String EXTRA_VOUCHER = "extra_voucher";
    public static final String EXTRA_MULTI_STORE_ADDED = "extra_multi_store_added";
    public static final String EXTRA_BARCODES = "extra_barcodes";
    public static final String EXTRA_LABELS = "extra_labels";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_SCAN = "extra_scan";
    public static final String EXTRA_CURRENT_STATUS_CODE = "extra_curr_order_status";
    public static final String EXTRA_CURRENT_STATUS_CODE_INT_STRING = "extra_curr_order_status_int_str";

    public static final String EXTRA_STORE_CATEGORY = "extra_store_category";
    public static final String EXTRA_STORE_CATEGORY_ID = "extra_store_category_id";
    public static final String EXTRA_PARENT_CATEGORY = "extra_parent_category";
    public static final String EXTRA_PARENT_CATEGORY_ID = "extra_parent_category_id";
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_SUB_CATEGORY = "extra_sub_category";
    public static final String EXTRA_SUB_CATEGORY_ID = "extra_sub_category_id";
    public static final String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static final String EXTRA_SKU_ID = "extra_sku_id";
    public static final String EXTRA_ATTRIBUTE1 = "extra_attribute1";
    public static final String EXTRA_ATTRIBUTE2 = "extra_attribute2";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_PRODUCT_COUNT = "extra_product_count";
    public static final String EXTRA_SELLING_PRICE = "extra_selling_price";
    public static final String EXTRA_PURCHASE_PRICE = "extra_purchase_price";
    public static final String EXTRA_SIZES = "extra_sizes";
    public static final String EXTRA_QTY = "extra_qty";
    public static final String EXTRA_BULK = "extra_bulk";
    public static final String EXTRA_STOCK_TRANSFER = "extra_stock_transfer";

    public static final String EXTRA_GST1 = "extra_gst1";
    public static final String EXTRA_GST2 = "extra_gst2";


    public static final String EXTRA_TO_SCREEN = "extra_to_screen";
    public static final String EXTRA_SCREEN_STORE_CATEGORY = "extra_screen_store_category";
    public static final String EXTRA_SCREEN_PARENT_CATEGORY = "extra_screen_parent_category";
    public static final String EXTRA_SCREEN_CATEGORY = "extra_screen_category";
    public static final String EXTRA_SCREEN_SUBCATEGORY = "extra_screen_subcategory";
    public static final String EXTRA_SCREEN_BRAND = "extra_screen_brand";
    public static final String EXTRA_SCREEN_SUPPLIER = "extra_screen_supplier";
    public static final String EXTRA_SCREEN_PRICE = "extra_screen_price";

    public static final String EXTRA_TIMESTAMP = "extra_timestamp";
    public static final String EXTRA_REPORT = "extra_report";
    public static final String EXTRA_REPORT_TYPE = "extra_report_type";

    public static final String EXTRA_FROM_ACTIVITY = "extra_from_activity";
    public static final String EXTRA_MSWIPE = "extra_mswipe";

    public static final String EXTRA_STOCK_TAKING_RESUME = "extra_stock_taking_resume";
    public static final String EXTRA_RESUME_STOCK_TAKING_CHECK = "extra_resume_stock_taking_check";
    public static final String EXTRA_TOTAL_QUANTITY = "extra_total_quantity";
    public static final String EXTRA_TOTAL_PRICE = "extra_total_price";
    public static final String EXTRA_IS_PENDING_CUSTOMER_REQUEST = "extra_is_pending_customer_request";

    public static final String EXTRA_HELP_INDEX = "extra_help_index";
    public static final int EXTRA_HELP_HELP = 0;
    public static final int EXTRA_HELP_REGISTRATION = 1;
    public static final int EXTRA_HELP_SETTINGS = 2;
    public static final int EXTRA_HELP_STORE_PROFILE = 3;
    public static final int EXTRA_HELP_DASHBOARD = 4;
    public static final int EXTRA_HELP_BILLING = 5;
    public static final int EXTRA_HELP_STOCK = 6;
    public static final int EXTRA_HELP_STOCK_TRANSFER = 7;
    public static final int EXTRA_HELP_VOUCHERS = 8;
    public static final int EXTRA_HELP_EMPLOYEES = 9;
    public static final int EXTRA_HELP_CUSTOMER = 10;
    public static final int EXTRA_HELP_SUPPLIER = 11;
    public static final int EXTRA_HELP_BRAND = 12;
    public static final int EXTRA_HELP_STOCK_TAKING = 13;

    public static final String OPERATION_ADD = "1";
    public static final String OPERATION_EDIT = "2";
    public static final String OPERATION_DELETE = "3";
    public static final String EXTRA_SELECTED_SIZE_POS = "selected_size_pos";
    public static final String EXTRA_STOCK_TYPE = "type";

    public static final String EXTRA_STOCK_EDIT = "editStock";
    public static final String EXTRA_STOCK_EDIT_FLAG = "editStockFlag";
    public static final String EXTRA_STOCK_EDIT_IMAGES = "stockImages";

    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    public static final String EXTRA_SUB_CATEGORY_NAME = "extra_sub_category_name";
    public static final String EXTRA_KEY_OPEN_ON_HOME = "extra_open_on_home";
    public static final String EXTRA_KEY_PUSH_NOTIFICATION = "pushnotification";
    public static final String EXTRA_KEY_TIME_SLOT = "time_slot_delivery_pickup";
    public static final String EXTRA_KEY_DELIVERY_TYPE = "extra_delivery_type";


    public static final long DIALOG_DISPLAY_TIME = 2000;

    public static final String EXTRA_IS_FROM_NEW_CAT_SUBCAT = "is from new cat subcat";

    public static boolean switchToOldGallery = false;

    public static String getCommaFormatedString(float amount) {
        DecimalFormat formatter = new DecimalFormat("##,##,##,##,###");
        return formatter.format(amount);
    }

    public static String getCommaFormatedStringWithDecimal(float amount) {
        DecimalFormat formatter = new DecimalFormat("##,##,##,##,##0.00");
        return formatter.format(amount);
    }

    public static String getDecimalFormatedString(double value) {
        String formatedValue = value + "";
        String decimal = formatedValue.substring(formatedValue.indexOf(".") + 1, formatedValue.length());
        if (decimal.length() > 3) {
            decimal = decimal.substring(0, 2);
        }
        if (ParseUtils.isValidString(decimal)) {
            formatedValue = value + "";
        } else if (Double.parseDouble(decimal) < 0) {
            formatedValue = value + "";
        } else {
            formatedValue = String.format(Locale.ENGLISH, "%.2f", value);
        }
        return formatedValue;
    }

    public static double getDoubleFromString(String amount) throws NumberFormatException {
        String replace = amount.replace(",", "").replace(" ", "");
        try {
            return Double.parseDouble(replace);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getAmountTill2Digits(double d) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        return df2.format(d);
    }


    public static String getTimeIn12hrFormat(String hrDotmin) {
        int hrIn24Format = 0;
        int min = 0;
        if (hrDotmin.contains(".")){
            int i = hrDotmin.indexOf(".");
            hrIn24Format = Integer.parseInt(hrDotmin.substring(0,i));
            min = Integer.parseInt(hrDotmin.substring(i+1));
        }
        else {
            try{
                hrIn24Format = Integer.parseInt(hrDotmin);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return getTimeIn12hrFormat(hrIn24Format, min);
    }

    public static String getTimeIn12hrFormat(int hourIn24Format, int min) {
        final String time = hourIn24Format + ":" + min;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.getDefault());
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("K:mm a", Locale.getDefault()).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
