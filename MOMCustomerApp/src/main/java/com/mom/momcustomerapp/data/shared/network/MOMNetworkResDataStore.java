package com.mom.momcustomerapp.data.shared.network;

import java.io.Serializable;

public abstract class MOMNetworkResDataStore implements Serializable
{
	public String  response = "";
	public int  responseCode;
	public int  errorNetworkResponse;


	public int isValidResponse;
	public int status;
	public int  statuscode;
	public String statusmsg;


}
