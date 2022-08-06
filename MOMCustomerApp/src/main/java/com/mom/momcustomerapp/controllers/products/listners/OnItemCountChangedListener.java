package com.mom.momcustomerapp.controllers.products.listners;


import com.mom.momcustomerapp.controllers.products.models.ItemDetails;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 11-04-2020 00:58
 * @modified by -
 * @updated on -
 * @since -
 */
public interface OnItemCountChangedListener{
    void onItemRemoved(ItemDetails itemsModel);
    void onItemDecremented(ItemDetails itemsModel);
    void onItemIncremented(ItemDetails itemsModel);
}
