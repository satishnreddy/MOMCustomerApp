package com.mom.momcustomerapp.controllers.customers.models;
/*
 * Created by nishant on 05/10/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeSelectionModel implements Parcelable {


    public static final Parcelable.Creator<EmployeeSelectionModel> CREATOR = new Parcelable.Creator<EmployeeSelectionModel>() {
        @Override
        public EmployeeSelectionModel createFromParcel(Parcel source) {
            return new EmployeeSelectionModel(source);
        }

        @Override
        public EmployeeSelectionModel[] newArray(int size) {
            return new EmployeeSelectionModel[size];
        }
    };
    /**
     * phone_number : 1234567842
     * name : emp test 3
     * person_id : 123
     * vendor_id : 15
     */

    private String phone_number;
    private String name;
    private String person_id;
    private String vendor_id;

    public EmployeeSelectionModel() {
    }

    public EmployeeSelectionModel(String phone_number, String name, String person_id, String vendor_id) {
        this.phone_number = phone_number;
        this.name = name;
        this.person_id = person_id;
        this.vendor_id = vendor_id;
    }

    protected EmployeeSelectionModel(Parcel in) {
        this.phone_number = in.readString();
        this.name = in.readString();
        this.person_id = in.readString();
        this.vendor_id = in.readString();
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return person_id;
    }

    public void setPersonId(String person_id) {
        this.person_id = person_id;
    }

    public String getVendorId() {
        return vendor_id;
    }

    public void setVendorId(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone_number);
        dest.writeString(this.name);
        dest.writeString(this.person_id);
        dest.writeString(this.vendor_id);
    }
}
