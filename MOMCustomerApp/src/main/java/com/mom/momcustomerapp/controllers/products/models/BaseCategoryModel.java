package com.mom.momcustomerapp.controllers.products.models;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Nishant on 05-06-2016.
 */
public class BaseCategoryModel implements Parcelable {
    public static final Creator<BaseCategoryModel> CREATOR = new Creator<BaseCategoryModel>() {
        @Override
        public BaseCategoryModel createFromParcel(Parcel in) {
            return new BaseCategoryModel(in);
        }

        @Override
        public BaseCategoryModel[] newArray(int size) {
            return new BaseCategoryModel[size];
        }
    };

    /**
     * id : 38
     * value : 123
     */

    private String id;
    private String value;

    public BaseCategoryModel() {
    }

    protected BaseCategoryModel(Parcel in) {
        this.id = in.readString();
        this.value = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.value);
    }
}
