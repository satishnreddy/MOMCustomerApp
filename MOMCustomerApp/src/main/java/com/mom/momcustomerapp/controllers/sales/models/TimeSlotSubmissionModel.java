package com.mom.momcustomerapp.controllers.sales.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * @author shailesh.lobo at 13-05-2020 19:26
 * @since -
 */
public class TimeSlotSubmissionModel {

    @SerializedName("delivery_type")
    @Expose
    public String deliveryType;

    @SerializedName("store_id")
    @Expose
    public String storeId;

    @SerializedName("time_from")
    @Expose
    public String timeFrom;

    @SerializedName("time_to")
    @Expose
    public String timeTo;

    @SerializedName("weekDaysSelected")
    @Expose
    public String weekDaysSelected = null;


    @SerializedName("sr_no")
    @Expose
    public String sr_no;

    public void setWeekDaysSelected(String weekDaysSelected) {
        this.weekDaysSelected = weekDaysSelected;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }
}