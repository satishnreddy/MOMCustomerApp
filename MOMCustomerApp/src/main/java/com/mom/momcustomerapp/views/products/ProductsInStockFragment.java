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
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.products.ItemsController;
import com.mom.momcustomerapp.controllers.products.adapter.ProductsListAdapter;
import com.mom.momcustomerapp.controllers.products.api.CustomerClient;
import com.mom.momcustomerapp.controllers.products.api.ItemsClient;
import com.mom.momcustomerapp.controllers.products.models.CategoryV2Model;
import com.mom.momcustomerapp.controllers.products.models.Items;
import com.mom.momcustomerapp.controllers.products.models.ItemsResp;
import com.mom.momcustomerapp.controllers.products.models.SubCategoryV2Model;
import com.mom.momcustomerapp.controllers.sales.SalesUpdateCustCartController;
import com.mom.momcustomerapp.controllers.sales.models.SalesUpdateCartResp;
import com.mom.momcustomerapp.customviews.EmptyRecyclerView;
import com.mom.momcustomerapp.customviews.dialogs.OutputResultDialogFragment;
import com.mom.momcustomerapp.customviews.dialogs.SingleBtnDialogFragment;
import com.mom.momcustomerapp.customviews.dialogs.TwoBtnDialogFragment;
import com.mom.momcustomerapp.data.application.Consts;
import com.mom.momcustomerapp.data.application.MOMApplication;
import com.mom.momcustomerapp.data.shared.network.MOMNetworkResDataStore;
import com.mom.momcustomerapp.interfaces.OnLoadMoreListener;
import com.mom.momcustomerapp.interfaces.RecyclerViewItemClickListener;
import com.mom.momcustomerapp.networkservices.ErrorUtils;
import com.mom.momcustomerapp.networkservices.ServiceGenerator;
import com.mom.momcustomerapp.observers.network.MOMNetworkResponseListener;
import com.mom.momcustomerapp.views.products.dialogs.ProductPriceDialogFragment;
import com.mom.momcustomerapp.views.shared.BaseActivity;
import com.mom.momcustomerapp.views.shared.BaseFragment;
import com.mom.momcustomerapp.widget.SafeClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsInStockFragment extends BaseFragment implements OnLoadMoreListener,
        RecyclerViewItemClickListener {

    private static final int RESULT_CODE_ITEMS_ADDED = 341;
    public boolean isSearchQuery = false;
    public boolean isLoaded = false;

    public Menu menu1;

    @BindView(R.id.header)
    TextView txtHeader;
    @BindView(R.id.fragment_products_in_stock_recycler_view)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.layout_search_edittext)
    EditText mSearchEditText;
    @BindView(R.id.layout_search_btn_go)
    ImageButton mBtnSearchGo;
    @BindView(R.id.fragment_products_in_stock_label_cat_subcat_tv)
    TextView catSubCatLabelTv;
    @BindView(R.id.fragment_products_in_stock_to_be_selected_cat_ll)
    ViewGroup categoriesVG;
    @BindView(R.id.fragment_products_in_stock_to_be_selected_subcat_ll)
    ViewGroup subCategoriesVG;
    Typeface boldTypeface;
    @BindView(R.id.fragment_products_in_stock_label_subcat)
    TextView getCatSubCatLabelTv;
    @BindView(R.id.fragment_products_in_stock_rl_edit_subcat)
    View editSubCatBtn;
    @BindView(R.id.fragment_products_in_stock_rl_edit_subcat_new)
    RelativeLayout mLINSubCatMain;
    @BindView(R.id.fragment_products_in_stock_lv_edit_subcat_new)
    ListView mLVSubCatMain;
    @BindView(R.id.fragment_products_in_stock_btn_edit_subcat_up)
    ImageView mIMGCatMainUp;
    @BindView(R.id.fragment_products_in_stock_btn_edit_subcat_down)
    ImageView mIMGCatMainDown;
    @BindView(R.id.fragment_products_in_stock_subcat)
    View subCatView;
    @BindView(R.id.cat_left)
    View catLeftShift;
    @BindView(R.id.cat_right)
    View catRightShift;

    /*private NestedScrollView mScrollView;*/
    private View btmSubCatsView;


    //    private ProductsRecyclerViewAdapter mProductsRecyclerViewAdapter;
    private ProductsListAdapter mProductsRecyclerViewAdapter;
    private ItemsResp mItemsListModel;
    private List<Items> mItemModelList = new ArrayList<>();

    //private List<ProductListModel> mSearchItemModelList = new ArrayList<>();
    private HashSet<Items> selectedProductsList = new HashSet<>();

    private int mPageNo = 0, mTotalRecords = 0, mPreviousPageNo = 0;
    /*private boolean inApiInProgress = false;*/
    private String searchQuery;
    /*private List<ProductListModel> newProductListModel;*/

    CategoryV2Model categoryModel;
    SubCategoryV2Model subCategoryModel;
    private List<CategoryV2Model> categoryList;
    private List<SubCategoryV2Model> subCategoryList;


    private static final String TAG = "ProductsInStockFragment";
    private boolean isPreSelectedSubCat = false;
    private boolean isPreSelectedSubCat2 = false;
    public boolean isEditing = true;
    private String checkboxvalue;

    public ProductsInStockFragment() {
        // Required empty public constructor
    }

    public static ProductsInStockFragment newInstance(boolean isForNewProduct)
    {
        ProductsInStockFragment fragment = new ProductsInStockFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.EXTRA_IS_FROM_NEW_CAT_SUBCAT, isForNewProduct);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ProductsInStockFragment newInstance(boolean isForNewProduct, String catId, String categoryName,
                                                      String subCatId, String subCategoryName)
    {
        ProductsInStockFragment fragment = new ProductsInStockFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.EXTRA_IS_FROM_NEW_CAT_SUBCAT, isForNewProduct);
        bundle.putString(Consts.EXTRA_CATEGORY_ID, catId);
        bundle.putString(Consts.EXTRA_CATEGORY_NAME, categoryName);
        bundle.putString(Consts.EXTRA_SUB_CATEGORY_ID, subCatId);
        bundle.putString(Consts.EXTRA_SUB_CATEGORY_NAME, subCategoryName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ProductsInStockFragment newInstance(boolean isForNewProduct, CategoryV2Model categoryV2Model,
                                                      SubCategoryV2Model subCategoryV2Model)
    {
        ProductsInStockFragment fragment = new ProductsInStockFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.EXTRA_IS_FROM_NEW_CAT_SUBCAT, isForNewProduct);
        bundle.putParcelable(Consts.EXTRA_CATEGORY_ID, categoryV2Model);
        bundle.putParcelable(Consts.EXTRA_SUB_CATEGORY_ID, subCategoryV2Model);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_products_in_stock, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);


        boldTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton mFab = getActivity().findViewById(R.id.fragment_home_fab);
        mRecyclerView.setScrollingFab(mFab);
        mProductsRecyclerViewAdapter = new ProductsListAdapter(mRecyclerView, mItemModelList,
                selectedProductsList, onProductClicked,
                ProductsInStockFragment.this, isEditing, isPreSelectedSubCat,
                checkboxvalue);

        mProductsRecyclerViewAdapter.setHasStableIds(false);
		mRecyclerView.setAdapter(mProductsRecyclerViewAdapter);
        /*mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));*/
        mRecyclerView.setNestedScrollingEnabled(false);


		/*btmSubCatsView = rootView.findViewById(R.id.fragment_products_in_stock_btm_subcat);
		mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged()
			{
				View view = (View)mScrollView.getChildAt(mScrollView.getChildCount() - 1);

				int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView
						.getScrollY()+ btmSubCatsView.getHeight() ));

				if (diff <= 0) {
					// your pagination code
					LogUtils.logd(TAG,"pagination triggered.");
				}
			}
		});*/

        setUpSearchView();
        /*
        if (getArguments() != null)
        {
            categoryModel = getArguments().getParcelable(EXTRA_CATEGORY_ID);
            subCategoryModel = getArguments().getParcelable(EXTRA_SUB_CATEGORY_ID);
            if (categoryModel != null) {
                isPreSelectedSubCat = true;
                isPreSelectedSubCat2 = true;
                isEditing = true;
                catSubCatLabelTv.setVisibility(View.VISIBLE);
                catSubCatLabelTv.setText(categoryModel.getCategoryName() + " > " + subCategoryModel.getCatName());
                setSelectionSubCategory(categoryModel, subCategoryModel);
                subCatView.setVisibility(View.GONE);
            } else {
                getCatList();
            }

        }
        */

        mPageNo = 0;
        mPreviousPageNo = 0;
        mTotalRecords = 0;
        mItemsListModel = null;
        mItemModelList.clear();

        isLoaded = true;

        editSubCatBtn.setOnClickListener(new SafeClickListener(new SafeClickListener.Callback() {
            @Override
            public void onClick(View v) {
                showSelectSubCatDialog();
            }
        }));

        rootView.findViewById(R.id.subcat_left).setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
        {
            @Override
            public void onClick(View v) {
                ((HorizontalScrollView) subCategoriesVG.getParent()).smoothScrollTo(0, 0);
            }
        }));

        rootView.findViewById(R.id.subcat_right).setOnClickListener(new SafeClickListener(new SafeClickListener.Callback()
        {
            @Override
            public void onClick(View v) {
                ((HorizontalScrollView) subCategoriesVG.getParent()).fullScroll(HorizontalScrollView.FOCUS_RIGHT);

            }
        }));

        catLeftShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int currentCategoryIndex = categoryList.indexOf(categoryModel);
                if (currentCategoryIndex > 0) {
                    setSelectionCategory(currentCategoryIndex - 1);
                }
            }
        });

        catRightShift.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int currentCategoryIndex = categoryList.indexOf(categoryModel);
                if (currentCategoryIndex < categoryList.size() - 1) {
                    setSelectionCategory(currentCategoryIndex + 1);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        changeToEditMode(isEditing);
    }

    public void changeToEditMode(boolean toEdit)
    {
        /*isEditing = toEdit;
        if (isEditing) {
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar()
                    .setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            setTitle(getString(R.string.products_generic_select_multiple_items));
        }

        else {
            selectedProductsList.clear();
            setTitle(getString(R.string.products_generic_title_products));
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar()
                    .setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        getActivity().invalidateOptionsMenu();
        if (mProductsRecyclerViewAdapter != null) {
            mProductsRecyclerViewAdapter.setEditing(toEdit);
        }*/

        txtHeader.setText(R.string.products_generic_title_products);

    }

    private void setTitle(String title)
    {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    private void setUpSearchView()
    {
        mSearchEditText.setHint(getString(R.string.product_search_hint));
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (/*id == R.id.search || */ id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_SEARCH)
                {
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
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (TextUtils.isEmpty(s)) {
                    searchQuery = "";
                    searchBarTextCleared();
                    mBtnSearchGo.setVisibility(View.GONE);
                } else {
                    mBtnSearchGo.setVisibility(View.VISIBLE);
                }
            }
        });

        mBtnSearchGo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                if (!TextUtils.isEmpty(mSearchEditText.getText()))
                {
                    searchQuery = mSearchEditText.getText().toString().trim();
                    searchBarEditorAction(searchQuery);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Consts.REQUEST_CODE_VIEW_PRODUCT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null && data.hasExtra(Consts.EXTRA_ISUPDATED))
                {
                    if (data.getBooleanExtra(Consts.EXTRA_ISUPDATED, false))
                    {
                        //loadRecords();
                    }
                }
            }
        }
    }

    private void clearData()
    {
        mPageNo = 0;
        mPreviousPageNo = 0;
        mTotalRecords = 0;
        mItemsListModel = null;
        mItemModelList.clear();
    }

    public void loadRecords()
    {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.invalidate();
        } else {
            return;
        }

        clearData();
        mProductsRecyclerViewAdapter = new ProductsListAdapter(mRecyclerView, mItemModelList,
                selectedProductsList, onProductClicked,
                ProductsInStockFragment.this, isEditing, isPreSelectedSubCat,
                checkboxvalue);
        mProductsRecyclerViewAdapter.setCurrentPage(mPageNo);
        mRecyclerView.setAdapter(mProductsRecyclerViewAdapter);
        mProductsRecyclerViewAdapter.setEditing(true, 0, 0);

    }

    @Override
    public void onLoadMore(int currentPage, int totalItemCount)
    {

        if (isSearchQuery)
        {
            getInStock(currentPage, totalItemCount, searchQuery);
        }
        else {
            getInStock(currentPage, totalItemCount,"");
        }
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

    private ProductsListAdapter.ProductListClickListener onProductClicked =
            new ProductsListAdapter.ProductListClickListener()
    {
        @Override
        public void onAddToStoreClicked(int position) {
            //showSetProductPriceDialog(productListModel);

        }

        @Override
        public void onViewInStoreClicked(int position)
        {
            //startActivity(new Intent(getActivity(), ViewInStoreActivity.class));
            /*ProductListModel productListModel = newProductListModel.get(position);
            showSetProductPriceDialog(productListModel);*/

        }

        @Override
        public void onProductSelected(Items model, int position)
        {
            updateCart(model.quantity_id, 1, position);

        }

        @Override
        public void onProductUnSelected(Items model, int position)
        {
            updateCart(model.quantity_id, 0, position);
        }

        @Override
        public void onnavigationbuttonset(int position)
        {
            /*if(selectedProductsList!=null) {
                MenuItem menuRefresh = menu1.findItem(R.id.menu_add_button);
                menuRefresh.setVisible(true);
            }
            if(selectedProductsList.size()==0){
                MenuItem menuRefresh = menu1.findItem(R.id.menu_add_button);
                menuRefresh.setVisible(false);
            }*/

        }
    };

    private void setDataToAdapter(List<Items> itemModelList)
    {
        /*setIdsOnList(itemModelList);*/
        mRecyclerView.setEmptyView(mEmptyView);
        mProductsRecyclerViewAdapter.removeItem(null);
        mProductsRecyclerViewAdapter.resetItems(itemModelList);
    }

	/*private void setIdsOnList(List<ProductListModel> itemModelList) {
		for (int i = 0; i < itemModelList.size(); i++) {
			ProductListModel productModel =  itemModelList.get(i);
			if (productModel == null) return;
			productModel.setIdOnUI(productModel.getCategoryId() + ":" +
					productModel.getSubcategoryId() + ":" + i
					+ (isSearchQuery?"s":"ns")+":"+productModel.getProductName());
		}
	}*/

    private void searchBarTextCleared()
    {
        if (isSearchQuery)
        {
            searchQuery = "";
            isSearchQuery = false;
            loadRecords();
        }
        else {
            searchQuery = "";
            isSearchQuery = false;
        }
    }

    public void searchBarEditorAction(String searchString)
    {
        if (!TextUtils.isEmpty(searchString))
        {
            searchQuery = searchString;
            isSearchQuery = true;
            mPreviousPageNo = 0;
            loadRecords();
        }
    }

    public void getInStock(final int currentPage, int totalItemCount, String searchQuery)
    {
        if (totalItemCount < mTotalRecords || mTotalRecords == 0)
        {
            if (currentPage <= 1 && mProductsRecyclerViewAdapter != null)
            {
                mProductsRecyclerViewAdapter.startFreshLoading();
            }

            try
            {
                showLoadingDialog();
                new ItemsController( getActivity(), new CustomerNetworkObserver())
                        .getItems(currentPage, searchQuery);

            }
            catch (Exception ex ) {
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(ex));
            }
        }
        else {
            try {
                mProductsRecyclerViewAdapter.removeItem(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

                mPreviousPageNo++;
                mItemsListModel = responseData;

                //the api call will actaully currentPage-1, so adding + 1 below to get
                //the actuall current page passed
                int currentPage = mPageNo +1;


                if (mItemsListModel == null)
                {
                    showErrorDialog(getString(R.string.sww));
                    return;
                }
                mPageNo = mItemsListModel.current_page;
                mTotalRecords = mItemsListModel.total_records;

                if (currentPage <= 1)
                {
                    mItemModelList = mItemsListModel.getItems();
                }
                else if (currentPage < mPageNo) {
                    mItemModelList.addAll(mItemsListModel.getItems());
                } else if (currentPage == mPageNo) {
                    if (mItemModelList.size() < mTotalRecords) {
                        if (mPreviousPageNo <= mPageNo) {
                            mItemModelList.addAll(mItemsListModel.getItems());
                        } else {
                            mPreviousPageNo = mPageNo;
                        }
                    }
                }
                if (mItemModelList != null)
                {
                    mProductsRecyclerViewAdapter.setCurrentPage(mPageNo);
                    setDataToAdapter(mItemModelList);
                }
                else {
                    showErrorDialog("Error", "Something went wrong: List is null");
                }

                //updateCategoryLeftRightArrow();

            }
            else {
                String errorMsg = "Error : " + responseData.statusmsg + " " + responseData.statuscode;
                showErrorDialog(errorMsg);
            }


        }
    }


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
                        mProductsRecyclerViewAdapter.setEditing(false, update_cart_mode, position);

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



    private void updateCategoryLeftRightArrow()
    {
        if (isPreSelectedSubCat) return;
        int selectedCatIndex = categoryList.indexOf(categoryModel);
        if (selectedCatIndex == 0)
        {

            catLeftShift.setEnabled(false);
            catRightShift.setEnabled(true);
        } else if (selectedCatIndex == categoryList.size() - 1) {
            catLeftShift.setEnabled(true);
            catRightShift.setEnabled(false);
        } else {
            catLeftShift.setEnabled(true);
            catRightShift.setEnabled(true);
        }
    }

    private void showProductAddedDialog()
    {
        final OutputResultDialogFragment acceptDeclineDialog = OutputResultDialogFragment
                .newInstance(true, getString(R.string.dialog_message_new_product_added),
                        null);
        acceptDeclineDialog.show(this.getChildFragmentManager(), "dialog");

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if (isAdded() && acceptDeclineDialog.isVisible()) {
                    acceptDeclineDialog.dismissAllowingStateLoss();
                }
            }
        }, DIALOG_DISPLAY_TIME);
    }

    private void showSetProductPriceDialog(final Items selectedProductListModel)
    {
        ProductPriceDialogFragment.showDialog(getActivity(), "",
                new ProductPriceDialogFragment.AddedPriceListener()
                {
                    @Override
                    public void onPriceAdded(String s)
                    {
                        //callAddProductApi(s, selectedProductListModel);
                    }
                }
        );
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if (!isPreSelectedSubCat)
        {
            if (isEditing)
            {

                requireActivity().getMenuInflater().inflate(R.menu.menu_product_listing_edit, menu);
                MenuItem menuRefresh = menu.findItem(R.id.menu_add_button);
                menuRefresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (mProductsRecyclerViewAdapter == null) return true;

                        HashSet<Items> selectedModelList = mProductsRecyclerViewAdapter.getSelectedModelList();
                        if (selectedModelList.size() == 0) {
                            DialogFragment dialogFragment = SingleBtnDialogFragment.newInstance(getString(R.string.no_product_selected_to_add));
                            dialogFragment.show(requireActivity().getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
                            return true;
                        }
                        ArrayList<Items> list = new ArrayList<>(selectedModelList);

                        //startActivityForResult(AddMulitpleGenericProductsActivity.getStartIntent(getActivity(), list), Consts.REQUEST_CODE_VIEW_PRODUCT);

                        changeToEditMode(false);
                        return true;
                    }
                });
            }
            else {
                requireActivity().getMenuInflater().inflate(R.menu.menu_product_listing, menu);
                MenuItem menuRefresh = menu.findItem(R.id.menu_add_button);
                menuRefresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        changeToEditMode(true);
                        return true;
                    }
                });
            }
        } else if (isPreSelectedSubCat2 = true) {

                if (isEditing) {
                    ProductsInStockFragment.this.menu1 = menu;
                    requireActivity().getMenuInflater().inflate(R.menu.menu_product_listing_edit, menu1);

                    MenuItem menuRefresh = menu1.findItem(R.id.menu_add_button);
                    menuRefresh.setVisible(false);

                            menuRefresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (mProductsRecyclerViewAdapter == null) return true;
                                    HashSet<Items> selectedModelList = mProductsRecyclerViewAdapter.getSelectedModelList();
                                if (selectedModelList.size() == 0) {
                                    DialogFragment dialogFragment = SingleBtnDialogFragment.newInstance(getString(R.string.no_product_selected_to_add));
                                    dialogFragment.show(requireActivity().getSupportFragmentManager(), BaseActivity.DIALOG_TAG);
                                    return true;
                                }

                                    ArrayList<Items> list = new ArrayList<>(selectedModelList);
                                    //startActivityForResult(AddMulitpleGenericProductsActivity.getStartIntent(getActivity(), list), Consts.REQUEST_CODE_VIEW_PRODUCT);
                                   // changeToEditMode(false);
                                    return true;
                                }
                            });

                }
                else {

                    requireActivity().getMenuInflater().inflate(R.menu.menu_product_listing, menu);
                    MenuItem menuRefresh = menu.findItem(R.id.menu_add_button);
                    menuRefresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            changeToEditMode(true);
                            return true;
                        }
                    });
                }

        }
        else {
            if (!categoryModel.getStoreId().equalsIgnoreCase("0")) {
                requireActivity().getMenuInflater().inflate(R.menu.menu_product_options, menu);

                MenuItem deleteSubcategory = menu.findItem(R.id.menu_category_delete_subcategory);

                deleteSubcategory.setOnMenuItemClickListener(item -> {


                    TwoBtnDialogFragment.showDialog(requireActivity(),
                            getString(R.string.general_use_are_you_sure_delete_subcategoty_title),
                            getString(R.string.general_use_are_you_sure_delete_subcategoty_desp),
                            getString(R.string.general_use_cancel), getString(R.string.general_use_delete),
                            new TwoBtnDialogFragment.CustomDialogListener() {
                                @Override
                                public void onClickLeftBtn() {
                                }

                                @Override
                                public void onClickRightBtn() {

                                    deleteSubcategory();

                                }
                            }
                    );

                    return false;

                });
            }
        }


        super.onCreateOptionsMenu(menu, inflater);

    }

    private void deleteSubcategory()
    {

    }

    private void getCatList()
    {
        /*
        CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MventryApp.getInstance().getToken());

        Call<CategoryV2PaginatedModel> call = customerClient.getCat(
                MventryApp.getInstance().getCurrentStoreId(),
                ShopxieSharedPreferences.getInstance().getMswipeUsername(),
                ShopxieSharedPreferences.getInstance().getVender(), "", 1 + ""
        );

        call.enqueue(new Callback<CategoryV2PaginatedModel>() {
            @Override
            public void onResponse(Call<CategoryV2PaginatedModel> call, Response<CategoryV2PaginatedModel> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()) {
                    CategoryV2PaginatedModel categoryPageModel = response.body();
                    if (categoryPageModel == null) {
                        showErrorDialog(getString(R.string.sww));
                        return;
                    }
                    mPageNo = categoryPageModel.getCurrentPage();
                    mTotalRecords = categoryPageModel.getTotalRecords();

                    categoryList = categoryPageModel.getData();

                    if (categoryList.size() == 0) {
                        showErrorDialog(getString(R.string.no_categories_found), getString(R.string.click_on_add_tab));
                        return;
                    }
                    setSelectionCategory(0);
                } else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<CategoryV2PaginatedModel> call, Throwable t) {
                if (!isAdded()) return;
                showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });

         */
    }

    private void updateCategoriesUI()
    {
        categoriesVG.removeAllViews();
        int selectedViewIndex = 0;
        String selectedCatID = categoryModel.getCategoryId();
        for (int i = 0; i < categoryList.size(); i++)
        {
            CategoryV2Model model = categoryList.get(i);

            TextView tv;
            if (selectedCatID.equals(model.getCategoryId())) {
                tv = (TextView) View.inflate(categoriesVG.getContext(),
                        R.layout.selected_simple_list_item, null);
                tv.setAlpha(1f);
                tv.setTypeface(boldTypeface);
                selectedViewIndex = i;
            } else {
                tv = (TextView) View.inflate(categoriesVG.getContext(),
                        android.R.layout.simple_list_item_1, null);
                tv.setAlpha(0.8f);
            }

            tv.setText(model.getCategoryName());
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectionCategory(finalI);
                }
            });
            categoriesVG.addView(tv);
        }
        categoriesVG.setVisibility(View.VISIBLE);
        subCategoriesVG.setVisibility(View.INVISIBLE);
        final int finalSelectedViewIndex = selectedViewIndex;
        categoriesVG.post(new Runnable() {
            @Override
            public void run() {
                ViewParent parent = categoriesVG.getParent();
                if (parent instanceof HorizontalScrollView) {
                    int left = categoriesVG.getChildAt(finalSelectedViewIndex).getLeft();
                    ((HorizontalScrollView) parent).smoothScrollTo(left, 0);
                }
            }
        });


        catLeftShift.setEnabled(false);
        catRightShift.setEnabled(false);

    }


    private void setSelectionCategory(int i)
    {
        categoryModel = categoryList.get(i);
        updateCategoriesUI();
        CustomerClient customerClient = ServiceGenerator.createService(CustomerClient.class, MOMApplication.getInstance().getToken());

        Call<String> call = customerClient.getSubCat(categoryModel.getCategoryId(), "", "1");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;
                hideLoadingDialog();
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    responseString = responseString.replaceAll("\\n", "");
                    try {
                        if (!TextUtils.isEmpty(responseString)) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            editSubCatBtn.setVisibility(View.VISIBLE);
                            subCategoryList = new ArrayList<>();
                            JSONArray data = jsonResponse.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
                                String category_name = jsonObject.getString("sub_category_name");
                                String category_id = jsonObject.getString("sub_category_id");
                                String img_url = jsonObject.getString("img_url");
                                SubCategoryV2Model model = new SubCategoryV2Model(category_name, category_id, img_url);
                                subCategoryList.add(model);
                            }
                            if (subCategoryList.size() == 0) {
                                showErrorDialog(getString(R.string.no_sub_categories_found));
                                return;
                            }
                            subCategoryModel = subCategoryList.get(0);
                            setSelectionSubCategory(categoryModel, subCategoryModel);
                            mLINSubCatMain.setVisibility(View.GONE);
                        } else {
                            showErrorDialog(getString(R.string.sww));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog(getString(R.string.invalid_server_response));
                    }
                } else {
                    showErrorDialog(ErrorUtils.getErrorString(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!isAdded()) return;
                hideLoadingDialog();
                showErrorDialog(ErrorUtils.getFailureError(t));
            }
        });

    }

    private void updateSubCategoriesUI()
    {
        if (!isAdded()) return;
        if (isPreSelectedSubCat) return;

        getCatSubCatLabelTv.setText(subCategoryModel.getCatName());
        subCategoriesVG.removeAllViews();
        int selectedViewIndex = 0;
        for (int i = 0; i < subCategoryList.size(); i++) {
            final SubCategoryV2Model mSubCatModel = subCategoryList.get(i);

            TextView tv = (TextView) View.inflate(categoriesVG.getContext(),
                    R.layout.selected_simple_list_item, null);
            /*tv.setAlpha(0.8f);*/

            if (subCategoryModel.getCatId().equals(mSubCatModel.getCatId())) {
                tv.setTypeface(boldTypeface);
                selectedViewIndex = i;
                /*tv.setAlpha(1f);*/
            }

            tv.setText(mSubCatModel.getCatName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subCategoryModel = mSubCatModel;
                    setSelectionSubCategory(categoryModel, subCategoryModel);
                }
            });
            subCategoriesVG.addView(tv);

        }
        subCategoriesVG.setVisibility(View.VISIBLE);

        final int finalSelectedViewIndex = selectedViewIndex;
        subCategoriesVG.post(new Runnable()
        {
            @Override
            public void run()
            {
                ViewParent parent = subCategoriesVG.getParent();
                if (parent instanceof HorizontalScrollView) {
                    int left = subCategoriesVG.getChildAt(finalSelectedViewIndex).getLeft();
                    ((HorizontalScrollView) parent).smoothScrollTo(left, 0);
                }
            }
        });

    }

    private void setSelectionSubCategory(CategoryV2Model model, SubCategoryV2Model subCatModel)
    {
        mSearchEditText.setText("");
        searchQuery = "";
        isSearchQuery = false;
        updateSubCategoriesUI();
        loadRecords();

    }


    private void showSelectSubCatDialog()
    {
		/*SelectSubCatDialogFragment fragemnt =  SelectSubCatDialogFragment.newInstance(categoryModel, new SelectSubCatDialogFragment.DialogListener() {
			@Override
			public void onSubCategorySelected(SubCategoryV2Model subCategoryV2Model) {
				subCategoryModel = subCategoryV2Model;
				setSelectionSubCategory(categoryModel, subCategoryV2Model);
			}
		});
		fragemnt.show(getActivity().getSupportFragmentManager(), BaseActivity.DIALOG_TAG);*/

        mLINSubCatMain.setVisibility(View.VISIBLE);

        addSubCategories();
    }

    int mSelectedPos = 0;
    int mSelectedPostemp = 0;

    private void addSubCategories()
    {

        mSelectedPostemp = mSelectedPos;

        final SubCatViewAdapter appViewAdapter = new SubCatViewAdapter(getActivity());
        mLVSubCatMain.setAdapter(appViewAdapter);
        mLVSubCatMain.setSelection(mSelectedPos);

        mLVSubCatMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                mSelectedPos = position;
                subCategoryModel = subCategoryList.get(position);
                setSelectionSubCategory(categoryModel, subCategoryModel);
                mLINSubCatMain.setVisibility(View.GONE);
            }
        });

        mIMGCatMainUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSelectedPostemp = mSelectedPostemp - 1;

                if (mSelectedPostemp > 0) {

                    mLVSubCatMain.smoothScrollToPosition(mSelectedPostemp);
                }
            }
        });

        mIMGCatMainDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSelectedPostemp = mSelectedPostemp + 1;

                if (mSelectedPostemp < subCategoryList.size()) {

                    mLVSubCatMain.smoothScrollToPosition(mSelectedPostemp);
                }
            }
        });

    }


    public class SubCatViewAdapter extends BaseAdapter {

        Context context;

        public SubCatViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            if (subCategoryList == null) {
                return 0;
            }
            return subCategoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.activity_product_subcategories_row, null);
            }

            TextView txtItem = convertView.findViewById(R.id.activity_product_subcategory_name);
            txtItem.setText(subCategoryList.get(position).getCatName());

            return convertView;
        }
    }

}
