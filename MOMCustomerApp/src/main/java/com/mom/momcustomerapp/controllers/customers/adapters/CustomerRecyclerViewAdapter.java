package com.mom.momcustomerapp.controllers.customers.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.customers.models.CustomerModel;
import com.mom.momcustomerapp.customviews.AbstractRecyclerViewLoadingAdapter;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.utils.AppUtils;

import java.util.HashSet;
import java.util.List;

/*
 * Created by nishant on 24/08/16.
 */

public class CustomerRecyclerViewAdapter extends AbstractRecyclerViewLoadingAdapter<CustomerModel>
{


    private boolean isPendingRequest = false;
    private OnClickListener onClickListener;
    private List<CustomerModel> mDataset;
    private String mUserType;
    private Fragment mFragment;
    /*private ArrayList<CustomerModel> customerModels = new ArrayList<>();*/
    private HashSet<CustomerModel> selectedList = new HashSet<>();

    public HashSet<CustomerModel> getSelectedList() {
        return selectedList;
    }

    private boolean isEditing = false;

    public void setEditing(boolean editing) {
        isEditing = editing;
        notifyDataSetChanged();
    }

    public void setPendingRequest(boolean pendingRequest) {
        isPendingRequest = pendingRequest;
    }

    public CustomerRecyclerViewAdapter(Fragment fragment, RecyclerView recyclerView, List<CustomerModel> items,
                                       OnClickListener itemClickListener,
                                       OnLoadMoreListener onLoadMoreListener, boolean isPendingRequest) {
        super(recyclerView, items, onLoadMoreListener);
        this.mFragment = fragment;
        this.mDataset = items;
        this.onClickListener = itemClickListener;
        this.mUserType = MOMApplication.getSharedPref().getUserType();
        this.isPendingRequest = isPendingRequest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_customers_person, parent, false);
        View itemRequestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_customers_person_pending_request, parent, false);
        if (isPendingRequest)
        {
            RecyclerView.ViewHolder viewHolder = new CustomerPendingRequestViewHolder(itemRequestView);
            return viewHolder;
        }
        else{
            RecyclerView.ViewHolder viewHolder =  new CustomerViewHolder(itemView);
            return viewHolder;
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, final int position)
    {
        CustomerViewHolder customerViewHolder = (CustomerViewHolder) viewHolder;
        CustomerModel customerModel = mDataset.get(position);

        customerViewHolder.position = position;

        if (!TextUtils.isEmpty(customerModel.getName())) {
            customerViewHolder.tvName.setVisibility(View.VISIBLE);
            customerViewHolder.tvName.setText(customerModel.getName());
        } else {
            customerViewHolder.tvName.setVisibility(View.GONE);
        }

        customerViewHolder.ivImage.setImageResource(R.color.black_80);
        if (customerModel.getImage() != null && !customerModel.getImage().isEmpty()
                && !TextUtils.isEmpty(customerModel.getImage().get(0).getImage_path())) {

            //GlideLoader.url(mFragment).load(customerModel.getImage().get(0).getImage_path()).into(customerViewHolder.ivImage);
            customerViewHolder.tvImageText.setVisibility(View.GONE);
        } else {
            customerViewHolder.tvImageText.setVisibility(View.VISIBLE);
            String name = customerModel.getName().trim();
            String initials = name.substring(0, 1);
            if (name.contains(" ")) {
                int pos = name.indexOf(" ");
                String secondInitial = name.substring(pos + 1, pos + 2);
                initials = initials + secondInitial;
            }
            customerViewHolder.tvImageText.setText(initials);
        }
        if (isEditing) {
            if (isInSelectedList(customerModel)) {
                customerViewHolder.selectedIV.setVisibility(View.VISIBLE);
            } else {
                customerViewHolder.selectedIV.setVisibility(View.GONE);
            }
        } else {
            customerViewHolder.selectedIV.setVisibility(View.GONE);
        }

        customerViewHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEditing) {
                    setEditing(true);
                    onClickListener.onEditingEnabled();
                    selectedList.add(mDataset.get(position));
                    onClickListener.onSelectedCountChanged(selectedList.size());
                }
                return false;
            }
        });

        if (customerViewHolder instanceof CustomerPendingRequestViewHolder){
            String status = customerModel.getStatus();
            CustomerPendingRequestViewHolder customerViewHolder1 = (CustomerPendingRequestViewHolder) customerViewHolder;
            customerViewHolder1.statusTv.setText(customerViewHolder1.statusTv.getContext().getString(R.string.status_pending_customer_request,
                    (TextUtils.isEmpty(status) ? "": AppUtils.getPendingRequestDispValue(status)))
            );
        }
    }

    private boolean isInSelectedList(CustomerModel customerModel) {
        if (selectedList.contains(customerModel)) {
            for (CustomerModel obj : selectedList) {
                if (obj.equals(customerModel))
                    return true;
            }
        }
        return false;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvImageText;
        private ImageView ivImage;
        private int position;
        private View rootView;
        private View selectedIV;

        public CustomerViewHolder(View convertView) {
            super(convertView);
            tvName = convertView.findViewById(R.id.item_row_person_tv_name);
            tvImageText = convertView.findViewById(R.id.item_row_person_tv_avatar);
            ivImage = convertView.findViewById(R.id.item_row_person_circleView);
            selectedIV = convertView.findViewById(R.id.item_row_person_selected);
            rootView = convertView;
            convertView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            CustomerModel customerModel = mDataset.get(getAdapterPosition());
            if (isEditing){
                if (selectedList.contains(customerModel)){
                    selectedList.remove(customerModel);
                }
                else {
                    selectedList.add(customerModel);
                }
                onClickListener.onSelectedCountChanged(selectedList.size());
                notifyItemChanged(getAdapterPosition());
            }
            else {
                onClickListener.onClicked(customerModel);
            }
        }

    }

    public class CustomerPendingRequestViewHolder extends CustomerViewHolder implements View.OnClickListener {
        private TextView tvName, tvImageText,statusTv;
        private ImageView ivImage;
        private int position;
        private View rootView;
        private View selectedIV;

        public CustomerPendingRequestViewHolder(View convertView) {
            super(convertView);
            tvName = convertView.findViewById(R.id.item_row_person_tv_name);
            tvImageText = convertView.findViewById(R.id.item_row_person_tv_avatar);
            ivImage = convertView.findViewById(R.id.item_row_person_circleView);
            selectedIV = convertView.findViewById(R.id.item_row_person_selected);
            statusTv = convertView.findViewById(R.id.item_row_person_pending_tv_status);

            rootView = convertView;
            convertView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            CustomerModel customerModel = mDataset.get(getAdapterPosition());
            if (isEditing){
                if (selectedList.contains(customerModel)){
                    selectedList.remove(customerModel);
                }
                else {
                    selectedList.add(customerModel);
                }
                onClickListener.onSelectedCountChanged(selectedList.size());
                notifyItemChanged(getAdapterPosition());
            }
            else {
                onClickListener.onClicked(customerModel);
            }
        }

    }

    public interface OnClickListener{

        void onClicked(CustomerModel customerModel);

        void onEditingEnabled();

        void onSelectedCountChanged(int newSelectedCount);
    }

    public void resetSelectedList(){
        selectedList.clear();
    }
}
