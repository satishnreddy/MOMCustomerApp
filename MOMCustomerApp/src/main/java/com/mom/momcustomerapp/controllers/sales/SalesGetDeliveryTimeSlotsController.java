package com.mom.momcustomerapp.controllers.sales;

import android.content.Context;
import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonToken;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.sales.models.SalesDeliveryTimeSlotsResp;
import com.mom.momcustomerapp.controllers.sales.models.TimeSlots;
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

public class SalesGetDeliveryTimeSlotsController extends NetworkManager
{

    /*The callback lisnter for the merchant login details */

    private MOMNetworkResponseListener mMOMNetworkResponseListener = null;
    private Context mContext = null;
    int iParseType;

    public SalesGetDeliveryTimeSlotsController(Context context,
                                   MOMNetworkResponseListener mMOMNetworkResponseListener)
    {
        super(context);
        this.mContext = context;
        this.mMOMNetworkResponseListener = mMOMNetworkResponseListener;
    }

    public void getDeliveryTimeSlots( final String delivery_type) throws Exception
    {
        isPost = true;
        mContentType = ContentType.FORM_URLENCODED;
        mHeaderMessageParams.put("Authorization", "Bearer " +  MOMApplication.getInstance().getToken());


        String store_id = MOMApplication.getInstance().getStoreId();
        String vendor_id = MOMApplication.getInstance().getVender();
        String person_id = MOMApplication.getInstance().getPersonId();
        String username = MOMApplication.getInstance().getMswipeUsername();


        mPostData = "vendor_id=" + vendor_id + "&person_id=" + person_id + "&store_id=" + store_id +
                "&user_name=" + username + "&delivery_type=" + delivery_type;
        mRequestUrl = MOMApplication.getInstance().getServerUrl() + "cust_sales/getDeliveryTimeSlots";


        start();


    }




    @Override
    public void parse(String httpResponse, String errMsg, int statusCode)
    {
        SalesDeliveryTimeSlotsResp oLoginCustomerResp = new SalesDeliveryTimeSlotsResp();

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

        Handler handler = new Handler(mContext.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mMOMNetworkResponseListener.onReponseData(oLoginCustomerResp);
            }
        });

    }


    public String processJSONResponseMessage(String httpResponse,  SalesDeliveryTimeSlotsResp oLoginCustomerResp) throws Exception
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
                            else if(key.equalsIgnoreCase("current_date")) {
                                oLoginCustomerResp.current_date = reader.nextString();

                            }
                            else if(key.equalsIgnoreCase("data"))
                            {
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

    public void jsonReadArrayData(JsonReader reader,  SalesDeliveryTimeSlotsResp oLoginCustomerResp) throws Exception
    {
        try
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                reader.beginObject();
                boolean isobjectfound = false;
                TimeSlots oSalesCustOrders = new TimeSlots();
                while (reader.hasNext())
                {
                    isobjectfound = true;
                    final String name = reader.nextName();
                    if(reader.peek()== JsonToken.NULL)
                    {
                        reader.skipValue();
                    }
                    else if (name.equalsIgnoreCase("fromSlot")) {
                        oSalesCustOrders.fromSlot = reader.nextString();
                     } else if(name.equalsIgnoreCase("toSlot")){
                        oSalesCustOrders.toSlot = reader.nextString();
                    } else if(name.equalsIgnoreCase("weekDaysSelected")){
                        oSalesCustOrders.weekDaysSelected = reader.nextString();
                    } else if(name.equalsIgnoreCase("sr_no")){
                        oSalesCustOrders.sr_no = reader.nextString();

                    } else {

                        reader.skipValue();
                    }

                }
                if(isobjectfound)
                {
                    oLoginCustomerResp.timeSlots.add(oSalesCustOrders);
                }
                reader.endObject();
            }

            reader.endArray();
        }catch (Exception ex) {
            throw ex;
        }
    }

}
