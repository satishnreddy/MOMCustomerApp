package com.mom.momcustomerapp.controllers.orders.models;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesCustOrder
{
	public String sale_id;
	public String invoice_filename;
	public String sale_time ;
	public String customer_id;
	public String employee_id;
	public String invoice_number;
	public String delivery_slot;
	public String delivery_date;
	public String delivered_date;
	public String delivery_status;
	public String order_status;
	public String total_price;
	public String txn_id;

	public String max_price;
	public String customerName;
	public String customerPhone;
	public String total_quantity;
	public String payment_type;
	public String email;
	public String address_1;
	public String address_2;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String land_mark;
	public String permit_image;
	public String count;
	public int  status;






	public int getImageStatus() {
		switch (order_status){
			case ORDER_STATUS.ORDER_STATUS_PENDING:
				return R.drawable.ic_order_pending;
			case ORDER_STATUS.ORDER_STATUS_ACCEPTED:
				return R.drawable.ic_order_accepted;
			case ORDER_STATUS.ORDER_STATUS_DELIVERED:
				return R.drawable.ic_order_delivered;
			case ORDER_STATUS.ORDER_STATUS_RETURNED:
				return R.drawable.ic_order_returned;
		}
		return R.drawable.ic_order_pending;
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