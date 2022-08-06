package com.mswipetech.sdk.network;

//import android.content.Context;
//import android.os.Build;
//
//
//import com.mswipetech.sdk.network.data.ApplicationData;
//import com.mswipetech.sdk.network.util.Constants;
//import com.mswipetech.sdk.network.util.Logs;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.ConnectException;
//import java.net.MalformedURLException;
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.util.concurrent.TimeUnit;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.TrustManagerFactory;
//import javax.net.ssl.X509TrustManager;
//
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.HttpException;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.Body;
//import retrofit2.http.POST;
//import retrofit2.http.Url;

public class NetworkManagerRetrofit extends Thread {}/*{

    public boolean isPost = false;
    public boolean isSoap = false;
    public boolean isREST = false;

    protected Context context;
    protected String mRequestUrl = "";
    protected String mPostData = "";
    protected String mEndPoint = "";

    public enum KeyStoreType {MSWIPETECH, MSWIPEOTASTORE, MSWIPEOTACOM, F2A2MSWIPEOTASTORE,
        MCARDAPI, MSWIPEAPI, MSWIPETECHCOIN ,APUMSWIPEOTA, MCARDS, EMIMCARDS, EMITOKEN,
        MSWIPEMOMO,  BPCL, IIFL, NONE}

    public KeyStoreType mKeyStoreType = KeyStoreType.MSWIPETECH;

    public NetworkManagerRetrofit(Context context) {
        this.context = context;
    }

    public enum ContentType {SOAP_XML, XML, JSON, OCTET_STREAM, FORM_URLENCODED, NONE}
    public ContentType mContentType = ContentType.JSON;

    @Override
    public void run()
    {
        httpRequest();
    }

    public Call<ResponseBody> postAPI(){

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

        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), mPostData);

        IApiInterface apiService = getClient().create(IApiInterface.class);
        return apiService.PostAPI(mEndPoint, requestBody);
    }

    protected void httpRequest(){


        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "mRequestUrl: " + mRequestUrl + "" + mEndPoint, true, true);

        Call<ResponseBody> call = postAPI();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if (response.isSuccessful() && response.code() == 200) {
                        try{
                            String responseRecieved = response.body().string();


                            if (ApplicationData.IS_DEBUGGING_ON)
                                Logs.v(ApplicationData.packName, "responseRecieved: " + responseRecieved, true, true);


                            parse(responseRecieved, "", ""+response.code());

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {

                        parse("", "Internal server error", ""+response.code());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "getMessage: " + t.getMessage(), true, true);

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.e(ApplicationData.packName, (HttpException) t, true, true);

                handleException(t);
                //parse("", t.getMessage(), "0");

            }
        });
    }


    private Retrofit getClient(){

        Retrofit retrofit = null;

        retrofit = new Retrofit.Builder()
                .baseUrl(mRequestUrl)
                .client(getOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    private OkHttpClient.Builder getOkHttpClient()
    {
        try {


            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            sslContext = getSecureConnection();

            if(sslContext != null)
            {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    sslSocketFactory = new TLSSocketFactory(sslContext.getSocketFactory());
                } else {
                    sslSocketFactory = sslContext.getSocketFactory();
                }
            }

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            
            builder.connectTimeout(ApplicationData.SOCKET_CONNECITON_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.readTimeout(ApplicationData.SOCKET_READ_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.writeTimeout(ApplicationData.SOCKET_READ_TIMEOUT, TimeUnit.MILLISECONDS);

            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();


                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName, "request: " + request.toString(), true, true);

                    return chain.proceed(request);
                }
            });

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName, "hostname: " + hostname, true, true);

                    return true;
                }
            });

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface IApiInterface {

        @POST()
        Call<ResponseBody> PostAPI(@Url String url, @Body RequestBody body);

    }

    protected void parse(String text, String errMsg, String errCode) {
    }

    private void handleException(Throwable e){

        String error = "";
        int networkErrCode = 0;
        StringBuffer ERROR_MESSAGE = new StringBuffer();

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "getLocalizedMessage " + e.getLocalizedMessage().toLowerCase(), true, true);

        if(e instanceof MalformedURLException) {
            e.printStackTrace();

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + errorMessage);
        }
		else if(e instanceof UnknownHostException) {

            *//* this exception is triggere either if the internet is not available(ie wiif and mobile data are turned off) or if its availabe the network is not able to resolve the mswipe domain
             * this below check will handle this two instances *//*
            if(!Constants.checkInternetConnection(context))
            {

                networkErrCode = 801;
                ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) +  " (" + networkErrCode + ")" + ", " +
                        Constants.getString(context, Constants.INTERNET_NOT_AVALIABLE));
            }
            else {

                networkErrCode = 805;
                ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) +  " (" + networkErrCode + ")" + ", " +
                        Constants.getString(context, Constants.UNABLE_TO_RESOLVE_MSWIPE_SERVER));

            }
            e.printStackTrace();
        }
        else if (e instanceof SocketTimeoutException){

            e.printStackTrace();

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            if(e.getLocalizedMessage() !=null && e.getLocalizedMessage().toLowerCase().contains("read timed out"))
            {
                networkErrCode = 804;
                ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);
            }
            else{
                networkErrCode = 803;
                ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);
            }
        }
        else if(e instanceof ConnectException){

            *//* this exception is triggere either if the internet is not available(ie wiif and mobile data are turned off) or if its availabe the network this the mswipe server
                     is down, this below check will handle this two instances *//*

            if(!Constants.checkInternetConnection(context))
            {

                networkErrCode = 801;

                ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")"  + ", " +
                        Constants.getString(context, Constants.INTERNET_NOT_AVALIABLE));


            }
            else {

                networkErrCode = 802;
                ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")"  + ", " +
                        Constants.getString(context, Constants.UNABLE_TO_CONNECT_MSWIPE_SERVER));
                e.printStackTrace();
            }

        }
        else if (e instanceof IOException) {

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            networkErrCode = 701;
            ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" +  errorMessage);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.e(ApplicationData.packName, (Exception) e, true, true);


            e.printStackTrace();

        }
		else if(e instanceof Exception) {

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            networkErrCode = 700;
            ERROR_MESSAGE.append(Constants.getString(context, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);
            e.printStackTrace();

        }

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "Network Manager : The network error is " + ERROR_MESSAGE.toString(), true, true);

        String mstConnectionErrDesc =  ERROR_MESSAGE.toString();

        if(networkErrCode == 801){

            mstConnectionErrDesc = String.format(Constants.getString(context, Constants.INTERNET_NOT_AVALIABLE), networkErrCode+"");
        }
        else if(networkErrCode == 802){

            mstConnectionErrDesc = String.format(Constants.getString(context, Constants.UNABLE_TO_CONNECT_MSWIPE_SERVER), networkErrCode+"");
        }
        else if(networkErrCode == 803){

            mstConnectionErrDesc = String.format(Constants.getString(context, Constants.CONNECTION_TIMEDOUT), networkErrCode+"");
        }
        else if(networkErrCode == 804){

            mstConnectionErrDesc = String.format(Constants.getString(context, Constants.READ_TIMEDOUT), networkErrCode+"");
        }
        else if(networkErrCode == 805){

            mstConnectionErrDesc = String.format(Constants.getString(context, Constants.UNABLE_TO_RESOLVE_MSWIPE_SERVER), networkErrCode+"");
        }
        parse("", mstConnectionErrDesc, networkErrCode+"");

    }
    
    private SSLContext getSecureConnection() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException, UnrecoverableKeyException {


        SSLContext sslContext = null;

        *//*
        for the aar as it keeps all the raw folder resources so we can direcltly
        read form the resource
        *//*

        if (mKeyStoreType == KeyStoreType.MSWIPETECH)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipetech);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEAPI)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipetech);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MCARDAPI)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mcardapi);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEOTASTORE)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEOTACOM)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipeotacomnew);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.F2A2MSWIPEOTASTORE)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.f2a2mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPETECHCOIN)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipetechcoin);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.APUMSWIPEOTA)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MCARDS)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mcards);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.EMIMCARDS)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.EMITOKEN)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipeotastore);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEMOMO)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipemomo);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.MSWIPEMOMO)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.mswipemomo);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.IIFL)
        {
            KeyStore keyStore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.iiflin);
            keyStore.load(in, "mswipe".toCharArray());
            in.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        }
        else if (mKeyStoreType == KeyStoreType.BPCL)
        {
            InputStream is = context.getResources().openRawResource(R.raw.bpclcert);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(is, "mswipe".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(keyStore, "mswipe".toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, null, null);
        }

        return sslContext;
    }

}*/