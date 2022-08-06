package com.mom.momcustomerapp.controllers.customers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author shailesh.lobo at 06-06-2020 18:52
 * @since -
 */
public class CustRequesterDetailModel {

    @SerializedName("row_id")
    @Expose
    private String rowId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address_1")
    @Expose
    private String address1;
    @SerializedName("address_2")
    @Expose
    private String address2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("land_mark")
    @Expose
    private String landMark;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("status")
    @Expose
    private String status;


    public CustRequesterDetailModel(String rowId, String name, String lastName, String phoneNumber,
                                    String email, String address1, String address2,
                                    String city, String state, String zip,
                                    String country, String status) {
        this.rowId = rowId;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.status = status;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRowId() {
        return rowId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
    }

    public String getDeleted() {
        return deleted;
    }

    public String getLandMark() {
        return landMark;
    }

    public String getState() {
        return state;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getZip() {
        return zip;
    }

    public String getStatus() {
        return status;
    }


    public String getFullAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addThisStr(address1,", "));
        stringBuilder.append(addThisStr(address2,", "));
        stringBuilder.append(addThisStr(city,""));
        return stringBuilder.toString();
    }

    private String addThisStr(String addressField, String postPend) {
        if (addressField == null || addressField.isEmpty()){
            return "";
        }
        return addressField + "" + postPend;
    }

}
