package com.mom.momcustomerapp.views.orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.adapters.ReturnsRecyclerViewAdapter;
import com.mom.momcustomerapp.controllers.sales.api.OrdersClient;
import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNew;
import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNewOuter;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.customviews.EmptyRecyclerView;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.views.home.HomeFragment;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mswipetech.sdk.network.util.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReturnsFragment extends BaseFragment implements RecyclerViewItemClickListener, OnLoadMoreListener
{


    public boolean isSearchQuery = false;
    public boolean isLoaded = false;

    @BindView(R.id.fragment_returns_recycler_view)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.layout_search_edittext)
    EditText mSearchEditText;
    @BindView(R.id.layout_search_btn_go)
    ImageButton mBtnSearchGo;

    private ReturnsRecyclerViewAdapter mReturnsRecyclerViewAdapter;
    private BillingListModelNewOuter mBillingListModel;
    private ArrayList<BillingListModelNew> mBillingModelArrayList = new ArrayList<>();
    private ArrayList<BillingListModelNew> mBillingSearchModelArrayList = new ArrayList<>();
    private int mPageNo = 0, mPreviousPageNo = 0;
    private int mTotalRecordsReturns = 0;
    private boolean returnsInProgress = false;
    private String searchQuery;
    private long lastUpdatedTime;
    private boolean isFetchInitiated = false;
    private boolean ignoreLoadMore = true;
    @BindView(R.id.header)
    LinearLayout header;

    public ReturnsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment PendingOrdersFragment.
     */
    public static ReturnsFragment newInstance() {
        ReturnsFragment fragment = new ReturnsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sales_returns, container, false);
        ButterKnife.bind(this, rootView);


//        Bundle bundle = this.getArguments();
//        if(bundle != null){
//            if(bundle.get("key")=="declined"){
//                header.setVisibility(View.VISIBLE);
//            }
//        }

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
        mReturnsRecyclerViewAdapter = new ReturnsRecyclerViewAdapter(mRecyclerView,
                mBillingModelArrayList, this, this);
        mRecyclerView.setAdapter(mReturnsRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/

        setUpSearchView();
        isLoaded = true;

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //loadReturns();


    }

    private void setUpSearchView()
    {
        mSearchEditText.setHint(getString(R.string.orders_search_hint));
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (/*id == R.id.search || */id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_SEARCH) {
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

        mSearchEditText.addTextChangedListener(new TextWatcher()
        {
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
        if (requestCode == Consts.REQUEST_CODE_CART) {
            //MventryCart.loadCartContents();
            mPageNo = 0;
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

    public void loadReturns()
    {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.invalidate();
        }

        mPageNo = 0;
        mPreviousPageNo = 0;
        mTotalRecordsReturns = 0;
        mPreviousPageNo = 0;
        mBillingListModel = null;
        mBillingModelArrayList.clear();
        mBillingSearchModelArrayList.clear();

        mReturnsRecyclerViewAdapter = new ReturnsRecyclerViewAdapter(mRecyclerView, mBillingModelArrayList, this, this);
        mReturnsRecyclerViewAdapter.setCurrentPage(mPageNo);
        mRecyclerView.setAdapter(mReturnsRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
    }

    @Override
    public void OnRecyclerViewItemClick(int position) {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        if (isSearchQuery) {
            intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingSearchModelArrayList.get(position).getSaleId());
        } else {
            intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingModelArrayList.get(position).getSaleId());
        }
        intent.putExtra(Consts.EXTRA_INVOICE_RETURN, Consts.INVOICE_TYPE_RETURN);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE, ORDER_STATUS.ORDER_STATUS_RETURNED);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE_INT_STRING,
                mBillingModelArrayList.get(position).getOrder_statusInIntString());
        startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);
    }

    @Override
    public void onLoadMore(int currentPage, int totalItemCount)
    {

        if (ignoreLoadMore)
        {
            ignoreLoadMore = false;
            return;
        }

        if (isSearchQuery) {
            //getOrdersReturns(currentPage, totalItemCount, searchQuery);
        } else {
            //getOrdersReturns(currentPage, totalItemCount,"");
        }

        /*if (isSearchQuery) {
            searchOrderReturns(currentPage, searchQuery);
        } else {
            getOrdersReturns(currentPage, totalItemCount,"");
        }*/
    }

    private void setDataToAdapter(List<BillingListModelNew> ordersModelList) {
        mRecyclerView.setEmptyView(mEmptyView);
        mReturnsRecyclerViewAdapter.removeItem(null);

        if (isSearchQuery) {
            mBillingSearchModelArrayList = new ArrayList<>(ordersModelList);
        } else {
            mBillingModelArrayList = new ArrayList<>(ordersModelList);
        }
        mReturnsRecyclerViewAdapter.resetItems(new ArrayList<>(ordersModelList));
    }

    private void getOrdersReturns(final int currentPage, int totalItemCount, String searchQuery)
    {
        if (!returnsInProgress) {
            returnsInProgress = true;
            if (totalItemCount <= mTotalRecordsReturns || mTotalRecordsReturns == 0) {
                if (currentPage <= 1 && mReturnsRecyclerViewAdapter != null) {
                    mReturnsRecyclerViewAdapter.startFreshLoading();
                }
                OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MOMApplication.getInstance().getToken());

                Call<BillingListModelNewOuter> call = ordersClient.returnsListingApiMBasket(
                        MOMApplication.getInstance().getStoreId(),searchQuery,currentPage + "");
                call.enqueue(new Callback<BillingListModelNewOuter>() {
                    @Override
                    public void onResponse(Call<BillingListModelNewOuter> call, Response<BillingListModelNewOuter> response) {
                        returnsInProgress = false;
                        if (response.isSuccessful()) {mPreviousPageNo++;
                            mBillingListModel = response.body();
                            if (mBillingListModel == null){
                                showErrorDialog(getString(R.string.sww));
                                return;
                            }
                            mPageNo = mBillingListModel.getCurrentPage();
                            mTotalRecordsReturns = mBillingListModel.getTotalRecords();

                            if (currentPage <= 1) {
                                mBillingModelArrayList = new ArrayList(mBillingListModel.getData());
                            } else if (currentPage < mPageNo) {
                                mBillingModelArrayList.addAll(mBillingListModel.getData());
                            } else if (currentPage == mPageNo) {
                                if (mBillingModelArrayList.size() < mTotalRecordsReturns) {
                                    if (mPreviousPageNo <= mPageNo) {
                                        mBillingModelArrayList.addAll(mBillingListModel.getData());
                                    } else {
                                        mPreviousPageNo = mPageNo;
                                    }
                                }
                            }

                            if (mBillingListModel != null) {
                                mReturnsRecyclerViewAdapter.setCurrentPage(mPageNo);
                                setDataToAdapter(mBillingModelArrayList);
                            } else {
                                showErrorDialog("Error", "Something went wrong: List is null");
                            }
                        } else {
                            showErrorDialog(ErrorUtils.getErrorString(response));
                        }
                        lastUpdatedTime = System.currentTimeMillis();
                    }

                    @Override
                    public void onFailure(Call<BillingListModelNewOuter> call, Throwable t) {
                        returnsInProgress = false;
                        showErrorDialog(ErrorUtils.getFailureError(t));
                    }
                });
            } else {
                returnsInProgress = false;
                mReturnsRecyclerViewAdapter.removeItem(null);
            }
        }
    }

    /*private void searchOrderReturns(final int currentPage, String query) {
        if (!returnsInProgress) {
            returnsInProgress = true;
            if (currentPage <= 1 && mReturnsRecyclerViewAdapter != null) {
                mReturnsRecyclerViewAdapter.startFreshLoading();
            }
            OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MventryApp.getInstance().getToken());

            Call<BillingListModel> call = ordersClient.searchOrderReturns(currentPage + "", query);
            call.enqueue(new Callback<BillingListModel>() {
                @Override
                public void onResponse(Call<BillingListModel> call, Response<BillingListModel> response) {
                    returnsInProgress = false;
                    if (response.isSuccessful()) {
                        BillingListModel searchBillingListModel = response.body();

                        if (currentPage <= 1) {
                            mBillingSearchModelArrayList = new ArrayList<>(searchBillingListModel.getData());
                        } else if (currentPage < mPageNo) {
                            mBillingSearchModelArrayList.addAll(searchBillingListModel.getData());
                        } else if (currentPage == mPageNo) {
                            if (mBillingSearchModelArrayList.size() < searchBillingListModel.getTotalRecords()) {
                                mBillingSearchModelArrayList.addAll(searchBillingListModel.getData());
                            }
                        }

                        if (mBillingSearchModelArrayList != null) {
                            mReturnsRecyclerViewAdapter.setCurrentPage(searchBillingListModel.getCurrentPage());
                            setDataToAdapter(mBillingSearchModelArrayList);
                        } else {
                            showErrorDialog("Error", "Something went wrong: List is null");
                        }
                    } else {
                        try {
                            if (response.code() == 401) {
                                AppUtils.logOutUserForUpdate(getActivity());
                            } else {
                                Response<String> response1 = Response.success(response.errorBody().string().replace("\n", ""));
                                parseError(response1, true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (response.body() != null) {
                                showErrorDialog("Error", "Something went wrong: " + response.body());
                            } else {
                                showErrorDialog("Error", "Something went wrong: " + response.message());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<BillingListModel> call, Throwable t) {
                    returnsInProgress = false;
                    if (t instanceof SocketTimeoutException || t instanceof IOException) {
                        showErrorDialog("Network Error", "No Network. Please check connection");
                    } else {
                        showErrorDialog("Error", "Something went wrong : " + t.getMessage());
                        Log.e("ERROR", "onFailure: Something went wrong", t);
                    }
                }
            });
        }
    }*/

    public void searchBarEditorAction(String searchString) {
        if (!TextUtils.isEmpty(searchString)) {
            searchQuery = searchString;
            isSearchQuery = true;

            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(null);
                mRecyclerView.invalidate();
            }

            mPageNo = 0;
            mPreviousPageNo = 0;
            mTotalRecordsReturns = 0;
            mBillingListModel = null;
            mBillingModelArrayList.clear();
            mReturnsRecyclerViewAdapter = new ReturnsRecyclerViewAdapter(mRecyclerView, mBillingModelArrayList, ReturnsFragment.this, ReturnsFragment.this);
            mReturnsRecyclerViewAdapter.setCurrentPage(mPageNo);
            mRecyclerView.setAdapter(mReturnsRecyclerViewAdapter);
            /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
            loadReturns();
        }
    }

    public void searchBarTextCleared() {
        if (isSearchQuery) {
            searchQuery = "";
            isSearchQuery = false;
            loadReturns();
        } else {
            searchQuery = "";
            isSearchQuery = false;
        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) onLoadMore(1, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
       // ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }


    @Override
    public void onStop() {
        super.onStop();
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
