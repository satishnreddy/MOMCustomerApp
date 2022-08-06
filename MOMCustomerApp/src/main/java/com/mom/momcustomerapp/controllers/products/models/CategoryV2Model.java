package com.mom.momcustomerapp.controllers.products.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 13-04-2020 13:32
 * @modified by -
 * @updated on -
 * @since -
 */


public class CategoryV2Model implements Parcelable
{

    @SerializedName("parent_category_id")
    @Expose
    private String parentCategoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("store_category_id")
    @Expose
    private String storeCategoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    @SerializedName("store_id")
    @Expose
    private String storeId;



    public final static Parcelable.Creator<CategoryV2Model> CREATOR = new Creator<CategoryV2Model>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryV2Model createFromParcel(Parcel in) {
            return new CategoryV2Model(in);
        }

        public CategoryV2Model[] newArray(int size) {
            return (new CategoryV2Model[size]);
        }

    }
            ;

    protected CategoryV2Model(Parcel in) {
        this.parentCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.storeCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.imgUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.storeId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CategoryV2Model() {
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreCategoryId() {
        return storeCategoryId;
    }

    public void setStoreCategoryId(String storeCategoryId) {
        this.storeCategoryId = storeCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(parentCategoryId);
        dest.writeValue(name);
        dest.writeValue(storeCategoryId);
        dest.writeValue(categoryName);
        dest.writeValue(categoryId);
        dest.writeValue(imgUrl);
        dest.writeValue(storeId);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof CategoryV2Model && ((CategoryV2Model) obj).getCategoryId().equalsIgnoreCase(this.getCategoryId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.getCategoryId() != null ? this.getCategoryId().hashCode() : 0);
        return hash;
    }
}
