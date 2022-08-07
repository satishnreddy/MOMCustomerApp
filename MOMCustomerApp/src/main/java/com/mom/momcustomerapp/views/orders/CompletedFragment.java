package com.mom.momcustomerapp.views.orders;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.SalesCustomerOrdersController;
import com.mom.momcustomerapp.controllers.orders.adapters.BillingMbasketRVAdapter;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrder;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrdersResp;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.customviews.EmptyRecyclerView;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.home.HomeFragment;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mswipetech.sdk.network.util.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedFragment extends BaseFragment implements RecyclerViewItemClickListener, OnLoadMoreListener {


    public boolean isSearchQuery = false;
    int iLoadMoreStatus = 0;
    public boolean bolIgnoreLoadMoreOnCreateView = false;


    @BindView(R.id.fragment_bills_recycler_view)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.layout_search_edittext)
    EditText mSearchEditText;
    @BindView(R.id.layout_search_btn_go)
    ImageButton mBtnSearchGo;


    private BillingMbasketRVAdapter mBillingRecyclerViewAdapter;
    private SalesCustOrdersResp mBillingListModel;
    private ArrayList<SalesCustOrder> mBillingModelArrayList = new ArrayList<>();


    private int mPageNo = 0, mPreviousPageNo = 0;
    private int mTotalRecordsBills = 0;
    private boolean billsInProgress = false;
    private String searchQuery = "";
    @BindView(R.id.header)
    LinearLayout header;

    private Home_Tab_Activity activity;


    public CompletedFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment CompletedFragment.
     */
    public static CompletedFragment newInstance()
    {
        CompletedFragment fragment = new CompletedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (Home_Tab_Activity) context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sales_completed_bills, container, false);
        ButterKnife.bind(this, rootView);



        ImageView backarrow = rootView.findViewById(R.id.back_arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.flFragment, HomeFragment.newInstance()).commit();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton mFab = getActivity().findViewById(R.id.fragment_home_fab);
        mRecyclerView.setScrollingFab(mFab);
        mBillingRecyclerViewAdapter = new BillingMbasketRVAdapter(mRecyclerView, mBillingModelArrayList,this, this);
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
        setUpSearchView();

        iLoadMoreStatus = 1;
        //bolIgnoreLoadMoreOnCreateView = true;


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
       // loadBills();

    }

    private void setUpSearchView()
    {
        mSearchEditText.setHint(getString(R.string.orders_search_hint));
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.search || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);

                    if (!TextUtils.isEmpty(mSearchEditText.getText())) {
                        searchQuery = mSearchEditText.getText().toString().trim();
                        searchBarEditorAction(searchQuery);
                        return true;
                    }
                }
                return false;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    searchQuery = "";
                } else {
                    searchQuery = s.toString().trim();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchQuery = "";
                    searchBarTextCleared();
                    mBtnSearchGo.setVisibility(View.GONE);
                } else {
                    mBtnSearchGo.setVisibility(View.VISIBLE);
                }
            }
        });

        mBtnSearchGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                if (!TextUtils.isEmpty(mSearchEditText.getText())) {
                    searchQuery = mSearchEditText.getText().toString().trim();
                    searchBarEditorAction(searchQuery);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == Consts.REQUEST_CODE_CART)
        {
            //MventryCart.loadCartContents();
            mPageNo = 0;
            mTotalRecordsBills = 0;
            mBillingListModel = null;
            mBillingModelArrayList.clear();
            onLoadMore(1, 0);

        } else if (requestCode == Consts.REQUEST_CODE_VIEW_INVOICE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.hasExtra(Consts.EXTRA_ISUPDATED)) {
                    if (data.getBooleanExtra(Consts.EXTRA_ISUPDATED, false)) {
                        onLoadMore(1, 0);
                    }
                }
            }
        }
    }

    public void loadBills()
    {

        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.invalidate();
        }

        mPageNo = 0;
        mTotalRecordsBills = 0;
        mPreviousPageNo = 0;
        mBillingListModel = null;
        mBillingModelArrayList.clear();
        //mBillingSearchModelArrayList.clear();

        mBillingRecyclerViewAdapter = new BillingMbasketRVAdapter(mRecyclerView, mBillingModelArrayList, this, this);
        mBillingRecyclerViewAdapter.setCurrentPage(mPageNo);
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
    }

    @Override
    public void OnRecyclerViewItemClick(int position)
    {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);

        intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingModelArrayList.get(position).sale_id);
        intent.putExtra(Consts.EXTRA_INVOICE_RETURN, Consts.INVOICE_TYPE_BILL);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE, ORDER_STATUS.ORDER_STATUS_DELIVERED);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE_INT_STRING, mBillingModelArrayList.get(position).order_status);
        startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);

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

        if (isSearchQuery) {
            getOrdersCompleted(currentPage,totalItemCount, searchQuery);
        }
        else {
            getOrdersCompleted(currentPage, totalItemCount,"");
        }
    }

    private void setDataToAdapter(List<SalesCustOrder> ordersModelList)
    {
        mRecyclerView.setEmptyView(mEmptyView);
        mBillingRecyclerViewAdapter.removeItem(null);


        mBillingModelArrayList = new ArrayList<>(ordersModelList);

        mBillingRecyclerViewAdapter.resetItems(new ArrayList<>(ordersModelList));
    }

    private void getOrdersCompleted(final int currentPage, int totalItemCount, String searchQuery)
    {

        if (!billsInProgress)
        {
            billsInProgress = true;
            if (totalItemCount <= mTotalRecordsBills || mTotalRecordsBills == 0)
            {
                if (currentPage <= 1 && mBillingRecyclerViewAdapter != null)
                {
                    mBillingRecyclerViewAdapter.startFreshLoading();
                }


                try {
                    new SalesCustomerOrdersController( activity, new CompletedFragment.CustomerNetworkObserver())
                            .getSalesCustCompletedOrders(currentPage, searchQuery);
                }
                catch (Exception ex ) {

                    showErrorDialog(ErrorUtils.getFailureError(ex));
                }


            } else {
                billsInProgress = false;
                if (mBillingRecyclerViewAdapter != null) {
                    mBillingRecyclerViewAdapter.removeItem(null);
                }
            }
        }

    }


    class CustomerNetworkObserver implements MOMNetworkResponseListener
    {

        @Override
        public void onReponseData(MOMNetworkResDataStore mMOMNetworkResDataStore)
        {
            SalesCustOrdersResp responseData = (SalesCustOrdersResp) mMOMNetworkResDataStore;
            if(responseData.status ==1)
            {
                billsInProgress = false;
                mPreviousPageNo++;
                mBillingListModel = responseData;

                if (mBillingListModel != null)
                {
                    mPageNo = mBillingListModel.current_page ;
                    mTotalRecordsBills = mBillingListModel.total_records;

                    //the api call will actaully currentPage-1, so adding + 1 below to get
                    //the actuall current page passed
                    int currentPage = mPageNo +1;

                    if (currentPage <= 1)
                    {
                        mBillingModelArrayList = new ArrayList<>(mBillingListModel.getData());
                    }
                    else if (currentPage < mPageNo)
                    {
                        mBillingModelArrayList.addAll(mBillingListModel.getData());
                    }
                    else if (currentPage == mPageNo)
                    {
                        if (mBillingModelArrayList.size() < mTotalRecordsBills)
                        {
                            if (mPreviousPageNo <= mPageNo)
                            {
                                mBillingModelArrayList.addAll(mBillingListModel.getData());
                            }
                            else {
                                mPreviousPageNo = mPageNo;
                            }
                        }
                    }

                    if (mBillingListModel != null)
                    {
                        mBillingRecyclerViewAdapter.setCurrentPage(mPageNo);
                        setDataToAdapter(mBillingModelArrayList);
                    }
                    else {
                        showErrorDialog("Error", "Something went wrong: List is null");
                    }

                }
                else {
                    showErrorDialog("Error", "Something went wrong: List is null");
                }


            }
            else {
                String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
                billsInProgress = false;
                showErrorDialog(errorMsg);
            }


        }
    }


    private void initiateBillingRecyclerViewAdapter(int currentPage)
    {
        mBillingRecyclerViewAdapter = new BillingMbasketRVAdapter(mRecyclerView, mBillingModelArrayList, this, this);
        mBillingRecyclerViewAdapter.setCurrentPage(currentPage);
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
    }

    private void searchBarEditorAction(String searchString)
    {
        if (!TextUtils.isEmpty(searchString)) {
            searchQuery = searchString;
            isSearchQuery = true;

            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(null);
                mRecyclerView.invalidate();
            }
            mPageNo = 0;
            mPreviousPageNo = 0;

            mTotalRecordsBills = 0;
            mBillingListModel = null;
            mBillingModelArrayList.clear();
            mBillingRecyclerViewAdapter = new BillingMbasketRVAdapter(mRecyclerView, mBillingModelArrayList, CompletedFragment.this, CompletedFragment.this);
            mBillingRecyclerViewAdapter.setCurrentPage(mPageNo);
            mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
            /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
            loadBills();
        }
    }

    private void searchBarTextCleared() {
        if (isSearchQuery) {
            searchQuery = "";
            isSearchQuery = false;
            loadBills();
        } else {
            searchQuery = "";
            isSearchQuery = false;
        }
    }


}

