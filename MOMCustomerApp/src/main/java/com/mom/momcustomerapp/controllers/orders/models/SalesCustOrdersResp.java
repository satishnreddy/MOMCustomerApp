package com.mom.momcustomerapp.controllers.orders.models;

import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesCustOrdersResp extends MOMNetworkResDataStore
{
	public int current_page;
	public int total_records;
	public ArrayList<SalesCustOrder> data = new ArrayList<SalesCustOrder>();

	public List<SalesCustOrder> getData()
	{
		return data;
	}


}

/*
	{
    "status": 1,
    "current_page": 0,
    "total_records": 1,
    "data": [
        {
            "sale_id": "645",
            "sale_time": "2022-06-11 11:39:08",
            "customer_id": "1",
            "employee_id": "943",
            "invoice_number": "7",
            "delivery_date": "2022-06-11 00:00:00",
            "delivered_date": null,
            "delivery_status": "Pending",
            "order_status": "0",
            "total_price": "55",
            "max_price": "0",
            "customerName": "Poonam",
            "customerPhone": "9885135489",
            "total_quantity": "0",
            "payment_type": "Cash",
            "address_1": "101",
            "address_2": "123",
            "city": "Hyderabad",
            "state": "TELANGANA",
            "zip": "500033",
            "country": "INDIA",
            "land_mark": "",
            "count": "1",
            "status": 0
        	}
    	]
	}

	*/