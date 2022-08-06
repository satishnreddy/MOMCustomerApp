package com.mom.momcustomerapp.controllers.sales.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.DrawableRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mom.momcustomerapp.R;

import java.util.HashMap;
import java.util.Map;
/*
 * Created by Shailesh on 9/04/20.
 */

public class BillingListModelNew implements Parcelable
{


    @SerializedName("sale_id")
    @Expose
    private String saleId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("invoice_number")
    @Expose
    private String invoiceNumber;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("delivered_date")
    @Expose
    private String deliveredDate;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;

    @SerializedName("order_status")
    @Expose
    private String order_status;

    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("max_price")
    @Expose
    private String maxPrice;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("total_quantity")
    @Expose
    private String totalQuantity;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("sale_time")
    @Expose
    private String saleTime;



    private Map<String, Object> additionalProperties = new HashMap<>();
    public final static Parcelable.Creator<BillingListModelNew> CREATOR = new Creator<BillingListModelNew>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BillingListModelNew createFromParcel(Parcel in) {
            return new BillingListModelNew(in);
        }

        public BillingListModelNew[] newArray(int size) {
            return (new BillingListModelNew[size]);
        }

    }
            ;

    protected BillingListModelNew(Parcel in) {
        this.saleId = ((String) in.readValue((String.class.getClassLoader())));
        this.customerId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.invoiceNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryDate = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveredDate = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.totalPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.maxPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.customerName = ((String) in.readValue((String.class.getClassLoader())));
        this.customerPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.totalQuantity = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentType = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((String) in.readValue((String.class.getClassLoader())));
        this.saleTime = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object> ) in.readValue((Map.class.getClassLoader())));
        this.order_status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BillingListModelNew() {
    }

    public String getSaleId() {
        return saleId;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getOrder_statusInIntString() {
        return order_status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(saleId);
        dest.writeValue(customerId);
        dest.writeValue(employeeId);
        dest.writeValue(invoiceNumber);
        dest.writeValue(deliveryDate);
        dest.writeValue(deliveredDate);
        dest.writeValue(deliveryStatus);
        dest.writeValue(totalPrice);
        dest.writeValue(maxPrice);
        dest.writeValue(customerName);
        dest.writeValue(customerPhone);
        dest.writeValue(totalQuantity);
        dest.writeValue(paymentType);
        dest.writeValue(count);
        dest.writeValue(saleTime);
        dest.writeValue(additionalProperties);
        dest.writeValue(order_status);
    }

    public int describeContents() {
        return 0;
    }

    @ORDER_STATUS
    public String getOrderStatus(){
        switch (this.getDeliveryStatus().toLowerCase()){
            case "pending":
                return ORDER_STATUS.ORDER_STATUS_PENDING;
            case "accepted":
                return ORDER_STATUS.ORDER_STATUS_ACCEPTED;
            case "delivered":
                return ORDER_STATUS.ORDER_STATUS_DELIVERED;
            case "returned":
                return ORDER_STATUS.ORDER_STATUS_RETURNED;
        }
        return ORDER_STATUS.ORDER_STATUS_NA;
    }

    @DrawableRes
    public int getImageStatus() {
        switch (getOrderStatus()){
            case ORDER_STATUS.ORDER_STATUS_PENDING:
                return R.drawable.ic_order_pending;
            case ORDER_STATUS.ORDER_STATUS_ACCEPTED:
                return R.drawable.ic_order_accepted;
            case ORDER_STATUS.ORDER_STATUS_DELIVERED:
                return R.drawable.ic_order_delivered;
            case ORDER_STATUS.ORDER_STATUS_RETURNED:
                return R.drawable.ic_order_returned;
        }
        return R.drawable.ic_order_pending;
    }
}