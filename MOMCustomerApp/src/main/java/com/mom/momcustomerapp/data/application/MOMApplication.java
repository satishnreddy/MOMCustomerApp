package com.mom.momcustomerapp.data.application;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import androidx.multidex.MultiDexApplication;


import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.utils.crashlogs.Sherlock;

public class MOMApplication extends MultiDexApplication
{

    private static MOMApplication mMOMApplication = null;

    private String mToken = "";
    private String mVendor_id = "";
    private String mStore_id = "";

    @Override
    public void onCreate()
    {
        super.onCreate();
        mMOMApplication = this;

        Sherlock.init(this);
        loadCredentials();



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
        if(app.IS_PROD)
            return app.baseWEBSOCKET_URL_LIVE;
        else
            return app.baseWEBSOCKET_URL_UAT;
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
