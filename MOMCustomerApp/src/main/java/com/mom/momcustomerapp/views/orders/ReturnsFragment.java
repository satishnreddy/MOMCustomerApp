package com.mom.momcustomerapp.views.orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.orders.SalesCustomerOrdersController;
import com.mom.momcustomerapp.controllers.orders.adapters.BillingMbasketRVAdapter;
import com.mom.momcustomerapp.controllers.orders.adapters.ReturnsRecyclerViewAdapter;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrder;
import com.mom.momcustomerapp.controllers.orders.models.SalesCustOrdersResp;
import com.mom.momcustomerapp.controllers.sales.api.OrdersClient;
import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNew;
import com.mom.momcustomerapp.controllers.sales.models.BillingListModelNewOuter;
import com.mom.momcustomerapp.controllers.sales.models.ORDER_STATUS;
import com.mom.momcustomerapp.customviews.EmptyRecyclerView;
import com.mom.momcustomerapp.customviews.RecyclerViewEmptyAndLoaderView;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.OnRecylerViewLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.home.HomeFragment;
import com.mom.momcustomerapp.views.home.Home_Tab_Activity;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mswipetech.sdk.network.util.Logs;

import java.net.ConnectException;
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
public class ReturnsFragment extends BaseFragment implements RecyclerViewItemClickListener,
        OnRecylerViewLoadMoreListener
{

    public boolean isSearchQuery = false;
    int iLoadMoreStatus = 0;
    boolean bolIgnoreLoadMoreOnCreateView = false;

    @BindView(R.id.fragment_returns_recycler_view)
    RecyclerViewEmptyAndLoaderView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.layout_search_edittext)
    EditText mSearchEditText;
    @BindView(R.id.layout_search_btn_go)
    ImageButton mBtnSearchGo;

    private ReturnsRecyclerViewAdapter mBillingRecyclerViewAdapter;
    private SalesCustOrdersResp mBillingListModel;

    private ArrayList<SalesCustOrder> mBillingModelArrayList = new ArrayList<>();
    private int mPageNo = 0, mTotalRecordsBills = 0;

    private boolean billsInProgress = false;
    private String searchQuery;


    @BindView(R.id.header)
    LinearLayout header;

    private Home_Tab_Activity activity;


    public ReturnsFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment PendingOrdersFragment.
     */
    public static ReturnsFragment newInstance()
    {
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

        ImageView backarrow = rootView.findViewById(R.id.back_arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.flFragment, HomeFragment.newInstance()).commit();

            }
        });



        mPageNo = 0;
        mTotalRecordsBills = 0;

        mBillingListModel = null;
        mBillingModelArrayList.clear();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBillingRecyclerViewAdapter = new ReturnsRecyclerViewAdapter(mRecyclerView, mBillingModelArrayList, this, this);
        mRecyclerView.setAdapter(mBillingRecyclerViewAdapter);
        mBillingRecyclerViewAdapter.mTotalRecordsBills = mTotalRecordsBills;


        setUpSearchView();

        iLoadMoreStatus = 1;
        //bolIgnoreLoadMoreOnCreateView = true;


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (Home_Tab_Activity) context;


    }

    private void setUpSearchView()
    {
        mSearchEditText.setHint(getString(R.string.orders_search_hint));
        mBtnSearchGo.setVisibility(View.VISIBLE);
        mSearchEditText.setInputType(InputType.TYPE_CLASS_NUMBER);


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
                    mSearchEditText.setHint(getString(R.string.orders_search_hint));
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
                        mSearchEditText.setHint(getString(R.string.orders_search_hint));
                        loadBills();
                    }else {
                        Toast.makeText(getActivity(), "loading in progress", Toast.LENGTH_SHORT);

                    }
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Consts.REQUEST_CODE_CART) {
            //MventryCart.loadCartContents();

            loadBills();

        } else if (requestCode == Consts.REQUEST_CODE_VIEW_INVOICE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.hasExtra(Consts.EXTRA_ISUPDATED)) {
                    if (data.getBooleanExtra(Consts.EXTRA_ISUPDATED, false)) {
                        loadBills();
                    }
                }
            }
        }
    }


    @Override
    public void OnRecyclerViewItemClick(int position)
    {
        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        intent.putExtra(Consts.EXTRA_ORDER_ID, mBillingModelArrayList.get(position).sale_id);
        intent.putExtra(Consts.EXTRA_ORDER_AMOUNT, mBillingModelArrayList.get(position).total_price);
        intent.putExtra(Consts.EXTRA_ORDER_INVOCIE, mBillingModelArrayList.get(position).invoice_number);
        intent.putExtra(Consts.EXTRA_INVOICE_RETURN, Consts.INVOICE_TYPE_BILL);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE, mBillingModelArrayList.get(position).delivery_status);
        intent.putExtra(Consts.EXTRA_CURRENT_STATUS_CODE_INT_STRING, mBillingModelArrayList.get(position).order_status);
        startActivityForResult(intent, Consts.REQUEST_CODE_VIEW_INVOICE);
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

        mBillingRecyclerViewAdapter = new ReturnsRecyclerViewAdapter(mRecyclerView, mBillingModelArrayList, this, this);
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
            getOrdersReturns(currentPage,totalItemCount, searchQuery);
        } else {
            getOrdersReturns(currentPage, totalItemCount,"");
        }
    }

    @Override
    public void onLoadMoreError(String msg)
    {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }


    private void getOrdersReturns(final int currentPage, int totalItemCount, String searchQuery)
    {

        if (!billsInProgress)
        {
            billsInProgress = true;


            try {
                new SalesCustomerOrdersController( activity, new ReturnsFragment.CustomerNetworkObserver())
                        .getSalesCustReturnedOrders(mPageNo + 1, searchQuery);
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
            SalesCustOrdersResp responseData = (SalesCustOrdersResp) mMOMNetworkResDataStore;
            if(responseData.status ==1)
            {
                billsInProgress = false;
                mBillingListModel = responseData;
                ArrayList<SalesCustOrder> arr = new ArrayList<SalesCustOrder>();


                if (mBillingListModel != null)
                {
                    mTotalRecordsBills = mBillingListModel.total_records;
                    mBillingRecyclerViewAdapter.mTotalRecordsBills = mTotalRecordsBills;

                    if(mBillingListModel.getData() !=null && mBillingListModel.getData().size() > 0)
                    {
                        arr.addAll(mBillingListModel.getData());
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





}
