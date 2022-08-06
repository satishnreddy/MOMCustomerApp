package com.mom.momcustomerapp.shared.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Created by shailesh.
 */
public class StatusMessageModel implements Parcelable
{

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public final static Creator<StatusMessageModel> CREATOR = new Creator<StatusMessageModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public StatusMessageModel createFromParcel(Parcel in) {
            return new StatusMessageModel(in);
        }

        public StatusMessageModel[] newArray(int size) {
            return (new StatusMessageModel[size]);
        }

    }
            ;

    protected StatusMessageModel(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StatusMessageModel() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatusSuccess(){
        return getStatus() != null && getStatus().equalsIgnoreCase("success");
    }
}
