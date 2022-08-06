package com.mom.momcustomerapp.data.shared;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Nishant on 16-06-2016.
 */
public class ErrorModel implements Parcelable {

    public static final Creator<ErrorModel> CREATOR = new Creator<ErrorModel>() {
        @Override
        public ErrorModel createFromParcel(Parcel source) {
            return new ErrorModel(source);
        }

        @Override
        public ErrorModel[] newArray(int size) {
            return new ErrorModel[size];
        }
    };


    private String status;
    private int errorcode;
    private String message;

    public ErrorModel() {
    }

    protected ErrorModel(Parcel in) {
        this.status = in.readString();
        this.errorcode = in.readInt();
        this.message = in.readString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeInt(this.errorcode);
        dest.writeString(this.message);
    }
}
