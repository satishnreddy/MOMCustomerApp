package com.mom.momcustomerapp.controllers.login;

import android.content.Context;
import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonToken;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.login.model.LoginCustomerResp;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mswipetech.sdk.network.NetworkManager;

import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;


/**
 * Created by mswipe on 13-Jun-17.
 */

public class LoginCustomerController extends NetworkManager
{

    /*The callback lisnter for the merchant login details */

    private MOMNetworkResponseListener mMOMNetworkResponseListener = null;
    private Context mContext = null;
    int iParseType;


    public LoginCustomerController(Context context,
                                   MOMNetworkResponseListener mMOMNetworkResponseListener)
    {
        super(context);
        this.mContext = context;
        this.mMOMNetworkResponseListener = mMOMNetworkResponseListener;
    }


    public void loginCustomer(String userName, String pwd, String app_version) throws Exception
    {
        isPost = true;
        mContentType = ContentType.FORM_URLENCODED;
        iParseType = 0;
        /*JSONObject requestData = new JSONObject();
        requestData.put("username", userName);
        requestData.put("password", pwd);
        requestData.put("app_version", app_version);
        requestData.put("login_source", "mobile");
        */

        mPostData = "username=" + userName + "&password=" + pwd + "&app_version=" + app_version +
            "&login_source=" + "mobile";
        mRequestUrl = MOMApplication.getInstance().getServerUrl() + "cust/validateCustomerLogin";


        start();


    }

    public void getCustWallet() throws Exception
    {
        isPost = true;
        mContentType = ContentType.FORM_URLENCODED;
        mHeaderMessageParams.put("Authorization", "Bearer " +  MOMApplication.getInstance().getToken());

        iParseType = 1;

        String store_id = MOMApplication.getInstance().getStoreId();
        String vendor_id = MOMApplication.getInstance().getVender();
        String person_id = MOMApplication.getInstance().getPersonId();
        String username = MOMApplication.getInstance().getMswipeUsername();

        mPostData = "vendor_id=" + vendor_id + "&person_id=" + person_id + "&store_id=" + store_id +
                "&user_name=" + username ;
        mRequestUrl = MOMApplication.getInstance().getServerUrl() + "cust_sales/getCustomerWalletBalance";


        start();


    }



    @Override
    public void parse(String httpResponse, String errMsg, int statusCode)
    {
        LoginCustomerResp oLoginCustomerResp = new LoginCustomerResp();
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
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                mMOMNetworkResponseListener.onReponseData(oLoginCustomerResp);
            }
        });

    }


    public String processJSONResponseMessage(String httpResponse,  LoginCustomerResp oLoginCustomerResp) throws Exception
    {

        String errMsg="";
        JsonReader reader = null;
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
                        if(oLoginCustomerResp.isValidResponse == 0 && key.toLowerCase().equals("status"))
                        {
                            oLoginCustomerResp.isValidResponse = 1;
                            oLoginCustomerResp.status = reader.nextInt();

                        }
                        else if(iParseType ==0 && oLoginCustomerResp.isValidResponse == 1)
                        {
                            if(key.toLowerCase().equals("message"))
                            {
                                oLoginCustomerResp.statusmsg = reader.nextString();
                            }
                            else if(key.toLowerCase().equals("statuscode"))
                            {
                                oLoginCustomerResp.statuscode = reader.nextInt();

                            }
                            else if(key.toLowerCase().equals("token"))
                            {
                                oLoginCustomerResp.token = reader.nextString();
                            }
                            else if(key.toLowerCase().equals("vendor_id")){
                                oLoginCustomerResp.vendor_id = reader.nextString();
                            }else if(key.toLowerCase().equals("person_id")){
                                oLoginCustomerResp.person_id = reader.nextString();}
                            else if(key.toLowerCase().equals("store_id")){
                                oLoginCustomerResp.store_id = reader.nextString();
                            }else if(key.toLowerCase().equals("name")){
                                oLoginCustomerResp.cust_name = reader.nextString();

                            }else if(key.toLowerCase().equals("phone_number")){
                                oLoginCustomerResp.customerPhone = reader.nextString();


                            }else if(key.toLowerCase().equals("email")){
                                oLoginCustomerResp.email = reader.nextString();
                            }else if(key.toLowerCase().equals("address_1")){
                                oLoginCustomerResp.address_1 = reader.nextString();
                            }else if(key.toLowerCase().equals("address_2")){
                                oLoginCustomerResp.address_2 = reader.nextString();
                            }else if(key.toLowerCase().equals("city")){
                                oLoginCustomerResp.city = reader.nextString();
                            }else if(key.toLowerCase().equals("state")){
                                oLoginCustomerResp.state = reader.nextString();
                            }else if(key.toLowerCase().equals("zip")){
                                oLoginCustomerResp.zip = reader.nextString();
                            }else if(key.toLowerCase().equals("country")){
                                oLoginCustomerResp.country = reader.nextString();
                            }else if(key.toLowerCase().equals("land_mark")){
                                oLoginCustomerResp.land_mark = reader.nextString();
                            }else if(key.toLowerCase().equals("delivery_type")){
                                oLoginCustomerResp.delivery_type = reader.nextInt();

                            }else {
                                reader.skipValue();
                            }

                        }
                        else if(iParseType ==1 && oLoginCustomerResp.isValidResponse == 1) {
                            if (key.toLowerCase().equals("message")) {
                                oLoginCustomerResp.statusmsg = reader.nextString();
                            } else if (key.toLowerCase().equals("statuscode")) {
                                oLoginCustomerResp.statuscode = reader.nextInt();

                            } else if (key.toLowerCase().equals("wallet _amt")) {
                                oLoginCustomerResp.waller_amt = reader.nextDouble();

                            } else {
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



}
