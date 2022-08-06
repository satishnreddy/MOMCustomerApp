package com.mom.momcustomerapp.controllers.products.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable
{


	public String category_id;
	public String category;
	public String subcategory_id;
	public String subcategory;
	public String name;
	public String selling_price;
	public String mrp;
	public String item_id;
	public String quantity_id;
	public String count;
	public String qty;
	public String item_total;



	public String img_url="";

	public int iQrySelected = 0;
	public Items()
	{

	}

	protected Items(Parcel in) {
		category_id = in.readString();
		category = in.readString();
		subcategory_id = in.readString();
		subcategory = in.readString();
		name = in.readString();
		selling_price = in.readString();
		mrp = in.readString();
		item_id = in.readString();
		quantity_id = in.readString();
		count = in.readString();
		qty = in.readString();
		item_total = in.readString();

	}

	public static final Creator<Items> CREATOR = new Creator<Items>() {
		@Override
		public Items createFromParcel(Parcel in) {
			return new Items(in);
		}

		@Override
		public Items[] newArray(int size) {
			return new Items[size];
		}
	};

	public boolean isInStore()
	{
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(category_id);
		dest.writeValue(category);
		dest.writeValue(subcategory_id);
		dest.writeValue(subcategory);
		dest.writeValue(subcategory);
		dest.writeValue(name);
		dest.writeValue(selling_price);
		dest.writeValue(mrp);
		dest.writeValue(item_id);
		dest.writeValue(quantity_id);
		dest.writeValue(count);
		dest.writeValue(qty);
		dest.writeValue(item_total);

	}



}


