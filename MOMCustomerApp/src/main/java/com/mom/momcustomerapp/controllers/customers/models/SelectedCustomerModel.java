package com.mom.momcustomerapp.controllers.customers.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by nishant on 08/06/18.
 */
public class SelectedCustomerModel implements Parcelable {


    public static final Parcelable.Creator<SelectedCustomerModel> CREATOR = new Parcelable.Creator<SelectedCustomerModel>() {
        @Override
        public SelectedCustomerModel createFromParcel(Parcel source) {
            return new SelectedCustomerModel(source);
        }

        @Override
        public SelectedCustomerModel[] newArray(int size) {
            return new SelectedCustomerModel[size];
        }
    };
    private List<String> customer_ids;

    public SelectedCustomerModel() {
    }

    public SelectedCustomerModel(List<String> customer_ids) {
        this.customer_ids = customer_ids;
    }

    protected SelectedCustomerModel(Parcel in) {
        this.customer_ids = in.createStringArrayList();
    }

    public List<String> getCustomerIds() {
        return customer_ids;
    }

    public void setCustomerIds(List<String> customer_ids) {
        this.customer_ids = customer_ids;
    }

    public void addCustomerId(String id) {
        if (this.customer_ids == null) {
            this.customer_ids = new ArrayList<>();
        }

        this.customer_ids.add(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.customer_ids);
    }
}
