package com.mom.momcustomerapp.networkservices;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.ErrorModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import com.mom.momcustomerapp.R;
/*
 * Created by Nishant on 16-06-2016.
 */
public class ErrorUtils
{

    public static ErrorModel parseError(Response<?> response)
    {

        Converter<ResponseBody, ErrorModel> converter = ServiceGenerator.retrofit()
                .responseBodyConverter(ErrorModel.class, new Annotation[0]);

        ErrorModel error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            error = new ErrorModel();
        } catch (Exception e) {
            e.printStackTrace();
            error = new ErrorModel();
        }

        return error;
    }

    public static ErrorModel parseErrorFromBody(Response<?> response)
    {

        Converter<ResponseBody, ErrorModel> converter = ServiceGenerator.retrofit()
                .responseBodyConverter(ErrorModel.class, new Annotation[0]);

        ErrorModel error;

        try {
            error = converter.convert(ResponseBody.create(MediaType.parse("name/html"), response.body().toString()));
        } catch (IOException e) {
            e.printStackTrace();
            error = new ErrorModel();
        } catch (Exception e) {
            e.printStackTrace();
            error = new ErrorModel();
        }

        return error;
    }

    public static String getErrorString(Response<?> response)
    {
        String string = "Something went wrong";
        try {
            string = response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.code() == 500)
        {
            String finalMsg = response.message() + "\nDetail :" + string;
            if (finalMsg.length() > 400) finalMsg = finalMsg.substring(0, 400) + "...";
            return finalMsg;
        }
        else if (response.code() == 422 && string.contains("status") && string.contains("message"))
        {
            //{"status":"error","message":"Time slot already exist"}
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject(string);
                String message = jsonObject.optString("message");
                if (message != null && !message.isEmpty()) return message;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (string.length() > 400) string = string.substring(0, 400) + "...";
        return "Code :" + response.code() + ". " + string;
    }

    public static String getFailureError(Throwable t)
    {
        ConnectivityManager cm =
                (ConnectivityManager) MOMApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            return MOMApplication.getInstance().getString(R.string.no_network_try_again);
        }
        String string = "Something went wrong" + " : " + t.getLocalizedMessage();
        return string;
    }
}
