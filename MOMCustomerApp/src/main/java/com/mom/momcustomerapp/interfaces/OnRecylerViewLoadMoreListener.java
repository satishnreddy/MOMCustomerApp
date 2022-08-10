package com.mom.momcustomerapp.interfaces;

/*
 * Created by nishant on 22/07/16.
 */
public interface OnRecylerViewLoadMoreListener {
    void onLoadMore(int currentPage, int totalItemCount);
    void onLoadMoreError(String msg);

}
