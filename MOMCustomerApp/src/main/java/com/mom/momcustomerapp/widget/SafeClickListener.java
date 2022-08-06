package com.mom.momcustomerapp.widget;

import android.os.SystemClock;
import android.view.View;

public class SafeClickListener implements View.OnClickListener {
    /**
     * Configurable Last Click Time
     */
    private final int INTERVAL_FOR_LAST_CLICK;
    /**
     * callback to be send to listener
     */
    private Callback onClickCallback;
    /**
     * it is used to avoid multiple button click at same time
     */
    private long mLastClickTime;

    /**
     * Register the callback using this
     *
     * @param onClickCallback to be registered
     */
    public SafeClickListener(Callback onClickCallback) {
        this.onClickCallback = onClickCallback;
        INTERVAL_FOR_LAST_CLICK = 2000;
    }

    /**
     * Register the callback to send the click listener to the listening object.<br>
     * This is used to add a delay between two clicks
     *
     * @param onClickCallback   Listener to send click callback to the listening object
     * @param lastClickInterval delay between last click
     */
    public SafeClickListener(Callback onClickCallback, int lastClickInterval) {
        this.onClickCallback = onClickCallback;
        INTERVAL_FOR_LAST_CLICK = lastClickInterval;
    }

    @Override
    public void onClick(View v) {
        //check if system elapsed (milliseconds - lastClick) time > LAST_CLICK_TIME
        long elapsedRealtime = SystemClock.elapsedRealtime();
        /*FalconXExclusiveDataUtil.logDebugText("onClick: elapsedRealtime"+ elapsedRealtime);
        FalconXExclusiveDataUtil.logDebugText("onClick: mLastClickTime"+ mLastClickTime);*/
        long diff = elapsedRealtime - mLastClickTime;
        /*FalconXExclusiveDataUtil.logDebugText( "onClick: diff"+ diff);*/
        if (diff > INTERVAL_FOR_LAST_CLICK) {//save last click time
            mLastClickTime = elapsedRealtime;
            if (onClickCallback != null) {
                onClickCallback.onClick(v);
            }
            /*FalconXExclusiveDataUtil.logDebugText( "onClick: if " );*/
        }
        /*else{
            FalconXExclusiveDataUtil.logDebugText("onClick: else");
        }*/
    }

    /**
     * Interface for Custom on click listener
     */
    public interface Callback {
        void onClick(View v);
    }
}