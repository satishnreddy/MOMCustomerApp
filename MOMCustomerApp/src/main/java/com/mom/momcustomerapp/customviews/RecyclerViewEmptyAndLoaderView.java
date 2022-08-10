package com.mom.momcustomerapp.customviews;

import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.utils.Logs;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class RecyclerViewEmptyAndLoaderView extends RecyclerView
{

    private View emptyView = null;

    public RecyclerViewEmptyAndLoaderView(Context context)
    {
        super(context);

    }

    public RecyclerViewEmptyAndLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptyAndLoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(Adapter adapter)
    {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }





        checkIfEmpty();

    }


    final private AdapterDataObserver observer = new AdapterDataObserver()
    {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }

    };

    void checkIfEmpty()
    {
        if (emptyView != null && getAdapter() != null)
        {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            //emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }


}