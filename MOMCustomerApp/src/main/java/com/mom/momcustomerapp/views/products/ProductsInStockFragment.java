package com.mom.momcustomerapp.views.products;

import static com.mom.momcustomerapp.data.application.Consts.DIALOG_DISPLAY_TIME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.ItemsController;
import com.mom.momcustomerapp.controllers.products.adapter.ProductsListAdapter;
import com.mom.momcustomerapp.controllers.products.models.Items;
import com.mom.momcustomerapp.controllers.products.models.ItemsResp;
import com.mom.momcustomerapp.controllers.sales.SalesUpdateCustCartController;
import com.mom.momcustomerapp.controllers.sales.models.SalesUpdateCartResp;
import com.mom.momcustomerapp.customviews.RecyclerViewEmptyAndLoaderView;

import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.OnRecylerViewLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsInStockFragment extends BaseFragment implements OnRecylerViewLoadMoreListener,
        RecyclerViewItemClickListener {

    public boolean isSearchQuery = false;
    int iLoadMoreStatus = 0;
    boolean  bolIgnoreLoadMoreOnCreateView = false;
    int iTabFirstSelectedIgnore = 0;

    public Menu menu1;

    @BindView(R.id.header)
    TextView txtHeader;
    @BindView(R.id.fragment_products_in_stock_recycler_view)
    RecyclerViewEmptyAndLoaderView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.layout_search_edittext)
    EditText mSearchEditText;
    @BindView(R.id.layout_search_btn_go)
    ImageButton mBtnSearchGo;
    Typeface boldTypeface;


    private ProductsListAdapter mBillingRecyclerViewAdapter;
    private ItemsResp mBillingListModel;

    private ArrayList<Items> mBillingModelArrayList = new ArrayList<>();
    private int mPageNo = 0, mTotalRecordsBills = 0;

    private boolean billsInProgress = false;
    private String searchQuery;


    private static final String TAG = "ProductsInStockFragment";

    public ProductsInStockFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_products_in_stock, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        boldTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");



        mPageNo = 0;
        mTotalRecordsBills = 0;
        mBillingListModel = null;
        mBillingModelArrayList.clear();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        mBillingRecyclerViewAdapter = new ProductsListAdapter(mRecyclerView, mBillingModelArrayList, onProductClicked, this);
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
        mBillingRecyclerViewAdapter.mTotalRecordsBills = mTotalRecordsBills;
        mRecyclerView.setNestedScrollingEnabled(false);




        setUpSearchView();

        iLoadMoreStatus = 1;
        //if(iTabFirstSelectedIgnore != 0)
        //bolIgnoreLoadMoreOnCreateView = true;
        //iTabFirstSelectedIgnore = 1;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setTitle(String title)
    {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    private void setUpSearchView()
    {
        mSearchEditText.setHint(getString(R.string.product_search_hint));
        mBtnSearchGo.setVisibility(View.VISIBLE);

        mSearchEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s))
                {
                    mSearchEditText.setHint(getString(R.string.product_search_hint));
                }
            }
        });


        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                    }

                }
                return false;
            }
        });



        mBtnSearchGo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                }
                if (!TextUtils.isEmpty(mSearchEditText.getText()))
                {
                    if(!billsInProgress)
                    {

                        searchQuery = mSearchEditText.getText().toString().trim();
                        mSearchEditText.setHint(searchQuery);

                        loadBills();
                    }else {
                        Toast.makeText(getActivity(), "loading in progress", Toast.LENGTH_SHORT);

                    }
                }else {
                    if(!billsInProgress)
                    {
                        mSearchEditText.setHint(getString(R.string.product_search_hint));
                        loadBills();
                    }else {
                        Toast.makeText(getActivity(), "loading in progress", Toast.LENGTH_SHORT);

                    }
                }
            }
        });
    }


    public void loadBills()
    {
        if (mRecyclerView != null)
        {
            mRecyclerView.setAdapter(null);
            mRecyclerView.invalidate();
        }

        mPageNo = 0;
        mTotalRecordsBills = 0;
        mBillingListModel = null;
        mBillingModelArrayList.clear();

        mBillingRecyclerViewAdapter = new ProductsListAdapter(mRecyclerView, mBillingModelArrayList, onProductClicked, this);
        mBillingRecyclerViewAdapter.mTotalRecordsBills = mTotalRecordsBills;
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);

    }

    @Override
    public void onLoadMore(int currentPage, int totalItemCount)
    {

        iLoadMoreStatus = 0;
        if (bolIgnoreLoadMoreOnCreateView)
        {
            bolIgnoreLoadMoreOnCreateView = false;
            return;
        }

        if (!TextUtils.isEmpty(searchQuery))
        {
            getInStock(currentPage,totalItemCount, searchQuery);
        } else {
            getInStock(currentPage, totalItemCount,"");
        }
    }

    @Override
    public void onLoadMoreError(String msg)
    {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void OnRecyclerViewItemClick(int position)
    {
        //mItemModelList.get(position);
        //showSetProductPriceDialog(mItemModelList.get(position));

		/*Intent intent = new Intent(getActivity(), ProductActivity.class);
		if (isSearchQuery) {
			intent.putExtra(Consts.EXTRA_ITEM, mSearchItemModelList.get(position));
		} else {
			intent.putExtra(Consts.EXTRA_ITEM, mItemModelList.get(position));
		}
		startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_PRODUCT);*/
    }

    public void getInStock(final int currentPage, int totalItemCount, String searchQuery)
    {
        if (!billsInProgress)
        {
            billsInProgress = true;
            try
            {

                new ItemsController( getActivity(), new CustomerNetworkObserver())
                        .getItems((mPageNo + 1), searchQuery);

            }
            catch (Exception ex ) {

                showErrorDialog(ErrorUtils.getFailureError(ex));
                if(mBillingRecyclerViewAdapter != null)
                    mBillingRecyclerViewAdapter.setLoadingCompleteWithData(null);
            }
        }else {
            if(mBillingRecyclerViewAdapter != null)
                mBillingRecyclerViewAdapter.setLoadingCompleteWithData(null);
        }

    }

    class CustomerNetworkObserver implements MOMNetworkResponseListener
    {
        @Override
        public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
        {
            hideLoadingDialog();
            ItemsResp responseData = (ItemsResp) mMOMNetworkResDataStore;

            if(responseData.status ==1)
            {
                billsInProgress = false;
                mBillingListModel = responseData;
                ArrayList<Items> arr = new ArrayList<Items>();

                if (mBillingListModel != null)
                {
                    mTotalRecordsBills = mBillingListModel.total_records;
                    mBillingRecyclerViewAdapter.mTotalRecordsBills = mTotalRecordsBills;

                    if(mBillingListModel.getItems() !=null && mBillingListModel.getItems().size() > 0)
                    {
                        arr.addAll(mBillingListModel.getItems());
                        mPageNo ++;
                    }

                }
                else {
                    showErrorDialog("Error", "Something went wrong: List is null");
                }

                if(mBillingRecyclerViewAdapter != null)
                    mBillingRecyclerViewAdapter.setLoadingCompleteWithData(arr);
                mRecyclerView.setEmptyView(mEmptyView);
                billsInProgress = false;


            }
            else {
                String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
                billsInProgress = false;
                showErrorDialog(errorMsg);
                if(mBillingRecyclerViewAdapter != null)
                    mBillingRecyclerViewAdapter.setLoadingCompleteWithData(null);

            }


        }
    }

    private ProductsListAdapter.ProductListClickListener onProductClicked = new ProductsListAdapter.ProductListClickListener()
    {

        @Override
        public void onAddToStoreClicked(int position) {

        }

        @Override
        public void onViewInStoreClicked(int position) {

        }

        @Override
        public void onProductSelected(Items model, int position) {
            updateCart(model.quantity_id, 1, position);
        }

        @Override
        public void onProductUnSelected(Items model, int position) {
            updateCart(model.quantity_id, 0, position);
        }


        @Override
        public void onnavigationbuttonset(int position) {

        }


    };


    public void updateCart(final String quantity_id, final int update_cart_mode, final int position)
    {
        try
        {
            showLoadingDialog();
            new SalesUpdateCustCartController( getActivity(), new MOMNetworkResponseListener()
            {
                @Override
                public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
                {
                    hideLoadingDialog();
                    SalesUpdateCartResp responseData = (SalesUpdateCartResp) mMOMNetworkResDataStore;
                    if(responseData.status ==1)
                    {
                        mBillingRecyclerViewAdapter.setEditing(false, update_cart_mode, position);

                    }
                    else {
                        String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
                        showErrorDialog(errorMsg);
                    }


                }
            }).updateCustCart(quantity_id, update_cart_mode, 1);

        }
        catch (Exception ex ) {
            hideLoadingDialog();
            showErrorDialog(ErrorUtils.getFailureError(ex));
        }

    }



}
