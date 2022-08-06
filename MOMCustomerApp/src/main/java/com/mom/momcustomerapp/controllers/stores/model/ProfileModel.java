package com.mom.momcustomerapp.controllers.stores.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Created by Nishant on 02-06-2016.
 */
public class ProfileModel implements Parcelable
{
    public static final Creator<ProfileModel> CREATOR = new Creator<ProfileModel>()
    {
        @Override
        public ProfileModel createFromParcel(Parcel source) {
            return new ProfileModel(source);
        }

        @Override
        public ProfileModel[] newArray(int size) {
            return new ProfileModel[size];
        }
    };
    /**
     * user_profile : {"username":"7718878972","person_id":"1","name":"Nishant Patil","phone_number":"7718878972","email":"nishant@shopxie.com","address_1":"Andheri Link Road, Milat Nagar","city":"Mumbai","state":"Maharashtra","zip":"400102","country":"INDIA","image":[{"image_path":"http://dev.shopxie.com/uploads/profile_pics/1/Nishant_Galleria_HT43U.jpeg"}],"user_type":"Expert"}
     * stores : [{"store_id":"1","name":"Nishant Patil","store_name":"Nishant Galleria","store_categories":["Fashion"],"parent_categories":["Women","Men","Unisex"],"store_phone_number":"7718878972","email":"nishant@shopxie.com","store_address":"Andheri Link Road, Milat Nagar","store_postal_code":"400102","store_city":"Mumbai","store_state":"Maharashtra","store_lat":"19.14189610","store_lng":"72.83442400","company_name":"","company_type":"","currency":"","language":"","company_pan":"","personal_pan":"","account_holder":"","bank_name":"","account_number":"","account_type":"","ifsc_code":"","image":[{"image_path":"http://dev.shopxie.com/uploads/profile_pics/1/Nishant_Galleria_HT43U.jpeg"}],"added_date":"02-01-2017","isOwner":true,"access_type":"Expert","vat_tin":"123456","cst_tin":"","pan":"","tan":"","invoice_tnc":""},{"store_id":"2","name":"Nishant Patil","store_name":"Nishant Hyper","store_categories":["Fashion"],"parent_categories":["Men"],"store_phone_number":"7718878972","email":"nishant@shopxie.com","store_address":"Andheri Link Road, Milat Nagar","store_postal_code":"400102","store_city":"Mumbai","store_state":"Maharashtra","store_lat":"19.14189610","store_lng":"72.83442400","company_name":"","company_type":"","currency":"","language":"","company_pan":"","personal_pan":"","account_holder":"","bank_name":"","account_number":"","account_type":"","ifsc_code":"","image":[{"image_path":"http://dev.shopxie.com/uploads/profile_pics/1/Nishant_Hyper_7SQ5A.jpeg"}],"added_date":"30-12-2016","isOwner":true,"access_type":"Expert","vat_tin":"123456","cst_tin":"123456","pan":"ASJPP1254P","tan":"ASHSU1946","invoice_tnc":"0"}]
     */
    @SerializedName("user_profile")
    @Expose
    private UserProfileModel user_profile;
    @SerializedName("stores")
    @Expose
    private List<StoreProfileModel> stores;

    public ProfileModel() {

    }

    protected ProfileModel(Parcel in) {
        this.user_profile = in.readParcelable(UserProfileModel.class.getClassLoader());
        this.stores = in.createTypedArrayList(StoreProfileModel.CREATOR);
    }

    public UserProfileModel getUserProfile() {
        return user_profile;
    }

    public void setUserProfile(UserProfileModel user_profile)
    {
        this.user_profile = user_profile;
    }

    public List<StoreProfileModel> getStores()
    {
        return stores;
    }

    public void setStores(List<StoreProfileModel> stores)
    {
        this.stores = stores;
    }

    public StoreProfileModel getStoreFromId(String storeId)
    {
        for (StoreProfileModel storeProfileModel : stores)
        {
            if (storeProfileModel.getStoreId().equalsIgnoreCase(storeId)) {
                return storeProfileModel;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(this.user_profile, flags);
        dest.writeTypedList(this.stores);
    }

    public static class UserProfileModel implements Parcelable
    {
        public static final Creator<UserProfileModel> CREATOR = new Creator<UserProfileModel>()
        {
            @Override
            public UserProfileModel createFromParcel(Parcel source) {
                return new UserProfileModel(source);
            }

            @Override
            public UserProfileModel[] newArray(int size) {
                return new UserProfileModel[size];
            }
        };
        /**
         * username : 7718878972
         * person_id : 1
         * name : Nishant Patil
         * phone_number : 7718878972
         * email : nishant@shopxie.com
         * address_1 : Andheri Link Road, Milat Nagar
         * city : Mumbai
         * state : Maharashtra
         * zip : 400102
         * country : INDIA
         * image : [{"image_path":"http://dev.shopxie.com/uploads/profile_pics/1/Nishant_Galleria_HT43U.jpeg"}]
         * user_type : Expert
         */
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("person_id")
        @Expose
        private String person_id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone_number")
        @Expose
        private String phone_number;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("address_1")
        @Expose
        private String address_1;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("zip")
        @Expose
        private String zip;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("user_type")
        @Expose
        private String user_type;
        @SerializedName("image")
        @Expose
        private List<ImageModel> image;

        public UserProfileModel() {
        }

        protected UserProfileModel(Parcel in) {
            this.username = in.readString();
            this.person_id = in.readString();
            this.name = in.readString();
            this.phone_number = in.readString();
            this.email = in.readString();
            this.address_1 = in.readString();
            this.city = in.readString();
            this.state = in.readString();
            this.zip = in.readString();
            this.country = in.readString();
            this.user_type = in.readString();
            this.image = in.createTypedArrayList(ImageModel.CREATOR);
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPersonId() {
            return person_id;
        }

        public void setPersonId(String person_id) {
            this.person_id = person_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phone_number;
        }

        public void setPhoneNumber(String phone_number) {
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
            dest.writeString(this.name);
            dest.writeString(this.phone_number);
            dest.writeString(this.email);
            dest.writeString(this.address_1);
            dest.writeString(this.city);
            dest.writeString(this.state);
            dest.writeString(this.zip);
            dest.writeString(this.country);
            dest.writeString(this.user_type);
            dest.writeTypedList(this.image);
        }
    }

    public static class StoreProfileModel implements Parcelable
    {
        public static final Creator<StoreProfileModel> CREATOR = new Creator<StoreProfileModel>() {
            @Override
            public StoreProfileModel createFromParcel(Parcel source) {
                return new StoreProfileModel(source);
            }

            @Override
            public StoreProfileModel[] newArray(int size) {
                return new StoreProfileModel[size];
            }
        };
        /**
         * store_id : 1
         * name : Nishant Patil
         * store_name : Nishant Galleria
         * store_categories : ["Fashion"]
         * parent_categories : ["Women","Men","Unisex"]
         * store_phone_number : 7718878972
         * email : nishant@shopxie.com
         * store_address : Andheri Link Road, Milat Nagar
         * store_postal_code : 400102
         * store_city : Mumbai
         * store_state : Maharashtra
         * store_lat : 19.14189610
         * store_lng : 72.83442400
         * company_name :
         * company_type :
         * currency :
         * language :
         * company_pan :
         * personal_pan :
         * account_holder :
         * bank_name :
         * account_number :
         * account_type :
         * ifsc_code :
         * image : [{"image_path":"http://dev.shopxie.com/uploads/profile_pics/1/Nishant_Galleria_HT43U.jpeg"}]
         * added_date : 02-01-2017
         * isOwner : true
         * access_type : Expert
         * vat_tin : 123456
         * cst_tin :
         * pan :
         * tan :
         * invoice_tnc :
         */
        @SerializedName("store_id")
        @Expose
        private String store_id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("store_name")
        @Expose
        private String store_name;
        @SerializedName("store_phone_number")
        @Expose
        private String store_phone_number;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("store_address")
        @Expose
        private String store_address;
        @SerializedName("store_postal_code")
        @Expose
        private String store_postal_code;
        @SerializedName("store_city")
        @Expose
        private String store_city;
        @SerializedName("store_state")
        @Expose
        private String store_state;
        @SerializedName("store_lat")
        @Expose
        private String store_lat;
        @SerializedName("store_lng")
        @Expose
        private String store_lng;
        @SerializedName("company_name")
        @Expose
        private String company_name;
        @SerializedName("company_type")
        @Expose
        private String company_type;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("company_pan")
        @Expose
        private String company_pan;
        @SerializedName("personal_pan")
        @Expose
        private String personal_pan;
        @SerializedName("account_holder")
        @Expose
        private String account_holder;
        @SerializedName("bank_name")
        @Expose
        private String bank_name;
        @SerializedName("account_number")
        @Expose
        private String account_number;
        @SerializedName("account_type")
        @Expose
        private String account_type;
        @SerializedName("ifsc_code")
        @Expose
        private String ifsc_code;
        @SerializedName("added_date")
        @Expose
        private String added_date;
        @SerializedName("isOwner")
        @Expose
        private boolean isOwner;
        @SerializedName("access_type")
        @Expose
        private String access_type;
        @SerializedName("vat_tin")
        @Expose
        private String vat_tin;
        @SerializedName("cst_tin")
        @Expose
        private String cst_tin;
        @SerializedName("gstin")
        @Expose
        private String gstin;
        @SerializedName("pan")
        @Expose
        private String pan;
        @SerializedName("tan")
        @Expose
        private String tan;
        @SerializedName("invoice_tnc")
        @Expose
        private String invoice_tnc;
        @SerializedName("store_categories")
        @Expose
        private List<String> store_categories;
        @SerializedName("parent_categories")
        @Expose
        private List<String> parent_categories;
        @SerializedName("image")
        @Expose
        private List<ImageModel> image;

        @SerializedName("store_image")
        @Expose
        private String store_image;

        @SerializedName("images_settings")
        @Expose
        private String images_settings;

        public StoreProfileModel() {
        }

        protected StoreProfileModel(Parcel in) {
            this.store_id = in.readString();
            this.name = in.readString();
            this.store_name = in.readString();
            this.store_phone_number = in.readString();
            this.email = in.readString();
            this.store_address = in.readString();
            this.store_postal_code = in.readString();
            this.store_city = in.readString();
            this.store_state = in.readString();
            this.store_lat = in.readString();
            this.store_lng = in.readString();
            this.company_name = in.readString();
            this.company_type = in.readString();
            this.currency = in.readString();
            this.language = in.readString();
            this.company_pan = in.readString();
            this.personal_pan = in.readString();
            this.account_holder = in.readString();
            this.bank_name = in.readString();
            this.account_number = in.readString();
            this.account_type = in.readString();
            this.ifsc_code = in.readString();
            this.added_date = in.readString();
            this.isOwner = in.readByte() != 0;
            this.access_type = in.readString();
            this.vat_tin = in.readString();
            this.cst_tin = in.readString();
            this.gstin = in.readString();
            this.pan = in.readString();
            this.tan = in.readString();
            this.invoice_tnc = in.readString();
            this.store_categories = in.createStringArrayList();
            this.parent_categories = in.createStringArrayList();
            this.image = in.createTypedArrayList(ImageModel.CREATOR);
            this.store_image =  in.readString();
            this.images_settings =  in.readString();
        }

        public String getStoreId() {
            return store_id;
        }

        public void setStoreId(String store_id) {
            this.store_id = store_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStoreName() {
            return store_name;
        }

        public void setStoreName(String store_name) {
            this.store_name = store_name;
        }

        public String getStorePhoneNumber() {
            return store_phone_number;
        }

        public void setStorePhoneNumber(String store_phone_number) {
            this.store_phone_number = store_phone_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStoreAddress() {
            return store_address;
        }

        public void setStoreAddress(String store_address) {
            this.store_address = store_address;
        }

        public String getStorePostalCode() {
            return store_postal_code;
        }

        public void setStorePostalCode(String store_postal_code) {
            this.store_postal_code = store_postal_code;
        }

        public String getStoreCity() {
            return store_city;
        }

        public void setStoreCity(String store_city) {
            this.store_city = store_city;
        }

        public String getStoreState() {
            return store_state;
        }

        public void setStoreState(String store_state) {
            this.store_state = store_state;
        }

        public String getStoreLat() {
            return store_lat;
        }

        public void setStoreLat(String store_lat) {
            this.store_lat = store_lat;
        }

        public String getStoreLng() {
            return store_lng;
        }

        public void setStoreLng(String store_lng) {
            this.store_lng = store_lng;
        }

        public String getCompanyName() {
            return company_name;
        }

        public void setCompanyName(String company_name) {
            this.company_name = company_name;
        }

        public String getCompanyType() {
            return company_type;
        }

        public void setCompanyType(String company_type) {
            this.company_type = company_type;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCompanyPan() {
            return company_pan;
        }

        public void setCompanyPan(String company_pan) {
            this.company_pan = company_pan;
        }

        public String getPersonalPan() {
            return personal_pan;
        }

        public void setPersonalPan(String personal_pan) {
            this.personal_pan = personal_pan;
        }

        public String getAccountHolder() {
            return account_holder;
        }

        public void setAccountHolder(String account_holder) {
            this.account_holder = account_holder;
        }

        public String getBankName() {
            return bank_name;
        }

        public void setBankName(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getAccountNumber() {
            return account_number;
        }

        public void setAccountNumber(String account_number) {
            this.account_number = account_number;
        }

        public String getAccountType() {
            return account_type;
        }

        public void setAccountType(String account_type) {
            this.account_type = account_type;
        }

        public String getIfscCode() {
            return ifsc_code;
        }

        public void setIfscCode(String ifsc_code) {
            this.ifsc_code = ifsc_code;
        }

        public String getAddedDate() {
            return added_date;
        }

        public void setAddedDate(String added_date) {
            this.added_date = added_date;
        }

        public boolean isOwner() {
            return isOwner;
        }

        public void setIsOwner(boolean isOwner) {
            this.isOwner = isOwner;
        }

        public String getAccessType() {
            return access_type;
        }

        public void setAccessType(String access_type) {
            this.access_type = access_type;
        }

        public String getVatTin() {
            return vat_tin;
        }

        public void setVatTin(String vat_tin) {
            this.vat_tin = vat_tin;
        }

        public String getCstTin() {
            return cst_tin;
        }

        public void setCstTin(String cst_tin) {
            this.cst_tin = cst_tin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getTan() {
            return tan;
        }

        public void setTan(String tan) {
            this.tan = tan;
        }

        public String getInvoiceTnC() {
            return invoice_tnc;
        }

        public void setInvoiceTnC(String invoice_tnc) {
            this.invoice_tnc = invoice_tnc;
        }

        public List<String> getStoreCategories()
        {
            return store_categories;
        }

        public void setStoreCategories(List<String> store_categories) {
            this.store_categories = store_categories;
        }

        public List<String> getParentCategories() {
            return parent_categories;
        }

        public void setParentCategories(List<String> parent_categories) {
            this.parent_categories = parent_categories;
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

        public String getStore_image() {
            return store_image;
        }

        public String getImages_settings() {
            return images_settings;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.store_id);
            dest.writeString(this.name);
            dest.writeString(this.store_name);
            dest.writeString(this.store_phone_number);
            dest.writeString(this.email);
            dest.writeString(this.store_address);
            dest.writeString(this.store_postal_code);
            dest.writeString(this.store_city);
            dest.writeString(this.store_state);
            dest.writeString(this.store_lat);
            dest.writeString(this.store_lng);
            dest.writeString(this.company_name);
            dest.writeString(this.company_type);
            dest.writeString(this.currency);
            dest.writeString(this.language);
            dest.writeString(this.company_pan);
            dest.writeString(this.personal_pan);
            dest.writeString(this.account_holder);
            dest.writeString(this.bank_name);
            dest.writeString(this.account_number);
            dest.writeString(this.account_type);
            dest.writeString(this.ifsc_code);
            dest.writeString(this.added_date);
            dest.writeByte(this.isOwner ? (byte) 1 : (byte) 0);
            dest.writeString(this.access_type);
            dest.writeString(this.vat_tin);
            dest.writeString(this.cst_tin);
            dest.writeString(this.gstin);
            dest.writeString(this.pan);
            dest.writeString(this.tan);
            dest.writeString(this.invoice_tnc);
            dest.writeStringList(this.store_categories);
            dest.writeStringList(this.parent_categories);
            dest.writeTypedList(this.image);
            dest.writeString(this.store_image);
            dest.writeString(this.images_settings);
        }
    }

    public static class ImageModel implements Parcelable
    {
        public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
            @Override
            public ImageModel createFromParcel(Parcel source) {
                return new ImageModel(source);
            }

            @Override
            public ImageModel[] newArray(int size) {
                return new ImageModel[size];
            }
        };
        /**
         * image_path : http://dev.shopxie.com/uploads/profile_pics/1/Nishant_Galleria_HT43U.jpeg
         */

        private String image_path;
        private String thumbnail;

        public ImageModel() {
        }

        protected ImageModel(Parcel in) {
            this.image_path = in.readString();
            this.thumbnail = in.readString();
        }

        public String getImagePath() {
            return image_path;
        }

        public void setImagePath(String image_path) {
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
