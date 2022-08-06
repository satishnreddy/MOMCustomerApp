package com.mom.momcustomerapp.controllers.orders.models;


import com.mom.momcustomerapp.controllers.products.models.ItemDetails;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesDetailsResp extends MOMNetworkResDataStore
{
	public SalesCustOrder salesCustOrder;
	public ArrayList<ItemDetails> items = new ArrayList<ItemDetails>();
	public List<ItemDetails> getItems()
	{
		return items;
	}

	public ArrayList<PaymentData> paymentData = new ArrayList<PaymentData>();
	public List<PaymentData> getPayment()
	{
		return paymentData;
	}

	public String getDelivery_address()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(addThisStr(salesCustOrder.address_1,", "));
		stringBuilder.append(addThisStr(salesCustOrder.address_2,", "));
		stringBuilder.append(addThisStr(salesCustOrder.city,", "));
		stringBuilder.append(addThisStr(salesCustOrder.state," "));
		stringBuilder.append(addThisStr(salesCustOrder.zip,", "));
		stringBuilder.append(addThisStr(salesCustOrder.country,""));
		return stringBuilder.toString();
	}

	private String addThisStr(String addressField, String postPend) {
		if (addressField == null || addressField.isEmpty()){
			return "";
		}
		return addressField + "" + postPend;
	}

}

/*
	{
"status": 1,
"data": {
"sale_id": "645",
"invoice_filename": "",
"sale_time": "2022-06-11 11:39:08",
"customer_id": "1",
"employee_id": "943",
"invoice_number": "7",
"delivery_date": "2022-06-11 00:00:00",
"delivery_slot": "00:00 - 00:00",
"delivered_date": null,
"delivery_status": "Pending",
"payment_type": "Cash",
"total_price": "55",
"customerName": "Poonam",
"customerPhone": "9885135489",
"email": "satishnalluru@gmail.com",
"address_1": "101",
"address_2": "123",
"city": "Hyderabad",
"state": "TELANGANA",
"zip": "500033",
"country": "INDIA",
"land_mark": "",
"permit_image": "",
"txn_id": "",
"Items": [
{
"item_id": "5927",
"name": "REDBULL ENERGY DRINK 250mlPK24",
"size": "Std",
"quantity": "1.00",
"selling_price": "25.00",
"mrp": "25.00",
"price": "25.00",
"quantity_id": "5927"
},
{
"item_id": "3435",
"name": "Bread",
"size": "Std",
"quantity": "1.00",
"selling_price": "30.00",
"mrp": "30.00",
"price": "30.00",
"quantity_id": "3435"
}
],
"Payment": [
{
"payment_type": "Cash",
"payment_amount": "55.00",
"txn_id": "",
"payment_status": null
}
]
}
}

	*/