package com.mom.momcustomerapp.controllers.orders.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrder;
import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNew;
import com.mom.momcustomerapp.customviews.AbstractRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.utils.DateTimeUtils;

import java.util.List;

/*
 * Created by nishant on 17/08/16.
 */

public class ReturnsRecyclerViewAdapter extends AbstractRecyclerViewLoadingAdapter<SalesCustOrder> {

    private RecyclerViewItemClickListener mRecyclerViewItemClickListener;
    private List<SalesCustOrder> mDataset;
    private String mUserType;
    private int mInvoiceType = Consts.INVOICE_TYPE_BILL;

    public ReturnsRecyclerViewAdapter(RecyclerView recyclerView, List<SalesCustOrder> items, RecyclerViewItemClickListener itemClickListener,
                                      OnLoadMoreListener onLoadMoreListener) {
        super(recyclerView, items, onLoadMoreListener);
        this.mDataset = items;
        this.mRecyclerViewItemClickListener = itemClickListener;
        this.mUserType = MOMApplication.getSharedPref().getUserType();;
    }

    public ReturnsRecyclerViewAdapter(RecyclerView recyclerView, List<SalesCustOrder> items, int invoiceType, RecyclerViewItemClickListener itemClickListener, OnLoadMoreListener onLoadMoreListener) {
        super(recyclerView, items, onLoadMoreListener);
        this.mDataset = items;
        this.mRecyclerViewItemClickListener = itemClickListener;
        this.mUserType = MOMApplication.getSharedPref().getUserType();
        this.mInvoiceType = invoiceType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View orderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_orders, parent, false);
        return new OrdersViewHolder(orderView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (mDataset != null) {
            OrdersViewHolder ordersViewHolder = (OrdersViewHolder) viewHolder;
            SalesCustOrder ordersModel = mDataset.get(position);
            ordersViewHolder.onBind(ordersModel, position);
        }
    }

    private class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCustomerName, tvCustomerPhone, tvAmount, tvInvoiceId, tvDate, tvStatus;
        private ImageView ivStatus;
        private int position;

        OrdersViewHolder(View convertView) {
            super(convertView);
            tvCustomerName = (TextView) convertView.findViewById(R.id.item_orders_tv_customer_name);
            tvCustomerPhone = (TextView) convertView.findViewById(R.id.item_orders_tv_customer_phone);
            tvAmount = (TextView) convertView.findViewById(R.id.item_orders_tv_amount);
            tvInvoiceId = (TextView) convertView.findViewById(R.id.item_orders_tv_invoice_id);
            tvDate = (TextView) convertView.findViewById(R.id.item_orders_tv_date);

            ivStatus = convertView.findViewById(R.id.item_orders_iv_status);
            tvStatus = convertView.findViewById(R.id.item_orders_tv_status);

            //ivPaymentType = (ImageView) convertView.findViewById(R.id.item_orders_iv_payment_type);

            convertView.setOnClickListener(this);
        }

        public void onBind(SalesCustOrder ordersModel, int position)
        {
            if (!TextUtils.isEmpty(ordersModel.sale_id)) {
                this.position = position;
                tvInvoiceId.setText(ordersModel.invoice_number);
                tvCustomerName.setText(ordersModel.customerName);
                tvCustomerPhone.setText(ordersModel.customerPhone);
                tvStatus.setText(ordersModel.delivery_status);
                ivStatus.setImageResource(R.drawable.ic_order_returned);

                String saleTime = ordersModel.sale_time;
                tvDate.setText(TextUtils.isEmpty(saleTime) ? "-": DateTimeUtils.convertDtTimeInAppFormat(saleTime));

                /*switch (ordersModel.getPaymentType()) {
                    case "Cash":
                        ivPaymentType.setImageResource(R.drawable.ic_cash_24dp);
                        break;
                    case "Credit Card":
                        ivPaymentType.setImageResource(R.drawable.ic_credit_card_24dp);
                        break;
                    case "Debit Card":
                        ivPaymentType.setImageResource(R.drawable.ic_debit_card_24dp);
                        break;
                    case "Net Banking":
                        ivPaymentType.setImageResource(R.drawable.ic_net_banking_24dp);
                        break;
                    case "Wallet":
                        ivPaymentType.setImageResource(R.drawable.ic_wallet_24dp);
                        break;
                    case "Voucher":
                        ivPaymentType.setImageResource(R.drawable.ic_voucher_24dp);
                        break;
                    case "Mix":
                        ivPaymentType.setImageResource(R.drawable.ic_mix_payment_24dp);
                        break;
                    case "Partial":
                        ivPaymentType.setImageResource(R.drawable.ic_partial_payment_24dp);
                        break;
                    default:
                        ivPaymentType.setImageResource(R.drawable.ic_cash_24dp);
                }*/

                String price;

                /*if (mInvoiceType == Consts.INVOICE_TYPE_PARTIAL) {
                    price = ordersModel.getPayment_amount();
                } else {
                    price = ordersModel.getTotalPrice() + "";
                }*/
                price = ordersModel.total_price + "";
                try {
                    float priceamount = Float.parseFloat(price.replaceAll(",", ""));
                    price = Consts.getCommaFormatedStringWithDecimal(priceamount);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                tvAmount.setText(price);
            }
        }

        @Override
        public void onClick(View view) {
            mRecyclerViewItemClickListener.OnRecyclerViewItemClick(position);
        }
    }
}