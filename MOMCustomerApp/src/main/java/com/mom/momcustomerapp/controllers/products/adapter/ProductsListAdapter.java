package com.mom.momcustomerapp.controllers.products.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.models.Items;
import com.mom.momcustomerapp.customviews.AbstractRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.customviews.BaseRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.OnRecylerViewLoadMoreListener;
import com.mswipetech.sdk.network.util.Logs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 13-04-2020 12:08
 * @modified by -
 * @updated on -
 * @since -
 */


public class ProductsListAdapter extends BaseRecyclerViewLoadingAdapter<Items>
{

    private ProductListClickListener mRecyclerViewItemClickListener;
    private ArrayList<Items> mDataset;


    public ProductsListAdapter(RecyclerView recyclerView, ArrayList<Items> items, ProductListClickListener itemClickListener,
                               OnRecylerViewLoadMoreListener onLoadMoreListener )
    {
        super(recyclerView, items, onLoadMoreListener);
        this.mDataset = items;
        this.mRecyclerViewItemClickListener = itemClickListener;


    }


    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType)
    {
        View orderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_cardview_small, parent, false);
        return new ProductItemViewHolder(orderView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if (mDataset != null)
        {
            ProductItemViewHolder ordersViewHolder = (ProductItemViewHolder) viewHolder;
            ordersViewHolder.onBind(mDataset.get(position), position);
        }
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }


    public class ProductItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTvTitle, mTvPrice,mTxtAddtoCart, mTxtAdd, mTxtRemove, mTxtQty;
        private int position;
        private ImageView mImgProduct;
        //private CheckBox checkBox;
        private View rootView;
        private LinearLayout mLNR_addrevmove;

        public ProductItemViewHolder(View convertView)
        {
            super(convertView);
            rootView = convertView;
            mTvTitle = convertView.findViewById(R.id.item_product_cardview_small_tv_title);
            mTvPrice = convertView.findViewById(R.id.item_product_cardview_small_tv_price);
            //addToStoreBtn = convertView.findViewById(R.id.item_product_cardview_small_add_to_store);
            mLNR_addrevmove = (LinearLayout)convertView.findViewById(R.id.item_product_LNR_addrevmove);
            mTxtAddtoCart = convertView.findViewById(R.id.item_product_cardview_small_addtocart);

            mTxtAdd = convertView.findViewById(R.id.item_product_add);
            mTxtRemove = convertView.findViewById(R.id.item_product_remove);
            mTxtQty = convertView.findViewById(R.id.item_product_qty);
            mImgProduct = convertView.findViewById(R.id.item_product_image);

            //checkBox = convertView.findViewById(R.id.item_product_cardview_small_checkbox);
            //addToStoreBtn.setOnClickListener(this);
            mTxtAddtoCart.setOnClickListener(this);
            mTxtAdd.setOnClickListener(this);
            mTxtRemove.setOnClickListener(this);
        }

        public void onBind(final Items model, int position)
        {
            /*if (position == 0){
                LogUtils.logd("asdf","log");
            }*/


            this.position = position;
            mTvTitle.setText(model.name);
            mTvPrice.setText(model.selling_price);

            int contains = model.iQrySelected;
            //mImgProduct.setImageResource(R.drawable.placeholder);

            Glide.with(mImgProduct.getContext())
                    .load(model.img_url)
                    //.override(64, 64)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(mImgProduct);
            /*
            Glide.with(item_product_image.getContext())
                    .load(model.getImgUrl())
                    .placeholder(R.color.grey500)
                    .into(categoryImg);


            */

            if(contains > 0)
            {

                mLNR_addrevmove.setVisibility(View.VISIBLE);
                mTxtAddtoCart.setVisibility(View.GONE);

                mTxtQty.setText(contains + "");
                mTxtAdd.setVisibility(View.VISIBLE);
                mTxtRemove.setVisibility(View.VISIBLE);
                mTxtQty.setVisibility(View.VISIBLE);

            }
            else {
                mLNR_addrevmove.setVisibility(View.GONE);
                mTxtAddtoCart.setVisibility(View.VISIBLE);

                mTxtAdd.setVisibility(View.GONE);
                mTxtRemove.setVisibility(View.GONE);
                mTxtQty.setVisibility(View.GONE);
            }


        }

        @Override
        public void onClick(View view)
        {

             if (view.getId() == mTxtAddtoCart.getId() || view.getId() == mTxtAdd.getId())
             {
                 //mDataset.get(position).iQrySelected = mDataset.get(position).iQrySelected + 1;
                 mRecyclerViewItemClickListener.onProductSelected(mDataset.get(position), position);
                 //notifyDataSetChanged();
             }
             else if(view.getId() == mTxtRemove.getId())
             {
                 //mDataset.get(position).iQrySelected = mDataset.get(position).iQrySelected - 1;
                 mRecyclerViewItemClickListener.onProductUnSelected(mDataset.get(position), position);
                 //notifyDataSetChanged();

             }




        }
    }

    public interface ProductListClickListener
    {
        void onAddToStoreClicked(int position);
        void onViewInStoreClicked(int position);
        void onProductSelected(Items model, int position);
        void onProductUnSelected(Items model, int position);
        void  onnavigationbuttonset(int position);

    }

    public void setEditing(boolean editing, int update_cart_mode, int position)
    {

        if(!editing)
        {
            if (update_cart_mode == 1)
                mDataset.get(position).iQrySelected = mDataset.get(position).iQrySelected + 1;
            else
                mDataset.get(position).iQrySelected = mDataset.get(position).iQrySelected - 1;
        }
        //notifyDataSetChanged();
        notifyItemChanged(position);



    }


}