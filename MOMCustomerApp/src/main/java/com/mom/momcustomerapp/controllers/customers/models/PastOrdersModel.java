package com.mom.momcustomerapp.controllers.customers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import com.mom.momcustomerapp.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;

/**
 * @author shailesh.lobo
 * @created - 24-04-2020 12:27
 */



public class PastOrdersModel implements Parcelable {

    public static final Creator<PastOrdersModel> CREATOR = new Creator<PastOrdersModel>() {
        @Override
        public PastOrdersModel createFromParcel(Parcel in) {
            return new PastOrdersModel(in);
        }

        @Override
        public PastOrdersModel[] newArray(int size) {
            return new PastOrdersModel[size];
        }
    };

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
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

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
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

    @SerializedName("sale_id")
    @Expose
    public String saleId;
    @SerializedName("sale_time")
    @Expose
    public String saleTime;
    @SerializedName("customer_id")
    @Expose
    public String customerId;
    @SerializedName("employee_id")
    @Expose
    public String employeeId;
    @SerializedName("invoice_number")
    @Expose
    public String invoiceNumber;
    @SerializedName("delivery_date")
    @Expose
    public String deliveryDate;

    @SerializedName("delivered_date")
    @Expose
    public String deliveredDate;
    @SerializedName("delivery_status")
    @Expose
    public String deliveryStatus;
    @SerializedName("total_price")
    @Expose
    public String totalPrice;
    @SerializedName("max_price")
    @Expose
    public String maxPrice;
    @SerializedName("customerName")
    @Expose
    public String customerName;
    @SerializedName("customerPhone")
    @Expose
    public String customerPhone;
    @SerializedName("total_quantity")
    @Expose
    public String totalQuantity;
    @SerializedName("payment_type")
    @Expose
    public String paymentType;
    @SerializedName("count")
    @Expose
    public String count;
    @SerializedName("order_status")
    @Expose
    private String order_status;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getSaleTime() {
        return saleTime;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
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
        dest.writeValue(order_status);

    }



    protected PastOrdersModel(Parcel in) {
        saleId = in.readString();
        saleTime = in.readString();
        customerId = in.readString();
        employeeId = in.readString();
        invoiceNumber = in.readString();
        deliveryDate = in.readString();
        deliveredDate = in.readString();
        deliveryStatus = in.readString();
        totalPrice = in.readString();
        maxPrice = in.readString();
        customerName = in.readString();
        customerPhone = in.readString();
        totalQuantity = in.readString();
        paymentType = in.readString();
        count = in.readString();
        order_status = in.readString();
    }

}
