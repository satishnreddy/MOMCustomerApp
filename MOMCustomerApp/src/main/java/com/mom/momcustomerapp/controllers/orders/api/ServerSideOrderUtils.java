package com.mom.momcustomerapp.controllers.orders.api;

import static com.mom.momcustomerapp.utils.AppUtils.PAYMENT_TYPE_CASH;
import static com.mom.momcustomerapp.utils.AppUtils.PAYMENT_TYPE_MIX;
import static com.mom.momcustomerapp.utils.AppUtils.PAYMENT_TYPE_MSWIPE;
import static com.mom.momcustomerapp.utils.AppUtils.PAYMENT_TYPE_UPI;
import static com.mom.momcustomerapp.utils.AppUtils.PAYMENT_TYPE_WALLET;

import androidx.annotation.NonNull;

import com.mom.momcustomerapp.controllers.products.models.ItemDetails;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.controllers.sales.models.OrderDetailItemModel;
import com.mom.momcustomerapp.controllers.sales.models.OrderDetailModel;
import com.mom.momcustomerapp.controllers.sales.models.OrderPaymentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 10-04-2020 16:05
 * @modified by -
 * @updated on -
 * @since -
 */
public class ServerSideOrderUtils {

    public static int getIntEquivalentOfOrder(@ORDER_STATUS String status){
        switch (status){
            case ORDER_STATUS.ORDER_STATUS_PENDING: return 0;
            case ORDER_STATUS.ORDER_STATUS_ACCEPTED: return 1;
            case ORDER_STATUS.ORDER_STATUS_DELIVERED: return 2;
            case ORDER_STATUS.ORDER_STATUS_RETURNED: return 3;
//            case ORDER_STATUS.ORDER_STATUS_PARTIAL_COMPLETED: return 4;
            case ORDER_STATUS.ORDER_STATUS_NA: return -1;
        }
        return -1;
    }

    public static List<ItemDetails> getRemovedItems(List<ItemDetails> completeList,
                                                    List<ItemDetails> newList)
    {

        List<ItemDetails> removedList = new ArrayList<>();

        for (int i = 0; i < completeList.size(); i++)
        {
            ItemDetails invoiceItemsModel = completeList.get(i);
            boolean isItemFound = false;
            for (int j = 0; j < newList.size(); j++)
            {
                if (invoiceItemsModel.item_id.equals(newList.get(j).item_id))
                {
                    isItemFound = true;
                    double countPresent = stringToDouble(newList.get(j).quantity);
                    double countOriginal = stringToDouble(invoiceItemsModel.quantity);

                    if (countOriginal > countPresent)
                    {
                        double diff = countOriginal -countPresent;
                        try
                        {
                            ItemDetails clone = invoiceItemsModel.clone();
                            clone.setOriginalQuantity(countOriginal);
                            clone.setQuantity(String.valueOf(diff));
                            removedList.add(clone);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            if (!isItemFound)
            {
                try
                {
                    ItemDetails clone = invoiceItemsModel.clone();
                    clone.setOriginalQuantity(completeList.get(i).getQuantityInDouble());
                    removedList.add(clone);
                }
                catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        return removedList;
    }

    public static double stringToDouble(String value){
        double parseDouble = Double.parseDouble(value);
        return parseDouble;
    }

    @NonNull
    public static JSONObject generateReturnJsonObject(List<OrderDetailItemModel> removedItemList,
                                                      OrderDetailModel invoiceModel, String paymentType, boolean isNotPaid) {
        /*
        *
        *
        * {
          "item": [
            {
              "item_id": "1185260",
              "price": 5000,
              "quantity": "2",
              "quantity_id": "1483890",
              "size": "Free"
            }
          ],
          "sale_id": "919312",
          "settlement_type": "Cash"
        }
        *
        * {"item":[{"item_id":"1204442","price":"30.00","quantity":"1.0",
        * "quantity_id":"1508802","size":"Std"}],"sale_id":"24","settlement_type":"Wallet","wallet_amt":"30.0"}
        * */
        double rateOfProduct;
        try{
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (OrderDetailItemModel invoiceItemsModel : removedItemList) {
                JSONObject voucherObj = new JSONObject();
                voucherObj.put("item_id", invoiceItemsModel.getItemId());
                rateOfProduct = invoiceItemsModel.getPriceInDouble() / invoiceItemsModel.getOriginalQuantity();
                voucherObj.put("price", rateOfProduct);
                voucherObj.put("quantity", invoiceItemsModel.getQuantity());
                voucherObj.put("quantity_id", invoiceItemsModel.getQuantityId());
                voucherObj.put("size", invoiceItemsModel.getSize());
                jsonArray.put(voucherObj);
            }
            jsonObject.put("item", jsonArray);
            jsonObject.put("sale_id", invoiceModel.getOrderID());
            jsonObject.put("settlement_type",((paymentType.equalsIgnoreCase("cash") ? paymentType: "Wallet")));
            jsonObject.put("customer_id", invoiceModel.getCustomerId());

            double orderTotalAmount = Double.parseDouble(invoiceModel.getOrderTotal());
            switch (invoiceModel.getPaymentType()){
                case PAYMENT_TYPE_CASH:
                case PAYMENT_TYPE_MSWIPE:
                case PAYMENT_TYPE_UPI:
                    jsonObject.put("payment_type", invoiceModel.getPaymentType());
                    jsonObject.put("payment_amt", orderTotalAmount);
                    jsonObject.put("wallet_amt", 0);
                    break;

                case PAYMENT_TYPE_WALLET:
                    jsonObject.put("payment_type", invoiceModel.getPaymentType());
                    jsonObject.put("payment_amt", 0);
                    jsonObject.put("wallet_amt", orderTotalAmount);
                    break;

                case PAYMENT_TYPE_MIX:
                    double paymentAmount = 0, walletAmount = 0;
                    List<OrderPaymentModel> mix_payment = invoiceModel.getPayment();
                    String paymentTypeForMixPayment = "";

                    for (int i = 0; i < mix_payment.size(); i++) {
                        OrderPaymentModel mixPaymentModel = mix_payment.get(i);
                        if (!mixPaymentModel.getPaymentType().equalsIgnoreCase("Wallet")){
                            paymentTypeForMixPayment =  mixPaymentModel.getPaymentType();
                            paymentAmount = Double.parseDouble(mixPaymentModel.getPaymentAmount());
                        }
                        else {
                            walletAmount = Double.parseDouble(mixPaymentModel.getPaymentAmount());
                        }
                    }

                    jsonObject.put("payment_type", paymentTypeForMixPayment);
                    jsonObject.put("payment_amt", paymentAmount);
                    jsonObject.put("wallet_amt", walletAmount);
                    break;
            }
            jsonObject.put("order_tot_amt", orderTotalAmount);
            jsonObject.put("payment_status", isNotPaid ? 0 : 1);

            /*double totalAmount = 0;
            for (int i = 0; i < removedItemList.size(); i++) {
                double singleAmount;
                try {
                    String s = removedItemList.get(i).getPrice();
                    singleAmount = Double.parseDouble(s);
                    totalAmount = totalAmount + singleAmount;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }*/
            /*jsonObject.put("wallet_amt" , String.valueOf(totAmt));*/


            return jsonObject;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
