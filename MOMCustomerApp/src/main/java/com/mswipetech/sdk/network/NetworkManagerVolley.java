package com.mswipetech.sdk.network;

//import android.content.Context;
//import android.os.Build;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkResponse;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.HttpHeaderParser;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.mswipetech.sdk.network.data.ApplicationData;
//import com.mswipetech.sdk.network.util.Constants;
//import com.mswipetech.sdk.network.util.Logs;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.ConnectException;
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Map;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManagerFactory;


public class NetworkManagerVolley extends Thread{}
/*{

    public boolean isPost = true;
    public boolean isSoap = false;
    public boolean isREST = false;
    public boolean isMultiPart = false;
    public boolean isGET = false;
    public boolean isPUT = false;
    public boolean isFormData = false;
    public boolean isKnownErrorMode = false;

    public String mRequestUrl = "";
    public String mPostData = "";
    public byte[] mPostByteData = null;

    public enum ContentType {SOAP_XML, XML, JSON, OCTET_STREAM, FORM_URLENCODED, NONE}
    public ContentType mContentType = ContentType.JSON;

    public enum KeyStoreType {MSWIPETECH, MSWIPEOTASTORE, MSWIPEOTACOM, F2A2MSWIPEOTASTORE,
        MCARDAPI, MSWIPEAPI, MSWIPETECHCOIN ,APUMSWIPEOTA, MCARDS, EMIMCARDS, EMITOKEN,
        MSWIPEMOMO,  BPCL, IIFL, NONE}

    public KeyStoreType mKeyStoreType = KeyStoreType.MSWIPETECH;

    public Hashtable<String, String> mParamsForFormData = new Hashtable<>();
    public Hashtable<String, VolleyMultipartRequest.DataPart> mParamsImageData = new Hashtable<>();

    public Hashtable<String, String> mHeaderMessageParams = new Hashtable<>();;
    public Hashtable<String, String> mUploadMessageParams = new Hashtable<>();

    public Context mContext = null;

    public NetworkManagerVolley(Context context) {
        this.mContext = context;
    }

    @Override
    public void run()
    {
        if(isMultiPart)
        {
            httpMultipartRequest();
        }
        else{
            httpRequest();
        }
    }


    public void httpRequest() {

        try {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "mRequestUrl: " + mRequestUrl, true, true);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "mRequestData: " + mPostData, true, true);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "isPost: " + isPost, true, true);

            Logs.d(ApplicationData.packName,  "RequestUrl => " + mRequestUrl, true, true);

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            int requestType = Request.Method.GET;

            if(isPost){
                requestType = Request.Method.POST;
            }

            SSLContext sslContext = getSecureConnection();

            if(sslContext != null)
            {
                SSLSocketFactory noSSLv3Factory = null;

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    noSSLv3Factory = new TLSSocketFactory(sslContext.getSocketFactory());
                } else {
                    noSSLv3Factory = sslContext.getSocketFactory();
                }

                HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);
            }

            StringRequest stringRequest = new StringRequest(requestType, mRequestUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //TODO: handle success

                    //Logs.d(ApplicationData.packName,  "Response received ", true, true);


                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "response: " + response.toString(), true, true);

                    parse(response, "", "0");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //TODO: handle failure

                    //Logs.d(ApplicationData.packName,  "Response error received ", true, true);

                    NetworkResponse networkResponse = error.networkResponse;

                    if(networkResponse != null)
                    {
                        if (ApplicationData.IS_DEBUGGING_ON)
                            Logs.v(ApplicationData.packName, "networkResponse: " + networkResponse.statusCode, true, true);
                    }

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "error: " + error.getMessage(), true, true);

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.e(ApplicationData.packName,  error, true, true);

                    Logs.e(ApplicationData.packName,  error, true, true);


                    if (error != null && error.getLocalizedMessage() != null){
                        Logs.d(ApplicationData.packName,  "LocalizedMessage => " + error.getLocalizedMessage(), true, true);
                    }

                    handleException(error);
                }
            }) {

                @Override
                public String getBodyContentType() {
                    //return "application/json; charset=utf-8";

                    String contentType = "";

                    if(mContentType == ContentType.JSON) {
                        contentType = "application/json";
                    } else if(mContentType == ContentType.FORM_URLENCODED) {
                        contentType = "application/x-www-form-urlencoded";
                    } else if(mContentType == ContentType.XML) {
                        contentType = "application/xml";
                    } else if(mContentType == ContentType.OCTET_STREAM) {
                        contentType = "application/octet-stream";
                    }

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "contentType: " + contentType, true, true);

                    return contentType;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {

                        //Logs.d(ApplicationData.packName,  "Request sent ", true, true);


                        if (ApplicationData.IS_DEBUGGING_ON)
                            Logs.v(ApplicationData.packName,  "getBody", true, true);

                        byte[] responseBytes;

                        if(mPostByteData != null){

                            if (ApplicationData.IS_DEBUGGING_ON)
                                Logs.v(ApplicationData.packName,  "mPostByteData: " + mPostByteData, true, true);

                            responseBytes = mPostByteData;
                        }
                        else{

                            if (ApplicationData.IS_DEBUGGING_ON)
                                Logs.v(ApplicationData.packName,  "mPostData: " + mPostData, true, true);

                            responseBytes = mPostData.getBytes("UTF-8");
                        }


                        Logs.d(ApplicationData.packName,  "PostData => " + mPostData, true, true);

                        Logs.d(ApplicationData.packName,  "Content length => " + responseBytes.length, true, true);

                        return responseBytes;

                    } catch (UnsupportedEncodingException uee) {

                        if (ApplicationData.IS_DEBUGGING_ON)
                            Logs.v(ApplicationData.packName,  "Unsupported Encoding while trying to get the bytes of " + mPostData + " using utf-8", true, true);

                        return null;
                    }
                }

                @Override
                protected Response parseNetworkResponse(NetworkResponse response) {

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "NetworkResponse ", true, true);

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "NetworkResponse " + response.statusCode, true, true);

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "NetworkResponse " + response.headers, true, true);

                    try {

                        String object = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        if (ApplicationData.IS_DEBUGGING_ON)
                            Logs.v(ApplicationData.packName,  "object " + object, true, true);

                        try {

                            return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));

                        }catch (Exception ex) {
                            return Response.error(new ParseError(ex));
                        }
                    }catch (UnsupportedEncodingException e) {

                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    if(mContentType == ContentType.JSON) {
                        params.put("Content-Type", "application/json");
                    } else if(mContentType == ContentType.FORM_URLENCODED) {
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                    } else if(mContentType == ContentType.XML) {
                        params.put("Content-Type", "application/xml");
                    } else if(mContentType == ContentType.OCTET_STREAM) {
                        params.put("Content-Type", "application/octet-stream");
                    }

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "params: " + params, true, true);

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    ApplicationData.SOCKET_READ_TIMEOUT,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void httpMultipartRequest()
    {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "mRequestUrl: " + mRequestUrl, true, true);

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "mRequestData: " + mPostData, true, true);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, mRequestUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            String responseData = new String(response.data);

                            if (ApplicationData.IS_DEBUGGING_ON)
                                Logs.v(ApplicationData.packName,  "responseData: " + responseData, true, true);

                            parse(responseData, "", "0");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (ApplicationData.IS_DEBUGGING_ON)
                            Logs.v(ApplicationData.packName,  "error: " + error.getMessage(), true, true);

                        handleException(error);

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName,  "params: " + params, true, true);


                *//*try {
                    String token = AppSharedPrefrences.getAppSharedPrefrencesInstace().getAWSToken();
                    if(token != null && !token.isEmpty()){
                        //params.put("Authorization", "Bearer "+token);
                        params.put("Authorization", token);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }*//*


                return params;
            }

            *//*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * *//*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParamsForFormData;
            }

            *//*
             * Here we are passing image by renaming it with a unique name
             * *//*
            @Override
            protected Map<String, DataPart> getByteData() {
                // params.put("pic", new DataPart(imagename + ".png",""));
                return mParamsImageData;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationData.SOCKET_READ_TIMEOUT,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //adding the request to volley
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest);
    }

    protected void parse(String response, String errMsg, String errCode) {}

    private void handleException(Exception e){


        String error = "";
        int networkErrCode = 0;
        StringBuffer ERROR_MESSAGE = new StringBuffer();

        if (e instanceof UnknownHostException) {

            *//* this exception is triggere either if the internet is not available(ie wiif and mobile data are turned off) or if its availabe the network is not able to resolve the mswipe domain
             * this below check will handle this two instances *//*
            if (!Constants.checkInternetConnection(mContext)) {
                networkErrCode = 801;
            } else {
                networkErrCode = 805;
            }
        } else if (e instanceof SocketTimeoutException) {

            if (e.getLocalizedMessage() != null && e.getLocalizedMessage().toLowerCase().contains("read timed out")) {
                networkErrCode = 804;
            } else {
                networkErrCode = 803;
            }
        } else if (e instanceof TimeoutError) {

            networkErrCode = 804;

        } else if (e instanceof ConnectException) {

            *//* this exception is triggere either if the internet is not available(ie wiif and mobile data are turned off) or if its availabe the network this the mswipe server
                     is down, this below check will handle this two instances *//*

            if (!Constants.checkInternetConnection(mContext)) {
                networkErrCode = 801;
            } else {
                networkErrCode = 802;
            }
        } else if (e instanceof IOException) {

            networkErrCode = 701;
        } else if (e instanceof Exception) {

            networkErrCode = 700;
        }


        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "getLocalizedMessage " + e.getLocalizedMessage(), true, true);

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "getMessage " + e.getMessage(), true, true);


        String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());
        ERROR_MESSAGE.append(Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "Network Manager : The network error is " + ERROR_MESSAGE.toString(), true, true);

        String mstConnectionErrDesc =  ERROR_MESSAGE.toString();

        if(networkErrCode == 801){

            mstConnectionErrDesc = String.format(Constants.getString(mContext, Constants.INTERNET_NOT_AVALIABLE), networkErrCode+"");
        }
        else if(networkErrCode == 802){

            mstConnectionErrDesc = String.format(Constants.getString(mContext, Constants.UNABLE_TO_CONNECT_MSWIPE_SERVER), networkErrCode+"");
        }
        else if(networkErrCode == 803){

            mstConnectionErrDesc = String.format(Constants.getString(mContext, Constants.CONNECTION_TIMEDOUT), networkErrCode+"");
        }
        else if(networkErrCode == 804){

            mstConnectionErrDesc = String.format(Constants.getString(mContext, Constants.READ_TIMEDOUT), networkErrCode+"");
        }
        else if(networkErrCode == 805){

            mstConnectionErrDesc = String.format(Constants.getString(mContext, Constants.UNABLE_TO_RESOLVE_MSWIPE_SERVER), networkErrCode+"");
        }

        parse("", mstConnectionErrDesc, networkErrCode+"");

    }

    private SSLContext getSecureConnection() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException, UnrecoverableKeyException {


        SSLContext context = null;

        *//*
        for the aar as it keeps all the raw folder resources so we can direcltly
        read form the resource
        *//*

        if (mKeyStoreType == KeyStoreType.MSWIPETECH)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipetech);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEAPI)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipetech);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MCARDAPI)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mcardapi);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEOTASTORE)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEOTACOM)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipeotacomnew);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.F2A2MSWIPEOTASTORE)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.f2a2mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPETECHCOIN)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipetechcoin);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.APUMSWIPEOTA)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MCARDS)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mcards);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.EMIMCARDS)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.EMITOKEN)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEMOMO)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipemomo);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEMOMO)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.mswipemomo);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.IIFL)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.iiflin);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.BPCL)
        {
            InputStream is = mContext.getResources().openRawResource(R.raw.bpclcert);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(is, "mswipe".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(keyStore, "mswipe".toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            context = SSLContext.getInstance("TLS");
            context.init(keyManagers, null, null);
        }

        return context;
    }

}*/





























