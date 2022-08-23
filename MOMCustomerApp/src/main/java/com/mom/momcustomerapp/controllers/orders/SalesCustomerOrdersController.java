package com.mom.momcustomerapp.controllers.orders;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.JsonToken;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrder;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrdersResp;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mswipetech.sdk.network.NetworkManager;
import com.mswipetech.sdk.network.util.Logs;

import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;


/**
 * Created by mswipe on 13-Jun-17.
 */

public class SalesCustomerOrdersController extends NetworkManager
{

    /*The callback lisnter for the merchant login details */

    private MOMNetworkResponseListener mMOMNetworkResponseListener = null;
    private Context mContext = null;

    public SalesCustomerOrdersController(Context context,
                                   MOMNetworkResponseListener mMOMNetworkResponseListener)
    {
        super(context);
        this.mContext = context;
        this.mMOMNetworkResponseListener = mMOMNetworkResponseListener;
    }


    public void getSalesCustPendingOrders(final int currentPage, String searchQuery) throws Exception
    {
        isPost = true;
        mContentType = ContentType.FORM_URLENCODED;
        mHeaderMessageParams.put("Authorization", "Bearer " +  MOMApplication.getInstance().getToken());

        /*JSONObject requestData = new JSONObject();
        requestData.put("username", userName);
        requestData.put("password", pwd);
        requestData.put("app_version", app_version);
        requestData.put("login_source", "mobile");
        */
        String store_id = MOMApplication.getInstance().getStoreId();
        String vendor_id = MOMApplication.getInstance().getVender();
        String person_id = MOMApplication.getInstance().getPersonId();
        String username = MOMApplication.getInstance().getMswipeUsername();



        mPostData = "vendor_id=" + vendor_id + "&person_id=" + person_id + "&store_id=" + store_id +
                "&user_name=" + username + "&page=" + currentPage + "&search=" + searchQuery;
        mRequestUrl = MOMApplication.getInstance().getServerUrl() + "cust/getSalesCustPendingOrders";

        start();


    }

    public void getSalesCustReturnedOrders(final int currentPage, String searchQuery) throws Exception
    {
        isPost = true;
        mContentType = ContentType.FORM_URLENCODED;
        mHeaderMessageParams.put("Authorization", "Bearer " +  MOMApplication.getInstance().getToken());

        /*JSONObject requestData = new JSONObject();
        requestData.put("username", userName);
        requestData.put("password", pwd);
        requestData.put("app_version", app_version);
        requestData.put("login_source", "mobile");
        */
        String store_id = MOMApplication.getInstance().getStoreId();
        String vendor_id = MOMApplication.getInstance().getVender();
        String person_id = MOMApplication.getInstance().getPersonId();
        String username = MOMApplication.getInstance().getMswipeUsername();



        mPostData = "vendor_id=" + vendor_id + "&person_id=" + person_id + "&store_id=" + store_id +
                "&user_name=" + username + "&page=" + currentPage + "&search=" + searchQuery;
        mRequestUrl = MOMApplication.getInstance().getServerUrl() + "cust/getSalesCustRetunredOrders";
        start();

    }

    public void getSalesCustCompletedOrders(final int currentPage, String searchQuery) throws Exception
    {
        isPost = true;
        mContentType = ContentType.FORM_URLENCODED;
        mHeaderMessageParams.put("Authorization", "Bearer " +  MOMApplication.getInstance().getToken());

        /*JSONObject requestData = new JSONObject();
        requestData.put("username", userName);
        requestData.put("password", pwd);
        requestData.put("app_version", app_version);
        requestData.put("login_source", "mobile");
        */
        String store_id = MOMApplication.getInstance().getStoreId();
        String vendor_id = MOMApplication.getInstance().getVender();
        String person_id = MOMApplication.getInstance().getPersonId();
        String username = MOMApplication.getInstance().getMswipeUsername();



        mPostData = "vendor_id=" + vendor_id + "&person_id=" + person_id + "&store_id=" + store_id +
                "&user_name=" + username + "&page=" + currentPage + "&search=" + searchQuery;
        mRequestUrl = MOMApplication.getInstance().getServerUrl() + "cust/getSalesCustCompletedOrders";
        start();

    }


    @Override
    public void parse(String httpResponse, String errMsg, int statusCode)
    {
        SalesCustOrdersResp oLoginCustomerResp = new SalesCustOrdersResp();

        if (statusCode == 200)
        {
            try {

                if(httpResponse == null)
                {
                    errMsg = this.mContext.getString(R.string.invalid_server_response);
                }
                else {

                    errMsg = processJSONResponseMessage(httpResponse, oLoginCustomerResp);


                }

            }
            catch(Exception ex)
            {
                errMsg = this.mContext.getString(R.string.invalid_server_response);
            }

            if(errMsg.length() == 0)
            {
                if(oLoginCustomerResp.isValidResponse == 0) {
                    oLoginCustomerResp.status = 0;
                    oLoginCustomerResp.statusmsg = this.mContext.getString(R.string.invalid_server_response);

                } else {

                }

            }else {
                oLoginCustomerResp.status = 0;
                oLoginCustomerResp.statusmsg = errMsg;

            }
        }
        else {
            oLoginCustomerResp.statuscode = statusCode;
            oLoginCustomerResp.statusmsg = errMsg;
            oLoginCustomerResp.status = 0;

        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mMOMNetworkResponseListener.onReponseData(oLoginCustomerResp);
            }
        });

    }


    public String processJSONResponseMessage(String httpResponse,  SalesCustOrdersResp oLoginCustomerResp) throws Exception
    {

        String errMsg="";
        JsonReader reader = null;
        boolean isDataTagFound = false;
        try
        {
            reader = new JsonReader(new StringReader(httpResponse));

            while (true)
            {
                    JsonToken token = reader.peek();

                    if(token == JsonToken.BEGIN_OBJECT) {
                        reader.beginObject();
                    }else if(token == JsonToken.END_OBJECT){
                        reader.endObject();
                    }else if(token == JsonToken.BEGIN_ARRAY) {
                        reader.beginArray();
                    }else if(token == JsonToken.END_ARRAY) {
                        reader.endArray();
                    }
                    else if(token == JsonToken.NAME)
                    {
                        String key = reader.nextName();
                        if(reader.peek()== JsonToken.NULL){
                            reader.skipValue();
                        }else
                        if(oLoginCustomerResp.isValidResponse == 0 && key.toLowerCase().equals("status"))
                        {
                            oLoginCustomerResp.isValidResponse = 1;
                            oLoginCustomerResp.status = reader.nextInt();

                        } else if(oLoginCustomerResp.isValidResponse == 1){
                            if(key.toLowerCase().equals("message"))
                            {
                                oLoginCustomerResp.statusmsg = reader.nextString();
                            }
                            else if(key.toLowerCase().equals("statuscode"))
                            {
                                oLoginCustomerResp.statuscode = reader.nextInt();

                            }
                            else if(key.equalsIgnoreCase("current_page"))
                            {
                                oLoginCustomerResp.current_page = reader.nextInt();
                            }
                            else if(key.equalsIgnoreCase("total_records"))
                            {
                                oLoginCustomerResp.total_records = reader.nextInt();

                            }
                            else if(key.equalsIgnoreCase("data"))
                            {
                                isDataTagFound = true;
                                jsonReadArrayData(reader, oLoginCustomerResp);




                            }else {
                                reader.skipValue();
                            }

                        }

                        //str.append("\"" + reader.nextName() + "\"");
                    /*case STRING:
                        //str.append("\"" + reader.nextString() + "\"");

                        break;
                    case BOOLEAN:
                        //str.append(reader.nextBoolean());
                        break;
                    case NULL:
                        reader.nextNull();
                        //str.append("null\n");
                        break;
                    case NUMBER:
                        str.append(reader.nextLong());
                        str.append("\n");
                        //break;*/
                    }else if(token == JsonToken.END_DOCUMENT) {
                        break;
                    }else {
                        errMsg = this.mContext.getString(R.string.invalid_server_response);
                        break;
                    }


            }


        }
        catch (Exception e)
        {
            throw e;
        }
        finally {
            if(reader != null)
                reader.close();
        }

        return errMsg;

    }


    public void jsonReadArrayData(JsonReader reader, SalesCustOrdersResp oLoginCustomerResp) throws Exception
    {
        try
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                reader.beginObject();
                boolean isobjectfound = false;
                SalesCustOrder oSalesCustOrders = new SalesCustOrder();
                while (reader.hasNext())
                {
                    isobjectfound = true;
                    final String name = reader.nextName();
                    if(reader.peek()== JsonToken.NULL){
                        reader.skipValue();
                    }else
                    if (name.equalsIgnoreCase("sale_id")) {
                        oSalesCustOrders.sale_id = reader.nextString();


                    } else if(name.equalsIgnoreCase("sale_time")){
                        oSalesCustOrders.sale_time = reader.nextString();
                    } else if(name.equalsIgnoreCase("customer_id")){
                        oSalesCustOrders.customer_id = reader.nextString();
                    } else if(name.equalsIgnoreCase("employee_id")){
                        oSalesCustOrders.employee_id = reader.nextString();

                    } else if(name.equalsIgnoreCase("invoice_number")){
                        oSalesCustOrders.invoice_number = reader.nextString();
                    } else if(name.equalsIgnoreCase("delivery_date")){
                        oSalesCustOrders.delivery_date = reader.nextString();
                    } else if(name.equalsIgnoreCase("delivered_date")){
                        oSalesCustOrders.delivered_date = reader.nextString();
                    } else if(name.equalsIgnoreCase("order_status")){
                        oSalesCustOrders.order_status = reader.nextString();

                    } else if(name.equalsIgnoreCase("delivery_status")){
                        oSalesCustOrders.delivery_status = reader.nextString();
                    } else if(name.equalsIgnoreCase("total_price")){
                        oSalesCustOrders.total_price= reader.nextString();
                    } else if(name.equalsIgnoreCase("max_price")){
                        oSalesCustOrders.max_price= reader.nextString();
                    } else if(name.equalsIgnoreCase("customerName")){
                        oSalesCustOrders.customerName= reader.nextString();
                    } else if(name.equalsIgnoreCase("customerPhone")){

                        oSalesCustOrders.customerPhone= reader.nextString();
                    } else if(name.equalsIgnoreCase("customerCarePhone")){
                        oSalesCustOrders.customerCarePhone= reader.nextString();
                    } else if(name.equalsIgnoreCase("total_quantity")){
                        oSalesCustOrders.total_quantity= reader.nextString();
                    } else if(name.equalsIgnoreCase("payment_type")){
                        oSalesCustOrders.payment_type= reader.nextString();
                    } else if(name.equalsIgnoreCase("address_1")){
                        oSalesCustOrders.address_1= reader.nextString();
                    } else if(name.equalsIgnoreCase("address_2")){
                        oSalesCustOrders.address_2= reader.nextString();
                    } else if(name.equalsIgnoreCase("city")){
                        oSalesCustOrders.city= reader.nextString();
                    } else if(name.equalsIgnoreCase("state")){
                        oSalesCustOrders.state= reader.nextString();
                    } else if(name.equalsIgnoreCase("zip")){
                        oSalesCustOrders.zip= reader.nextString();
                    } else if(name.equalsIgnoreCase("country")){
                        oSalesCustOrders.country= reader.nextString();
                    } else if(name.equalsIgnoreCase("land_mark")){
                        oSalesCustOrders.land_mark= reader.nextString();
                    } else if(name.equalsIgnoreCase("count")){
                        oSalesCustOrders.count= reader.nextString();
                    } else if(name.equalsIgnoreCase("status")){
                        oSalesCustOrders.status= reader.nextInt();

                    } else {

                        reader.skipValue();
                    }

                }
                if(isobjectfound)
                {
                    oLoginCustomerResp.data.add(oSalesCustOrders);
                }
                reader.endObject();
            }

            reader.endArray();
        }catch (Exception ex) {
            throw ex;
        }
    }



}
