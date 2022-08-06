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
import com.mom.momcustomerapp.controllers.orders.adapters.BillingMbasketRVAdapter;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrder;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrdersResp;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.customviews.EmptyRecyclerView;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
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
    public boolean isLoaded = false;

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
    private ArrayList<SalesCustOrder> mBillingSearchModelArrayList = new ArrayList<>();
    private int mPageNo = 0, mPreviousPageNo = 0;
    private int mTotalRecordsBills = 0;
    private boolean billsInProgress = false;
    private String searchQuery = "";
    private boolean ignoreLoadMore = true;
    Home_Tab_Activity activity;
    @BindView(R.id.header)
    LinearLayout header;
    public CompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment CompletedFragment.
     */
    public static CompletedFragment newInstance() {
        CompletedFragment fragment = new CompletedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
       // ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

//        this.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//       activity = (Home_Tab_Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        isLoaded = true;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        /*loadBills();*/

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
        mBillingSearchModelArrayList.clear();

        mBillingRecyclerViewAdapter = new BillingMbasketRVAdapter(mRecyclerView, mBillingModelArrayList, this, this);
        mBillingRecyclerViewAdapter.setCurrentPage(mPageNo);
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
    }

    @Override
    public void OnRecyclerViewItemClick(int position) {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        if (isSearchQuery) {
            intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingSearchModelArrayList.get(position).sale_id);
        } else {
            intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingModelArrayList.get(position).sale_id);
        }


        intent.putExtra(Consts.EXTRA_INVOICE_RETURN, Consts.INVOICE_TYPE_BILL);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE, ORDER_STATUS.ORDER_STATUS_DELIVERED);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE_INT_STRING, mBillingModelArrayList.get(position).order_status);
        startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);

    }

    @Override
    public void onLoadMore(int currentPage, int totalItemCount)
    {

        /*if (ignoreLoadMore){
            ignoreLoadMore = false;
            return;
        }*/
        if (isSearchQuery) {
            searchOrderPartial(currentPage, searchQuery);
        } else {
            getOrdersPartial(currentPage, totalItemCount);
        }
    }

    private void setDataToAdapter(List<SalesCustOrder> ordersModelList) {
        mRecyclerView.setEmptyView(mEmptyView);
        mBillingRecyclerViewAdapter.removeItem(null);

        if (isSearchQuery) {
            mBillingSearchModelArrayList = new ArrayList<>(ordersModelList);
        } else {
            mBillingModelArrayList = new ArrayList<>(ordersModelList);
        }
        mBillingRecyclerViewAdapter.resetItems(new ArrayList<>(ordersModelList));
    }

    private void getOrdersPartial(final int currentPage, int totalItemCount)
    {
        /*
        if (!billsInProgress) {
            billsInProgress = true;
            if (totalItemCount <= mTotalRecordsBills || mTotalRecordsBills == 0) {
                if (currentPage <= 1 && mBillingRecyclerViewAdapter != null) {
                    mBillingRecyclerViewAdapter.startFreshLoading();
                }
                OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MventryApp.getInstance().getToken());
                LogUtils.logd("query search","query "+searchQuery+"/.");
                Call<BillingListModelNewOuter> call = ordersClient.getOrdersCompleted(MventryApp.getInstance().getCurrentStoreId(),currentPage + "",searchQuery);
                call.enqueue(new Callback<BillingListModelNewOuter>() {
                    @Override
                    public void onResponse(Call<BillingListModelNewOuter> call, Response<BillingListModelNewOuter> response) {
                        billsInProgress = false;
                        if (response.isSuccessful()) {
                            mPreviousPageNo++;
                            mBillingListModel = response.body();
                            if (mBillingListModel == null){
                                showErrorDialog(getString(R.string.sww));
                                return;
                            }
                            mPageNo = mBillingListModel.getCurrentPage();
                            mTotalRecordsBills = mBillingListModel.getTotalRecords();

                            if (currentPage <= 1) {
                                mBillingModelArrayList = new ArrayList<>(mBillingListModel.getData());
                            } else if (currentPage < mPageNo) {
                                mBillingModelArrayList.addAll(mBillingListModel.getData());
                            } else if (currentPage == mPageNo) {
                                if (mBillingModelArrayList.size() < mTotalRecordsBills) {
                                    if (mPreviousPageNo <= mPageNo) {
                                        mBillingModelArrayList.addAll(mBillingListModel.getData());
                                    } else {
                                        mPreviousPageNo = mPageNo;
                                    }
                                }
                            }

                            if (mBillingListModel != null) {
                                mBillingRecyclerViewAdapter.setCurrentPage(mPageNo);
                                setDataToAdapter(mBillingModelArrayList);
                            } else {
                                showErrorDialog("Error", "Something went wrong: List is null");
                            }
                        }
                        else
                            showErrorDialog(ErrorUtils.getErrorString(response));
                    }

                    @Override
                    public void onFailure(Call<BillingListModelNewOuter> call, Throwable t) {
                        billsInProgress = false;
                        showErrorDialog(ErrorUtils.getFailureError(t));
                        if (!(t instanceof IOException)) {
                        }
                    }
                });
            } else {
                billsInProgress = false;
                mBillingRecyclerViewAdapter.removeItem(null);
            }
        }

         */
    }

    private void searchOrderPartial(final int currentPage, String query)
    {
        /*
        if (!billsInProgress) {
            billsInProgress = true;
            if (currentPage <= 1 && mBillingRecyclerViewAdapter != null) {
                mBillingRecyclerViewAdapter.startFreshLoading();
            }
            OrdersClient ordersClient = ServiceGenerator.createService(OrdersClient.class, MventryApp.getInstance().getToken());
            LogUtils.logd("query search","query "+query+"/.");
            Call<BillingListModelNewOuter> call = ordersClient.getOrdersCompleted(MventryApp.getInstance().getCurrentStoreId(),currentPage + "", query);
            call.enqueue(new Callback<BillingListModelNewOuter>() {
                @Override
                public void onResponse(Call<BillingListModelNewOuter> call, Response<BillingListModelNewOuter> response) {
                    billsInProgress = false;
                    if (response.isSuccessful()) {
                        BillingListModelNewOuter searchBillingListModel = response.body();
                        if (searchBillingListModel == null){
                            showErrorDialog(getString(R.string.sww));
                            return;
                        }
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
                            mBillingRecyclerViewAdapter.setCurrentPage(searchBillingListModel.getCurrentPage());
                            setDataToAdapter(mBillingSearchModelArrayList);
                        } else {
                            showErrorDialog("Error", "Something went wrong: List is null");
                        }
                    } else {
                        showErrorDialog(ErrorUtils.getErrorString(response));
                    }
                }

                @Override
                public void onFailure(Call<BillingListModelNewOuter> call, Throwable t) {
                    billsInProgress = false;
                    showErrorDialog(ErrorUtils.getFailureError(t));
                    if (!(t instanceof IOException)) {
                    }
                }
            });
        }

         */
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) onLoadMore(1, 0);
    }
}

