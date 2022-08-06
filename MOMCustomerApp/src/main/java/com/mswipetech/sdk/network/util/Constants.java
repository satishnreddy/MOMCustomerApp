/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Satish N
 */

package com.mswipetech.sdk.network.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mswipetech.sdk.network.data.ApplicationData;
import com.mswipetech.sdk.network.util.impl.ImplData;
import com.mswipetech.sdk.network.util.impl.ImplementationData;
import com.mswipetech.sdk.network.util.impl.data.MetaDataImpl;


public class Constants
{

    public static final int INTERNET_NOT_AVALIABLE = 801;
    public static final int UNABLE_TO_CONNECT_MSWIPE_SERVER = 802;
    public static final int CONNECTION_TIMEDOUT = 803;
    public static final int READ_TIMEDOUT = 804;
    public static final int UNABLE_TO_RESOLVE_MSWIPE_SERVER = 805;

    public static final int ERROR_CONNECTING_MSWIPE = 10001;
    public static final int NETWORK_BUSY_PROCESSING_OTHER_REQUEST = 10005;
    public static final int INCORRECT_URL = 10006;
    public static final int HOST_CONNECTING = 10007;
    public static final int HOST_ONLINE = 10008;
    public static final int HOST_OFFLINE = 10009;

    public final static int META_DATA = 1;
    public final static int DATA = 2;
    public final static int REP_DATA = 3;


    public static String compressMetaData(String data) throws Exception {
        return compress(data, Constants.META_DATA, 1);
    }

    public static String extractMetaData(String data) throws Exception {
        return compress(data, Constants.META_DATA, 0);
    }

    public static String compress(String data, int type, int enctype) throws Exception {
        ImplData implData = null;

        if (type == META_DATA) {
            implData = ImplementationData.getInstance().createImplementation(new MetaDataImpl());

        }

        if (enctype == 1)
            return implData.compressData(data);
        else
            return implData.extractData(data, new byte[1]);

    }

    public static String getString(Context context, int stringCode)
    {

        try{
            String language = "";
            String requestString = "";

            try{

                SharedPreferences preferences = context.getSharedPreferences(ApplicationData.MSSharedPreferences, context.MODE_PRIVATE);
                language = preferences.getString("language","en");
                requestString = "";

            }catch(Exception ex){

                language = "en";
            }

           /* Locale locale = context.getResources().getConfiguration().locale;
            language = locale.getDefault().getLanguage();*/

            switch (stringCode){


                case INTERNET_NOT_AVALIABLE:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "data service is not available, please check your Wifi connection or mobile data connection (error code %s).";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "data service is not available, please check your Wifi connection or mobile data connection (error code %s).";
                    else
                        requestString =  "डेटा सेवा उपलब्ध नहीं है, अपने वाईफाई कनेक्शन या मोबाइल डेटा कनेक्शन की जाँच करें (एरर कोड %s)";

                    break;

                case UNABLE_TO_CONNECT_MSWIPE_SERVER:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "mswipe’s server is busy / no route to the server, please try again (error code %s).";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "mswipe’s server is busy / no route to the server, please try again (error code %s).";
                    else
                        requestString =  "mswipe के सर्वर व्यस्त / गैर संवेदनशील है, पुन: प्रयास करें (एरर कोड %s)";

                    break;

                case CONNECTION_TIMEDOUT:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "internet connection reset (error code %s).";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "internet connection reset (error code %s).";
                    else
                        requestString =  "इंटरनेट कनेक्शन रीसेट (त्रुटि कोड %s))";

                    break;

                case READ_TIMEDOUT:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "no response received / read timeout (error code %s).";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "no response received / read timeout (error code %s).";
                    else
                        requestString =  "कोई जवाब नहीं मिला / पढ़ मध्यांतर (त्रुटि कोड %s)";

                    break;

                case UNABLE_TO_RESOLVE_MSWIPE_SERVER:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "cannot resolve mswipe host domain, please check your Wifi connection or mobile data connection (error code %s).";
                    else  if(language.equalsIgnoreCase("in"))
                        requestString =  "cannot resolve mswipe host domain, please check your Wifi connection or mobile data connection (error code %s).";
                    else
                        requestString =  "mswipe होस्ट डोमेन को हल नहीं कर सकते, अपने वाईफाई कनेक्शन या मोबाइल डेटा कनेक्शन की जाँच करें (एरर कोड %s)";

                    break;

                case ERROR_CONNECTING_MSWIPE:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "error connecting to mswipe,";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "error connecting to mswipe,";
                    else
                        requestString =  "mswipe करने के लिए कनेक्ट होने में त्रुटि ,";

                    break;

                case NETWORK_BUSY_PROCESSING_OTHER_REQUEST:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "the network is busy processing other request, please try again after the current operation ends.";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "the network is busy processing other request, please try again after the current operation ends.";
                    else
                        requestString =  "बाद वर्तमान आपरेशन समाप्त होता नेटवर्क अन्य अनुरोध संसाधित करने में व्यस्त है , कृपया पुनः प्रयास करें ।";

                    break;

                case INCORRECT_URL:

                    if(language.equalsIgnoreCase("en"))
                    requestString =  "incorrect HTTP URL.";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "incorrect HTTP URL.";
                    else
                        requestString =  "गलत HTTP URL।";

                    break;

                case HOST_CONNECTING:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "host connecting";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "host connecting";
                    else
                        requestString =  "को जोड़ने की मेजबानी";

                    break;

                case HOST_ONLINE:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "host online";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "host online";
                    else
                        requestString =  "ऑनलाइन की मेजबानी";

                    break;

                case HOST_OFFLINE:

                    if(language.equalsIgnoreCase("en"))
                        requestString =  "host offline";
                    else if(language.equalsIgnoreCase("in"))
                        requestString =  "host offline";
                    else
                        requestString =  "ऑफ़लाइन मेजबानी";

                    break;

            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "language "+ language + " requestString " + requestString, true, true);

            return requestString;

        }catch(Exception ex){

            return "";
        }
    }

    public static boolean checkInternetConnection(Context context)
    {

        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

            return connected;

        } catch (Exception e) {

        }
        return false;
    }
}
