package com.mom.momcustomerapp.controllers.products.models;


import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemsResp extends MOMNetworkResDataStore
{
	public int current_page;
	public int total_records;
	public ArrayList<Items> data = new ArrayList<Items>();

	public List<Items> getItems()
	{
		return data;
	}


}

/*
	{
    "current_page": 1,
    "total_records": 4,
    "data": [
        {
            "category_id": "2",
            "category": "Bread, Dairy and Eggs",
            "subcategory_id": "9",
            "subcategory": "Butter & Cheese",
            "name": "Amul Butter 100 Gms",
            "selling_price": "45.00",
            "mrp": "45.00",
            "item_id": "3438",
            "quantity_id": "3438",
            "count": "4"
        },
        {
            "category_id": "2",
            "category": "Bread, Dairy and Eggs",
            "subcategory_id": "8",
            "subcategory": "Bread & Bakery",
            "name": "Bread",
            "selling_price": "30.00",
            "mrp": "30.00",
            "item_id": "3435",
            "quantity_id": "3435",
            "count": "4"
        },
        {
            "category_id": "2",
            "category": "Bread, Dairy and Eggs",
            "subcategory_id": "8",
            "subcategory": "Bread & Bakery",
            "name": "Britania",
            "selling_price": "120.00",
            "mrp": "120.00",
            "item_id": "5928",
            "quantity_id": "5928",
            "count": "4"
        },
        {
            "category_id": "1",
            "category": "Beverages",
            "subcategory_id": "1",
            "subcategory": "Energy Drinks",
            "name": "REDBULL ENERGY DRINK 250mlPK24",
            "selling_price": "25.00",
            "mrp": "25.00",
            "item_id": "5927",
            "quantity_id": "5927",
            "count": "4"
        }
    ]
}

	*/