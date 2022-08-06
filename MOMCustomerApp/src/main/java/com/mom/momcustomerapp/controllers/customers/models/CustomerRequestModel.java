package com.mom.momcustomerapp.controllers.customers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author shailesh.lobo at 05-06-2020 20:34
 */
public class CustomerRequestModel {

    @SerializedName("row_id")
    @Expose
    private String rowId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("status")
    @Expose
    private String status;

    public CustomerRequestModel(String rowId, String name, String lastName, String status) {
        this.rowId = rowId;
        this.name = name;
        this.lastName = lastName;
        this.status = status;
    }

    public String getRowId() {
        return rowId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }
}


