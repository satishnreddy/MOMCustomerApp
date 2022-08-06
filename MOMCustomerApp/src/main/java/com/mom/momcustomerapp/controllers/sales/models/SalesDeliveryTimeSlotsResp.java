package com.mom.momcustomerapp.controllers.sales.models;


import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesDeliveryTimeSlotsResp extends MOMNetworkResDataStore
{
	public String current_date;
	public ArrayList<TimeSlots> timeSlots = new ArrayList<TimeSlots>();

	public List<TimeSlots> getTimeSlots(){
		return timeSlots;
	}

}

/*
    {
    "status": "success",
    "current_date": "2022-07-25",
    "data": [
        {
            "store_id": "3",
            "delivery_type": "0",
            "sr_no": "1",
            "toSlot": "10.00",
            "fromSlot": "7.00",
            "weekDaysSelected": "1111111",
            "created_date": "2021-05-28 12:57:15",
            "update_date": "2021-06-02 12:27:09"
        },
        {
            "store_id": "3",
            "delivery_type": "0",
            "sr_no": "2",
            "toSlot": "21.00",
            "fromSlot": "18.00",
            "weekDaysSelected": "1111111",
            "created_date": "2021-06-02 12:28:10",
            "update_date": null
        }
    ]
}


*/