package com.mom.momcustomerapp.customviews;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.OnRecylerViewLoadMoreListener;
import com.mom.momcustomerapp.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by nishant on 22/07/16.
 */
public abstract class BaseRecyclerViewLoadingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_VIEW_TYPE_BASIC = 0;
    private final int ITEM_VIEW_TYPE_FOOTER = 99;

    private boolean isLoading = true;
    private ArrayList<T> dataSet;
    List<T> newDataSetItems = null;

    public int mTotalRecordsBills = 0;
    private boolean isLoadingViewAdded;
    private boolean iDisplayOnloadErroronce = false;

    public BaseRecyclerViewLoadingAdapter(RecyclerView recyclerView, ArrayList<T> dataSet, final OnRecylerViewLoadMoreListener onLoadMoreListener)
    {
        mTotalRecordsBills = 0;
        newDataSetItems = null;
        iDisplayOnloadErroronce = false;
        this.dataSet = dataSet;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                //super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //int visibleItemCount = linearLayoutManager.getChildCount();
                //int totalItemCount = linearLayoutManager.getItemCount();
                //int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                //Logs.adb("RecycleView onScroll " + visibleItemCount + " " + totalItemCount + " " + pastVisibleItems);

                //Logs.adb("RecycleView onScroll " + dy + " " + recyclerView.getMeasuredHeight());


                if (dy <= 0) {
                    return;
                }




                int findLastVisibleItemPosition = -1;
                if (linearLayoutManager != null)
                    findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if(isLoading && isLoadingViewAdded)
                {
                    return;
                }

                if(isLoading && !isLoadingViewAdded)
                {
                   if( mTotalRecordsBills ==0 ) {
                       return;
                   }
                   else if(findLastVisibleItemPosition == -1) {
                       if(!iDisplayOnloadErroronce)
                            sendOnLoadErrorListner("unable to get data, refresh to retry", onLoadMoreListener);
                       iDisplayOnloadErroronce = true;
                       return;
                   }
                   else if(newDataSetItems == null || newDataSetItems.size() == 0) {
                       if(!iDisplayOnloadErroronce)
                           sendOnLoadErrorListner("unable to get data, refresh to retry", onLoadMoreListener);
                       iDisplayOnloadErroronce = true;
                       return;

                   }else if(newDataSetItems.size() > mTotalRecordsBills ||  dataSet.size() > mTotalRecordsBills) {
                       if(!iDisplayOnloadErroronce)
                           sendOnLoadErrorListner("invalid data, data exceeds the max data, refresh to retry", onLoadMoreListener);
                       iDisplayOnloadErroronce = true;
                       return;
                   }else if(dataSet.size() >= mTotalRecordsBills) {
                       return;

                   } else if(findLastVisibleItemPosition == (dataSet.size() -1)
                           && mTotalRecordsBills > findLastVisibleItemPosition)
                   {
                       isLoading = false;
                   }
                }


                if ( !isLoading && mTotalRecordsBills > 0)
                {


                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (onLoadMoreListener != null)
                                {
                                    addItemNull();
                                    isLoadingViewAdded = true;

                                    onLoadMoreListener.onLoadMore(0, 0);
                                    isLoading = true;
                                }
                            }
                        });


                }
            }
        });


        if(dataSet != null && dataSet.size() == 0 )
        {
            if (onLoadMoreListener != null)
            {
                 new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        isLoading = true;
                        addItemNull();
                        isLoadingViewAdded = true;
                        onLoadMoreListener.onLoadMore(0, 0);

                    }
                });
            }
        }


    }

    private  void sendOnLoadErrorListner(String msg, OnRecylerViewLoadMoreListener onLoadMoreListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMoreError(msg);
                }
            }
        });
    }

    private void addItems(@NonNull ArrayList<T> newDataSetItems)
    {
        dataSet.addAll(newDataSetItems);
        notifyDataSetChanged();

    }

    public void setLoadingCompleteWithData(@NonNull ArrayList<T> newDataSetItems)
    {

        if(newDataSetItems == null)
        {
            removeItemNull();
            isLoadingViewAdded = false;
            newDataSetItems = null;
        }
        else {
            if(newDataSetItems.size() == 0)
            {
                removeItemNull();
                isLoadingViewAdded = false;
                newDataSetItems = null;
            }
            else {
                int indexOfItem = dataSet.indexOf(null);
                if (indexOfItem != -1) {
                    this.dataSet.remove(indexOfItem);
                    isLoadingViewAdded = false;

                }
                addItems(newDataSetItems);
                this.newDataSetItems = newDataSetItems;
            }
        }
        //isLoading = false;
    }

    private void addItemNull()
    {
        dataSet.add(null);
        notifyItemInserted(dataSet.size() - 1);

    }

    private void removeItemNull()
    {
       if(isLoadingViewAdded)
        {
            int indexOfItem = dataSet.size() -1;
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        return dataSet.get(position) != null ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER)
        {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_FOOTER)
        {
            onBindFooterView(viewHolder, position);
        } else {
            onBindItemViewHolder(viewHolder, position);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType)
    {
        //noinspection ConstantConditions
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
        return new ProgressViewHolder(v);
    }

    public void onBindFooterView(RecyclerView.ViewHolder genericHolder, int position)
    {
        //((ProgressViewHolder) genericHolder).progressBar.setIndeterminate(true);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder
    {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v)
        {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }


}
