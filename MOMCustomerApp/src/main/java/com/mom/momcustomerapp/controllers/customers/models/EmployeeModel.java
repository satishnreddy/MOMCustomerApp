package com.mom.momcustomerapp.controllers.customers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/*
 * Created by Nishant on 20-05-2016.
 */
public class EmployeeModel implements Parcelable {

    public static final Creator<EmployeeModel> CREATOR = new Creator<EmployeeModel>() {
        @Override
        public EmployeeModel createFromParcel(Parcel source) {
            return new EmployeeModel(source);
        }

        @Override
        public EmployeeModel[] newArray(int size) {
            return new EmployeeModel[size];
        }
    };

    private String username;
    private String person_id;
    private String vendor_id;
    private String name;
    private String phone_number;
    private String email;
    private String gender;
    private String address_1;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String user_type;
    private List<ImageModel> image;

    public EmployeeModel() {
    }

    public EmployeeModel(String username, String person_id, String vendor_id, String name,
                         String phone_number, String email, String gender, String address_1,
                         String city, String state, String zip, String country, String user_type, List<ImageModel> image) {
        this.username = username;
        this.person_id = person_id;
        this.vendor_id = vendor_id;
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.gender = gender;
        this.address_1 = address_1;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.user_type = user_type;
        this.image = image;
    }

    protected EmployeeModel(Parcel in) {
        this.username = in.readString();
        this.person_id = in.readString();
        this.vendor_id = in.readString();
        this.name = in.readString();
        this.phone_number = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.address_1 = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.zip = in.readString();
        this.country = in.readString();
        this.user_type = in.readString();
        this.image = in.createTypedArrayList(EmployeeModel.ImageModel.CREATOR);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
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

    public String getAddress() {
        return address_1;
    }

    public void setAddress(String address_1) {
        this.address_1 = address_1;
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

    public String getUserType() {
        return user_type;
    }

    public void setUserType(String user_type) {
        this.user_type = user_type;
    }

    public List<ImageModel> getImage() {
        return image;
    }

    public void setImage(List<ImageModel> image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.person_id);
        dest.writeString(this.vendor_id);
        dest.writeString(this.name);
        dest.writeString(this.phone_number);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.address_1);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.zip);
        dest.writeString(this.country);
        dest.writeString(this.user_type);
        dest.writeTypedList(this.image);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setLenient().create();
        String json = gson.toJson(this);
        return json;
    }

    public static class ImageModel implements Parcelable {
        public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
            @Override
            public EmployeeModel.ImageModel createFromParcel(Parcel source) {
                return new EmployeeModel.ImageModel(source);
            }

            @Override
            public EmployeeModel.ImageModel[] newArray(int size) {
                return new EmployeeModel.ImageModel[size];
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
}
