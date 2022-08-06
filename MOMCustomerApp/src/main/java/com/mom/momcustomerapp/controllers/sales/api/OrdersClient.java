package com.mom.momcustomerapp.controllers.sales.api;

import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNewOuter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
 * Created by nishant on 16/08/16.
 */
public interface OrdersClient {

    /*
    @GET("cart/orderlist")
    Call<BillingListModelNewOuter> getOrdersBills(@Query("_pge") String pageNo);

*/
    @POST("order/pending")
    @FormUrlEncoded
    Call<BillingListModelNewOuter> getOrdersBillsV2(@Field("store_id") String storeId, @Field("search") String search, @Field("page") String pageNo);

    @POST("custcart/getSalesReturned")
    @FormUrlEncoded
    Call<BillingListModelNewOuter> returnsListingApiMBasket(@Field("store_id") String storeId, @Field("search") String search, @Field("page") String pageNo);


    @POST("order/completed")
    @FormUrlEncoded
    Call<BillingListModelNewOuter> getOrdersCompleted(@Field("store_id") String storeId, @Field("page") String pageNo,
                                                      @Field("search") String search);


    @POST("store/getDeliveryType")
    @FormUrlEncoded
    Call<String> getDeliveryType(@Field("field") String empty);


    @GET("store/getLockCustomer")
    Call<String> getLockCustomer();


    @POST("store/updateDeliveryType")
    @FormUrlEncoded
    Call<String> updateDeliveryType(@Field("delivery_type") String storeId);

    @POST("store/updateLockCustomer")
    @FormUrlEncoded
    Call<String> updateLockCustomer(@Field("lock_customer") int storeId);

    /*
    @GET("cart/orderlist?type=return")
    Call<BillingListModel> getOrdersReturns(@Query("_pge") String pageNo);

    @GET("listpartialpayment")
    Call<BillingListModel> getOrdersPartial(@Query("_pge") String pageNo);
*/
    /*
    @POST("cart/orderdetail/{orderId}")
    @FormUrlEncoded
    Call<OrderDetailOuter> getInvoiceDetailsFromOrderId(
            @Path("orderId") String orderId,
            @Field("store_id") String store_id,
            @Field("delivery_type") String delivery_type
    );

     */

/*
    @POST("cart/returndetail/{orderId}")
    @FormUrlEncoded
    Call<OrderDetailOuter> getReturnInvoiceDetailsFromOrderId(
            @Path("orderId") String orderId,
            @Field("store_id") String store_id,
            @Field("delivery_type") String delivery_type
    );
    */

    /*@GET("cart/returndetail/{orderId}")
    Call<OrderDetailOuter> getReturnInvoiceDetailsFromOrderId(
            @Path("orderId") String orderId
    );*/


    /*
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("detailspartialpayment")
    Call<InvoiceModel> getPartialDetailsFromOrderId(@Field("order_id") String orderId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("cart/orderlist/search")
    Call<BillingListModelNewOuter> searchOrderBills(@Query("_pge") String pageNo, @Field("search") String search);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("cart/returnorderlist/search?type=return")
    Call<BillingListModel> searchOrderReturns(@Query("_pge") String pageNo, @Field("search") String search);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("listpartialpayment")
    Call<BillingListModelNewOuter> searchOrderPartial(@Query("_pge") String pageNo, @Field("search") String search);

    */

    @POST("cart/invoiceReport")
    @FormUrlEncoded
    Call<String> generateInvoiceReport(@Field("default_range") String default_range, @Field("email") String email);

    @POST("cart/invoiceReport")
    @FormUrlEncoded
    Call<String> generateInvoiceReportRange(@Field("start_date") String start_date, @Field("end_date") String end_date, @Field("email") String email);

    /*
    @Headers("Content-Type: application/json")
    @POST("return/products")
    Call<String> returnProduct(@Body ReturnModel returnModel);

    @Headers("Content-Type: application/json")
    @POST("settlementpartialpayment")
    Call<String> settlementPartialPayment(@Body AddPaymentTypeModel addPaymentTypeModel);

    */
    @Headers("Content-Type: application/json")
    @POST("profile/saveStoreInvoiceInfo")
    Call<String> saveInvoiceTaxNumbers(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST("profile/saveStoreTermsInfo")
    Call<String> saveInvoiceTermsInfo(@Body String jsonObject);

}
