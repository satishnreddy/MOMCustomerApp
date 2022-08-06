package com.mom.momcustomerapp.controllers.stores.api;



import com.mom.momcustomerapp.controllers.stores.model.ProfileModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/*
 * Created by Nishant on 02-06-2016.
 */
public interface ProfileClient
{

    @GET("profile")
    Call<ProfileModel> getProfile();

    @Headers("Content-Type: application/json")
    @POST("profile/save")
    Call<String> saveProfile(@Body ProfileModel.StoreProfileModel storeProfileModel);

    /*
    @Multipart
    @POST("profile/upload/{venderId}")
    Call<String> uploadPic(@Path("venderId") String venderId, @Part MultipartBody.Part files);

    @GET("allSubCategory")
    Call<List<StoreCategoryConfigModel>> getProfileSubCategoriesList();

    @GET("profile/getConfigTax")
    Call<InvoiceTaxModel> getInvoiceVatTaxes();

    @Headers("Content-Type: application/json")
    @POST("profile/saveConfigTax")
    Call<InvoiceTaxModel> saveInvoiceVatTaxes(@Body InvoiceTaxModel invoiceTaxModel);

    @POST("profile/getgstconfigtax")
    Call<InvoiceTaxModel> getGstTaxConfig();

    @Headers("Content-Type: application/json")
    @POST("profile/savegstconfigtax")
    Call<InvoiceTaxModel> saveGstTaxConfig(@Body InvoiceTaxModel invoiceTaxModel);

    @Headers("Content-Type: application/json")
    @POST("profile/removecatgst")
    Call<String> removeGstTaxForCategory(@Body String jsonString);

    @POST("profile/saveInvoiceConfig")
    @FormUrlEncoded
    Call<String> saveInvoiceConfig(@Field("invoice_configration") String invoiceConfigration);

    @GET("profile/getInvoiceConfig")
    Call<String> getInvoiceConfig();

    @GET("profile/businessdetails")
    Call<StoreBusinessModel> getBusinessSettings();

    @Headers("Content-Type: application/json")
    @POST("profile/savebankdetails")
    Call<StoreBusinessModel.BanksModel> saveBankDetails(@Body StoreBusinessModel.BanksModel banksModel);

    @Headers("Content-Type: application/json")
    @POST("profile/configtaxsave")
    Call<StoreBusinessModel> saveTaxConfig(@Body StoreBusinessModel storeBusinessModel);

    @GET
    @Headers("Content-Type: application/json")
    Call<VersionModel> checkUpdates(@Url String url);

    @Headers("Content-Type: application/json")
    @POST("addmultistore")
    Call<String> addMultiStore(@Body ProfileModel.StoreProfileModel storeProfileModel);
    */

}
