package com.mom.momcustomerapp.controllers.sales.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 03-06-2020 23:32
 * @modified by -
 * @updated on -
 * @since -
 */
public class OrderPaymentModel {

    @SerializedName("payment_type")
    @Expose
    public String paymentType;

    @SerializedName("payment_amount")
    @Expose
    public String paymentAmount;

    @SerializedName("txn_id")
    @Expose
    public String txnId;

    @SerializedName("payment_status")
    @Expose
    public String paymentStatus;

    public String getPaymentType() {
        return paymentType;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

}
