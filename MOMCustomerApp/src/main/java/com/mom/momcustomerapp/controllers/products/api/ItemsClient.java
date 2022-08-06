package com.mom.momcustomerapp.controllers.products.api;


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

/*
 * Created by Nishant on 03-06-2016.
 */
public interface ItemsClient {


	@POST("cust/getStatsCount")
	@FormUrlEncoded
	Call<String> getStatsCount(@Field("store_id") String storeId,
							   @Field("date") String date);

	@POST("cust/getStockStatsCount")
	@FormUrlEncoded
	Call<String> getStockStatsCount(@Field("store_id") String storeId);

	/*
	@POST("cust/listAllProducts")
	@FormUrlEncoded
	Call<ProductItemsListPaginationModel> listAllProducts(@Field("store_id") String storeId,
														  @Field("search") String search,
														  @Field("vendor_id") String vendorId,
														  @Field("page") String pageNo);

	@GET("items")
	Call<ItemsListModel> getNewItemsListModel(@Query("_pge") String pageNo);

	@GET("items/instock")
	Call<ItemsListModel> getInStockListModel(@Query("_pge") String pageNo);

	@GET("items/outofstock")
	Call<ItemsListModel> getOutOfStockListModel(@Query("_pge") String pageNo);

	@POST("items/view/{itemId}")
	Call<ItemModel> getItemFromId(@Path("itemId") String itemId);

	@Headers("Content-Type: application/json")
	@POST("items/save/-1")
	Call<ItemModel> saveItem(@Body AddItemModel addItemModel);

	@Headers("Content-Type: application/json")
	@POST("items/saveV2/{itemId}")
	Call<ItemModel> updateItem(@Path("itemId") String itemId, @Query("type") String type, @Body ItemAddModel itemAddModel);

	*/
	@Multipart
	@POST("items/createStoreCategory")
	Call<String> createCategory(@Part("cat_name") String cat_name,
								@Part("has_image") String has_image,
								@Part("parentcategory_id") String parentcategory_id,
								@Part MultipartBody.Part files);

	/*
	@POST("deleteStoreCat")
	@FormUrlEncoded
	Call<String> deleteCategory(@Field("cat_id") String cat_id);

	@GET("getDeletedStoreCat")
	Call<ArchivedCategoriesList> getDeletedCategories();

	@POST("restoreDeletedStoreCat")
	@FormUrlEncoded
	Call<String> restoreDeletedCategory(@Field("cat_id") String cat_id);
*/
	@POST("updateStoreImageSettings")
	@FormUrlEncoded
	Call<String> updateStoreImageSettings(@Field("img_setting") String img_setting);


	@Multipart
	@POST("items/createStoreSubCategory")
	Call<String> createSubCategory(@Part("cat_name") String cat_name,
									@Part("has_image") String has_image,
									@Part("cat_id") String cat_id,
									@Part MultipartBody.Part files);
	/*
	@POST("deleteStoreSubCat")
	@FormUrlEncoded
	Call<String> deleteSubCategory(@Field("sub_cat_id") String cat_id);

	@POST("getDeletedStoreSubCat")
	@FormUrlEncoded
	Call<ArchivedCategoriesList> getDeletedSubcategories(@Field("cat_id") String cat_id);

	@POST("restoreDeletedStoreSubCat")
	@FormUrlEncoded
	Call<String> restoreDeletedSubcategory(@Field("sub_cat_id") String sub_cat_id);

	@POST("items/search")
	@FormUrlEncoded
	Call<ItemsListModel> searchItem(@Query("_pge") String pageNo, @Field("search") String query);

	@POST("items/barcode/search")
	@FormUrlEncoded
	Call<List<ItemModel>> searchBarcode(@Field("search") String barcode);

	@POST("items/barcode/{itemId}")
	@FormUrlEncoded
	Call<String> assignBarcode(@Path("itemId") String itemId, @Field("barcode") String barcode, @Field("isForced") String isForced);
	*/
	@Headers("Content-Type: application/json")
	@POST("items/instock/{itemId}")
	Call<String> moveItemInStock(@Path("itemId") String itemId, @Body String addItemModel);

	/*
	@GET("items/outofstock/{itemId}")
	Call<String> moveItemOutOfStock(@Path("itemId") String itemId);

	@GET("items/deleteItem/{itemId}")
	Call<String> deleteItem(@Path("itemId") String itemId);

	@Multipart
	@POST("items/sx_bulk_product")
	Call<BulkUploadModel> bulkUpload(@Part MultipartBody.Part upload);

	@Multipart
	@POST("items/upload/{itemId}")
	Call<String> uploadPics(@Path("itemId") String itemId, @Part MultipartBody.Part files);


	@POST("items/verify_barcode")
	@FormUrlEncoded
	Call<List<String>> validateBarcode(@Field("start") String startBarcode, @Field("end") String endBarcode);

	@POST("generateBarcodes")
	@FormUrlEncoded
	Call<String> generateBarcodes(@Field("quantity") String quantity);


	@POST("items/instocksearch")
	@FormUrlEncoded
	Call<ItemsListModel> searchInStockItem(@Query("_pge") String pageNo, @Field("search") String query);

	@POST("items/outofstocksearch")
	@FormUrlEncoded
	Call<ItemsListModel> searchOutOfStockItem(@Query("_pge") String pageNo, @Field("search") String query);

	@POST("items/instockitemsreport")
	Call<String> reportInStockItems();

	@POST("items/outofstockitemsreport")
	Call<String> reportOutOfStockItems();

	@POST("items/getbarcodelist")
	@FormUrlEncoded
	Call<ItemsListModel> getStockForPrinting(@Query("_pge") String pageNo, @Field("start") String startbarcode, @Field("end") String endbarcode);

	@Headers("Content-Type: application/json")
	@POST("items/addstockV2")
	Call<AddItemBulkResultModel> bulkAddItems(@Query("type") String type, @Body ItemAddModel itemAddModel);

	@Headers("Content-Type: application/json")
	@POST("items/addstockV2")
	Call<AddItemBulkResultModel> bulkAddItemsJson(@Query("type") String type, @Body AddProductFinal itemAddModel);

	@POST("items/addstockV2")
	@FormUrlEncoded
	Call<AddItemBulkResultModel> addToStockApi(
			@Field("items_data") String items_data,
			@Field("category_id") String category_id,
			@Field("sub_category_id") String sub_category_id);

	*/

	@Multipart
	@POST("items/addItem")
	Call<String> addItem(@Part("has_image") String has_image,
													@Part("price") String price,
													 @Part("mrp") String mrp,
													 @Part("item_name") String items_name,
													@Part("category_id") String category_id,
													@Part("sub_category_id") String sub_category_id,
													@Part MultipartBody.Part files);

	/*
    @Multipart
    @POST("items/addItemTemp")
    Call<AddItemBulkResultModel> addToStockApiImage(@Part("has_image") String has_image,
                                    @Part("items_data") String items_data,
                                    @Part("category_id") String category_id,
                                    @Part("sub_category_id") String sub_category_id,
                                    @Part MultipartBody.Part files);

	*/

	@POST("items/editItemPriceNQty")
	@FormUrlEncoded
	Call<String> editItemPriceNQty(
			@Field("item_id") String item_id,
			@Field("qty_id") String qty_id,
			@Field("price") String price,
			@Field("qty") String qty,
			@Field("mrp") String mrp
			);
/*
	@POST("items/addstockV2Multiple")
	@FormUrlEncoded
	Call<String> bulkAddItemsJsonMultipleNew(
			@Field("items_data") String items_data);



	@Headers("Content-Type: application/json")
	@POST("items/addstockV2Multiple")
	Call<String> bulkAddItemsJsonMultiple(@Query("type") String type, @Body MultipleAddProductFinal itemAddModel);

	@GET("items/getbarcode")
	Call<String> getBarcode();

	@POST("items/listbarcodeitem")
	@FormUrlEncoded
	Call<List<ItemModel>> searchItemByBarcode(@Field("barcode") String barcode);

	@Headers("Content-Type: application/json")
	@POST("items/additem/-1")
	Call<List<ItemModel>> addItem(@Body String jsonString);

	@Headers("Content-Type: application/json")
	@POST("items/add_custom_size")
	Call<String> addSize(@Body String jsonString);

	@FormUrlEncoded
	@POST("items/getItemsBySearch")
	Call<ProductSearchModel> getItemsByNameSearch(@Field("search") String search,
												  @Field("type") String type);

	@FormUrlEncoded
	@POST("items/getItemDetailsByItemId")
	Call<ItemsListModel> getItemsByID(@Field("itemId") String itemid);
*/
}
