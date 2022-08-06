package com.mom.momcustomerapp.controllers.products.models;


import com.mom.momcustomerapp.data.application.Consts;

public class ItemDetails implements Cloneable
{
	public String item_id;
	public String name;
	public String size;
	public String quantity;
	public String selling_price;
	public String mrp;
	public String price;
	public String quantity_id;
	double originalQuantity = 0;

	public double getPriceInDouble() throws NumberFormatException {
		return Consts.getDoubleFromString(price);
	}

	public double getQuantityInDouble(){
		return Double.parseDouble(quantity);
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setOriginalQuantity(double originalQuantity) {
		this.originalQuantity = originalQuantity;
	}

	public double getOriginalQuantity() {
		return originalQuantity;
	}

	@Override
	public ItemDetails clone() throws CloneNotSupportedException {
		return (ItemDetails) super.clone();/*
        Parcel p = Parcel.obtain();
        p.writeValue(this);
        p.setDataPosition(0);
        OrderDetailItemModel foo2 = (OrderDetailItemModel)p.readValue(OrderDetailItemModel.class.getClassLoader());
        p.recycle();
        return foo2;*/
	}

}

