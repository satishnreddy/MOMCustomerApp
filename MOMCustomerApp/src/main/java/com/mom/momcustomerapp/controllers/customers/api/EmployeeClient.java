package com.mom.momcustomerapp.controllers.customers.api;


import com.mom.momcustomerapp.controllers.customers.models.EmployeeListModel;
import com.mom.momcustomerapp.controllers.customers.models.EmployeeModel;
import com.mom.momcustomerapp.controllers.customers.models.EmployeeSelectionModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
import retrofit2.http.Query;
import retrofit2.http.Url;

/*
 * Created by Nishant on 22-05-2016.
 */
public interface EmployeeClient {

    @GET("employees")
    Call<EmployeeListModel> getEmployees(@Query("_pge") String pageNo);


    @Headers("Content-Type: application/json")
    @POST("employees/save/-1")
    Call<EmployeeModel> saveEmployee(@Body EmployeeModel employeeModel);


    @Headers("Content-Type: application/json")
    @POST("employees/save/{employeeId}")
    Call<EmployeeModel> updateEmployee(
            @Path("employeeId") String employeeId,
            @Body EmployeeModel employeeModel
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("employees/useraccess/{employeeId}")
    Call<String> updateEmployeeAccess(
            @Path("employeeId") String employeeId,
            @Field("user_type") String userType
    );


    @POST("employees/view/{employeeId}")
    Call<EmployeeModel> getEmployeeFromId(
            @Path("employeeId") String employeeId
    );


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("employees/search")
    Call<EmployeeListModel> searchEmployee(@Query("_pge") String pageNo, @Field("search") String search);


//    @GET("employees?search={search}")
//    Call<EmployeeListModel> alphaSearchEmployee(@Path("search") String search);

    @Multipart
    @POST("employees/upload/{employeeId}")
    Call<String> uploadPic(@Path("employeeId") String employeeId, @Part MultipartBody.Part files);

    @POST("employees/vendoremployeelist")
    Call<List<EmployeeSelectionModel>> getVendorEmployees();

    @Headers("Content-Type: application/json")
    @POST("employees/updateemployeestore")
    Call<String> updateEmployeeStore(@Body String jsonPersonIds);


    @POST("profile/getCityState")
    @FormUrlEncoded
    public Call<String> getLocationDetails(@Field("zip") String zipCode);

    @Multipart
    @POST("profile/uploadImage")
    Call<String> uploadProfileImage(@Part("store_id") RequestBody store_id,
                                    @Part MultipartBody.Part files);
}
