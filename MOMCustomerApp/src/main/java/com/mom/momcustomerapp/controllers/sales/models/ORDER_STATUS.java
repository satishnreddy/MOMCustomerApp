package com.mom.momcustomerapp.controllers.sales.models;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 09-04-2020 17:30
 * @modified by -
 * @updated on -
 * @since -
 */
@Retention(SOURCE)
@StringDef({ORDER_STATUS.ORDER_STATUS_PENDING, ORDER_STATUS.ORDER_STATUS_ACCEPTED,
        ORDER_STATUS.ORDER_STATUS_DELIVERED, ORDER_STATUS.ORDER_STATUS_RETURNED/*, ORDER_STATUS.ORDER_STATUS_PARTIAL_COMPLETED*/,
        ORDER_STATUS.ORDER_STATUS_NA})
public @interface ORDER_STATUS {
    String ORDER_STATUS_PENDING = "Pending";
    String ORDER_STATUS_ACCEPTED = "Accepted";
    String ORDER_STATUS_DELIVERED = "Delivered";
    String ORDER_STATUS_RETURNED = "Returned";
//    String ORDER_STATUS_PARTIAL_COMPLETED = "PartialReturned";
    String ORDER_STATUS_NA = "Not Available";


}
