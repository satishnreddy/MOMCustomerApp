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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by nishant on 22/07/16.
 */
public abstract class AbstractRecyclerViewLoadingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_VIEW_TYPE_BASIC = 0;
    private final int ITEM_VIEW_TYPE_FOOTER = 99;

    private int visibleThreshold = 5, firstVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private int currentPage = 0, startingPageIndex = 0;
    private boolean loading = true;

    private RecyclerView.LayoutManager mLayoutManager;
    private List<T> dataSet;


    public AbstractRecyclerViewLoadingAdapter(RecyclerView recyclerView, List<T> dataSet, final OnLoadMoreListener onLoadMoreListener) {

        this.dataSet = dataSet;
        this.mLayoutManager = recyclerView.getLayoutManager();

        if (mLayoutManager instanceof GridLayoutManager) {
            visibleThreshold = visibleThreshold * ((GridLayoutManager) mLayoutManager).getSpanCount();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            visibleThreshold = visibleThreshold * ((StaggeredGridLayoutManager) mLayoutManager).getSpanCount();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) {
                    return;
                }

                totalItemCount = mLayoutManager.getItemCount();
                visibleItemCount = mLayoutManager.getChildCount();

                if (mLayoutManager instanceof LinearLayoutManager) {

                    firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {

                    int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null);
                    firstVisibleItem = firstVisibleItemPositions[0];
                } else if (mLayoutManager instanceof GridLayoutManager)
                {
                    firstVisibleItem = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                }

                if (loading)
                {
                    if (totalItemCount > previousTotal)
                    {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (onLoadMoreListener != null) {
                                addItem(null);
                                currentPage++;
                                onLoadMoreListener.onLoadMore(currentPage, totalItemCount);
                                loading = true;
                            }
                        }
                    });
                }
            }
        });

        if (mLayoutManager.getItemCount() == 0) {
            if (onLoadMoreListener != null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addItem(null);
                        currentPage++;
                        onLoadMoreListener.onLoadMore(currentPage, totalItemCount);
                    }
                }, 500);
            }
            loading = true;
        }
    }

    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }

    public void freshCall() {
        previousTotal = 0;
    }

    public void resetItems(@NonNull List<T> newDataSet) {
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;

        dataSet.clear();
        addItems(newDataSet);
    }

    public void resetItems(@NonNull List<T> newDataSet, boolean isFreshCall) {
        if (isFreshCall) {
            freshCall();
        }

        resetItems(newDataSet);
    }

    public void addItems(@NonNull List<T> newDataSetItems) {
        dataSet.addAll(newDataSetItems);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public void startFreshLoading() {
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;

        dataSet.clear();
        notifyDataSetChanged();
        addItem(null);
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
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

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        //noinspection ConstantConditions
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_load_more, parent, false);
        return new ProgressViewHolder(v);
    }

    public void onBindFooterView(RecyclerView.ViewHolder genericHolder, int position)
    {
        ((ProgressViewHolder) genericHolder).progressBar.setIndeterminate(true);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder
    {
       @BindView(R.id.layout_load_more_progress)
        public ProgressBar progressBar;

        public ProgressViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
