package com.mom.momcustomerapp.controllers.stores.model;

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
@StringDef({DELIVERY_TYPE.DELIVERY_TYPE_NA,DELIVERY_TYPE.DELIVERY_TYPE_DELIVERY, DELIVERY_TYPE.DELIVERY_TYPE_PICKUP, DELIVERY_TYPE.DELIVERY_TYPE_BOTH})
public @interface DELIVERY_TYPE {
    String DELIVERY_TYPE_NA = "-1";
    String DELIVERY_TYPE_DELIVERY = "0";
    String DELIVERY_TYPE_PICKUP = "1";
    String DELIVERY_TYPE_BOTH = "2";

                        /*[09/04/2020 6:50 PM] Satish Reddy Nalluri: 0 - delivery
                            [09/04/2020 6:50 PM] Satish Reddy Nalluri: 1 - pickup
                            [09/04/2020 6:50 PM] Satish Reddy Nalluri: 2 both*/

}
