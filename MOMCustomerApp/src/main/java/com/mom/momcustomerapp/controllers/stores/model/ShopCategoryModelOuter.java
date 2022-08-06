package com.mom.momcustomerapp.controllers.stores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mom.momcustomerapp.controllers.stores.model.ShopCategoryModel;

import java.util.ArrayList;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 14-05-2020 20:03
 * @modified by -
 * @updated on -
 * @since -
 */
public class ShopCategoryModelOuter {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private ArrayList<ShopCategoryModel> data;

    public ArrayList<ShopCategoryModel> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
}