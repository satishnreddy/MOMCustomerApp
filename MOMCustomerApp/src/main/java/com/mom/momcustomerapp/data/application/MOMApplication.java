package com.mom.momcustomerapp.data.application;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import androidx.multidex.MultiDexApplication;


import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


import com.mom.momcustomerapp.R;

public class MOMApplication extends MultiDexApplication
{

    public static final int INTERNAL_MBASKET_VERSION_CODE = 1;
    public static final boolean is_DEBUGGING_ON = true;
    public static final boolean IS_PROD = false;
    //public static final String APPLICATION_ID = "ccom.mom.momcustomerapp";

    public static final String packName = "com.mom";
    private static MOMApplication mMOMApplication = null;

    //private static final String baseWEBSOCKET_URL_LIVE = "https://556b-2405-201-c00b-9895-104e-a526-a67b-3ba.in.ngrok.io/momapi/api/";
    //private static final String baseWEBSOCKET_URL_UAT  = "https://556b-2405-201-c00b-9895-104e-a526-a67b-3ba.in.ngrok.io/momapi/api/";

    //private static final String baseWEBSOCKET_URL_LIVE = "http://ec2-13-126-69-193.ap-south-1.compute.amazonaws.com/momapi/api/";
    //private static final String baseWEBSOCKET_URL_UAT  = "http://ec2-13-126-69-193.ap-south-1.compute.amazonaws.com/momapi/api/";

    private static final String baseWEBSOCKET_URL_LIVE = "http://10.0.2.2/momapi/api/";
    private static final String baseWEBSOCKET_URL_UAT  = "http://10.0.2.2/momapi/api/";


    private String mToken = "";
    private String mVendor_id = "";
    private String mStore_id = "";

    @Override
    public void onCreate()
    {
        super.onCreate();
        mMOMApplication = this;

        loadCredentials();
        /*
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build())).build()
        );
        */


    }

    public static MOMApplication getInstance()
    {
        return mMOMApplication;
    }

    public static MOMSharedPreferences getSharedPref()
    {
        return MOMSharedPreferences.getInstance(mMOMApplication.getApplicationContext());
    }

    public String getServerUrl()
    {
        if(IS_PROD)
            return baseWEBSOCKET_URL_LIVE;
        else
            return baseWEBSOCKET_URL_UAT;
    }



    private void loadCredentials()
    {

    }


    public String getToken()
    {
        mToken = getSharedPref().getToken();
        return mToken;
    }

    public void setToken(String token)
    {
        getSharedPref().setToken(token);
        mToken = token;
    }

    public String getVender() {
        mVendor_id = getSharedPref().getVender();
        return mVendor_id;
    }

    public void setVender(String vendor_id)
    {
        getSharedPref().setVender(vendor_id);
        mVendor_id = vendor_id;
    }

    public String getStoreId()
    {
        mStore_id = getSharedPref().getStoreId();
        return mStore_id;
    }



    public void setStoreId(String store_id)
    {
        getSharedPref().setStoreId(store_id);
        mStore_id = store_id;
    }

    public void setPersonId(String vender) {
        getSharedPref().setPersonIdInSync(vender);

    }
    public String getPersonId(){
        return getSharedPref().getPersonId();


    }

    public void setMswipeUsername(String vender) {
        getSharedPref().setMswipeUsername(vender);

    }
    public String getMswipeUsername(){
        return  getSharedPref().getMswipeUsername();


    }


}
