package com.mom.momcustomerapp.utils;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.mom.momcustomerapp.R;

/*
 * Created by nishant on 24/12/16.
 */

public class GlideLoader {
    static public DrawableRequestBuilder<String> url(Context context) {
        return Glide.with(context)
                .fromString()
                .placeholder(R.drawable.ic_no_photo_64dp)
                .error(R.drawable.ic_no_photo_64dp)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    static public DrawableRequestBuilder<String> url(Fragment fragment) {
        return Glide.with(fragment.getContext())
                .fromString()
                .placeholder(R.drawable.ic_no_photo_64dp)
                .error(R.drawable.ic_no_photo_64dp)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    static public DrawableRequestBuilder<String> url(Activity activity) {
        return Glide.with(activity)
                .fromString()
                .placeholder(R.drawable.ic_no_photo_64dp)
                .error(R.drawable.ic_no_photo_64dp)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }


    static public DrawableRequestBuilder<String> urlNoCache(Activity activity) {
        return Glide.with(activity)
                .fromString()
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache(true);
    }



    static public DrawableRequestBuilder<String> urlWithSignature(Activity activity) {
        return Glide.with(activity)
                .fromString()
                .signature(new StringSignature("a"));
    }
}
