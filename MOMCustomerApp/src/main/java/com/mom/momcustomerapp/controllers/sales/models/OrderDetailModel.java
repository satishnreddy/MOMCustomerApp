package com.mom.momcustomerapp.controllers.sales.models;

/**
 * @author shailesh.lobo on 03-06-2020 23:31
 *
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailModel {

    @SerializedName("orderID")
    @Expose
    public String orderID;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("customer_id")
    @Expose
    public String customerId;

    @SerializedName("employee_id")
    @Expose
    public String employeeId;

    @SerializedName("Invoice")
    @Expose
    public String invoice;

    @SerializedName("delivery_date")
    @Expose
    public String deliveryDate;

    @SerializedName("delivered_date")
    @Expose
    public Object deliveredDate;

    @SerializedName("delivery_slot")
    @Expose
    public String deliverySlot;

    @SerializedName("delivery_status")
    @Expose
    public String deliveryStatus;

    @SerializedName("payment_type")
    @Expose
    public String paymentType;
    @SerializedName("order_total")
    @Expose
    public String orderTotal;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("Items")
    @Expose
    public List<OrderDetailItemModel> items = null;
    @SerializedName("Payment")
    @Expose
    public List<OrderPaymentModel> payment = null;


    @SerializedName("address_1")
    private String address_1;

    @SerializedName("address_2")
    private String address_2;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip")
    private String zip;

    @SerializedName("country")
    private String country;

    @SerializedName("land_mark")
    private String land_mark;

    @SerializedName("txn_id")
    private String txn_id;


    @SerializedName("permit_image")
    @Expose
    public String permitImage;

    @SerializedName("invoice_filename")
    @Expose
    public String invoiceFilename;

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public Object getDeliveredDate() {
        return deliveredDate;
    }

    public String getDeliverySlot() {
        return deliverySlot;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDate() {
        return date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getOrderID() {
        return orderID;
    }

    public List<OrderDetailItemModel> getItems() {
        return items;
    }

    public List<OrderPaymentModel> getPayment() {
        return payment;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    @ORDER_STATUS
    public String getOrderStatus(){
        return deliveryStatus;
        /*if (this.deliveryStatus == null || deliveryStatus.equals("0")) return ORDER_STATUS.ORDER_STATUS_PENDING;
        else if (deliveryStatus.equalsIgnoreCase("1")) return ORDER_STATUS.ORDER_STATUS_ACCEPTED;
        else if (deliveryStatus.equalsIgnoreCase( "2")) return ORDER_STATUS.ORDER_STATUS_DELIVERED;
        else if (deliveryStatus.equalsIgnoreCase( "3")) return ORDER_STATUS.ORDER_STATUS_RETURNED;
        else return ORDER_STATUS.ORDER_STATUS_NA;*/
    }

    public String getDelivery_address() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addThisStr(address_1,", "));
        stringBuilder.append(addThisStr(address_2,", "));
        stringBuilder.append(addThisStr(city,", "));
        stringBuilder.append(addThisStr(state," "));
        stringBuilder.append(addThisStr(zip,", "));
        stringBuilder.append(addThisStr(country,""));
        return stringBuilder.toString();
    }

    private String addThisStr(String addressField, String postPend) {
        if (addressField == null || addressField.isEmpty()){
            return "";
        }
        return addressField + "" + postPend;
    }

    public String getLand_mark() {
        return land_mark;
    }

    public String getTxn_id(){
        return txn_id;
    }

    public String getPermitImage() {
        return permitImage;
    }

    public String getInvoiceFilename() {
        return invoiceFilename;
    }

    public void setInvoiceFilename(String invoiceFilename) {
        this.invoiceFilename = invoiceFilename;
    }
}
