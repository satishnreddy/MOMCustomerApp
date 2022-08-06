package com.mom.momcustomerapp.controllers.sales.models;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 13-05-2020 18:29
 * @modified by - Bharath
 * @updated on - 23-07-2020 14:30
 * @since -
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotModel implements Parcelable
{

    public final static Parcelable.Creator<TimeSlotModel> CREATOR = new Creator<TimeSlotModel>() {

        @SuppressWarnings({
                "unchecked"
        })
        public TimeSlotModel createFromParcel(Parcel in) {
            return new TimeSlotModel(in);
        }

        public TimeSlotModel[] newArray(int size) {
            return (new TimeSlotModel[size]);
        }

    };

    public String status;
    public String current_date;
    public List<TimeSlot> data;



    protected TimeSlotModel(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.current_date = ((String) in.readValue((String.class.getClassLoader())));
        this.data = new ArrayList<>();
    }

    public TimeSlotModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(current_date);
        dest.writeList(this.data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public List<TimeSlot> getData() {
        return data;
    }

    public void setData(List<TimeSlot> data) {
        this.data = data;
    }



    public static class TimeSlot implements Parcelable {

        public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
            @Override
            public TimeSlot createFromParcel(Parcel source) {
                return new TimeSlot(source);
            }

            @Override
            public TimeSlot[] newArray(int size) {
                return new TimeSlot[size];
            }
        };


        @SerializedName("store_id")
        @Expose
        public String storeId;
        @SerializedName("delivery_type")
        @Expose
        public String deliveryType;
        @SerializedName("fromSlot")
        @Expose
        public String fromSlot;
        @SerializedName("toSlot")
        @Expose
        public String toSlot;
        @SerializedName("weekDaysSelected")
        @Expose
        public String weekDaysSelected;

        @SerializedName("sr_no")
        @Expose
        public String sr_no;


        protected TimeSlot(Parcel in) {
            this.storeId = ((String) in.readValue((String.class.getClassLoader())));
            this.deliveryType = ((String) in.readValue((String.class.getClassLoader())));
            this.fromSlot = ((String) in.readValue((String.class.getClassLoader())));
            this.toSlot = ((String) in.readValue((String.class.getClassLoader())));
            this.weekDaysSelected = ((String) in.readValue((String.class.getClassLoader())));
            this.sr_no = ((String) in.readValue((String.class.getClassLoader())));
        }

        public TimeSlot() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(storeId);
            dest.writeValue(deliveryType);
            dest.writeValue(fromSlot);
            dest.writeValue(toSlot);
            dest.writeValue(weekDaysSelected);
            dest.writeValue(sr_no);
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getFromSlot() {
            return fromSlot;
        }

        public void setFromSlot(String fromSlot) {
            this.fromSlot = fromSlot;
        }

        public String getToSlot() {
            return toSlot;
        }

        public void setToSlot(String toSlot) {
            this.toSlot = toSlot;
        }

        public String getWeekDaysSelected() {
            return weekDaysSelected;
        }

        public void setWeekDaysSelected(String weekDaysSelected) {
            this.weekDaysSelected = weekDaysSelected;
        }

        public String getSr_no() {
            return sr_no;
        }

        public void setSr_no(String sr_no) {
            this.sr_no = sr_no;
        }
    }
}