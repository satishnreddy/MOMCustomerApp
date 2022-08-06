package com.mom.momcustomerapp.controllers.customers.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/*
 * Created by nishant on 11/07/16.
 */
public class CustomersListModel implements Parcelable {

    public static final Creator<CustomersListModel> CREATOR = new Creator<CustomersListModel>() {
        @Override
        public CustomersListModel createFromParcel(Parcel source) {
            return new CustomersListModel(source);
        }

        @Override
        public CustomersListModel[] newArray(int size) {
            return new CustomersListModel[size];
        }
    };

    private List<CustomerModel> data;
    private int total_records;
    private int current_page;

    public CustomersListModel() {

    }

    public CustomersListModel(List<CustomerModel> data, int total_records, int current_page) {
        this.data = data;
        this.total_records = total_records;
        this.current_page = current_page;
    }

    protected CustomersListModel(Parcel in) {
        this.data = in.createTypedArrayList(CustomerModel.CREATOR);
        this.total_records = in.readInt();
        this.current_page = in.readInt();
    }

    public int getTotal_records() {
        return total_records;
    }

    public void setTotal_records(int total_records) {
        this.total_records = total_records;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<CustomerModel> getData() {
        return data;
    }

    public void setData(List<CustomerModel> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
        dest.writeInt(this.total_records);
        dest.writeInt(this.current_page);
    }
}
