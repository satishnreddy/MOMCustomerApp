package com.mom.momcustomerapp.controllers.products.models;

import android.os.Parcel;


/*
 * Created by Nishant on 05-06-2016.
 */
public class SubCategoryModel extends BaseCategoryModel {

    public static final Creator<SubCategoryModel> CREATOR = new Creator<SubCategoryModel>() {
        @Override
        public SubCategoryModel createFromParcel(Parcel source) {
            return new SubCategoryModel(source);
        }

        @Override
        public SubCategoryModel[] newArray(int size) {
            return new SubCategoryModel[size];
        }
    };


    public SubCategoryModel() {
    }

    public SubCategoryModel(String id, String name) {
        setId(id);
        setValue(name);
    }

    protected SubCategoryModel(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
