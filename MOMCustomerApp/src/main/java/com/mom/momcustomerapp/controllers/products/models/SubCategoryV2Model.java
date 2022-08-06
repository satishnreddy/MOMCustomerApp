package com.mom.momcustomerapp.controllers.products.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @since -
 */
public class SubCategoryV2Model implements Parcelable {

    private final String catId;
    private final String catUrl;
    private final String catName;

    public SubCategoryV2Model(String catName, String catId, String catUrl){
        this.catName = catName;
        this.catId = catId;
        this.catUrl = catUrl;
    }

    protected SubCategoryV2Model(Parcel in) {
        catId = in.readString();
        catUrl = in.readString();
        catName = in.readString();
    }

    public static final Creator<SubCategoryV2Model> CREATOR = new Creator<SubCategoryV2Model>() {
        @Override
        public SubCategoryV2Model createFromParcel(Parcel in) {
            return new SubCategoryV2Model(in);
        }

        @Override
        public SubCategoryV2Model[] newArray(int size) {
            return new SubCategoryV2Model[size];
        }
    };

    public String getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public String getCatUrl() {
        return catUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(catId);
        dest.writeString(catUrl);
        dest.writeString(catName);
    }
}
