package com.mom.momcustomerapp.controllers.sales.models;


import com.mom.momcustomerapp.controllers.products.models.Items;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesUpdateCartResp extends MOMNetworkResDataStore
{
	public int item_added;
	public ArrayList<Items> items = new ArrayList<Items>();

	public List<Items> getItems(){
		return items;
	}




}

/*

    {
        "status": "1",
        "statuscode": "200",
        "item_added": "0",
        "Items": [
        {
            "item_id": 5928,
            "name": "Britania",
            "quantity_id": 5928,
            "qty": 1,
            "selling_price": "120.00",
            "item_total": "120.00"
        },
        {
            "item_id": 3438,
            "name": "Amul Butter 100 Gms",
            "quantity_id": 3438,
            "qty": 8,
            "selling_price": "45.00",
            "item_total": "360.00"
        }

    }


*/