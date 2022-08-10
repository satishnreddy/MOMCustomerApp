package com.mom.momcustomerapp.controllers.orders.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrder;
import com.mom.momcustomerapp.customviews.AbstractRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.customviews.BaseRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.OnRecylerViewLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by nishant on 17/08/16.
 */

public class BillingMbasketRVAdapter extends BaseRecyclerViewLoadingAdapter<SalesCustOrder>
{

    private RecyclerViewItemClickListener mRecyclerViewItemClickListener;
    private ArrayList<SalesCustOrder> mDataset;

    public BillingMbasketRVAdapter(RecyclerView recyclerView, ArrayList<SalesCustOrder> items, RecyclerViewItemClickListener itemClickListener,
                                   OnRecylerViewLoadMoreListener onLoadMoreListener)
    {
        super(recyclerView, items, onLoadMoreListener);
        this.mDataset = items;
        this.mRecyclerViewItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType)
    {
        View orderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_orders,
                parent, false);
        return new OrdersViewHolder(orderView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if (mDataset != null)
        {
            OrdersViewHolder ordersViewHolder = (OrdersViewHolder) viewHolder;
            SalesCustOrder ordersModel = mDataset.get(position);
            ordersViewHolder.onBind(ordersModel, position);
        }
    }

    private class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvCustomerName, tvCustomerPhone, tvAmount, tvInvoiceId, tvDate, tvStatus;
        private ImageView ivStatus;
        private int position;

        OrdersViewHolder(View convertView)
        {
            super(convertView);
            tvCustomerName = convertView.findViewById(R.id.item_orders_tv_customer_name);
            tvCustomerPhone = convertView.findViewById(R.id.item_orders_tv_customer_phone);
            tvAmount = convertView.findViewById(R.id.item_orders_tv_amount);
            tvInvoiceId = convertView.findViewById(R.id.item_orders_tv_invoice_id);
            tvDate = convertView.findViewById(R.id.item_orders_tv_date);
            ivStatus = convertView.findViewById(R.id.item_orders_iv_status);
            tvStatus = convertView.findViewById(R.id.item_orders_tv_status);
            convertView.setOnClickListener(this);
        }

        public void onBind(SalesCustOrder ordersModel, int position)
        {
            this.position = position;
            tvInvoiceId.setText(ordersModel.invoice_number);
            tvCustomerName.setText(ordersModel.customerName);
            tvCustomerPhone.setText(ordersModel.customerPhone);
            String saleTime = ordersModel.sale_time;
            tvDate.setText(TextUtils.isEmpty(saleTime) ? "-": DateTimeUtils.convertDtTimeInAppFormat(saleTime));
            ivStatus.setImageResource(ordersModel.getImageStatus());
            tvStatus.setText(ordersModel.delivery_status);
            String price = ordersModel.total_price + "";
            try
            {
                float priceamount = Float.parseFloat(price.replaceAll(",", ""));
                price = Consts.getCommaFormatedStringWithDecimal(priceamount);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
            tvAmount.setText(price);
        }

        @Override
        public void onClick(View view)
        {
            mRecyclerViewItemClickListener.OnRecyclerViewItemClick(position);
        }
    }
}