package com.mom.momcustomerapp.controllers.login.model;


import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;

import java.io.Serializable;

public class LoginCustomerResp extends MOMNetworkResDataStore
{

	public String token;
	public String vendor_id;
	public String person_id;
	public String store_id;
	public String cust_name;
	public double waller_amt = 0.0;

	public String customerPhone;
	public String email;
	public String address_1;
	public String address_2;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String land_mark;
	public int delivery_type;


}
