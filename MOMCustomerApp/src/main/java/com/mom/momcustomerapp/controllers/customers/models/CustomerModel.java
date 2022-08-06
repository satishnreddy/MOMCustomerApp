package com.mom.momcustomerapp.controllers.customers.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Created by Nishant on 21-05-2016.
 */
public class CustomerModel implements Parcelable {

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel source) {
            return new CustomerModel(source);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };

    private String person_id;
    private String name;
    private String phone_number;
    private String email;
    private String gstin;
    private String gender;
    private String address_1;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String is_new;
    private List<ImageModel> image;
    private boolean isChecked;
    private String address_2;
    private String land_mark;

    @SerializedName("row_id")
    @Expose
    private String rowId;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("password")
    @Expose
    private String password;


    public CustomerModel() {
    }

    public CustomerModel(String person_id, String name, String phone_number, String email, String gstin, String gender,
                         String address_1, String city, String state, String zip, String country, String is_new, List<ImageModel> image) {
        this.person_id = person_id;
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.gstin = gstin;
        this.gender = gender;
        this.address_1 = address_1;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.is_new = is_new;
        this.image = image;
    }

    protected CustomerModel(Parcel in) {
        this.person_id = in.readString();
        this.name = in.readString();
        this.phone_number = in.readString();
        this.email = in.readString();
        this.gstin = in.readString();
        this.gender = in.readString();
        this.address_1 = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.zip = in.readString();
        this.country = in.readString();
        this.is_new = in.readString();
        this.image = in.createTypedArrayList(CustomerModel.ImageModel.CREATOR);
        this.address_2 = in.readString();
        this.land_mark = in.readString();
        this.lastName = in.readString();
        this.rowId = in.readString();
        this.status = in.readString();
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_2() {
        return address_2;
    }

    public String getLand_mark() {
        return land_mark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLand_mark(String land_mark) {
        this.land_mark = land_mark;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword()
    {
        return this.password ;
    }


    public List<ImageModel> getImage() {
        return image;
    }

    public void setImage(List<ImageModel> image) {
        this.image = image;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStatus() {
        return status;
    }

    public String getRowId() {
        return rowId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.person_id);
        dest.writeString(this.name);
        dest.writeString(this.phone_number);
        dest.writeString(this.email);
        dest.writeString(this.gstin);
        dest.writeString(this.gender);
        dest.writeString(this.address_1);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.zip);
        dest.writeString(this.country);
        dest.writeString(this.is_new);
        dest.writeTypedList(this.image);
        dest.writeString(address_2);
        dest.writeString(land_mark);
        dest.writeString(lastName);
        dest.writeString(rowId);
        dest.writeString(status);
    }

    public static class ImageModel implements Parcelable {
        public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
            @Override
            public CustomerModel.ImageModel createFromParcel(Parcel source) {
                return new CustomerModel.ImageModel(source);
            }

            @Override
            public CustomerModel.ImageModel[] newArray(int size) {
                return new CustomerModel.ImageModel[size];
            }
        };
        private String image_path;
        private String thumbnail;

        public ImageModel() {
        }

        public ImageModel(String image_path, String thumbnail) {
            this.image_path = image_path;
            this.thumbnail = thumbnail;
        }

        protected ImageModel(Parcel in) {
            this.image_path = in.readString();
            this.thumbnail = in.readString();
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.image_path);
            dest.writeString(this.thumbnail);
        }
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof CustomerModel)) return false;
        CustomerModel model = (CustomerModel) obj;
        return getPerson_id() != null && getPerson_id().equals(model.person_id);
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (person_id != null ? person_id.hashCode() : 0);
        return hash;
    }
}
