package com.mom.momcustomerapp.utils;

import android.content.Intent;


import com.mom.momcustomerapp.controllers.orders.models.SalesDetailsResp;
import com.mom.momcustomerapp.controllers.sales.models.OrderDetailModel;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.views.login.SplachScreenActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @since -
 */
public class AppUtils {

    public static final String PAYMENT_TYPE_CASH = "Cash";
    public static final String PAYMENT_TYPE_UPI = "UPI";
    public static final String PAYMENT_TYPE_MSWIPE = "mswipe";
    public static final String PAYMENT_TYPE_WALLET = "Wallet";
    public static final String PAYMENT_TYPE_MIX = "Mix";

    public static final String JSON_ADD_PRODUCT = "{\n" +
            "    \"mcc_code\": \"5411\",\n" +
            "    \"category_id\": \"511\",\n" +
            "    \"category\": \"Personal care\",\n" +
            "    \"subcategory_id\": \"4754\",\n" +
            "    \"subcategory\": \"0\",\n" +
            "    \"name\": \"NIKE N150 GRN STORM EDT DEO 200 ml\",\n" +
            "    \"size\": \"Std\",\n" +
            "    \"store_category_id\": null,\n" +
            "    \"parent_category_id\": \"126\",\n" +
            "    \"add_to_store\": \"1\",\n" +
            "    \"item_id\": null,\n" +
            "    \"count\": \"7\",\n" +
            "    \"supplier_id\": \"\",\n" +
            "    \"supplier_name\": \"\",\n" +
            "    \"brand_id\": \"\",\n" +
            "    \"brand_name\": \"\",\n" +
            "    \"description\": \"\",\n" +
            "    \"sku_id\": \"\",\n" +
            "    \"Store_type\": \"0\",\n" +
            "    \"gst_variable\": 1,\n" +
            "    \"discount_variable\": 1,\n" +
            "    \"price_variable\": 1,\n" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"size\": \"Std\",\n" +
            "            \"quantity\": \"1\",\n" +
            "            \"gst_variable\": 1,\n" +
            "            \"gst_percentage\": \"0\",\n" +
            "            \"discount_variable\": 1,\n" +
            "            \"discount_percentage\": \"0\",\n" +
            "            \"selling_price\": \"100.50\",\n" +
            "            \"mrp\": \"1\",\n" +
            "            \"purchase_price\": \"1\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"attributes\": [\n" +
            "        {\n" +
            "            \"attribute_id\": \"1\",\n" +
            "            \"attribute_name\": \"Attribute1\",\n" +
            "            \"attribute_value\": \"\",\n" +
            "            \"item_attr_billinfo_1\": 0\n" +
            "        },\n" +
            "        {\n" +
            "            \"attribute_id\": \"2\",\n" +
            "            \"attribute_name\": \"Attribute2\",\n" +
            "            \"attribute_value\": \"\",\n" +
            "            \"item_attr_billinfo_2\": 0\n" +
            "        },\n" +
            "        {\n" +
            "            \"attribute_id\": \"3\",\n" +
            "            \"attribute_name\": \"Attribute3\",\n" +
            "            \"attribute_value\": \"\",\n" +
            "            \"item_attr_billinfo_3\": 0\n" +
            "        },\n" +
            "        {\n" +
            "            \"attribute_id\": \"4\",\n" +
            "            \"attribute_name\": \"Attribute4\",\n" +
            "            \"attribute_value\": \"\",\n" +
            "            \"item_attr_billinfo_4\": 0\n" +
            "        },\n" +
            "        {\n" +
            "            \"attribute_id\": \"5\",\n" +
            "            \"attribute_name\": \"Attribute5\",\n" +
            "            \"attribute_value\": \"\",\n" +
            "            \"item_attr_billinfo_5\": 0\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static boolean isValidMinPrice(Double doubleText) {
        return doubleText > 0;
    }

    public static void logOutUserForUpdate(/*MventryApp activity*/) {
        MOMApplication context = MOMApplication.getInstance();
        //MventryApp.getInstance().setServerUrl("");
        MOMApplication.getSharedPref().setLoggedOut();
        Intent intent = new Intent(context, SplachScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        /*activity.finish();*/


    }

    public static String getStringOfDays(String weekDays) {

        ArrayList<String> stringArrayList = new ArrayList<>();
        if (weekDays.charAt(0) == '1') stringArrayList.add("Sun");
        if (weekDays.charAt(1) == '1') stringArrayList.add("Mon");
        if (weekDays.charAt(2) == '1') stringArrayList.add("Tue");
        if (weekDays.charAt(3) == '1') stringArrayList.add("Wed");
        if (weekDays.charAt(4) == '1') stringArrayList.add("Thu");
        if (weekDays.charAt(5) == '1') stringArrayList.add("Fri");
        if (weekDays.charAt(6) == '1') stringArrayList.add("Sat");

        StringBuilder strToReturn = new StringBuilder();
        for (int i = 0; i < stringArrayList.size(); i++) {
            strToReturn.append(stringArrayList.get(i)).append((stringArrayList.size() - 1 != i) ? ", " : "");
        }
        return strToReturn.toString();
    }

    public static boolean isPaymentTypeMswipe(SalesDetailsResp mInvoiceModel){
        return mInvoiceModel.salesCustOrder.payment_type != null &&
                mInvoiceModel.salesCustOrder.payment_type.equalsIgnoreCase(PAYMENT_TYPE_MSWIPE);
    }

    public static boolean isPaymentTypeUPI(SalesDetailsResp mInvoiceModel){
        return mInvoiceModel.salesCustOrder.payment_type != null &&
                mInvoiceModel.salesCustOrder.payment_type.equalsIgnoreCase(PAYMENT_TYPE_UPI);
    }

    public static boolean isUPIPaymentDone(SalesDetailsResp mInvoiceModel){

        if( mInvoiceModel.getPayment().size() == 0) {
            return false;
        }

        if( mInvoiceModel.getPayment().get(0).payment_status ==  null) {
            return false;
        }

        boolean isPaid = mInvoiceModel.getPayment().get(0).payment_status.equalsIgnoreCase("1");

        return isPaid;
    }

    public static String  getPendingRequestDispValue(String status) {
        switch (status){
            case "0":
            case "1":
                return "Pending";
            case "2":
                return "Declined";
                default:
                    break;
        }
        return "-";
    }


}
