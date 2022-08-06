package com.mom.momcustomerapp.controllers.sales.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mom.momcustomerapp.data.application.Consts;

/**
 * @author shailesh.lobo at 03-06-2020 23:32
 * @since -
 */
public class OrderDetailItemModel implements Cloneable {

    @SerializedName("item_id")
    @Expose
    public String itemId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("mrp")
    @Expose
    public String mrp;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("quantity_id")
    @Expose
    public String quantityId;
    public double originalQuantity;


    public String getName() {
        return name;
    }

    public String getItemId() {
        return itemId;
    }

    public String getMrp() {
        return mrp;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getQuantityId() {
        return quantityId;
    }

    public String getSize() {
        return size;
    }

    public double getQuantityInDouble(){
        return Double.parseDouble(getQuantity());
    }


    public double getPriceInDouble() throws NumberFormatException {
        return Consts.getDoubleFromString(getPrice());
    }

    public double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public OrderDetailItemModel clone() throws CloneNotSupportedException {
        return (OrderDetailItemModel) super.clone();/*
        Parcel p = Parcel.obtain();
        p.writeValue(this);
        p.setDataPosition(0);
        OrderDetailItemModel foo2 = (OrderDetailItemModel)p.readValue(OrderDetailItemModel.class.getClassLoader());
        p.recycle();
        return foo2;*/
    }
}


