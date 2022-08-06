package com.mom.momcustomerapp.controllers.stores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 14-05-2020 20:03
 * @modified by -
 * @updated on -
 * @since -
 */
public class ShopCategoryModel{

    @SerializedName("mcc_code")
    @Expose
    private String mccCode;

    @SerializedName("mcc_merchant")
    @Expose
    private String mccMerchant;

    public String getMccCode() {
        return mccCode;
    }

    public String getMccMerchant() {
        return mccMerchant;
    }

    ShopCategoryModel(String mccCode, String mccMerchant){
        this.mccCode = mccCode;
        this.mccMerchant = mccMerchant;
    }
}