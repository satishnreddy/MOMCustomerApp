package com.mom.momcustomerapp.controllers.products.api;


import com.mom.momcustomerapp.controllers.customers.models.CustRequesterDetailModel;
import com.mom.momcustomerapp.controllers.customers.models.CustomerModel;
import com.mom.momcustomerapp.controllers.customers.models.CustomersListModel;
import com.mom.momcustomerapp.controllers.customers.models.SelectedCustomerModel;

import com.mom.momcustomerapp.controllers.sales.models.TimeSlotModel;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlotSubmissionModel;
import com.mom.momcustomerapp.shared.models.StatusMessageModel;

import java.util.ArrayList;

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

/*
 * Created by Nishant on 22-05-2016.
 */
public interface CustomerClient
{


    @FormUrlEncoded
    @POST("cust/editItemPrice")
    Call<String> editItemPrice(@Field("item_id") String item_id,
                               @Field("selling_price") String sellingPrice);


    @FormUrlEncoded
    @POST("items/deleteItem/{itemId}")
    Call<String> removeInstoreProduct(
            @Path("itemId") String item_id,
            @Field("deleted") String deleted);


    @FormUrlEncoded
    @POST("items/deleteItem/{itemId}")
    Call<String> addAgainProduct(
            @Path("itemId") String item_id,
            @Field("deleted") String deleted);


    @GET("customers")
    Call<CustomersListModel> getCustomers(@Query("_pge") String pageNo);


    @POST("store/getPendingCustList")
    @FormUrlEncoded
    Call<CustomersListModel> getPendingCustList(
            @Field("page") String page,
            @Field("search") String search);


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("customers/save/-1")
    Call<CustomerModel> saveCustomer(
            @Field("name") String name,
            @Field("email") String email,
            @Field("gstin") String gstin,
            @Field("phone_number") String phone,
            @Field("address_1") String address,
            @Field("address_2") String address2,
            @Field("land_mark") String land_mark,
            @Field("zip") String zip,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("password") String password


    );


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("customers/save/{customerId}")
    Call<CustomerModel> updateCustomer(
            @Path("customerId") String customerId,
            @Field("name") String name,
            @Field("email") String email,
            @Field("gstin") String gstin,
            @Field("phone_number") String phone,
            @Field("address_1") String address,
            @Field("address_2") String address2,
            @Field("land_mark") String land_mark,
            @Field("zip") String zip,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("password") String password

            );


    @POST("customers/view/{customerId}")
    Call<CustomerModel> getCustomerFromId(
            @Path("customerId") String customerId
    );


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("customers")
    Call<CustomersListModel> searchCustomer(@Query("_pge") String pageNo, @Field("search") String search);

    @Multipart
    @POST("customers/upload/{customerId}")
    Call<String> uploadPic(@Path("customerId") String customerId, @Part MultipartBody.Part files);

    @Headers("Content-Type: application/json")
    @POST("customers/bulk_delete")
    Call<String> bulkDeleteCustomers(@Body SelectedCustomerModel selectedCustomerModel);


    @POST("cust/SendSMS")
    @FormUrlEncoded
    Call<String> getSmsLink(@Field("store_id") String storeId,/* @Field("mobile_no") String mobileNum,*/
                                @Field("user_name") String UserName, @Field("vendor_id") String vendorId);

    @POST("store/getCustDetail")
    @FormUrlEncoded
    Call<CustRequesterDetailModel> getCustDetailOfRequester(@Field("row_id") String row_id);

    @POST("store/approveCust")
    @FormUrlEncoded
    Call<String> approveCust(@Field("row_id") String row_id);

    @POST("store/declineCust")
    @FormUrlEncoded
    Call<String> declineCust(@Field("row_id") String row_id);

    @POST("cust/updateOrderStatus")
    @FormUrlEncoded
    Call<String> updateOrderStatus(@Field("sale_id") String sale_id, @Field("status") String status, @Field("delivery_date") String delivery_date, @Field("delivery_slot") String delivery_slot);

    @Headers("Content-Type: application/json")
    @POST("return/products")
    Call<String> returnProducts(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST("custcart/return")
    Call<String> returnProductsV2(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST("custcart/returnAccepted")
    Call<String> returnAccepted(@Body String jsonObject);

/*
    @POST("items/getStoreCategories")
    @FormUrlEncoded
    Call<CategoryV2PaginatedModel> getCat(@Field("store_id") String storeId, @Field("username") String UserName,
                                          @Field("vendor_id") String vendorId,
                                          @Field("search") String searchText, @Field("page") String pageNumber);

*/
    @POST("items/getStoreSubCategories")
    @FormUrlEncoded
    Call<String> getSubCat(@Field("category_id") String category_id,
                           @Field("search") String searchText, @Field("page") String pageNumber);
/*

    @POST("items/getStoreItemsByCategories")
    @FormUrlEncoded
    Call<ProductItemsListPaginationModel> getSubCatProducts(@Field("store_id") String storeId,
                                                            @Field("vendor_id") String vendorId,
                                                            @Field("search") String searchText,
                                                            @Field("page") String pageNumber,
                                                            @Field("category_id") String category_id,
                                                            @Field("sub_category_id") String sub_category_id);


    @POST("items/getStoreItemsByCategory")
    @FormUrlEncoded
    Call<String> getStoreItemsByCategory(@Field("store_id") String storeId,
                                                       @Field("vendor_id") String vendorId,
                                                       @Field("search") String searchText,
                                                       @Field("page") String pageNumber,
                                                       @Field("person_id") String person_id,
                                                       @Field("category_id") String category_id,
                                                       @Field("sub_category_id") String sub_category_id,
                                                       @Field("include_deleted") String include_deleted);


    @POST("profile/getProducts")
    @FormUrlEncoded
    Call<ItemsListModelOuter> getSubCatInStoreProducts(@Field("store_id") String storeId,
                                                       @Field("vendor_id") String vendorId,
                                                       @Field("search") String searchText,
                                                       @Field("page") String pageNumber,
                                                       @Field("person_id") String person_id,
                                                       @Field("subCat_id") String sub_category_id,
                                                       @Field("include_deleted") String include_deleted);


    @POST("cust/listAllProducts")
    @FormUrlEncoded
    Call<ProductItemsListPaginationModel> listAllProducts(@Field("store_id") String storeId,
                                                          @Field("vendor_id") String vendorId,
                                                          @Field("search") String search,
                                                          @Field("page") String pageNo);


    @POST("profile/getOrderList")
    @FormUrlEncoded
    Call<PastOrderModelPagination> getOrderList(@Field("store_id") String store_id, @Field("customer_id") String customer_id,
                                                @Field("page") String page, @Field("vendor_id") String vendor_id);


    @POST("profile/gettransactionstatus")
    @FormUrlEncoded
    Call<String> checkOrderPaymentStatus(@Field("txn_id") String Txn_id,
                                         @Field("username") String username,
                                         @Field("vendor_id") String vendor_id);

    @POST("order/sendPaymentLink")
    @FormUrlEncoded
    Call<String> sendPaymentLink(@Field("user_name") String user_name,
                                 @Field("customer_mobile") String customer_mobile,
                                 @Field("sale_id") String sale_id,
                                 @Field("store_name") String store_name,
                                 @Field("invoice_number") String invoice_number,
                                 @Field("amount") String amount);

    @POST("profile/searchProducts")
    @FormUrlEncoded
    Call<ItemsListModel> getSearchInInStoreProducts(@Field("store_id") String storeId,
                                                    @Field("vendor_id") String vendorId,
                                                    @Field("search") String searchText,
                                                    @Field("page") String pageNumber);
*/
    @POST("cust/getSlot")
    @FormUrlEncoded
    Call<TimeSlotModel> getSlot(@Field("store_id") String storeId,
                                @Field("delivery_type") String delivery_type);


    /*
    * {
    "delivery_type": "0",
    "store_id": "1",
    "time_from": "2",
    "time_to": "3",
    "days":["sun"]
    *
    * {
    "delivery_type": "0",
    "store_id": "1",
    "time_from": "9",
    "time_to": "15",
    "weekDaysSelected":"0111111"
}
    * }
    * */
    @POST("cust/addSlot")
    Call<String> addTimeSlots(@Body TimeSlotSubmissionModel model);

    @POST("cust/deleteSlot")
    @FormUrlEncoded
    Call<StatusMessageModel> deleteTimeSlot(@Field("store_id") String storeId,
                                            @Field("delivery_type") String delivery_type,
                                            @Field("sr_no") String sr_no);


    @POST("cust/updateSlot")
    Call<String> updateTimeSlots(@Body TimeSlotSubmissionModel model);


    @POST("profile/checkVPAStatus")
    @FormUrlEncoded
    Call<String> checkVPAStatus(@Field("qrid") String qrid,
                                         @Field("username") String username);



    @Multipart
    @POST("order/uploadInvoice")
    Call<String> uploadProfileImage(@Part("sale_id") RequestBody sale_id,
                                    @Part MultipartBody.Part files);

    @FormUrlEncoded
    @POST("{type}")
    Call<String> getReport(
            @Path("type") String type,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date
    );
}
