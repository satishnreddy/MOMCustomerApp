package com.mom.momcustomerapp.utils;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 22-04-2020 17:22
 * @modified by -
 * @updated on -
 * @since -
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shailesh on 27/2/18.
 */

public class IntentUtils {

    public static void dialAPhoneCall(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }


    public static void sendEmailWithAttachments(Context context, String[] emailAddress,
                                                String subject, String body,
                                                ArrayList<Uri> uris){
        Intent intent;
        if (uris.size() == 0){
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            Intent.createChooser(intent, "Select your Email App ");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
        else if (uris.size() == 1){
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
            intent.putExtra(Intent.EXTRA_TEXT, body);
            Intent.createChooser(intent, "Select your Email App ");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
        else {
            intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            Intent.createChooser(intent, "Select your Email App ");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }

    public static void sendWhatsAppMessageToNumber(Context context,
                                                   String number, String message){

        try {
            number = number.replace(" ", "").replace("+", "");

            Intent sendIntent = new Intent("android.intent.action.MAIN");

            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");

            context.startActivity(sendIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(context,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"Sorry this customer is not available on WhatsApp kindly contact via call",Toast.LENGTH_SHORT).show();
        }
    }


    public static void sendWhatsAppMessageWithAttachments(Context context,
                                                          String phoneNumber, String text,
                                                          ArrayList<Uri> urilist) {

        // TODO: Dubai 30/7/18
        // check if whats app is installed. todo
        try{





            Intent whatsAppIntent ;
            if (urilist.size() == 1 || urilist.size() == 0){
                whatsAppIntent = new Intent("android.intent.action.SEND");
                if (urilist.size() == 1){
                    whatsAppIntent.putExtra(Intent.EXTRA_STREAM, urilist.get(0));
                }
            }
            else{
                whatsAppIntent = new Intent("android.intent.action.SEND_MULTIPLE");
                whatsAppIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,urilist);
            }
            whatsAppIntent.setComponent(new ComponentName("com.whatsapp",
                    "com.whatsapp.ContactPicker"));
            whatsAppIntent.setType("image");
            whatsAppIntent.putExtra("jid", phoneNumber+"@s.whatsapp.net");
            whatsAppIntent.putExtra(Intent.EXTRA_TEXT,text);
            context.startActivity(whatsAppIntent);
        }
        catch(Exception e)
        {
            Toast.makeText(context,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
        }


    }

    public static Intent getImagePickerIntent() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        pickIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        if (Build.VERSION.SDK_INT >= 18) {
            getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        }

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, pickIntent);
        return chooserIntent;
    }


    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + id));
        //https://www.youtube.com/watch?v=hv3HYfcqHC4
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void playVideoOnYtbeOrWeb(Context context,String url ){
        String ytbePrefix = "https://www.youtube.com/watch?v=";
        if (url.contains(ytbePrefix)){
            String id = url.substring(ytbePrefix.length());
            IntentUtils.watchYoutubeVideo(context,id);
        }
        else{
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            context.startActivity(webIntent);
        }
    }

    public static String getYoutubeEmbedVideoUrl(String url ){
        String ytbePrefix = "https://www.youtube.com/watch?v=";
        if (url.contains(ytbePrefix)){
            String id = url.substring(ytbePrefix.length());
            return "https://www.youtube.com/embed/"+id;
        }
        return null;
    }

    public static Bundle convertHashMapToBundle(Map<String,String> hashMap){
        Bundle bundle = new Bundle();
        if (hashMap != null) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

    public static void sendSimpleMsg(Context context, String shareMsg) {
        context.startActivity(getIntentSimpleMsg(shareMsg));
    }

    public static Intent getIntentSimpleMsg(String shareMsg){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static Intent getIntentSimpleMsgOnlyWhatapp(String shareMsg) {
        return getIntentSimpleMsg(shareMsg)
                .setComponent(new ComponentName("com.whatsapp",
                        "com.whatsapp.ContactPicker"));
    }

    public static void shareMsgOnWhatsapp(Activity activity, String msg){
        try {
            activity.startActivity(getIntentSimpleMsg(msg));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity,"Whatsapp is not installed.",Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * @Testing
     *
     * */
    public static void shareMsgOnWhatsappForCallback(Fragment fragment, String msg, int requestCode){
        try {
            fragment.startActivityForResult(getIntentSimpleMsg(msg),requestCode);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(fragment.getContext(),"Whatsapp is not installed.",Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * @Testing
     *
     * */
    public static void shareMsgOnWhatsappForCallback(Activity activity, String msg,int requestCode){
        try {
            activity.startActivityForResult(getIntentSimpleMsg(msg),requestCode);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity,"Whatsapp is not installed.",Toast.LENGTH_SHORT).show();
        }
    }


    public static void openUrlOutsideApp(Context activity, String urlRaw){
        String url = appendHttpsIfNotFound(urlRaw);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
        else{
            Toast.makeText(activity.getApplicationContext(),
                    "No applications found to open this url",Toast.LENGTH_SHORT).show();
        }
    }

    public static String appendHttpsIfNotFound(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "https://" + url;
        return url;
    }


    public static void showAppOnPlayStore(Context context, String appId){

        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }

}
