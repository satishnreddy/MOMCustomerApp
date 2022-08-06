package com.mom.momcustomerapp.controllers.sales.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/*
 * Created by Shailesh on 9/04/20.
 */
public class BillingListModelNewOuter implements Parcelable
{

    @SerializedName("total_records")
    @Expose
    private int total_records;

    @SerializedName("current_page")
    @Expose
    private int current_page;

    private List<BillingListModelNew> data = null;
    public final static Parcelable.Creator<BillingListModelNewOuter> CREATOR = new Creator<BillingListModelNewOuter>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BillingListModelNewOuter createFromParcel(Parcel in) {
            return new BillingListModelNewOuter(in);
        }

        public BillingListModelNewOuter[] newArray(int size) {
            return (new BillingListModelNewOuter[size]);
        }

    }
            ;

    protected BillingListModelNewOuter(Parcel in) {
        this.total_records = ((int) in.readValue((String.class.getClassLoader())));
        this.current_page = ((int) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (BillingListModelNew.class.getClassLoader()));
    }

    public BillingListModelNewOuter() {
    }

    public int getTotalRecords() {
        return total_records;
    }

    public void setTotalRecords(int totalRecords) {
        this.total_records = totalRecords;
    }

    public int getCurrentPage() {
        return current_page;
    }

    public void setCurrentPage(int currentPage) {
        this.current_page = currentPage;
    }

    public List<BillingListModelNew> getData() {
        return data;
    }

    public void setData(List<BillingListModelNew> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(total_records);
        dest.writeValue(current_page);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}