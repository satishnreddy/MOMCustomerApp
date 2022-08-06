package com.mom.momcustomerapp.controllers.customers.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.momcustomerapp.controllers.customers.models.PastOrdersModel;
import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNew;
import com.mom.momcustomerapp.customviews.AbstractRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Shailesh on 17/08/16.
 */

public class PastOrdersRVAdapter extends AbstractRecyclerViewLoadingAdapter<PastOrdersModel> {
    public String searchQuery = "";
    public boolean isSearchQuery = false;
    private RecyclerViewItemClickListener mRecyclerViewItemClickListener;
    private List<PastOrdersModel> mDataSet;
    public Context context;
    private ArrayList<BillingListModelNew> mBillingModelArrayList = new ArrayList<>();
    private ArrayList<BillingListModelNew> mBillingSearchModelArrayList = new ArrayList<>();
    public PastOrdersRVAdapter(RecyclerView recyclerView, List<PastOrdersModel> items, RecyclerViewItemClickListener itemClickListener,
                               OnLoadMoreListener onLoadMoreListener) {
        super(recyclerView, items, onLoadMoreListener);
        this.mDataSet = items;
        this.mRecyclerViewItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View orderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_past_orders, parent, false);
        return new OrdersViewHolder(orderView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (mDataSet != null) {
            OrdersViewHolder ordersViewHolder = (OrdersViewHolder) viewHolder;
            PastOrdersModel ordersModel = mDataSet.get(position);
            ordersViewHolder.onBind(ordersModel, position);
        }
    }



    private class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCustomerName,tvAmount, tvInvoiceId, tvDate, tvStatus;
        private ImageView ivStatus;
        private int position;

        OrdersViewHolder(View convertView) {
            super(convertView);
            tvCustomerName = convertView.findViewById(R.id.item_orders_tv_customer_name);
            tvAmount = convertView.findViewById(R.id.item_orders_tv_amount);
            tvInvoiceId = convertView.findViewById(R.id.item_orders_tv_invoice_id);
            tvDate = convertView.findViewById(R.id.item_orders_tv_date);
            ivStatus = convertView.findViewById(R.id.item_orders_iv_status);
            tvStatus = convertView.findViewById(R.id.item_orders_tv_status);
            convertView.setOnClickListener(this);
        }

        public void onBind(PastOrdersModel ordersModel, int position){
            this.position = position;
            tvInvoiceId.setText(ordersModel.getInvoiceNumber());
            tvCustomerName.setText(ordersModel.getCustomerName());
            tvDate.setText(DateTimeUtils.convertDtTimeInAppFormat(ordersModel.getSaleTime()));
            ivStatus.setImageResource(ordersModel.getImageStatus());
            tvStatus.setText(ordersModel.getDeliveryStatus());
            String price = ordersModel.getTotalPrice() + "";
            try {
                float priceAmount = Float.parseFloat(price.replaceAll(",", ""));
                price = Consts.getCommaFormatedStringWithDecimal(priceAmount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            tvAmount.setText(price);
        }

        @Override
        public void onClick(View view) {
//
//            Intent intent = new Intent((Activity) context, OrderDetailsActivity.class);
//            if (isSearchQuery) {
//                intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingSearchModelArrayList.get(position).getSaleId());
//            } else {
//                intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingModelArrayList.get(position).getSaleId());
//            }
//
//
//            intent.putExtra(Consts.EXTRA_INVOICE_RETURN, Consts.INVOICE_TYPE_BILL);
//            intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE, ORDER_STATUS.ORDER_STATUS_DELIVERED);
//            intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE_INT_STRING, mBillingModelArrayList.get(position).getOrder_statusInIntString());
//            ((Activity) context).startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);

            if (mRecyclerViewItemClickListener != null)
                mRecyclerViewItemClickListener.OnRecyclerViewItemClick(position);
        }
    }
}