package com.mswipetech.sdk.network;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.mswipetech.sdk.network.data.ApplicationData;
import com.mswipetech.sdk.network.util.Constants;
import com.mswipetech.sdk.network.util.Logs;

import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import com.mom.momcustomerapp.R;


public class NetworkManager extends Thread
{

    private String ERROR_MESSAGE;
    public boolean isPost = false;
    public String mRequestUrl = "";
    public String mPostData = "";
    public byte[] mPostByteData = null;


    public enum KeyStoreType {MSWIPETECH, MSWIPEOTASTORE, MSWIPEOTACOM, F2A2MSWIPEOTASTORE,
        MCARDAPI, MSWIPEAPI, MSWIPETECHCOIN ,APUMSWIPEOTA, MCARDS, EMIMCARDS, EMITOKEN,
        MSWIPEMOMO,  BPCL, IIFL, NONE}
    public KeyStoreType mKeyStoreType = KeyStoreType.NONE;

    public enum ContentType {SOAP_XML, XML, JSON, OCTET_STREAM, FORM_URLENCODED, NONE}
    public ContentType mContentType = ContentType.SOAP_XML;

    private Handler networkHandler = null;
    private Context mContext = null;

    public Hashtable<String, String> mHeaderMessageParams = new Hashtable<>();;
    protected int requestReadTimeOut = 60 * 1000;


    public NetworkManager(Context context) {
        this.mContext = context;

    }

    public void setNetworkHandler(Handler handler) {
        networkHandler = handler;
    }

    @Override
    public void run() {
        httpRequest();

    }

    public void httpRequest()
    {

        int networkErrCode = 0;
        StringBuffer response = new StringBuffer();

        OutputStream ost = null;
        OutputStreamWriter osw = null;
        HttpURLConnection httpConn = null;
        BufferedReader isr = null;

        try {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "url " + mRequestUrl + " => post " + isPost + " => content " + mContentType, true, true);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "request " + mPostData, true, true);

            URL url = new URL(mRequestUrl);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
            {

                System.setProperty("http.keepAlive", "false");
            }

            System.setProperty("http.keepAlive", "false");

            URLConnection urlconnection = url.openConnection();

            if (!(urlconnection instanceof URLConnection)) {
                ERROR_MESSAGE = (Constants.getString(mContext, Constants.INCORRECT_URL));


            }
            else
            {
                if (url.getProtocol().toLowerCase().equals("https"))
                {

                    if (ApplicationData.IS_DEBUGGING_ON)
                        Logs.v(ApplicationData.packName,  "The mKeyStoreType is  " + mKeyStoreType, true, true);

                    SSLContext sslContext = getSecureConnection();

                    HttpsURLConnection https = (HttpsURLConnection) urlconnection;

                    if(sslContext != null && mKeyStoreType != KeyStoreType.NONE)
                    {
                        SSLSocketFactory noSSLv3Factory = null;


                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                            noSSLv3Factory = new TLSSocketFactory(sslContext.getSocketFactory());
                        } else {
                            noSSLv3Factory = sslContext.getSocketFactory();
                        }

                        https.setSSLSocketFactory(noSSLv3Factory);
                    }

                    httpConn = https;

                }
                else
                {

                    httpConn = (HttpURLConnection) urlconnection;
                }

                httpConn.setConnectTimeout(15 * 1000);
                httpConn.setReadTimeout(requestReadTimeOut);


                //set the output to true, indicating you are outputting(uploading) POST data
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);

                if(mContentType == ContentType.OCTET_STREAM)
                {
                    httpConn.setRequestProperty("Content-Type", "application/octet-stream");

                } else if(mContentType == ContentType.FORM_URLENCODED) {

                    httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                } else if(mContentType == ContentType.XML)
                {
                    httpConn.setRequestProperty("Content-Type", "application/xml");
                }
                else {
                    httpConn.setRequestProperty("Content-Type", "application/json");
                }

                if(mHeaderMessageParams != null && mHeaderMessageParams.size() > 0)
                {

                    for (Map.Entry<String, String> entry : mHeaderMessageParams.entrySet()) {

                        String key = entry.getKey();
                        String value = entry.getValue();

                        if (ApplicationData.IS_DEBUGGING_ON)
                            Logs.v(ApplicationData.packName,  "The key value " + key +  " valye " + value , true, true);

                        httpConn.setRequestProperty(key, value);
                    }


                }

                /*think the server does not even receive a request in your case. I think setting the http.keepAlive to false is a good fix. I have spent a fair amount of time investigating this issue and it is more than obvious that this is a bug in Android library.
                 The reason is that the server does not want to keep all those connections open due to potentially large number of them so it closes some of them from time to time. However, the connection pool used in Android library does not want to accept this fact and tries using the old closed connection anyway.
                 Unsetting the http.keepAlive is a workaround rather than a solution, however you do get a reliable HTTP handling, albeit with some performance cost.
                 */
                //System.setProperty("http.keepAlive", "false");

                //byte[] responseBytes = mPostData.getBytes("UTF-8");
                byte[] responseBytes;

                if(mPostByteData != null){

                    responseBytes = mPostByteData;
                }
                else{
                    responseBytes = mPostData.getBytes("UTF-8");
                }

                //once you set the output to true, you don't really need to set the request method to post, but I'm doing it anyway
                //Android documentation suggested that you set the length of the data you are sending to the server, BUT
                // do NOT specify this length in the header by using conn.setRequestProperty("Content-Length", length);
                //use this instead.
                httpConn.setFixedLengthStreamingMode(responseBytes.length);

                ost = new BufferedOutputStream(httpConn.getOutputStream());

                ost.write(responseBytes);
                ost.flush();
                ost.close();
                ost = null;


                int resCode = httpConn.getResponseCode();
                String resMessage = httpConn.getResponseMessage();

                if (resCode == HttpURLConnection.HTTP_OK)
                {

                    isr = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    String line = "";
                    while ((line = isr.readLine()) != null)
                    {
                        response.append(line);
                    }

                    isr.close();
                    networkErrCode = resCode;

                }
                else if (resCode == 201 || resCode == 401) {

                    /**
                     * This to handle the error that we get wwhile uploading image for the
                     */

                    //response.append(httpConn.getResponseMessage());
                    networkErrCode = resCode;
                    ERROR_MESSAGE = (resMessage);

                }
                else if (resCode == 400) {

                    /**
                     * This to handle the verfication api response like myntra
                     */

                    //response.append(httpConn.getResponseMessage());

                    networkErrCode = resCode;
                    ERROR_MESSAGE = (resMessage);

                }
                else {

                    //response.append(httpConn.getResponseMessage());


                    networkErrCode = resCode;
                    ERROR_MESSAGE = (resMessage);
                }

                httpConn.disconnect();
                httpConn = null;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + errorMessage);
        }
        catch (UnknownHostException e) {

            /* this exception is triggere either if the internet is not available(ie wiif and mobile data are turned off) or if its availabe the network is not able to resolve the mswipe domain
             * this below check will handle this two instances */
            if(!Constants.checkInternetConnection(mContext))
            {

                networkErrCode = 801;
                ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) +  " (" + networkErrCode + ")" + ", " +
                        Constants.getString(mContext, Constants.INTERNET_NOT_AVALIABLE));
            }
            else {

                networkErrCode = 805;
                ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) +  " (" + networkErrCode + ")" + ", " +
                        Constants.getString(mContext, Constants.UNABLE_TO_RESOLVE_MSWIPE_SERVER));

            }
            e.printStackTrace();
        }
        catch (SocketTimeoutException e){

            e.printStackTrace();

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            if(e.getLocalizedMessage() !=null && e.getLocalizedMessage().toLowerCase().contains("read timed out"))
            {
                networkErrCode = 804;
                ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);
            }
            else{
                networkErrCode = 803;
                ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);
            }
        }
        catch(ConnectException e){

            /* this exception is triggere either if the internet is not available(ie wiif and mobile data are turned off) or if its availabe the network this the mswipe server
                     is down, this below check will handle this two instances */

            if(!Constants.checkInternetConnection(mContext))
            {

                networkErrCode = 801;

                ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")"  + ", " +
                        Constants.getString(mContext, Constants.INTERNET_NOT_AVALIABLE));


            }
            else {

                networkErrCode = 802;
                ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")"  + ", " +
                        Constants.getString(mContext, Constants.UNABLE_TO_CONNECT_MSWIPE_SERVER));
                e.printStackTrace();
            }

        }
        catch (IOException e) {

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            networkErrCode = 701;
            ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" +  errorMessage);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.e(ApplicationData.packName,  e, true, true);


            e.printStackTrace();

        }
        catch (Exception e) {

            String errorMessage = (e.getLocalizedMessage() == null ? "" : ", " + e.getLocalizedMessage());

            networkErrCode = 700;
            ERROR_MESSAGE = (Constants.getString(mContext, Constants.ERROR_CONNECTING_MSWIPE) + " (" + networkErrCode + ")" + errorMessage);
            e.printStackTrace();

        }
        finally {
            try {
                if (isr != null)
                    isr.close();
            } catch (Exception ex) {
            }

            try {
                if (ost != null) {
                    ost.flush();
                    ost.close();
                }
            } catch (Exception ex) {


            }

            try {
                if (osw != null) {
                    osw.flush();
                    osw.close();
                }
            } catch (Exception ex) {

            }
            try {
                if (httpConn != null)
                    httpConn.disconnect();
            } catch (Exception ex) {

            }
        }


        String mstConnectionErrDesc =  ERROR_MESSAGE;

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

         if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "Response " + response.toString() + " error  " + mstConnectionErrDesc + " code " + networkErrCode, true, true);


        if (networkHandler == null) {
            parse(response.toString(), mstConnectionErrDesc, networkErrCode);
        }
        else {

            Message messageToParent = new Message();
            Bundle messageData = new Bundle();
            messageToParent.what = 0;
            messageData.putString("httperror", mstConnectionErrDesc);
            messageData.putString("networkErrCode", networkErrCode+"");
            messageData.putString("responsedata", response.toString());
            messageToParent.setData(messageData);
            networkHandler.sendMessage(messageToParent);
        }
    }


    protected void parse(String text, String errMsg) {
    }

    protected void parse(String text, String errMsg, int errCode) {
    }

    private SSLContext getSecureConnection() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException, UnrecoverableKeyException {


        SSLContext context = null;

        /*
        for the aar as it keeps all the raw folder resources so we can direcltly
        read form the resource
        */

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
}





























